package com.liubang.crossbreeding.item;

import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.util.SimpleModeCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class GameteItem extends Item {

    public GameteItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        // appendHoverText 只在客户端被调用，这里直接用缓存即可
        if (!SimpleModeCache.isClientSimpleMode()) return;

        Gamete gamete = stack.get(ModDataComponents.GAMETE.get());
        if (gamete == null) {
            tooltip.add(Component.literal("基因型：未知"));
            return;
        }

        tooltip.add(Component.literal("基因型：" + gamete.toString()));
    }
}