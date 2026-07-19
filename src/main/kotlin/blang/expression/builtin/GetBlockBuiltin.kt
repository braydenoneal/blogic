package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.BlockValue
import net.minecraft.core.BlockPos
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.Value

data class GetBlockBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val x = (arguments.integerValue(program, "x", 0))
        val y = (arguments.integerValue(program, "y", 1))
        val z = (arguments.integerValue(program, "z", 2))

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x.value, entityPos.y + y.value, entityPos.z + z.value)
        val world = program.context.entity.level ?: throw RunException("World is null")

        return BlockValue(world.getBlockState(pos).block)
    }
}
