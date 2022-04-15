package me.alphamode.nebula.content.planets;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record Planet (
        Identifier dimension,
        int radius,

        int atmosphereHeight,
        Storage<FluidVariant> atmosphereGases,

        Function<Integer,Double> heightToPressure,
        Function<Integer,Double> heightToTemperature
)   {

    public static class Builder {

        private static final Function<Integer,Double> EMPTY_FUNCTION = y -> 0.0;

        private final Identifier dimension;
        private int radius = 0;

        private int atmosphereHeight = 0;
        private Storage<FluidVariant> atmosphereGases = Storage.empty();

        private Function<Integer,Double> heightToPressure = EMPTY_FUNCTION;
        private Function<Integer,Double> heightToTemperature = EMPTY_FUNCTION;

        public Builder(Identifier dimension) {
            this.dimension = dimension;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        @SafeVarargs
        public final Builder atmosphere(int height, ResourceAmount<FluidVariant>... gases) {
            atmosphereHeight = height;
            atmosphereGases = new AtmosphereStorage(gases);
            return this;
        }

        public Builder heightToPressure(Function<Integer,Double> heightToPressure) {
            this.heightToPressure = heightToPressure;
            return this;
        }

        public Builder heightToTemperature(Function<Integer,Double> heightToTemperature) {
            this.heightToTemperature = heightToTemperature;
            return this;
        }

        public Planet build() {
//            if (atmosphereGases.isEmpty())
//                atmosphereGases.add(new GasVolume(NebulaGases.EMPTY, 0));
            return new Planet(dimension, radius, atmosphereHeight, atmosphereGases, heightToPressure, heightToTemperature);
        }

//        public Planet register() {
//            return Registry.register(NebulaRegistry.PLANET, dimension, build());
//        }

    }

    public static void fromJson() {

    }
}
