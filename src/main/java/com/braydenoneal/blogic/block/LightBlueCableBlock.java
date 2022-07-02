package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class LightBlueCableBlock extends AbstractCableBlock {
	public LightBlueCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return state.isOf(ModBlocks.CABLE) || state.isOf(ModBlocks.LIGHT_BLUE_CABLE);
	}
}
