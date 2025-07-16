package com.braydenoneal.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneReaderBlockEntity extends AbstractNetworkBlockEntity {
    private int redstoneValue = 0;
    private String name;

    public RedstoneReaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_READER_BLOCK_ENTITY, pos, state);
        name = pos.toShortString();
    }

    public int getRedstoneValue() {
        return redstoneValue;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
        int previousRedstoneValue = redstoneValue;
        int nextRedstoneValue = world.getReceivedRedstonePower(pos.offset(state.get(Properties.FACING)));

        if (previousRedstoneValue != nextRedstoneValue) {
            redstoneValue = nextRedstoneValue;
            markDirty();
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("name", name);
        view.putInt("redstoneValue", redstoneValue);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        name = view.getString("name", "");
        redstoneValue = view.getInt("redstoneValue", 0);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
