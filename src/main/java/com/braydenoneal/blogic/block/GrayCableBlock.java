package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class GrayCableBlock extends AbstractCableBlock {
	public GrayCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.GRAY_CABLE);
	}
}
