package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.value.*

object GetItemCountBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val predicate = get<FunctionValue>("predicate")
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
