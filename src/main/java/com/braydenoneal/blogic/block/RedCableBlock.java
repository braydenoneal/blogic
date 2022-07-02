package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class RedCableBlock extends AbstractCableBlock {
	public RedCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.RED_CABLE);
	}
}
