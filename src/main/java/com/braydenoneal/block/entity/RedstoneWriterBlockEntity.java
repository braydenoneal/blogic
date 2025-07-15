package com.braydenoneal.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class RedstoneWriterBlockEntity extends BlockEntity {
    public RedstoneWriterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_WRITER_BLOCK_ENTITY, pos, state);
    }

    private int redstoneValue = 0;

    public int getRedstoneValue() {
        return redstoneValue;
    }

    public void update(World world, BlockPos pos, BlockState state) {
        int previousRedstoneValue = redstoneValue;

        BlockEntity facingBlock = world.getBlockEntity(pos.offset(state.get(Properties.FACING).getOpposite()));

        int nextRedstoneValue = 0;

        if (facingBlock instanceof RedstoneReaderBlockEntity readerBlockEntity) {
            nextRedstoneValue = readerBlockEntity.getRedstoneValue();
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
        view.putInt("redstoneValue", redstoneValue);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        redstoneValue = view.getInt("redstoneValue", 0);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
