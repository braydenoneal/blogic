import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.Minecraft
import networking.ControllerPayload

object ModNetworking {
    fun initialize() {
        PayloadTypeRegistry.clientboundPlay().register(ControllerPayload.ID, ControllerPayload.CODEC)

        ClientPlayNetworking.registerGlobalReceiver(ControllerPayload.ID) { payload: ControllerPayload, context: ClientPlayNetworking.Context ->
            context.client().execute({
                val screen = Minecraft.getInstance().gui.screen()

                if (screen is ControllerScreen && screen.menu.payload.pos == payload.pos) {
                    screen.updateConsole(payload.console)
                }
            })
        }
    }
}
