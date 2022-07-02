package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class BrownCableBlock extends AbstractCableBlock {
	public BrownCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.BROWN_CABLE);
	}
}
