package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class LightGrayCableBlock extends AbstractCableBlock {
	public LightGrayCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.LIGHT_GRAY_CABLE);
	}
}
