package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.item.CrossbreedingSeedItem;
import com.liubang.crossbreeding.item.CrossbreedingWheatItem;
import com.liubang.crossbreeding.item.CustomBreadItem;
import com.liubang.crossbreeding.item.GameteItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items REGISTRY =
            DeferredRegister.createItems(Crossbreeding.MOD_ID);

    // ---------- 配子 ----------

    public static final DeferredItem<Item> FEMALE_GAMETE = REGISTRY.register(
            "female_gamete",
            () -> new GameteItem(new Item.Properties())
    );

    public static final DeferredItem<Item> MALE_GAMETE = REGISTRY.register(
            "male_gamete",
            () -> new GameteItem(new Item.Properties())
    );

    // ---------- 杂交种子（种植用） ----------

    public static final DeferredItem<Item> CROSSBREEDING_SEED = REGISTRY.register(
            "crossbreeding_seed",
            () -> new CrossbreedingSeedItem(new Item.Properties())
    );

    // ---------- 杂交小麦（收获物，用于合成面包） ----------

    public static final DeferredItem<Item> CROSSBREEDING_WHEAT = REGISTRY.register(
            "crossbreeding_wheat",
            () -> new CrossbreedingWheatItem(new Item.Properties())
    );

    // ---------- 四种面包 ----------
    // 实际回复饱和度 = nutrition × saturationModifier × 2
    //
    //   杂交面包          : 饱食 5，饱和 6
    //   高饱食面包        : 饱食 7，饱和 6
    //   高饱和面包        : 饱食 5，饱和 8
    //   高饱食高饱和面包  : 饱食 7，饱和 8

    /** 杂交面包：饱食 5，饱和 6。 */
    public static final DeferredItem<Item> PLAIN_BREAD = REGISTRY.register(
            "plain_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .build()))
    );

    /** 高饱食面包：饱食 7，饱和 6。 */
    public static final DeferredItem<Item> HIGH_NUTRITION_BREAD = REGISTRY.register(
            "high_nutrition_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(7)
                            .saturationModifier(0.4286f)
                            .build()))
    );

    /** 高饱和面包：饱食 5，饱和 8。 */
    public static final DeferredItem<Item> HIGH_SATURATION_BREAD = REGISTRY.register(
            "high_saturation_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.8f)
                            .build()))
    );

    /** 高饱食高饱和面包：饱食 7，饱和 8。 */
    public static final DeferredItem<Item> HIGH_NUTRITION_SATURATION_BREAD = REGISTRY.register(
            "high_nutrition_saturation_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(7)
                            .saturationModifier(0.5714f)
                            .build()))
    );

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}