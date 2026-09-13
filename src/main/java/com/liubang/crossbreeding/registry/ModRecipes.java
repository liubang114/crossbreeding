package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.recipe.CrossbreedingBreadRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Crossbreeding.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Crossbreeding.MOD_ID);

    // ---------- 面包合成配方 ----------

    public static final DeferredHolder<RecipeType<?>, RecipeType<CrossbreedingBreadRecipe>>
            BREAD_TYPE = TYPES.register("bread",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(
                    Crossbreeding.MOD_ID, "bread")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrossbreedingBreadRecipe>>
            BREAD_SERIALIZER = SERIALIZERS.register("bread",
            CrossbreedingBreadRecipe.Serializer::new);

    public static void register(IEventBus modBus) {
        TYPES.register(modBus);
        SERIALIZERS.register(modBus);
    }
}