package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class BlueCableBlock extends AbstractCableBlock {
	public BlueCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.BLUE_CABLE);
	}
}
