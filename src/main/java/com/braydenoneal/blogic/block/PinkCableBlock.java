package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class PinkCableBlock extends AbstractCableBlock {
	public PinkCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.PINK_CABLE);
	}
}
