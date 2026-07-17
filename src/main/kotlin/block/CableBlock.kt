package block

import block.entity.ControllerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class CableBlock(settings: Properties) : Block(settings) {
    init {
        registerDefaultState(stateDefinition.any().setValue(UP, false).setValue(DOWN, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST)
    }

    override fun getCollisionShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return getShape(state)
    }

    override fun getShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return getShape(state)
    }

    private fun getShape(state: BlockState): VoxelShape {
        var shape = box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0)

        val directionShapeMap = mapOf(
            Direction.UP to box(5.0, 11.0, 5.0, 11.0, 16.0, 11.0),
            Direction.DOWN to box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0),
            Direction.NORTH to box(5.0, 5.0, 0.0, 11.0, 11.0, 5.0),
            Direction.EAST to box(11.0, 5.0, 5.0, 16.0, 11.0, 11.0),
            Direction.SOUTH to box(5.0, 5.0, 11.0, 11.0, 11.0, 16.0),
            Direction.WEST to box(0.0, 5.0, 5.0, 5.0, 11.0, 11.0),
        )

        for (direction in UPDATE_SHAPE_ORDER) {
            if (state.getValue(DIRECTION_BOOLEAN_PROPERTY_MAP[direction]!!)) {
                shape = Shapes.or(shape, directionShapeMap[direction]!!)
            }
        }

        return shape
    }

    override fun updateShape(state: BlockState, world: LevelReader, tickView: ScheduledTickAccess, pos: BlockPos, fromDirection: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource): BlockState {
        return getUpdatedState(state, world, pos)
    }

    private fun getUpdatedState(state: BlockState, world: LevelReader, pos: BlockPos): BlockState {
        var newState = state

        for (direction in UPDATE_SHAPE_ORDER) {
            val adjacentPos = pos.relative(direction)
            val adjacentState = world.getBlockState(adjacentPos)
            val adjacentBlockEntity = world.getBlockEntity(adjacentPos)
            val connectSide = adjacentState.block is CableBlock || adjacentBlockEntity is ControllerBlockEntity || adjacentBlockEntity is BaseContainerBlockEntity
            newState = newState.setValue(DIRECTION_BOOLEAN_PROPERTY_MAP[direction]!!, connectSide)
        }

        return newState
    }

    override fun setPlacedBy(world: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack)
        world.setBlockAndUpdate(pos, getUpdatedState(state, world, pos))
    }

    companion object {
        val UP: Property<Boolean> = BlockStateProperties.UP
        val DOWN: Property<Boolean> = BlockStateProperties.DOWN
        val NORTH: Property<Boolean> = BlockStateProperties.NORTH
        val EAST: Property<Boolean> = BlockStateProperties.EAST
        val SOUTH: Property<Boolean> = BlockStateProperties.SOUTH
        val WEST: Property<Boolean> = BlockStateProperties.WEST

        val DIRECTION_BOOLEAN_PROPERTY_MAP: MutableMap<Direction, Property<Boolean>> = mutableMapOf(
            Pair(Direction.UP, UP),
            Pair(Direction.DOWN, DOWN),
            Pair(Direction.NORTH, NORTH),
            Pair(Direction.EAST, EAST),
            Pair(Direction.SOUTH, SOUTH),
            Pair(Direction.WEST, WEST),
        )

        fun settings(): Properties {
            return Properties.ofFullCopy(Blocks.STONE).noOcclusion()
        }
    }
}
