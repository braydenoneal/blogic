package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class OrangeCableBlock extends AbstractCableBlock {
	public OrangeCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.ORANGE_CABLE);
	}
}
