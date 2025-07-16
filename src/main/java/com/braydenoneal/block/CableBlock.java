package com.braydenoneal.block;

import com.braydenoneal.block.entity.RedstoneReaderBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class CableBlock extends Block {
    public CableBlock(Settings settings) {
        super(settings);
    }

    public static ArrayList<BlockPos> getConnectedSensors(World world, BlockPos pos) {
        Set<BlockPos> cables = new HashSet<>();
        ArrayList<BlockPos> sensors = new ArrayList<>();

        Stack<BlockPos> stack = new Stack<>();
        stack.push(pos);

        while (!stack.isEmpty()) {
            BlockPos currentPos = stack.pop();

            for (Direction direction : DIRECTIONS) {
                BlockPos adjacentPos = currentPos.offset(direction);

                if (cables.contains(adjacentPos)) {
                    continue;
                }

                Block adjacentBlock = world.getBlockState(adjacentPos).getBlock();

                if (adjacentBlock instanceof CableBlock) {
                    stack.push(adjacentPos);
                    cables.add(adjacentPos);
                }

                if (sensors.contains(adjacentPos)) {
                    continue;
                }

                BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);

                if (adjacentBlockEntity instanceof RedstoneReaderBlockEntity) {
                    sensors.add(adjacentPos);
                }
            }
        }

        return sensors;
    }
}
