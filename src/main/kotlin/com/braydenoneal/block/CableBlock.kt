package com.braydenoneal.block

import com.braydenoneal.block.entity.ControllerBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView
import java.util.Map

class CableBlock(settings: Settings) : Block(settings) {
    init {
        defaultState = getStateManager().getDefaultState()
            .with(UP, false)
            .with(DOWN, false)
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST)
    }

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return getShape(state)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return getShape(state)
    }

    private fun getShape(state: BlockState): VoxelShape {
        var shape = createCuboidShape(5.0, 5.0, 5.0, 11.0, 11.0, 11.0)

        val directionShapeMap = Map.of(
            Direction.UP, createCuboidShape(5.0, 11.0, 5.0, 11.0, 16.0, 11.0),
            Direction.DOWN, createCuboidShape(5.0, 0.0, 5.0, 11.0, 5.0, 11.0),
            Direction.NORTH, createCuboidShape(5.0, 5.0, 0.0, 11.0, 11.0, 5.0),
            Direction.EAST, createCuboidShape(11.0, 5.0, 5.0, 16.0, 11.0, 11.0),
            Direction.SOUTH, createCuboidShape(5.0, 5.0, 11.0, 11.0, 11.0, 16.0),
            Direction.WEST, createCuboidShape(0.0, 5.0, 5.0, 5.0, 11.0, 11.0)
        )

        for (direction in DIRECTIONS) {
            if (state.get(DIRECTION_BOOLEAN_PROPERTY_MAP[direction])) {
                shape = VoxelShapes.union(shape, directionShapeMap[direction])
            }
        }

        return shape
    }

    override fun getStateForNeighborUpdate(state: BlockState, world: WorldView, tickView: ScheduledTickView, pos: BlockPos, fromDirection: Direction, neighborPos: BlockPos, neighborState: BlockState, random: Random): BlockState {
        return getUpdatedState(state, world, pos)
    }

    private fun getUpdatedState(state: BlockState, world: WorldView, pos: BlockPos): BlockState {
        var newState = state

        for (direction in DIRECTIONS) {
            val adjacentPos = pos.offset(direction)
            val adjacentState = world.getBlockState(adjacentPos)
            val adjacentBlockEntity = world.getBlockEntity(adjacentPos)
            val connectSide = adjacentState.block is CableBlock || adjacentBlockEntity is ControllerBlockEntity || adjacentBlockEntity is LockableContainerBlockEntity
            newState = newState.with(DIRECTION_BOOLEAN_PROPERTY_MAP[direction], connectSide)
        }

        return newState
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        super.onPlaced(world, pos, state, placer, itemStack)
        world.setBlockState(pos, getUpdatedState(state, world, pos))
    }

    companion object {
        val UP: BooleanProperty = Properties.UP
        val DOWN: BooleanProperty = Properties.DOWN
        val NORTH: BooleanProperty = Properties.NORTH
        val EAST: BooleanProperty = Properties.EAST
        val SOUTH: BooleanProperty = Properties.SOUTH
        val WEST: BooleanProperty = Properties.WEST

        val DIRECTION_BOOLEAN_PROPERTY_MAP: MutableMap<Direction, BooleanProperty> =
            Map.of(
                Direction.UP, UP,
                Direction.DOWN, DOWN,
                Direction.NORTH, NORTH,
                Direction.EAST, EAST,
                Direction.SOUTH, SOUTH,
                Direction.WEST, WEST
            )

        fun settings(): Settings {
            return Settings.create().nonOpaque()
        }
    }
}
