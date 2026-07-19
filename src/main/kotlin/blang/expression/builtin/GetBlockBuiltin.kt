package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.BlockValue
import net.minecraft.core.BlockPos
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.IntegerValue
import program.expression.value.Value

data class GetBlockBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val x = arguments.get<IntegerValue>(program, "x").value
        val y = arguments.get<IntegerValue>(program, "y").value
        val z = arguments.get<IntegerValue>(program, "z").value

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.level ?: throw RunException("World is null")

        return BlockValue(world.getBlockState(pos).block)
    }
}
