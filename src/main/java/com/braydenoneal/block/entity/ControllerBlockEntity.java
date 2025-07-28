package com.braydenoneal.block.entity;

import com.braydenoneal.Blogic;
import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.parameter.types.VoidParameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity {
    private CustomFunction function;
    private Map<String, Terminal> variables;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        function = new CustomFunction("main", new VoidParameter(), Map.of(), List.of());
        variables = Map.of();
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        function = view.read("function", CustomFunction.CODEC).orElse(new CustomFunction("main", new VoidParameter(), Map.of(), List.of()));
        variables = view.read("variables", Codec.unboundedMap(Codec.STRING, Terminal.CODEC)).orElse(Map.of());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.put("function", CustomFunction.CODEC, function);
        view.put("variables", Codec.unboundedMap(Codec.STRING, Terminal.CODEC), variables);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public Map<String, Terminal> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Terminal> variables) {
        this.variables = variables;
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
    }

    public static void tick(World world, BlockPos blockPos, BlockState ignoredBlockState, ControllerBlockEntity entity) {
        if (world.isReceivingRedstonePower(blockPos)) {
            Terminal terminal = entity.function.call(new Context(world, blockPos, Map.of()), Map.of());
            Blogic.LOGGER.info("Terminal: {}", terminal);
        }
    }
}
