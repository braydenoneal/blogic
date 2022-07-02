package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class PurpleCableBlock extends AbstractCableBlock {
	public PurpleCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.PURPLE_CABLE);
	}
}
