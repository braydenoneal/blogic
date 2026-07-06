package blang

import block.entity.ControllerBlockEntity
import net.minecraft.util.math.BlockPos

data class Context(val pos: BlockPos, val entity: ControllerBlockEntity)
