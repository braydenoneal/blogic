package networking

import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class ControllerClientPayload(
    val pos: BlockPos,
    val name: String,
    val source: String,
    val draft: String,
    val console: String,
    val cursor: Int,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return ID
    }

    companion object {
        val ID: CustomPacketPayload.Type<ControllerClientPayload> = CustomPacketPayload.Type(
            Identifier.fromNamespaceAndPath("blogic", "controller_client_payload"),
        )

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ControllerClientPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ControllerClientPayload::pos,
            ByteBufCodecs.STRING_UTF8, ControllerClientPayload::name,
            ByteBufCodecs.STRING_UTF8, ControllerClientPayload::source,
            ByteBufCodecs.STRING_UTF8, ControllerClientPayload::draft,
            ByteBufCodecs.STRING_UTF8, ControllerClientPayload::console,
            ByteBufCodecs.INT, ControllerClientPayload::cursor,
            ::ControllerClientPayload,
        )
    }
}