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


public class RedstoneReaderBlockEntity extends BlockEntity {
    public RedstoneReaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_READER_BLOCK_ENTITY, pos, state);
    }

    private int redstoneValue = 0;

    public int getRedstoneValue() {
        return redstoneValue;
    }

    public static void tick(World world, BlockPos pos, BlockState state, RedstoneReaderBlockEntity blockEntity) {
        int previousRedstoneValue = blockEntity.redstoneValue;
        int nextRedstoneValue = world.getReceivedRedstonePower(pos.offset(state.get(Properties.FACING)));

        if (previousRedstoneValue != nextRedstoneValue) {
            blockEntity.redstoneValue = nextRedstoneValue;
            blockEntity.markDirty();
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
