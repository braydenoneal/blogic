package com.braydenoneal

import com.braydenoneal.block.entity.ModBlockEntities.Companion.CONTROLLER_SCREEN_HANDLER
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens

object BlogicClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(CONTROLLER_SCREEN_HANDLER, ::ControllerScreen)
    }
}
