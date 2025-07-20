package com.braydenoneal.block;

import com.braydenoneal.block.entity.AbstractNetworkBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CableBlock extends Block {
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;

    public static final Map<Direction, BooleanProperty> DIRECTION_BOOLEAN_PROPERTY_MAP = Map.of(
            Direction.UP, UP,
            Direction.DOWN, DOWN,
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST
    );

    public CableBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(UP, false)
                .with(DOWN, false)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
        );
    }

    public static AbstractBlock.Settings settings() {
        return AbstractBlock.Settings.create().nonOpaque();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    private VoxelShape getShape(BlockState state) {
        VoxelShape shape = Block.createCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

        Map<Direction, VoxelShape> directionShapeMap = Map.of(
                Direction.UP, Block.createCuboidShape(5.0D, 11.0D, 5.0D, 11.0D, 16.0D, 11.0D),
                Direction.DOWN, Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D),
                Direction.NORTH, Block.createCuboidShape(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 5.0D),
                Direction.EAST, Block.createCuboidShape(11.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D),
                Direction.SOUTH, Block.createCuboidShape(5.0D, 5.0D, 11.0D, 11.0D, 11.0D, 16.0D),
                Direction.WEST, Block.createCuboidShape(0.0D, 5.0D, 5.0D, 5.0D, 11.0D, 11.0D)
        );

        for (Direction direction : DIRECTIONS) {
            if (state.get(DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction))) {
                shape = VoxelShapes.union(shape, directionShapeMap.get(direction));
            }
        }

        return shape;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction fromDirection, BlockPos neighborPos, BlockState neighborState, Random random) {
        return getUpdatedState(state, world, pos);
    }

    private BlockState getUpdatedState(BlockState state, WorldView world, BlockPos pos) {
        BlockState newState = state;

        for (Direction direction : DIRECTIONS) {
            BlockPos adjacentPos = pos.offset(direction);
            BlockState adjacentState = world.getBlockState(adjacentPos);
            BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);

            boolean connectSide = adjacentState.getBlock() instanceof CableBlock ||
                    adjacentBlockEntity instanceof AbstractNetworkBlockEntity;

            newState = newState.with(DIRECTION_BOOLEAN_PROPERTY_MAP.get(direction), connectSide);
        }

        return newState;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos, getUpdatedState(state, world, pos));
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
        AbstractNetworkBlockEntity.updateNetwork(world, pos);
    }
}
