package com.braydenoneal.blogicmod.block;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CableBlock extends Block {
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final BooleanProperty POWERED = Properties.POWERED;

	public static final Map<Direction, BooleanProperty> DIRECTION_BOOLEAN_PROPERTY_MAP = Map.of(
			Direction.UP, UP,
			Direction.DOWN, DOWN,
			Direction.NORTH, NORTH,
			Direction.EAST, EAST,
			Direction.SOUTH, SOUTH,
			Direction.WEST, WEST
	);

	public CableBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState()
				.with(UP, true)
				.with(DOWN, true)
				.with(NORTH, true)
				.with(EAST, true)
				.with(SOUTH, true)
				.with(WEST, true)
				.with(POWERED, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, POWERED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getStateWithUpdatedShape(this.getDefaultState(), ctx.getWorld(), ctx.getBlockPos());
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return this.getStateWithUpdatedShape(state, world, pos);
	}

	public BlockState getStateWithUpdatedShape(BlockState state, WorldAccess world, BlockPos pos) {
		BlockState newState = state;
		int connectedSides = 0;
		BooleanProperty sideToSetTrue = NORTH;

		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.offset(direction);
			BlockState neighborState = world.getBlockState(neighborPos);
			boolean neighborEmitsRedstonePower = neighborState.emitsRedstonePower();
			if (neighborEmitsRedstonePower) {
				connectedSides++;
				sideToSetTrue = DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction.getOpposite());
			}
			newState = newState.with(DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction), neighborEmitsRedstonePower);
		}

		if (connectedSides == 1) {
			newState = newState.with(sideToSetTrue, true);
		} else if (connectedSides == 0) {
			newState = this.getDefaultState().with(POWERED, newState.get(POWERED));
		}

		return newState;
	}
}
