package com.scalpelred.bruhgen.bruhgen1.chunkGenerators;

import com.scalpelred.bruhgen.bruhgen1.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.server.level.WorldGenRegion;
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_Sections extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_Sections> CODEC =
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
                                    .forGetter(c -> c.settings))
                    .apply(instance, ChunkGenerator_Sections::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;

    public ChunkGenerator_Sections(Registry<StructureSet> structureSetRegistry,
                                  Registry<NormalNoise.NoiseParameters> noiseParameters,
                                  BiomeSource biomeSource, long seed,
                                  Holder<NoiseGeneratorSettings> settings) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;

        divX = Config.Sections_A.get();
        divZ = Config.Sections_B.get();
        borderBlock = ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Config.Sections_BorderBlock.get())).defaultBlockState();

    }

    private final double divX;
    private final double divZ;
    private final BlockState borderBlock;

    @Override
    public Codec<ChunkGenerator_Sections> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion,
                             StructureFeatureManager structureFeatureManager,
                             ChunkAccess chunkAccess) {

        super.buildSurface(worldGenRegion, structureFeatureManager, chunkAccess);

        ChunkPos cp = chunkAccess.getPos();
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++){
            mbp.setX(x);
            for (int z = 0; z < 16; z++){
                mbp.setZ(z);

                int dx = cp.x * 16 + x;
                int dz = cp.z * 16 + z;

                if ((dx % divX == 0) || (dz % divZ == 0)) {
                    for (int y = -64; y <= 320; y++){
                        chunkAccess.setBlockState(mbp.setY(y), borderBlock, false);
                    }
                }
            }
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                        StructureFeatureManager structureFeatureManager,
                                                        ChunkAccess chunkAccess) {
        return super.fillFromNoise(executor, blender, structureFeatureManager, chunkAccess);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator_Sections(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings);
    }



}

