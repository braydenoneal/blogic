package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.*
import program.expression.value.util.Null
import kotlin.math.min

object ExportAllItemsBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        val predicate = get<FunctionValue>("predicate")
        val initialCount = getAny("count", Null.VALUE)
        var count: Int? = null

        if (initialCount is IntegerValue) {
            count = initialCount.value
        }

        val deleteOverflow = get<BooleanValue>("deleteOverflow", BooleanValue(false)).value

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
                val predicateResult = context(predicateArguments) { predicate.call().cast<BooleanValue>() }

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
