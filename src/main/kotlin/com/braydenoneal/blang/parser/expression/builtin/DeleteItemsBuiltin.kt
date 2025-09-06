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

        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        val containers = program.context().entity!!.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.size()) {
                val stack = container.getStack(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), Map.of())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                container.removeStack(slot)
            }
        }

        return Null.VALUE
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.DELETE_ITEMS_BUILTIN

    companion object {
        val CODEC: MapCodec<DeleteItemsBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(DeleteItemsBuiltin::arguments)
            ).apply(instance, ::DeleteItemsBuiltin)
        }
    }
}
