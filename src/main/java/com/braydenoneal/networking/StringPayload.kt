package com.braydenoneal.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record StringPayload(BlockPos pos, String string) implements CustomPayload {
    public static final CustomPayload.Id<StringPayload> ID = new CustomPayload.Id<>(Identifier.of("blogic", "string"));
    public static final PacketCodec<RegistryByteBuf, StringPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, StringPayload::pos,
            PacketCodecs.STRING, StringPayload::string,
            StringPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
