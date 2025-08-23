package com.braydenoneal.block.entity;

import com.braydenoneal.blang.Context;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.Scope;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.block.CableBlock;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.world.RedstoneView.DIRECTIONS;

public class ControllerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {
    private Program program;
    private Map<String, Value<?>> variables;
    private String source;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        source = "name;";
        program = new Program(source, new Context(world, pos, this));
        variables = Map.of();
    }

    public String source() {
        return source;
    }

    public Program program() {
        return program;
    }

    public void setSource(String source) {
        this.source = source;
        program = new Program(source, new Context(world, pos, this));
        program.run();
        variables = program.topScope().variables();
        markDirty();
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        variables = view.read("variables", Scope.VARIABLES_CODEC).orElse(Map.of());
        source = view.read("source", Codec.STRING).orElse("name;");

        program = new Program(source, new Context(world, pos, this));
        program.topScope().setVariables(variables);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.put("variables", Scope.VARIABLES_CODEC, variables);
        view.put("source", Codec.STRING, source);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public static void tick(World world, BlockPos blockPos, BlockState ignoredBlockState, ControllerBlockEntity entity) {
        if (world.isReceivingRedstonePower(blockPos)) {
            entity.program.runMain();
            entity.variables = entity.program.topScope().variables();
            entity.markDirty();
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ControllerScreenHandler(syncId, playerInventory, pos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }

    public List<ControllerBlockEntity> getConnectedControllerBlockEntities() {
        Set<BlockPos> cables = new HashSet<>();
        List<BlockPos> networkBlocks = new ArrayList<>();

        Stack<BlockPos> stack = new Stack<>();
        stack.push(pos);

        while (!stack.isEmpty()) {
            BlockPos pos = stack.pop();

            for (Direction direction : DIRECTIONS) {
                BlockPos adjacentPos = pos.offset(direction);

                if (cables.contains(adjacentPos)) {
                    continue;
                }

                assert world != null;
                Block adjacentBlock = world.getBlockState(adjacentPos).getBlock();

                if (adjacentBlock instanceof CableBlock) {
                    stack.push(adjacentPos);
                    cables.add(adjacentPos);
                }

                if (networkBlocks.contains(adjacentPos)) {
                    continue;
                }

                BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);

                if (adjacentBlockEntity instanceof ControllerBlockEntity) {
                    networkBlocks.add(adjacentPos);
                }
            }
        }

        List<ControllerBlockEntity> controllers = new ArrayList<>();

        for (BlockPos pos : networkBlocks) {
            BlockEntity adjacentBlockEntity = world.getBlockEntity(pos);

            if (adjacentBlockEntity instanceof ControllerBlockEntity controller) {
                controllers.add(controller);
            }
        }

        return controllers;
    }
}
