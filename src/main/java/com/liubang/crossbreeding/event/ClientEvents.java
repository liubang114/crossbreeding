package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.util.SimpleModeCache;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

/**
 * 仅客户端的监听器。状态本身存储在 {@link SimpleModeCache} 中，
 * 这里只负责在玩家登出时清空缓存。
 */
@EventBusSubscriber(modid = "crossbreeding", value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        SimpleModeCache.setClientSimpleMode(false);
    }
}