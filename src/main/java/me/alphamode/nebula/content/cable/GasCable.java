package me.alphamode.nebula.content.cable;

import me.alphamode.nebula.content.AllBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class GasCable extends Block {
    private static final VoxelShape NODE = Block.createCuboidShape(5, 5, 5, 11, 11, 11);
    private static final VoxelShape N_UP = Block.createCuboidShape(5.5, 5.5, 5.5, 10.5, 16, 10.5);
    private static final VoxelShape N_DOWN = Block.createCuboidShape(5.5, 0, 5.5, 10.5, 5.5, 10.5);
    private static final VoxelShape N_NORTH = Block.createCuboidShape(5.5, 5.5, 0, 10.5, 10.5, 5.5);
    private static final VoxelShape N_SOUTH = Block.createCuboidShape(5.5, 5.5, 5.5, 10.5, 10.5, 16);
    private static final VoxelShape N_EAST = Block.createCuboidShape(5.5, 5.5, 5.5, 16, 10.5, 10.5);
    private static final VoxelShape N_WEST = Block.createCuboidShape(0, 5.5, 5.5, 10.5, 10.5, 10.5);

    public GasCable() {
        super(FabricBlockSettings.of(Material.METAL));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.UP, false)
                .with(Properties.DOWN, false)
                .with(Properties.NORTH, false)
                .with(Properties.SOUTH, false)
                .with(Properties.EAST, false)
                .with(Properties.WEST, false));
    }

    public boolean canConnect(World world, BlockPos pos, Direction direction) {
        if(world.getBlockState(pos.offset(direction)).isOf(AllBlocks.BASIC_GAS_CABLE) || (!(world.isClient()) && FluidStorage.SIDED.find(world, pos.offset(direction), direction.getOpposite()) != null))
            return true;
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.UP, Properties.DOWN, Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        for (Direction dir : Direction.values()) {
            if (canConnect(ctx.getWorld(), ctx.getBlockPos(), dir)) {
                state = state.with(getFacing(dir), true);
            }
        }
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.getBlockState(neighborPos).isOf(AllBlocks.BASIC_GAS_CABLE))
            return state.with(getFacing(direction), true);
        return state.with(getFacing(direction), false);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape nodeShape = NODE;
        if (state.get(Properties.UP))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_UP, BooleanBiFunction.OR);
        if (state.get(Properties.DOWN))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_DOWN, BooleanBiFunction.OR);
        if (state.get(Properties.NORTH))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_NORTH, BooleanBiFunction.OR);
        if (state.get(Properties.SOUTH))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_SOUTH, BooleanBiFunction.OR);
        if (state.get(Properties.EAST))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_EAST, BooleanBiFunction.OR);
        if (state.get(Properties.WEST))
            nodeShape = VoxelShapes.combineAndSimplify(nodeShape, N_WEST, BooleanBiFunction.OR);
        return nodeShape;
    }

    public static BooleanProperty getFacing(Direction dir) {
        return switch (dir) {
            case UP -> Properties.UP;
            case DOWN -> Properties.DOWN;
            case NORTH -> Properties.NORTH;
            case SOUTH -> Properties.SOUTH;
            case EAST -> Properties.EAST;
            case WEST -> Properties.WEST;
        };
    }
}
