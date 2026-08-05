package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.BlockValue
import net.minecraft.core.BlockPos
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.Callable
import program.expression.value.IntegerValue
import program.expression.value.Value
import program.expression.value.get

object GetBlockBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.level ?: throw RunException("World is null")

        return BlockValue(world.getBlockState(pos).block)
    }
}
