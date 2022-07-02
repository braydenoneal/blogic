package com.braydenoneal.blogic.block;

import net.minecraft.block.BlockState;

public class CableBlock extends AbstractCableBlock {
	public CableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canConnectToCableOfState(BlockState state) {
		return true;
	}
}
