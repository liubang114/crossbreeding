package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.block.CrossbreedingCropBlock;
import com.liubang.crossbreeding.block.DiseasedWheatBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks REGISTRY =
            DeferredRegister.createBlocks(Crossbreeding.MOD_ID);

    /**
     * 杂交小麦作物方块。
     * 属性参考原版 CropBlock 的构造参数。
     */
    public static final DeferredBlock<Block> CROSSBREEDING_CROP = REGISTRY.register(
            "crossbreeding_crop",
            () -> new CrossbreedingCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .noCollission()
                    .pushReaction(PushReaction.DESTROY))
    );

    /**
     * 染病枯死的小麦方块。
     * 永远使用原版小麦 stage0 的模型与贴图。
     */
    public static final DeferredBlock<Block> DISEASED_WHEAT = REGISTRY.register(
            "diseased_wheat",
            () -> new DiseasedWheatBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.CROP)
                    .noCollission()
                    .pushReaction(PushReaction.DESTROY))
    );

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}