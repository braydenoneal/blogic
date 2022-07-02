package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class InverterBlock extends AbstractDiodeBlock {
	protected InverterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean shouldBePowered(WorldAccess world, BlockPos pos, Direction facing) {
		BlockPos powerSourcePos = pos.offset(facing);
		BlockState powerSourceState = world.getBlockState(powerSourcePos);
		int powerSourcePower = powerSourceState.getWeakRedstonePower(world, powerSourcePos, facing);
		return powerSourcePower == 0;
	}
}
