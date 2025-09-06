package com.braydenoneal.block

import com.braydenoneal.block.entity.ControllerBlockEntity
import com.braydenoneal.block.entity.ModBlockEntities
import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class ControllerBlock(settings: Settings) : BlockWithEntity(settings) {
    init {
        defaultState = getStateManager().getDefaultState().with<Direction, Direction>(Properties.FACING, Direction.UP)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(Properties.FACING, ctx.playerLookDirection.opposite)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec<ControllerBlock> { settings: Settings -> ControllerBlock(settings) }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ControllerBlockEntity(pos, state)
    }

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hit: BlockHitResult): ActionResult {
        if (!world.isClient) {
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        if (!world.isClient) {
            return validateTicker(type, ModBlockEntities.CONTROLLER_BLOCK_ENTITY, ControllerBlockEntity::tick)
        }

        return null
    }
}
