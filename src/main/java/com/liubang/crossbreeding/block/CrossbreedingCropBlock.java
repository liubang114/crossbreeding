package com.liubang.crossbreeding.block;

import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModBlocks;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrossbreedingCropBlock extends CropBlock implements EntityBlock {

    public static final int MAX_AGE = 7;

    public CrossbreedingCropBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrossbreedingCropBlockEntity(pos, state);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CROSSBREEDING_SEED.get();
    }

    private static @Nullable Genome getGenome(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            return be.getGenome();
        }
        return null;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        // 兜底：如果 BE 里没 Genome，随机生成一份
        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            if (be.getGenome() == null) {
                be.setGenome(Genome.random(random));
                be.setChanged();
            }
        }

        int age = getAge(state);

        // 染病判定：仅在不抗病且阶段 0 → 1 的瞬间
        if (age == 0) {
            Genome genome = getGenome(level, pos);
            if (genome != null && !genome.isDominant(Gene.B)) {
                if (random.nextInt(5) == 0) {
                    level.removeBlockEntity(pos);
                    level.setBlock(pos, ModBlocks.DISEASED_WHEAT.get().defaultBlockState(),
                            Block.UPDATE_ALL);
                    return;
                }
            }
        }

        // 正常生长
        if (age < getMaxAge() && level.getRawBrightness(pos, 0) >= 9) {
            float speed = CropBlock.getGrowthSpeed(state, level, pos);
            Genome genome = getGenome(level, pos);
            if (genome != null && genome.isRecessive(Gene.F)) {
                speed *= 2.0F; // 生长旺盛（ff）：双倍速度
            }
            int bound = (int) (25.0F / speed) + 1;
            if (random.nextInt(bound) == 0) {
                level.setBlock(pos, getStateForAge(age + 1), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos,
                              BlockState state, @Nullable BlockEntity blockEntity,
                              ItemStack tool) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;

        int age = getAge(state);

        Genome genome = null;
        if (blockEntity instanceof CrossbreedingCropBlockEntity be) {
            genome = be.getGenome();
        }

        if (age == getMaxAge() && genome != null) {
            RandomSource random = serverLevel.random;

            // 所有成熟杂交小麦都掉 1 个杂交小麦 + 2~3 个配子
            ItemStack wheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
            wheat.set(ModDataComponents.GENOME.get(), genome);
            popResource(level, pos, wheat);

            int gameteCount = 2 + random.nextInt(2);
            for (int i = 0; i < gameteCount; i++) {
                boolean female = random.nextBoolean();
                ItemStack gameteStack = new ItemStack(
                        female ? ModItems.FEMALE_GAMETE.get() : ModItems.MALE_GAMETE.get());
                Gamete gamete = genome.createGamete(random);
                gameteStack.set(ModDataComponents.GAMETE.get(), gamete);
                popResource(level, pos, gameteStack);
            }

            // 多穗：额外掉落 2~3 个杂交小麦
            if (genome.isRecessive(Gene.A)) {
                int extra = 2 + random.nextInt(2);
                for (int i = 0; i < extra; i++) {
                    ItemStack extraWheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
                    extraWheat.set(ModDataComponents.GENOME.get(), genome);
                    popResource(level, pos, extraWheat);
                }
            }
        }
    }
}

        