package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
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
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null).value
        val y = (arguments.integerValue(program, "y", 1) ?: return null).value
        val z = (arguments.integerValue(program, "z", 2) ?: return null).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3) ?: return null)
        val initialCount = if (arguments.namelessArguments.size > 4 || arguments.namedArguments.containsKey("count")) (arguments.integerValue(program, "count", 4) ?: return null).value else null
        var count = initialCount
        val deleteOverflow = if (arguments.namelessArguments.size > 5 || arguments.namedArguments.containsKey("deleteOverflow")) (arguments.booleanValue(program, "deleteOverflow", 5) ?: return null).value else false

        val world = program.context.entity.level ?: throw RunException("World is null")

        val entityPos = program.context.pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is BaseContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                for (exportSlot in 0..<exportEntity.containerSize) {
                    if (count != null && count <= 0) {
                        return Null.VALUE
                    }

                    val exportStack = exportEntity.getItem(exportSlot)

                    if (exportStack.`is`(Items.AIR)) {
                        if (count != null) {
                            if (count - stack.count >= 0) {
                                count -= stack.count
                            } else {
                                stack.shrink(count)

                                val newStack = stack.copy()
                                newStack.count = count
                                exportEntity.setItem(exportSlot, newStack)

                                return Null.VALUE
                            }
                        }

                        container.removeItemNoUpdate(slot)
                        exportEntity.setItem(exportSlot, stack)
                        break
                    }

                    if (!exportStack.`is`(stack.item)) {
                        continue
                    }

                    var move = min(stack.count, exportStack.maxStackSize - exportStack.count)

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

                    stack.shrink(move)
                    exportStack.grow(move)

                    if (stack.isEmpty) {
                        container.removeItemNoUpdate(slot)
                        exportEntity.setItem(exportSlot, exportStack)
                        break
                    }
                }

                if (deleteOverflow) {
                    container.removeItemNoUpdate(slot)
                }
            }
        }

        return Null.VALUE
    }
}
