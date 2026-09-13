package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Genome;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents REGISTRY =
            DeferredRegister.createDataComponents(Crossbreeding.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Gamete>> GAMETE =
            REGISTRY.register("gamete", () -> DataComponentType.<Gamete>builder()
                    .persistent(Gamete.CODEC)
                    .networkSynchronized(Gamete.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Genome>> GENOME =
            REGISTRY.register("genome", () -> DataComponentType.<Genome>builder()
                    .persistent(Genome.CODEC)
                    .networkSynchronized(Genome.STREAM_CODEC)
                    .build());

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}