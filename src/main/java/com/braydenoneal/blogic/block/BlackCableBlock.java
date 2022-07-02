package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class BlackCableBlock extends AbstractCableBlock {
	public BlackCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.BLACK_CABLE);
	}
}
