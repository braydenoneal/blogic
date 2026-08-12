package networking

import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class ControllerPayload(
    val pos: BlockPos,
    val name: String,
    val source: String,
    val draft: String,
    val console: String,
    val cursor: Int,
    val isDraft: Boolean,
    val run: Boolean,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return ID
    }

    companion object {
        val ID: CustomPacketPayload.Type<ControllerPayload> =
            CustomPacketPayload.Type(Identifier.fromNamespaceAndPath("blogic", "controller_payload"))
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ControllerPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ControllerPayload::pos,
            ByteBufCodecs.STRING_UTF8, ControllerPayload::name,
            ByteBufCodecs.STRING_UTF8, ControllerPayload::source,
            ByteBufCodecs.STRING_UTF8, ControllerPayload::draft,
            ByteBufCodecs.STRING_UTF8, ControllerPayload::console,
            ByteBufCodecs.INT, ControllerPayload::cursor,
            ByteBufCodecs.BOOL, ControllerPayload::isDraft,
            ByteBufCodecs.BOOL, ControllerPayload::run,
            ::ControllerPayload
        )
    }
}
