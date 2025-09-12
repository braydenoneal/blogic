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
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import java.util.Map
import kotlin.math.min

data class UseItemBuiltin(val arguments: Arguments) : Expression {
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

                val facing = program.context().entity!!.facing
                val hit = BlockHitResult(pos.toCenterPos(), facing, pos, false)
                val player = FakePlayer.get(world as ServerWorld)

                var result = world.getBlockState(pos).onUseWithItem(stack, world, player, Hand.MAIN_HAND, hit)

                if (result is ActionResult.PassToDefaultBlockAction) {
                    if (stack.useOnBlock(ItemUsageContext(world, player, Hand.MAIN_HAND, stack, hit)) !is ActionResult.Pass) {
                        return BooleanValue(true)
                    }
                }

                player.setPosition(pos.toCenterPos().add(0.0, -1.5, 0.0))
                player.inventory.clear()
                val newStack = stack.split(1)

                if (stack.isEmpty) {
                    container.removeStack(slot)
                }

                player.giveItemStack(newStack)
                result = player.mainHandStack.item.use(world, player, Hand.MAIN_HAND)

                if (result is ActionResult.Success) {
                    player.giveItemStack(result.newHandStack)

                    for (playerSlot in 0..<player.inventory.size()) {
                        val playerStack = player.inventory.getStack(playerSlot)

                        if (playerStack.isOf(Items.AIR)) {
                            continue
                        }

                        for (container in program.context().entity!!.getConnectedContainers()) {
                            for (slot in 0..<container.size()) {
                                val stack = container.getStack(slot)

                                if (stack.isOf(Items.AIR)) {
                                    container.setStack(slot, playerStack)
                                    player.inventory.removeStack(playerSlot)
                                    break
                                }

                                if (!playerStack.isOf(stack.item)) {
                                    continue
                                }

                                val move = min(playerStack.count, stack.maxCount - stack.count)

                                if (move <= 0) {
                                    continue
                                }

                                playerStack.decrement(move)
                                stack.increment(move)

                                if (playerStack.isEmpty) {
                                    player.inventory.removeStack(playerSlot)
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

    override val type: ExpressionType<*> get() = ExpressionTypes.USE_ITEM_BUILTIN

    companion object {
        val CODEC: MapCodec<UseItemBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(UseItemBuiltin::arguments)
            ).apply(it, ::UseItemBuiltin)
        }
    }
}
