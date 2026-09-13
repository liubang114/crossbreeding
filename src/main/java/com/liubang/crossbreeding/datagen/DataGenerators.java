package com.liubang.crossbreeding.datagen;

import com.liubang.crossbreeding.Crossbreeding;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 只注册掉落表；模型/blockstate 全手写
        generator.addProvider(event.includeServer(),
                new LootTableProvider(output, Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(
                                ModBlockLootProvider::new,
                                LootContextParamSets.BLOCK
                        )
                ), lookupProvider));

        // ❌ 不要加下面这两行：
        // generator.addProvider(event.includeClient(), new ModItemModelProvider(...));
        // generator.addProvider(event.includeClient(), new ModBlockStateProvider(...));
    }
}