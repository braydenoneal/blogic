package block.entity

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import networking.ControllerPayload

class ControllerScreenHandler(
    syncId: Int,
    playerInventory: Inventory,
    val payload: ControllerPayload,
) : AbstractContainerMenu(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId) {
    val entity: ControllerBlockEntity =
        playerInventory.player.level().getBlockEntity(payload.pos) as ControllerBlockEntity

    fun setSource(payload: ControllerPayload) {
        entity.setSource(payload)
    }

    override fun quickMoveStack(player: Player, i: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun stillValid(player: Player): Boolean {
        return true
    }
}
