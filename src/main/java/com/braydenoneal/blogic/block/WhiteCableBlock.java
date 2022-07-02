package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class WhiteCableBlock extends AbstractCableBlock {
	public WhiteCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.WHITE_CABLE);
	}
}
