package com.braydenoneal.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class ControllerScreenHandler extends ScreenHandler {
    private final ControllerBlockEntity entity;

    public ControllerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, (ControllerBlockEntity) playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public ControllerScreenHandler(int syncId, PlayerInventory ignoredPlayerInventory, ControllerBlockEntity entity) {
        super(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId);
        this.entity = entity;
    }

    public String source() {
        return entity.source();
    }

    public BlockPos pos() {
        return entity.getPos();
    }

    public void setSource(String source) {
        entity.setSource(source);
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
