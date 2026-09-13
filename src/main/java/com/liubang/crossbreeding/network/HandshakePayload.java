package com.liubang.crossbreeding.network;

import com.liubang.crossbreeding.Crossbreeding;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HandshakePayload(String modVersion) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HandshakePayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(Crossbreeding.MOD_ID, "handshake"));

    public static final StreamCodec<RegistryFriendlyByteBuf, HandshakePayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeUtf(payload.modVersion()),
                    buf -> new HandshakePayload(buf.readUtf())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}