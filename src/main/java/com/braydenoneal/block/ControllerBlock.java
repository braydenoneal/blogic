package com.braydenoneal.block;

import com.braydenoneal.block.entity.AbstractNetworkBlockEntity;
import com.braydenoneal.block.entity.ControllerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class ControllerBlock extends BlockWithEntity {
    public ControllerBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(Properties.FACING, Direction.UP));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(ControllerBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ControllerBlockEntity(pos, state);
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
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ControllerBlockEntity controllerBlockEntity) {
            return controllerBlockEntity.getEmitRedstoneValue();
        }

        return 0;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }
}
