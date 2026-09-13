package com.liubang.crossbreeding.datagen;

import com.liubang.crossbreeding.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {

    public ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropNothing(ModBlocks.DISEASED_WHEAT.get());
        dropNothing(ModBlocks.CROSSBREEDING_CROP.get());
    }

    private void dropNothing(Block block) {
        add(block, noDrop());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Set.of(
                ModBlocks.CROSSBREEDING_CROP.get(),
                ModBlocks.DISEASED_WHEAT.get()
        );
    }
}