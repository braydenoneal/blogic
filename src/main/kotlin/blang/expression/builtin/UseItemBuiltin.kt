package blang.expression.builtin

import blang.Context
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import program.Program
import program.expression.Arguments
import program.expression.value.BooleanValue
import program.expression.value.Value
import kotlin.math.min

object UseItemBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val pos = getBlockPos()
        val predicate = getPredicate()
        val level = getLevel()
        val containers = context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                if (stack.`is`(Items.AIR)) {
                    continue
                }

                if (!getPredicateResult(stack.item, predicate)) {
                    continue
                }

                val facing = context.entity.facing
                val hit = BlockHitResult(Vec3.atCenterOf(pos), facing, pos, false)
                val player = FakePlayer.get(level as ServerLevel)

                var result = level.getBlockState(pos).useItemOn(stack, level, player, InteractionHand.MAIN_HAND, hit)

                if (result is InteractionResult.TryEmptyHandInteraction) {
                    if (stack.useOn(UseOnContext(level, player, InteractionHand.MAIN_HAND, stack, hit)) !is InteractionResult.Pass) {
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
                result = player.mainHandItem.item.use(level, player, InteractionHand.MAIN_HAND)

                if (result is InteractionResult.Success) {
                    player.addItem(result.heldItemTransformedTo()!!)

                    for (playerSlot in 0..<player.inventory.containerSize) {
                        val playerStack = player.inventory.getItem(playerSlot)

                        if (playerStack.`is`(Items.AIR)) {
                            continue
                        }

                        for (container in context.entity.getConnectedContainers()) {
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
