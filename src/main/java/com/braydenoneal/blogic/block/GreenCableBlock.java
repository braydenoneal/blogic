package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class GreenCableBlock extends AbstractCableBlock {
	public GreenCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.GREEN_CABLE);
	}
}
