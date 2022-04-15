package me.alphamode.nebula.content.dimensions;

import me.alphamode.nebula.Nebula;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class AllDimensions {
    public static RegistryKey<DimensionOptions> SPACE_DIMENSION = RegistryKey.of(Registry.DIMENSION_KEY, Nebula.asResource("Space"));

    public static RegistryKey<World> SPACE = RegistryKey.of(Registry.WORLD_KEY, SPACE_DIMENSION.getValue());
}
