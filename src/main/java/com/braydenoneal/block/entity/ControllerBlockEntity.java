package com.braydenoneal.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity {
    private String name;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        name = pos.toShortString();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("name", name);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        name = view.getString("name", "");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
    }
}
