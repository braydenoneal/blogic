package blang.expression.builtin

import blang.Context
import blang.expression.value.BlockPosValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value

object GetBlockPosBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        return BlockPosValue(context.entity.blockPos)
    }
}
