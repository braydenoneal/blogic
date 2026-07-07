package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.Null
import parser.expression.value.Value
import kotlin.math.min

data class ExportAllItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null).value
        val y = (arguments.integerValue(program, "y", 1) ?: return null).value
        val z = (arguments.integerValue(program, "z", 2) ?: return null).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3) ?: return null)
        val initialCount = if (arguments.arguments.size > 4 || arguments.namedArguments.containsKey("count")) (arguments.integerValue(program, "count", 4) ?: return null).value else null
        var count = initialCount
        val deleteOverflow = if (arguments.arguments.size > 5 || arguments.namedArguments.containsKey("deleteOverflow")) (arguments.booleanValue(program, "deleteOverflow", 5) ?: return null).value else false

        val world = program.context.entity.getWorld() ?: throw RunException("World is null")

        val entityPos = program.context.pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is LockableContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.size()) {
                val stack = container.getStack(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
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
}
