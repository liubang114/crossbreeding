package com.liubang.crossbreeding.recipe;

import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import com.liubang.crossbreeding.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * 面包配方：用杂交小麦合成对应性状的面包。
 *
 * <p>使用 CustomRecipe 只需要一个 JSON，配方匹配与结果全在代码里动态判定：
 * <ul>
 *   <li>少壳（ee）：1 个杂交小麦，多壳（E_）：3 个杂交小麦</li>
 *   <li>高饱食（cc）：结果面包额外 +2 饱食度</li>
 *   <li>高饱和（dd）：结果面包额外 +2 饱和度</li>
 * </ul>
 */
public class CrossbreedingBreadRecipe extends CustomRecipe {

    public CrossbreedingBreadRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (!stack.is(ModItems.CROSSBREEDING_WHEAT.get())) return false;
            if (!stack.has(ModDataComponents.GENOME.get())) return false;
            count++;
        }
        // 只接受 1 或 3 个杂交小麦
        return count == 1 || count == 3;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        int count = 0;
        Genome genome = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (genome == null) {
                genome = stack.get(ModDataComponents.GENOME.get());
            }
            count++;
        }

        if (genome == null) return ItemStack.EMPTY;

        // 壳性状：少壳（ee）需要 1 个，多壳（E_）需要 3 个
        boolean isShellLess = genome.isRecessive(Gene.E);
        if (isShellLess && count != 1) return ItemStack.EMPTY;
        if (!isShellLess && count != 3) return ItemStack.EMPTY;

        // 饱食与饱和性状：c/d 是隐性时分别代表高饱食/高饱和
        boolean highNutrition = genome.isRecessive(Gene.C);
        boolean highSaturation = genome.isRecessive(Gene.D);

        if (highNutrition && highSaturation) {
            return new ItemStack(ModItems.HIGH_NUTRITION_SATURATION_BREAD.get());
        } else if (highNutrition) {
            return new ItemStack(ModItems.HIGH_NUTRITION_BREAD.get());
        } else if (highSaturation) {
            return new ItemStack(ModItems.HIGH_SATURATION_BREAD.get());
        } else {
            return new ItemStack(ModItems.PLAIN_BREAD.get());
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BREAD_SERIALIZER.get();
    }

    // ---------------------------------------------------------------
    // Serializer：自己实现，不继承 CustomRecipe.Serializer
    // ---------------------------------------------------------------

    public static class Serializer implements RecipeSerializer<CrossbreedingBreadRecipe> {

        /** 该配方不需要任何配置字段，JSON 里只有 type。 */
        public static final MapCodec<CrossbreedingBreadRecipe> CODEC =
                MapCodec.unit(() -> new CrossbreedingBreadRecipe(CraftingBookCategory.MISC));

        /** 网络同步时也不传输额外数据，显式指定 RegistryFriendlyByteBuf。 */
        public static final StreamCodec<RegistryFriendlyByteBuf, CrossbreedingBreadRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buf, recipe) -> {},
                        buf -> new CrossbreedingBreadRecipe(CraftingBookCategory.MISC)
                );

        @Override
        public MapCodec<CrossbreedingBreadRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrossbreedingBreadRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}