package networking

import block.entity.ControllerBlockEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ModNetworking {
    fun initialize() {
        PayloadTypeRegistry.serverboundPlay().register(ControllerPayload.ID, ControllerPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(ControllerPayload.ID) { controllerPayload: ControllerPayload, context: ServerPlayNetworking.Context ->
            context.server().execute {
                val entity = context.player().level().getBlockEntity(controllerPayload.pos) as ControllerBlockEntity
                entity.setSource(controllerPayload)
            }
        }
    }
}
