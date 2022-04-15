package me.alphamode.nebula.data;

import me.alphamode.nebula.data.providers.NebulaModelProvider;
import me.alphamode.nebula.data.providers.PlanetProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NebulaData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        gen.addProvider(new PlanetProvider());
        gen.addProvider(new NebulaModelProvider(gen));
    }
}
