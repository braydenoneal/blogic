package com.braydenoneal;

import com.braydenoneal.block.entity.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class BlogicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, ControllerScreen::new);
    }
}
