package me.alphamode.nebula.content;

import com.mojang.serialization.Codec;
import me.alphamode.nebula.content.worldgen.SpaceChunkGenerator;
import me.alphamode.nebula.utils.AutoRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class AllChunkGenerators extends AutoRegistry<Codec<? extends ChunkGenerator>> {
    public AllChunkGenerators() {
        super(Registry.CHUNK_GENERATOR);
    }

    public static Codec<SpaceChunkGenerator> SPACE = SpaceChunkGenerator.CODEC;
}
