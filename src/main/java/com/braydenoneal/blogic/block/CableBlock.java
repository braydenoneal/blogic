package com.braydenoneal.blogic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		CableGroup cableGroup = new CableGroup(world, pos);
		cableGroup.setPowerOfCableGroup(world, cableGroup.shouldBePowered);
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWERED) && state.get(DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction.getOpposite())) ? 15 : 0;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		world.setBlockState(pos, this.getStateWithUpdatedShape(state, world, pos), 3);
		CableGroup cableGroup = new CableGroup(world, pos);
		cableGroup.setPowerOfCableGroup(world, cableGroup.shouldBePowered);
		return this.getStateWithUpdatedShape(state, world, pos);
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 0.6F;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.union(
				Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
				Block.createCuboidShape(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D),
				Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
				Block.createCuboidShape(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D),
				Block.createCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D),
				Block.createCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
		);
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

	public static class CableGroup {
		public final Set<BlockPos> cableGroup = new HashSet<>();
		public boolean shouldBePowered = false;

		public CableGroup(WorldAccess world, BlockPos startingPos) {
			this.addToCableGroupAt(world, startingPos);
		}

		public void addToCableGroupAt(WorldAccess world, BlockPos pos) {
			cableGroup.add(pos);

			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = pos.offset(direction);
				BlockState neighborState = world.getBlockState(neighborPos);

				if (!cableGroup.contains(neighborPos)) {
					if (neighborState.isOf(ModBlocks.CABLE)) {
						this.addToCableGroupAt(world, neighborPos);
					} else if (neighborState.getWeakRedstonePower(world, pos, direction) > 0) {
						BlockState state = world.getBlockState(pos);
						if (state.isOf(ModBlocks.CABLE)) {
							shouldBePowered = state.get(DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction));
						}
					}
				}
			}
		}

		public void setPowerOfCableGroup(WorldAccess world, boolean powered) {
			for (BlockPos pos : cableGroup) {
				BlockState state = world.getBlockState(pos);
				if (state.isOf(ModBlocks.CABLE)) {
					world.setBlockState(pos, state.with(POWERED, powered), 3);
				}
			}
		}
	}
}
