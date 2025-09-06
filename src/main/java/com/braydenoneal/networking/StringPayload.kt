package com.braydenoneal.networking

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

data class StringPayload(val pos: BlockPos, val string: String) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    companion object {
        val ID: CustomPayload.Id<StringPayload> = CustomPayload.Id(Identifier.of("blogic", "string"))
        val CODEC: PacketCodec<RegistryByteBuf, StringPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, StringPayload::pos,
            PacketCodecs.STRING, StringPayload::string,
        ) { pos: BlockPos, string: String -> StringPayload(pos, string) }
    }
}
