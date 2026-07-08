package blang.expression.builtin

import blang.expression.value.ItemValue
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.Null
import parser.expression.value.Value

data class DeleteItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 0) ?: return null)
        val initialCount = if (arguments.arguments.size > 1 || arguments.namedArguments.containsKey("count")) (arguments.integerValue(program, "count", 1) ?: return null).value else null
        var count = initialCount

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                if (count != null && count <= 0) {
                    return Null.VALUE
                }

                val stack = container.getItem(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                if (count != null) {
                    if (count - stack.count >= 0) {
                        count -= stack.count
                    } else {
                        stack.shrink(count)
                        return Null.VALUE
                    }
                }

                container.removeItemNoUpdate(slot)
            }
        }

        return Null.VALUE
    }
}
