package com.braydenoneal.blogic.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class XorGateBlock extends AbstractGateBlock {
	protected XorGateBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean shouldBePowered(WorldAccess world, BlockPos pos, Direction facing) {
		return this.getPoweredInputs(world, pos, facing) == 1 ^ this.getInverted(world, pos, facing);
	}
}
