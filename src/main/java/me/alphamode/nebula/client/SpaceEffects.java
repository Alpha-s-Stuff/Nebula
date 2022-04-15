package me.alphamode.nebula.client;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SpaceEffects extends DimensionEffects {
    public SpaceEffects() {
        super(Float.NaN, false, SkyType.NONE, false, true);
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }

    @Nullable
    @Override
    public float[] getFogColorOverride(float skyAngle, float tickDelta) {
        return null;
    }
}
