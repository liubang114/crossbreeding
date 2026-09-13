package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.block.CrossbreedingCropBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Crossbreeding.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrossbreedingCropBlockEntity>>
            CROSSBREEDING_CROP = REGISTRY.register("crossbreeding_crop",
            () -> BlockEntityType.Builder
                    .of(CrossbreedingCropBlockEntity::new, ModBlocks.CROSSBREEDING_CROP.get())
                    .build(null));

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}