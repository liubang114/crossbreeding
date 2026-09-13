package com.liubang.crossbreeding.network;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.util.SimpleModeCache;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SimpleModeSyncPayload.TYPE,
                SimpleModeSyncPayload.STREAM_CODEC,
                (payload, context) -> SimpleModeCache.setClientSimpleMode(payload.simpleMode())
        );

        registrar.playToServer(
                HandshakePayload.TYPE,
                HandshakePayload.STREAM_CODEC,
                (payload, context) -> {
                    String serverVersion = "1.0.0";
                    if (!serverVersion.equals(payload.modVersion())) {
                        org.slf4j.LoggerFactory.getLogger("crossbreeding").warn(
                                "客户端模组版本不匹配: 服务器={}, 客户端={}",
                                serverVersion, payload.modVersion());
                    }
                }
        );
    }
}