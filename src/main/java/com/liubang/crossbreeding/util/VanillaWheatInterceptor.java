package com.liubang.crossbreeding.util;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID)
public class VanillaWheatInterceptor {

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockState state = event.getState();
        if (!state.is(Blocks.WHEAT)) return;

        int age = state.getValue(BlockStateProperties.AGE_7);
        if (age != 7) return;

        BlockPos pos = event.getPos();
        RandomSource random = level.random;

        event.getDrops().clear();

        Genome genome = Genome.random(random);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // 所有成熟小麦都掉 1 个杂交小麦 + 2~3 个配子
        ItemStack wheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
        wheat.set(ModDataComponents.GENOME.get(), genome);
        event.getDrops().add(new ItemEntity(level, x, y, z, wheat));

        int gameteCount = 2 + random.nextInt(2);
        for (int i = 0; i < gameteCount; i++) {
            boolean female = random.nextBoolean();
            ItemStack gameteStack = new ItemStack(
                    female ? ModItems.FEMALE_GAMETE.get() : ModItems.MALE_GAMETE.get());
            Gamete gamete = genome.createGamete(random);
            gameteStack.set(ModDataComponents.GAMETE.get(), gamete);
            event.getDrops().add(new ItemEntity(level, x, y, z, gameteStack));
        }

        // 多穗：额外 2~3 个杂交小麦
        if (genome.isRecessive(Gene.A)) {
            int extra = 2 + random.nextInt(2);
            for (int i = 0; i < extra; i++) {
                ItemStack extraWheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
                extraWheat.set(ModDataComponents.GENOME.get(), genome);
                event.getDrops().add(new ItemEntity(level, x, y, z, extraWheat));
            }
        }
    }
}