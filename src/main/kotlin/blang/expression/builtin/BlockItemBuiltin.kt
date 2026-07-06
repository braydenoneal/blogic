package blang.expression.builtin

import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Null
import parser.expression.value.Value

data class BlockItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        val item = (blang.expression.BlogicArguments.blockValue(arguments, program, "block", 0) ?: return null).value.asItem()

        return if (item == null) {
            Null.VALUE
        } else {
            _root_ide_package_.blang.expression.value.ItemValue(item)
        }
    }
}
