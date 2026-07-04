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
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.util.math.BlockPos
import java.util.Map


data class ReadItemCountBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val x = arguments.integerValue(program, "x", 0).value
        val y = arguments.integerValue(program, "y", 1).value
        val z = arguments.integerValue(program, "z", 2).value
        val itemPredicate = arguments.functionValue(program, "itemPredicate", 3)
        var count = 0

        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        val entityPos = program.context().pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is LockableContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        exportEntity.iterator().forEachRemaining { stack ->
            val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), Map.of())
            val predicateResult = itemPredicate.call(program, predicateArguments)

            if (predicateResult !is BooleanValue) {
                throw RunException("itemPredicate is not a predicate")
            }

            if (predicateResult.value) {
                count += stack.count
            }
        }

        return IntegerValue(count)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.READ_ITEM_COUNT_BUILTIN

    companion object {
        val CODEC: MapCodec<ReadItemCountBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(ReadItemCountBuiltin::arguments)
            ).apply(it, ::ReadItemCountBuiltin)
        }
    }
}
