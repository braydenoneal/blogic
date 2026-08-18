package networking

import controller.ControllerScreen
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.Minecraft

object ModClientNetworking {
    fun initialize() {
        PayloadTypeRegistry.clientboundPlay().register(ControllerClientPayload.ID, ControllerClientPayload.CODEC)

        ClientPlayNetworking.registerGlobalReceiver(ControllerClientPayload.ID) { payload: ControllerClientPayload, context: ClientPlayNetworking.Context ->
            context.client().execute {
                val screen = Minecraft.getInstance().gui.screen()

                if (screen is ControllerScreen && screen.menu.payload.pos == payload.pos) {
                    screen.updateConsole(payload.console)
                }
            }
        }
    }
}
