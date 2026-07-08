package block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class ControllerScreenHandler(syncId: Int, ignoredPlayerInventory: Inventory, private val entity: ControllerBlockEntity) : AbstractContainerMenu(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId) {
    constructor(syncId: Int, playerInventory: Inventory, pos: BlockPos) : this(syncId, playerInventory, (playerInventory.player.level().getBlockEntity(pos) as ControllerBlockEntity))

    fun source(): String {
        return entity.program.source
    }

    fun pos(): BlockPos {
        return entity.blockPos
    }

    fun setSource(source: String) {
        entity.setSource(source)
    }

    override fun quickMoveStack(player: Player, i: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun stillValid(player: Player): Boolean {
        return true
    }
}
