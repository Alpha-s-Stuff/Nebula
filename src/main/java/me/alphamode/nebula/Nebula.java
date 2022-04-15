package me.alphamode.nebula;

import me.alphamode.nebula.content.*;
import me.alphamode.nebula.content.fluids.AllFluids;
import me.alphamode.nebula.content.planets.PlanetManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Nebula implements ModInitializer {

    public static String MODID = "nebula";

    @Override
    public void onInitialize() {
        new AllBlocks();
        new AllBlockEntities();
        new AllEntities();
        new AllStats();
        new AllScreenHandlers();

        new AllFluids();
        new AllChunkGenerators();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new PlanetManager());
    }

    public static Identifier asResource(String path) {
        return new Identifier(MODID, path.toLowerCase().replace(' ', '_')); // Silently make the path lowercase if it's not.
    }
}
