package com.braydenoneal.block.entity;

import com.braydenoneal.block.CableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RedstoneWriterBlockEntity extends AbstractNetworkBlockEntity {
    private int redstoneValue = 0;
    private String readName = "";

    public RedstoneWriterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_WRITER_BLOCK_ENTITY, pos, state);
    }

    public int getRedstoneValue() {
        return redstoneValue;
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
        int previousRedstoneValue = redstoneValue;
        int nextRedstoneValue = 0;

        ArrayList<BlockPos> connectedSensors = CableBlock.getConnectedSensors(world, pos);

        for (BlockPos sensorPos : connectedSensors) {
            BlockEntity sensorBlock = world.getBlockEntity(sensorPos);

            if (sensorBlock instanceof RedstoneReaderBlockEntity readerBlockEntity
                    && readerBlockEntity.getName().equals(readName)) {
                nextRedstoneValue = readerBlockEntity.getRedstoneValue();
            }
        }

        if (previousRedstoneValue != nextRedstoneValue) {
            redstoneValue = nextRedstoneValue;
            markDirty();
            world.updateNeighbors(pos, state.getBlock());
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("readName", readName);
        view.putInt("redstoneValue", redstoneValue);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        readName = view.getString("readName", "");
        redstoneValue = view.getInt("redstoneValue", 0);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
