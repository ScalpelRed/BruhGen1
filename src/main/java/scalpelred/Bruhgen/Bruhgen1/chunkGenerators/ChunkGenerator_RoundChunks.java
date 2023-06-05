package scalpelred.Bruhgen.Bruhgen1.chunkGenerators;

import scalpelred.Bruhgen.Bruhgen1.Config;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.RegistryOps;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.server.level.WorldGenRegion;
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_RoundChunks extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_RoundChunks> CODEC =
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
                    .apply(instance, ChunkGenerator_RoundChunks::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;

    public ChunkGenerator_RoundChunks(Registry<StructureSet> structureSetRegistry,
                                     Registry<NormalNoise.NoiseParameters> noiseParameters,
                                     BiomeSource biomeSource, long seed,
                                     Holder<NoiseGeneratorSettings> settings) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;

        r = Config.RoundChunks_R.get();
        filler = ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Config.RoundChunks_Filler.get())).defaultBlockState();
    }

    private double r;
    private BlockState filler;

    @Override
    public Codec<ChunkGenerator_RoundChunks> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion,
                             StructureFeatureManager structureFeatureManager,
                             ChunkAccess chunkAccess) {

        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (int x = -8; x < 8; x++){
            for (int z = -8; z < 8; z++){

                double dr = Math.sqrt(x * x + z * z);

                if (dr > r)
                    for (int y = -64; y <= 320; y++){
                        chunkAccess.setBlockState(mbp.set(x + 8, y, z + 8),
                                filler, false);
                    }
            }
        }

        super.buildSurface(worldGenRegion, structureFeatureManager, chunkAccess);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                        StructureFeatureManager structureFeatureManager,
                                                        ChunkAccess chunkAccess) {

        return super.fillFromNoise(executor, blender, structureFeatureManager, chunkAccess);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator_RoundChunks(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings);
    }



}

