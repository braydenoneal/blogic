package com.braydenoneal.blogicmod.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class OrGateBlock extends AbstractGateBlock {
	protected OrGateBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean shouldBePowered(WorldAccess world, BlockPos pos, Direction facing) {
		return this.getPoweredInputs(world, pos, facing) > 0 ^ this.getInverted(world, pos, facing);
	}
}
