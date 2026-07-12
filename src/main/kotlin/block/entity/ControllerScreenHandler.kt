package block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import networking.ControllerPayload

class ControllerScreenHandler(syncId: Int, @Suppress("unused") ignoredPlayerInventory: Inventory, private val entity: ControllerBlockEntity) : AbstractContainerMenu(ModBlockEntities.CONTROLLER_SCREEN_HANDLER, syncId) {
    constructor(syncId: Int, playerInventory: Inventory, pos: BlockPos) : this(syncId, playerInventory, (playerInventory.player.level().getBlockEntity(pos) as ControllerBlockEntity))

    fun pos(): BlockPos {
        return entity.blockPos
    }

    fun name(): String {
        return entity.program.name
    }

    fun source(): String {
        return entity.program.source
    }

    fun cursorPosition(): Int {
        return entity.program.cursorPosition
    }

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
