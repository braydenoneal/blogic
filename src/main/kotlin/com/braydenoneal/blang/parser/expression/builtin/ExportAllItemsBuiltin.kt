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
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
import java.util.Map
import kotlin.math.min


data class ExportAllItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val x = arguments.integerValue(program, "x", 0).value
        val y = arguments.integerValue(program, "y", 1).value
        val z = arguments.integerValue(program, "z", 2).value
        val itemPredicate = arguments.functionValue(program, "itemPredicate", 3)
        val initialCount = if (arguments.arguments.size > 4 || arguments.namedArguments.containsKey("count")) arguments.integerValue(program, "count", 4).value else null
        var count = initialCount
        val deleteOverflow = if (arguments.arguments.size > 5 || arguments.namedArguments.containsKey("deleteOverflow")) arguments.booleanValue(program, "deleteOverflow", 5).value else false

        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        val entityPos = program.context().pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is LockableContainerBlockEntity) {
            throw RunException("Block at position is not a container")
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

                for (exportSlot in 0..<exportEntity.size()) {
                    if (count != null && count <= 0) {
                        return Null.VALUE
                    }

                    val exportStack = exportEntity.getStack(exportSlot)

                    if (exportStack.isOf(Items.AIR)) {
                        if (count != null) {
                            if (count - stack.count >= 0) {
                                count -= stack.count
                            } else {
                                stack.decrement(count)

                                val newStack = stack.copy()
                                newStack.count = count
                                exportEntity.setStack(exportSlot, newStack)

                                return Null.VALUE
                            }
                        }

                        container.removeStack(slot)
                        exportEntity.setStack(exportSlot, stack)
                        break
                    }

                    if (!exportStack.isOf(stack.item)) {
                        continue
                    }

                    var move = min(stack.count, exportStack.maxCount - exportStack.count)

                    if (move <= 0) {
                        continue
                    }

                    if (count != null) {
                        if (count - move >= 0) {
                            count -= move
                        } else {
                            move = count
                            count = 0
                        }
                    }

                    stack.decrement(move)
                    exportStack.increment(move)

                    if (stack.isEmpty) {
                        container.removeStack(slot)
                        exportEntity.setStack(exportSlot, exportStack)
                        break
                    }
                }

                if (deleteOverflow) {
                    container.removeStack(slot)
                }
            }
        }

        return Null.VALUE
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.EXPORT_ALL_ITEMS_BUILTIN

    companion object {
        val CODEC: MapCodec<ExportAllItemsBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(ExportAllItemsBuiltin::arguments)
            ).apply(it, ::ExportAllItemsBuiltin)
        }
    }
}
