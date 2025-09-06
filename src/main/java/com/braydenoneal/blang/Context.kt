package com.braydenoneal.blang

import com.braydenoneal.block.entity.ControllerBlockEntity
import net.minecraft.util.math.BlockPos

data class Context(val pos: BlockPos, val entity: ControllerBlockEntity?)
