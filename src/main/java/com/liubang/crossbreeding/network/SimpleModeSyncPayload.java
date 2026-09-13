package com.liubang.crossbreeding.network;

import com.liubang.crossbreeding.Crossbreeding;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SimpleModeSyncPayload(boolean simpleMode) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SimpleModeSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(Crossbreeding.MOD_ID, "simple_mode_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleModeSyncPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeBoolean(payload.simpleMode()),
                    buf -> new SimpleModeSyncPayload(buf.readBoolean())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}