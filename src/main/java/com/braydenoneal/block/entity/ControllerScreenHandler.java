package com.braydenoneal.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ControllerScreenHandler extends ScreenHandler {
    public ControllerScreenHandler(int syncId, PlayerInventory ignoredPlayerInventory) {
        super(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId);
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
