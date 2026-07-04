package com.braydenoneal.block.entity

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class ControllerScreenHandler(syncId: Int, ignoredPlayerInventory: PlayerInventory, private val entity: ControllerBlockEntity) : ScreenHandler(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId) {
    constructor(syncId: Int, playerInventory: PlayerInventory, pos: BlockPos) : this(syncId, playerInventory, (playerInventory.player.world.getBlockEntity(pos) as ControllerBlockEntity))

    fun source(): String {
        return entity.source()
    }

    fun pos(): BlockPos {
        return entity.getPos()
    }

    fun setSource(source: String) {
        entity.setSource(source)
    }

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack? {
        return null
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}
