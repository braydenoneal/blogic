package blang

import block.entity.ControllerBlockEntity
import net.minecraft.core.BlockPos

data class Context(val pos: BlockPos, val entity: ControllerBlockEntity)
