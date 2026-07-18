package blang.expression.builtin

import blang.expression.BlogicArguments.blockValue
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.Value

data class BlockItemBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        return ItemValue((blockValue(arguments, program, "block", 0)).value.asItem())
    }
}
