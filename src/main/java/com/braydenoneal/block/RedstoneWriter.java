package com.braydenoneal.block;

import com.braydenoneal.block.entity.AbstractNetworkBlockEntity;
import com.braydenoneal.block.entity.RedstoneWriterBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class RedstoneWriter extends BlockWithEntity {
    public RedstoneWriter(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(Properties.FACING, Direction.UP));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection());
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(RedstoneWriter::new);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneWriterBlockEntity(pos, state);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        AbstractNetworkBlockEntity.updateNetwork(world, pos);
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        int redstoneValue = 0;

        if (world.getBlockEntity(pos) instanceof RedstoneWriterBlockEntity redstoneWriterBlockEntity) {
            redstoneValue = redstoneWriterBlockEntity.getRedstoneValue();
        }

        return direction.getOpposite() == state.get(Properties.FACING) ? redstoneValue : 0;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getStrongRedstonePower(state, world, pos, direction);
    }
}
