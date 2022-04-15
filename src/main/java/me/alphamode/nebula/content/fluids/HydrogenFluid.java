package me.alphamode.nebula.content.fluids;

import me.alphamode.star.world.fluids.StarFluid;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public abstract class HydrogenFluid extends StarFluid {
    public HydrogenFluid() {
        super(Direction.UP);
    }

    @Override
    public Fluid getStill() {
        return AllFluids.HYDROGEN.still();
    }

    @Override
    public Fluid getFlowing() {
        return AllFluids.HYDROGEN.flowing();
    }

    @Override
    public Item getBucketItem() {
        return AllFluids.HYDROGEN.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return AllFluids.HYDROGEN.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends HydrogenFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends HydrogenFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }

}
