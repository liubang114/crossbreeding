package com.liubang.crossbreeding.item;

import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.core.Phenotype;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.util.SimpleModeCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * 杂交小麦物品：由成熟杂交作物掉落，带有 Genome 组件。
 * 悬停时显示表型；简单模式下额外显示基因型。
 */
public class CrossbreedingWheatItem extends Item {

    public CrossbreedingWheatItem(Properties properties) {
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
}