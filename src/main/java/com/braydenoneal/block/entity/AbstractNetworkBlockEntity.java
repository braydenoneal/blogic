package com.braydenoneal.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractNetworkBlockEntity extends BlockEntity {
    public AbstractNetworkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void updateNetwork(World world, BlockPos fromPos) {
    }

    public abstract void update(World world, BlockPos pos, BlockState state);
}
