package com.braydenoneal.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CableBlock extends Block {
    public CableBlock(Settings settings) {
        super(settings);
    }

    public List<Block> getConnectedDevices(World world, BlockPos pos) {
        List<Block> blocks = List.of();
        return blocks;
    }
}
