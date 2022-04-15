package me.alphamode.nebula.content.fluids;

import me.alphamode.nebula.utils.AutoRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AllFluids extends AutoRegistry<Fluid> {
    public AllFluids() {
        super(Registry.FLUID);
    }

    public static final FluidInfo HYDROGEN = new FluidInfo(new HydrogenFluid.Still(), new HydrogenFluid.Flowing());
    public static final FluidInfo OXYGEN = new FluidInfo(new OxygenFluid.Still(), new OxygenFluid.Flowing());

    @Override
    public Fluid register(Object obj, Identifier id) {
        if(obj instanceof FluidInfo fluidInfo) {
            fluidInfo.register(id);
            return fluidInfo.still();
        }
        return super.register(obj, id);
    }
}
