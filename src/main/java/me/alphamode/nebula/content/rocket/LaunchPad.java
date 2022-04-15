package me.alphamode.nebula.content.rocket;

import me.alphamode.nebula.content.AllBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LaunchPad extends Block implements BlockEntityProvider {
    public static final BooleanProperty CENTER = BooleanProperty.of("center");
    public LaunchPad(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(CENTER, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CENTER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(state.get(CENTER))
            return VoxelShapes.cuboid(0, 0, 0, 1, .4, 1);
        return VoxelShapes.cuboid(0, 0, 0, 1, .3, 1);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for(Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos newPos = ctx.getBlockPos().offset(direction);
            if(!ctx.getWorld().getBlockState(newPos).isOf(AllBlocks.LAUNCH_PAD)) {
                return super.getPlacementState(ctx);
            }
        }
        return super.getPlacementState(ctx).with(CENTER, true);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        for(Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos newPos = pos.offset(dir);
            if(!world.getBlockState(newPos).isOf(AllBlocks.LAUNCH_PAD)) {
                return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).with(CENTER, false);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).with(CENTER, true);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if(state.get(CENTER))
            return new LaunchPadBlockEntity(pos, state);
        return null;
    }
}
