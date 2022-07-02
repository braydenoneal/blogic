package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class MagentaCableBlock extends AbstractCableBlock {
	public MagentaCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.MAGENTA_CABLE);
	}
}
