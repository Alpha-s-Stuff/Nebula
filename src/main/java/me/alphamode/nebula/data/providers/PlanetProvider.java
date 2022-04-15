package me.alphamode.nebula.data.providers;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;

import java.io.IOException;

public class PlanetProvider implements DataProvider {
    @Override
    public void run(DataCache cache) throws IOException {

    }

    @Override
    public String getName() {
        return "planets";
    }
}
