package com.braydenoneal.block.entity;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.types.NotFunction;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity {
    // TODO: Replace this name with the anvil name
    private String name;
    private int emitRedstoneValue;
    private Function function;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        name = pos.toShortString();
        emitRedstoneValue = 0;
        function = new NotFunction(Either.left(new BooleanTerminal(false)));
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        name = view.getString("name", "");
        emitRedstoneValue = view.getInt("emitRedstoneValue", 0);
        function = view.read("function", Function.CODEC).orElse(new NotFunction(Either.left(new BooleanTerminal(false))));
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("name", name);
        view.putInt("emitRedstoneValue", emitRedstoneValue);
        view.put("function", Function.CODEC, function);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
        function.call(world, pos, Map.of());
    }

    public int getEmitRedstoneValue() {
        return emitRedstoneValue;
    }

    public void setEmitRedstoneValue(int emitRedstoneValue) {
        this.emitRedstoneValue = emitRedstoneValue;
        markDirty();
    }
}
