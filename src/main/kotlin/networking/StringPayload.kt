package networking

import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class StringPayload(val pos: BlockPos, val string: String) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return ID
    }

    companion object {
        val ID: CustomPacketPayload.Type<StringPayload> = CustomPacketPayload.Type(Identifier.fromNamespaceAndPath("blogic", "string"))
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, StringPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, StringPayload::pos,
            ByteBufCodecs.STRING_UTF8, StringPayload::string,
        ) { pos: BlockPos, string: String -> StringPayload(pos, string) }
    }
}
