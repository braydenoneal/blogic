package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class LimeCableBlock extends AbstractCableBlock {
	public LimeCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.LIME_CABLE);
	}
}
