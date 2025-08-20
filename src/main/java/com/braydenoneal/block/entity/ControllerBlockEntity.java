package com.braydenoneal.block.entity;

import com.braydenoneal.blang.Context;
import com.braydenoneal.blang.parser.Program;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {
    private Program program;
    private String source;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        source = "";
        program = new Program("", new Context(world, pos, this));
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        source = view.read("source", Codec.STRING).orElse("");

        program = new Program(source, new Context(world, pos, this));
        program.run();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.put("source", Codec.STRING, source);

        program = new Program(source, new Context(world, pos, this));
        program.run();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public static void tick(World world, BlockPos blockPos, BlockState ignoredBlockState, ControllerBlockEntity entity) {
        if (world.isReceivingRedstonePower(blockPos)) {
            entity.program.runMain();
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public String source() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        markDirty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ControllerScreenHandler(syncId, playerInventory, pos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }
}
