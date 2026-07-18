package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.BooleanValue
import program.expression.value.IntegerValue
import program.expression.value.Value

data class GetItemCountBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 0))
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
