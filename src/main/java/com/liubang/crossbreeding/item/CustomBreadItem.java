package com.liubang.crossbreeding.item;

import net.minecraft.world.item.Item;

/**
 * 杂交面包。四种面包共用此类，饱食度和饱和度由构造时的
 * FoodProperties 静态决定（在 ModItems 中注册时传入）。
 * 使用原版面包的模型与贴图。
 */
public class CustomBreadItem extends Item {

    public CustomBreadItem(Properties properties) {
        super(properties);
    }
}