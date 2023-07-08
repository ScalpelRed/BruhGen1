package com.scalpelred.bruhgen.bruhgen1.chunkGenerators;

import com.scalpelred.bruhgen.bruhgen1.Config;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_Sine extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_Sine> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY)
                                    .fieldOf("structureSets")
                                    .forGetter(c -> c.structureSets),
                            RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY)
                                    .fieldOf("noiseParameters")
                                    .forGetter(c -> c.noiseParameters),
                            BiomeSource.CODEC.fieldOf("biomeSource")
                                    .forGetter(c -> c.biomeSource),
                            Codec.LONG.fieldOf("seed")
                                    .forGetter(c -> c.seed),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings")
                                    .forGetter(c -> c.settings),
                            BlockState.CODEC.fieldOf("filler")
                                    .forGetter(c -> c.filler))
                    .apply(instance, ChunkGenerator_Sine::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;
    public final BlockState filler;

    public ChunkGenerator_Sine(Registry<StructureSet> structureSetRegistry,
                                   Registry<NormalNoise.NoiseParameters> noiseParameters,
                                   BiomeSource biomeSource, long seed,
                                   Holder<NoiseGeneratorSettings> settings,
                                   BlockState filler) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;
        this.filler = filler;

        a = Config.Sine_a.get();
        b = Config.Sine_b.get();
        w = Config.Sine_w.get();
        T = Config.Sine_T.get();

    }

    private final double a;
    private final double b;
    private final double w;
    private final double T;

    @Override
    public Codec<ChunkGenerator_Sine> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                        StructureFeatureManager structureFeatureManager,
                                                        ChunkAccess chunkAccess) {
        ChunkPos cp = chunkAccess.getPos();
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++){
            for (int z = 0; z < 16; z++){
                float dx = (cp.x * 16 + x);
                float dz = (cp.z * 16 + z);
                int vl = (int)(0.5 *
                        ((a * Math.sin(w * dx + T) + b) + (a * Math.sin(w * dz + T) + b)));
                if (vl > 320) vl = 320;
                for (int y = -64; y <= vl; y++){
                        chunkAccess.setBlockState(mbp.set(x, y, z), filler, false);
                }
            }
        }

        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator_Sine(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings, filler);
    }
}

