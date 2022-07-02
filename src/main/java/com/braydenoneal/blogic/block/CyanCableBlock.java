package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class CyanCableBlock extends AbstractCableBlock {
	public CyanCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.CYAN_CABLE);
	}
}
