package com.braydenoneal.block.entity;

import com.braydenoneal.Blogic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ControllerScreenHandler extends ScreenHandler {
    private final ControllerBlockEntity controllerBlockEntity;

    public ControllerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, null);
        Blogic.LOGGER.info("Created");
    }

    public ControllerScreenHandler(int syncId, PlayerInventory ignoredPlayerInventory, ControllerBlockEntity controllerBlockEntity) {
        super(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId);
        this.controllerBlockEntity = controllerBlockEntity;
        Blogic.LOGGER.info("Created with {}", controllerBlockEntity);
    }

    public ControllerBlockEntity getControllerBlockEntity() {
        return controllerBlockEntity;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
