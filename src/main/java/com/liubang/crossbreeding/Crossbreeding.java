package com.liubang.crossbreeding;

import com.liubang.crossbreeding.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Crossbreeding.MOD_ID)
public class Crossbreeding {

    public static final String MOD_ID = "crossbreeding";
    public static final String VERSION = "1.0.0";

    public Crossbreeding(IEventBus modBus, ModContainer container) {
        // 注册所有 DeferredRegister 到 mod 事件总线
        ModItems.register(modBus);
        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModDataComponents.register(modBus);
        ModCreativeTabs.register(modBus);
        ModRecipes.register(modBus);

        // 注意：网络包注册在 ModNetworking 类中通过 @EventBusSubscriber 自动完成
        // 指令注册和游戏事件在 CommonEvents 中通过 @EventBusSubscriber 自动完成
    }
}