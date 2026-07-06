package blang.expression.builtin

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
            for (slot in 0..<container.size()) {
                if (count != null && count <= 0) {
                    return Null.VALUE
                }

                val stack = container.getStack(slot)

                val predicateArguments = Arguments(mutableListOf(_root_ide_package_.blang.expression.value.ItemValue(stack.item)), mutableMapOf())
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
                        stack.decrement(count)
                        return Null.VALUE
                    }
                }

                container.removeStack(slot)
            }
        }

        return Null.VALUE
    }
}
