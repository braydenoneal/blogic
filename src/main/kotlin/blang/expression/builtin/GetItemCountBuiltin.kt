package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.IntegerValue
import parser.expression.value.Value

data class GetItemCountBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 0) ?: return null)
        var count = 0

        for (container in program.context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (predicateResult.value) {
                    count += stack.count
                }
            }
        }

        return IntegerValue(count)
    }
}
