package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截打草（矮草/高草）掉落的小麦种子，替换为带随机基因型的杂交种子。
 *
 * <p>这样自然生成的小麦（从打草获得）就拥有了随机基因型，
 * 玩家可以直接种出杂交小麦，而不必先种原版小麦。
 */
@EventBusSubscriber(modid = Crossbreeding.MOD_ID)
public class GrassDropInterceptor {

    @SubscribeEvent
    public static void onGrassDrops(BlockDropsEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        // 只处理矮草和高草
        if (!event.getState().is(Blocks.SHORT_GRASS)
                && !event.getState().is(Blocks.TALL_GRASS)) {
            return;
        }

        // 检查掉落里是否有原版小麦种子
        boolean hasWheatSeeds = false;
        for (ItemEntity entity : event.getDrops()) {
            if (entity.getItem().is(net.minecraft.world.item.Items.WHEAT_SEEDS)) {
                hasWheatSeeds = true;
                break;
            }
        }
        if (!hasWheatSeeds) return;

        RandomSource random = level.random;

        // 复制原来的掉落列表，替换小麦种子
        List<ItemEntity> newDrops = new ArrayList<>(event.getDrops().size());
        double x = event.getPos().getX() + 0.5;
        double y = event.getPos().getY() + 0.5;
        double z = event.getPos().getZ() + 0.5;

        for (ItemEntity entity : event.getDrops()) {
            ItemStack stack = entity.getItem();

            if (stack.is(net.minecraft.world.item.Items.WHEAT_SEEDS)) {
                // 替换为带随机基因型的杂交种子
                ItemStack seed = new ItemStack(ModItems.CROSSBREEDING_SEED.get(), stack.getCount());
                Genome genome = Genome.random(random);
                seed.set(ModDataComponents.GENOME.get(), genome);
                newDrops.add(new ItemEntity(level, x, y, z, seed));
            } else {
                newDrops.add(entity);
            }
        }

        // 用新列表替换原掉落
        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
    }
}