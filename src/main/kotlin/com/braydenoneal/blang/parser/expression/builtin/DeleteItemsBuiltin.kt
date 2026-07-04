package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.Null
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.Map


data class DeleteItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val itemPredicate = arguments.functionValue(program, "itemPredicate", 0)
        val initialCount = if (arguments.arguments.size > 1 || arguments.namedArguments.containsKey("count")) arguments.integerValue(program, "count", 1).value else null
        var count = initialCount

        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        val containers = program.context().entity!!.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.size()) {
                if (count != null && count <= 0) {
                    return Null.VALUE
                }

                val stack = container.getStack(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), Map.of())
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

    override val type: ExpressionType<*> get() = ExpressionTypes.DELETE_ITEMS_BUILTIN

    companion object {
        val CODEC: MapCodec<DeleteItemsBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(DeleteItemsBuiltin::arguments)
            ).apply(it, ::DeleteItemsBuiltin)
        }
    }
}
