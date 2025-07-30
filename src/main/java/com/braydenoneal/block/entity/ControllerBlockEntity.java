package com.braydenoneal.block.entity;

import com.braydenoneal.Blogic;
import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.parameter.types.VoidParameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity implements NamedScreenHandlerFactory {
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

    public CustomFunction getFunction() {
        return function;
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
    }

    // TODO: Some way to choose between every tick or only on interaction
    public static void tick(World world, BlockPos blockPos, BlockState ignoredBlockState, ControllerBlockEntity entity) {
        if (world.isReceivingRedstonePower(blockPos)) {
            entity.function.call(new Context(world, blockPos, Map.of()), Map.of());
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        Blogic.LOGGER.info("CREATE MENU {}", this);
        return new ControllerScreenHandler(syncId, playerInventory, this);
    }
}
