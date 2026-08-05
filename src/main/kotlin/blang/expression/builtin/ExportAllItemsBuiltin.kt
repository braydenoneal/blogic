package blang.expression.builtin

import blang.Context
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.*
import program.expression.value.util.Null
import kotlin.math.min

object ExportAllItemsBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val pos = getBlockPos()
        val predicate = getPredicate()
        var count = getNullable<IntegerValue>("count")?.value
        val deleteOverflow = get<BooleanValue>("deleteOverflow", BooleanValue(false)).value

        val level = getLevel()
        val exportEntity = level.getBlockEntity(pos)

        if (exportEntity !is BaseContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        val containers = context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                if (!getPredicateResult(stack.item, predicate)) {
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
