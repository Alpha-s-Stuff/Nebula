package me.alphamode.nebula.content.planets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import me.alphamode.nebula.Nebula;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class PlanetManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static Map<Identifier, Planet> planets = ImmutableMap.of();

    public static Planet deserializePlanet(Identifier id, JsonObject json) {
        ImmutableList.Builder<ResourceAmount<FluidVariant>> builder = new ImmutableList.Builder<>();
        JsonHelper.getObject(json, "atmosphereGases").entrySet().forEach((gas) -> {
            Fluid fluid = Registry.FLUID.get(new Identifier(gas.getKey()));
            builder.add(new ResourceAmount<>(FluidVariant.of(fluid), gas.getValue().getAsInt()));
        });
        return new Planet(new Identifier(JsonHelper.getString(json, "dimension")), JsonHelper.getInt(json, "radius"), JsonHelper.getInt(json, "atmosphereHeight"), new AtmosphereStorage(builder.build()), null, null);
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public PlanetManager() {
        super(GSON, "planets");
    }

    @Override
    public Identifier getFabricId() {
        return Nebula.asResource("Planet Loader");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        ImmutableMap.Builder<Identifier, Planet> builder = new ImmutableMap.Builder<>();
        for(Identifier id : prepared.keySet()) {
            builder.put(id, PlanetManager.deserializePlanet(id, JsonHelper.asObject(prepared.get(id), "Planet Info")));
        }
        planets = builder.build();
        LogUtils.getLogger().info("Loaded {} planets", prepared.size());
    }

    public static Map<Identifier, Planet> getPlanets() {
        return planets;
    }

    public static Planet getPlanet(Identifier id) {
        return planets.get(id);
    }

    @Nullable
    public static Planet getPlanet(World world) {
        for(Planet planet : planets.values())
            if(world.getRegistryKey().getValue().equals(planet.dimension()))
                return planet;
        return null;
    }
}
