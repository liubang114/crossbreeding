package com.liubang.crossbreeding.item;

import com.liubang.crossbreeding.block.CrossbreedingCropBlockEntity;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.core.Phenotype;
import com.liubang.crossbreeding.registry.ModBlocks;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.util.SimpleModeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CrossbreedingSeedItem extends Item {

    public CrossbreedingSeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Genome genome = stack.get(ModDataComponents.GENOME.get());
        if (genome == null) return;

        tooltip.add(Component.literal("表型：" + Phenotype.describe(genome)));

        if (SimpleModeCache.isClientSimpleMode()) {
            tooltip.add(Component.literal("基因型：" + genome.toString()));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(pos);

        if (!clickedState.is(Blocks.FARMLAND)) {
            return InteractionResult.PASS;
        }

        BlockPos cropPos = pos.above();
        if (!level.isEmptyBlock(cropPos)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        level.setBlock(cropPos, ModBlocks.CROSSBREEDING_CROP.get().defaultBlockState(),
                Block.UPDATE_ALL);

        if (level.getBlockEntity(cropPos) instanceof CrossbreedingCropBlockEntity be) {
            Genome genome = context.getItemInHand().get(ModDataComponents.GENOME.get());
            if (genome == null) {
                genome = Genome.random(level.random);
            }
            be.setGenome(genome);
            be.setChanged();
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}