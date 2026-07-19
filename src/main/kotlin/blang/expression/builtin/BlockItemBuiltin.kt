package blang.expression.builtin

import blang.expression.value.BlockValue
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.Value

data class BlockItemBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        return ItemValue(arguments.get<BlockValue>(program, "block").value.asItem())
    }
}
