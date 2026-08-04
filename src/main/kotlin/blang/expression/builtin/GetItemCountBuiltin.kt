package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.value.BooleanValue
import program.expression.value.FunctionValue
import program.expression.value.IntegerValue
import program.expression.value.Value

object GetItemCountBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        context(program) {
            val program = BlogicProgram.cast(program.actionProgram)
            val predicate = arguments.get<FunctionValue>("predicate")
            var count = 0

            for (container in program.context.entity.getConnectedContainers()) {
                container.iterator().forEachRemaining { stack ->
                    val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                    val predicateResult = context(predicateArguments) { predicate.call().cast<BooleanValue>() }

                    if (predicateResult.value) {
                        count += stack.count
                    }
                }
            }

            return IntegerValue(count)
        }
    }
}
