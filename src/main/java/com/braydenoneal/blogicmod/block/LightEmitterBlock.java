package com.braydenoneal.blogicmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LightEmitterBlock extends FacingBlock {
	protected LightEmitterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(Properties.LIT, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.LIT);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.LIT, shouldBeLit(ctx.getWorld(), ctx.getBlockPos()));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		world.updateNeighbors(pos, state.getBlock());
		return state.with(Properties.LIT, shouldBeLit(world, pos));
	}

	private boolean shouldBeLit(WorldAccess world, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.offset(direction);
			BlockState neighborState = world.getBlockState(neighborPos);
			int neighborRedstonePower = neighborState.getWeakRedstonePower(world, neighborPos, direction);
			if (neighborRedstonePower > 0) return true;
		}
		return false;
	}
}
