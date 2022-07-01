package com.braydenoneal.blogicmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGateBlock extends FacingBlock {
	private static final IntProperty POWERED_INPUTS = IntProperty.of("powered_inputs", 0, 4);
	private static final BooleanProperty INVERTED = BooleanProperty.of("inverted");

	protected AbstractGateBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState()
				.with(Properties.FACING, Direction.NORTH)
				.with(Properties.POWERED, false)
				.with(POWERED_INPUTS, 0)
				.with(INVERTED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING, Properties.POWERED, POWERED_INPUTS, INVERTED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getPlayerLookDirection().getOpposite();
		return this.getDefaultState().with(Properties.FACING, facing)
				.with(Properties.POWERED, shouldBePowered(ctx.getWorld(), ctx.getBlockPos(), facing))
				.with(POWERED_INPUTS, getPoweredInputs(ctx.getWorld(), ctx.getBlockPos(), facing))
				.with(INVERTED, getInverted(ctx.getWorld(), ctx.getBlockPos(), facing));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		world.updateNeighbors(pos, state.getBlock());
		return state.with(Properties.POWERED, shouldBePowered(world, pos, getFacing(state)))
				.with(POWERED_INPUTS, getPoweredInputs(world, pos, getFacing(state)))
				.with(INVERTED, getInverted(world, pos, getFacing(state)));
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) && direction.equals(getFacing(state)) ? 15 : 0;
	}

	public abstract boolean shouldBePowered(WorldAccess world, BlockPos pos, Direction facing);

	public boolean getInverted(WorldAccess world, BlockPos pos, Direction facing) {
		BlockPos neighborPos = pos.offset(facing);
		BlockState neighborState = world.getBlockState(pos.offset(facing));
		return neighborState.getWeakRedstonePower(world, neighborPos, facing) > 0;
	}

	public int getPoweredInputs(WorldAccess world, BlockPos pos, Direction facing) {
		int poweredInputs = 0;

		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.offset(direction);
			BlockState neighborState = world.getBlockState(neighborPos);
			if (direction != facing && neighborState.getWeakRedstonePower(world, neighborPos, direction) > 0) {
				poweredInputs++;
			}
		}

		return poweredInputs;
	}

	public Direction getFacing(BlockState state) {
		return state.get(Properties.FACING);
	}
}
