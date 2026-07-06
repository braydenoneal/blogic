package com.braydenoneal.blang.wrapper.expression.builtin

import com.braydenoneal.blang.wrapper.BlogicProgram
import com.braydenoneal.blang.wrapper.expression.value.BlockValue
import net.minecraft.util.math.BlockPos
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class GetBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null)
        val y = (arguments.integerValue(program, "y", 1) ?: return null)
        val z = (arguments.integerValue(program, "z", 2) ?: return null)

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x.value, entityPos.y + y.value, entityPos.z + z.value)
        val world = program.context.entity.getWorld() ?: throw RunException("World is null")

        return BlockValue(world.getBlockState(pos).block)
    }
}
