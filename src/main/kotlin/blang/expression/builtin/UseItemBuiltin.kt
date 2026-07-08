package blang.expression.builtin

import blang.expression.value.ItemValue
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.Value
import kotlin.math.min

data class UseItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null).value
        val y = (arguments.integerValue(program, "y", 1) ?: return null).value
        val z = (arguments.integerValue(program, "z", 2) ?: return null).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3) ?: return null)

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.level ?: throw RunException("World is null")

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                if (stack.`is`(Items.AIR)) {
                    continue
                }

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                val facing = program.context.entity.facing
                val hit = BlockHitResult(Vec3.atCenterOf(pos), facing, pos, false)
                val player = FakePlayer.get(world as ServerLevel)

                var result = world.getBlockState(pos).useItemOn(stack, world, player, InteractionHand.MAIN_HAND, hit)

                if (result is InteractionResult.TryEmptyHandInteraction) {
                    if (stack.useOn(UseOnContext(world, player, InteractionHand.MAIN_HAND, stack, hit)) !is InteractionResult.Pass) {
                        return BooleanValue(true)
                    }
                }

                player.setPos(Vec3.atCenterOf(pos).add(0.0, -1.5, 0.0))
                player.inventory.clearContent()
                val newStack = stack.split(1)

                if (stack.isEmpty) {
                    container.removeItemNoUpdate(slot)
                }

                player.addItem(newStack)
                result = player.mainHandItem.item.use(world, player, InteractionHand.MAIN_HAND)

                if (result is InteractionResult.Success) {
                    player.addItem(result.heldItemTransformedTo()!!)

                    for (playerSlot in 0..<player.inventory.containerSize) {
                        val playerStack = player.inventory.getItem(playerSlot)

                        if (playerStack.`is`(Items.AIR)) {
                            continue
                        }

                        for (container in program.context.entity.getConnectedContainers()) {
                            for (slot in 0..<container.containerSize) {
                                val stack = container.getItem(slot)

                                if (stack.`is`(Items.AIR)) {
                                    container.setItem(slot, playerStack)
                                    player.inventory.removeItemNoUpdate(playerSlot)
                                    break
                                }

                                if (!playerStack.`is`(stack.item)) {
                                    continue
                                }

                                val move = min(playerStack.count, stack.maxStackSize - stack.count)

                                if (move <= 0) {
                                    continue
                                }

                                playerStack.shrink(move)
                                stack.grow(move)

                                if (playerStack.isEmpty) {
                                    player.inventory.removeItemNoUpdate(playerSlot)
                                    break
                                }
                            }
                        }
                    }
                }

                return BooleanValue(true)
            }
        }

        return BooleanValue(false)
    }
}
