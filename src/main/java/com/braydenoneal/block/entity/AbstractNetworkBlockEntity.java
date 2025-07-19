package com.braydenoneal.block.entity;

import com.braydenoneal.Blogic;
import com.braydenoneal.block.CableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static net.minecraft.world.RedstoneView.DIRECTIONS;

public abstract class AbstractNetworkBlockEntity extends BlockEntity {
    public AbstractNetworkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static ArrayList<BlockPos> getConnectedNetworkBlocks(World world, BlockPos fromPos) {
        Set<BlockPos> cables = new HashSet<>();
        ArrayList<BlockPos> networkBlocks = new ArrayList<>();

        Stack<BlockPos> stack = new Stack<>();
        stack.push(fromPos);

        while (!stack.isEmpty()) {
            BlockPos pos = stack.pop();

            for (Direction direction : DIRECTIONS) {
                BlockPos adjacentPos = pos.offset(direction);

                if (cables.contains(adjacentPos)) {
                    continue;
                }

                Block adjacentBlock = world.getBlockState(adjacentPos).getBlock();

                if (adjacentBlock instanceof CableBlock) {
                    stack.push(adjacentPos);
                    cables.add(adjacentPos);
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue;
                }

                BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);

                if (adjacentBlockEntity instanceof AbstractNetworkBlockEntity) {
                    networkBlocks.add(adjacentPos);
                }
            }
        }

        return networkBlocks;
    }

    public static void updateNetwork(World world, BlockPos fromPos) {
        Blogic.LOGGER.info("Updated network from {}", fromPos);
        ArrayList<BlockPos> networkBlocks = getConnectedNetworkBlocks(world, fromPos);

        for (BlockPos pos : networkBlocks) {
            BlockEntity adjacentBlockEntity = world.getBlockEntity(pos);

            if (adjacentBlockEntity instanceof AbstractNetworkBlockEntity) {
                ((AbstractNetworkBlockEntity) adjacentBlockEntity).update(world, pos, world.getBlockState(pos));
            }
        }
    }

    public abstract void update(World world, BlockPos pos, BlockState state);
}
