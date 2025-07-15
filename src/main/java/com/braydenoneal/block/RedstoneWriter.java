package com.braydenoneal.block;

import com.braydenoneal.block.entity.ModBlockEntities;
import com.braydenoneal.block.entity.RedstoneWriterBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RedstoneWriter extends BlockWithEntity {
    public RedstoneWriter(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(Properties.POWERED, false)
                .with(Properties.FACING, Direction.UP)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED, Properties.FACING);
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.REDSTONE_WRITER_BLOCK_ENTITY, RedstoneWriterBlockEntity::tick);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (direction.getOpposite() == state.get(Properties.FACING) && state.get(Properties.POWERED)) ? 15 : 0;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (direction.getOpposite() == state.get(Properties.FACING) && state.get(Properties.POWERED)) ? 15 : 0;
    }
}
