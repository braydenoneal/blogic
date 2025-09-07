package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
import java.util.Map


data class PlaceBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val x = arguments.integerValue(program, "x", 0).value
        val y = arguments.integerValue(program, "y", 1).value
        val z = arguments.integerValue(program, "z", 2).value
        val itemPredicate = arguments.functionValue(program, "itemPredicate", 3)

        val entityPos = program.context().pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        if (world.getBlockState(pos).block !== Blocks.AIR) {
            return BooleanValue(false)
        }

        val containers = program.context().entity!!.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.size()) {
                val stack = container.getStack(slot)

                if (stack.isOf(Items.AIR)) {
                    continue
                }

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), Map.of())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                for (entry in BlockItem.BLOCK_ITEMS.entries) {
                    if (!stack.isOf(entry.value)) {
                        continue
                    }

                    stack.decrement(1)

                    if (stack.isEmpty) {
                        container.setStack(slot, ItemStack.EMPTY)
                    } else {
                        container.setStack(slot, stack)
                    }

                    world.setBlockState(pos, entry.key.defaultState)
                    return BooleanValue(true)
                }
            }
        }

        return BooleanValue(false)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.PLACE_BLOCK_BUILTIN

    companion object {
        val CODEC: MapCodec<PlaceBlockBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(PlaceBlockBuiltin::arguments)
            ).apply(it, ::PlaceBlockBuiltin)
        }
    }
}
