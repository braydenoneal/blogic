package com.braydenoneal.blogic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDiodeBlock extends FacingBlock {
	protected AbstractDiodeBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState()
				.with(Properties.FACING, Direction.NORTH)
				.with(Properties.POWERED, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING, Properties.POWERED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getPlayerLookDirection().getOpposite();
		return this.getDefaultState().with(Properties.FACING, facing);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered(world, pos, getFacing(state))));
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		world.updateNeighbors(pos, state.getBlock());
		return state.with(Properties.POWERED, shouldBePowered(world, pos, getFacing(state)));
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

	public Direction getFacing(BlockState state) {
		return state.get(Properties.FACING);
	}
}
