package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.Map


data class GetItemCountBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val itemPredicate = arguments.functionValue(program, "itemPredicate", 0)
        var count = 0

        for (container in program.context().entity!!.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), Map.of())
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

    override val type: ExpressionType<*> get() = ExpressionTypes.GET_ITEM_COUNT_BUILTIN

    companion object {
        val CODEC: MapCodec<GetItemCountBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(GetItemCountBuiltin::arguments)
            ).apply(it, ::GetItemCountBuiltin)
        }
    }
}
