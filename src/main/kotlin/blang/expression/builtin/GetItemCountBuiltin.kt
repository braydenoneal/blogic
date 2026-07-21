package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.BooleanValue
import program.expression.value.FunctionValue
import program.expression.value.IntegerValue
import program.expression.value.Value

data class GetItemCountBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun innerEvaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val predicate = arguments.get<FunctionValue>(program, "predicate")
        var count = 0

        for (container in program.context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = predicate.call(program, predicateArguments).cast<BooleanValue>()

                if (predicateResult.value) {
                    count += stack.count
                }
            }
        }

        return IntegerValue(count)
    }
}
