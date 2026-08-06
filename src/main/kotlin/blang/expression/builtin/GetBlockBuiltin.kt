package blang.expression.builtin

import blang.Context
import blang.expression.value.block.BlockValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value

object GetBlockBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        return BlockValue(getLevel().getBlockState(getBlockPos()).block)
    }
}
