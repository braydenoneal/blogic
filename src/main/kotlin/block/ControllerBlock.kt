package block

import block.entity.ControllerBlockEntity
import block.entity.ModBlockEntities
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult

class ControllerBlock(settings: Properties) : BaseEntityBlock(settings) {
    init {
        registerDefaultState(stateDefinition.any().setValue<Direction, Direction>(BlockStateProperties.FACING, Direction.UP))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(BlockStateProperties.FACING, ctx.nearestLookingDirection.opposite)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return simpleCodec<ControllerBlock> { ControllerBlock(it) }
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ControllerBlockEntity(pos, state)
    }

    override fun useWithoutItem(state: BlockState, world: Level, pos: BlockPos, player: Player, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide) {
            val screenHandlerFactory = state.getMenuProvider(world, pos)

            if (screenHandlerFactory != null) {
                player.openMenu(screenHandlerFactory)
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun <T : BlockEntity> getTicker(world: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        if (!world.isClientSide) {
            return createTickerHelper(type, ModBlockEntities.CONTROLLER_BLOCK_ENTITY, ControllerBlockEntity::tick)
        }

        return null
    }
}
