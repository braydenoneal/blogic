import block.entity.ModBlockEntities.Companion.CONTROLLER_SCREEN_HANDLER
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screens.MenuScreens

object BlogicClient : ClientModInitializer {
    override fun onInitializeClient() {
        MenuScreens.register(CONTROLLER_SCREEN_HANDLER, ::ControllerScreen)
    }
}
