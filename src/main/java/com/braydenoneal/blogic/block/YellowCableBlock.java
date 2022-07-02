package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class YellowCableBlock extends AbstractCableBlock {
	public YellowCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.YELLOW_CABLE);
	}
}
