package scalpelred.Bruhgen.Bruhgen1.chunkGenerators;

import scalpelred.Bruhgen.Bruhgen1.Config;
import net.minecraft.core.Holder;
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
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_CubeGrid extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_CubeGrid> CODEC =
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
                    .apply(instance, ChunkGenerator_CubeGrid::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;

    public ChunkGenerator_CubeGrid(Registry<StructureSet> structureSetRegistry,
                                    Registry<NormalNoise.NoiseParameters> noiseParameters,
                                    BiomeSource biomeSource, long seed,
                                    Holder<NoiseGeneratorSettings> settings) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;

        div = Config.CubeGrid_divider.get();
    }

    private int div;

    @Override
    public Codec<ChunkGenerator_CubeGrid> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion,
                             StructureFeatureManager structureFeatureManager,
                             ChunkAccess chunkAccess) {

        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (int y = 0; y <= 384; y++) {
            if ((y / 16) % div != 0) {
                mbp.setY(y - 64);
                for (int x = 0; x < 16; x++) {
                    mbp.setX(x);
                    for (int z = 0; z < 16; z++) {
                        chunkAccess.setBlockState(mbp.setZ(z),
                                Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }

        super.buildSurface(worldGenRegion, structureFeatureManager, chunkAccess);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                        StructureFeatureManager structureFeatureManager,
                                                        ChunkAccess chunkAccess) {

        if ((chunkAccess.getPos().x % div == 0) && (chunkAccess.getPos().z % div == 0))
            return super.fillFromNoise(executor, blender, structureFeatureManager, chunkAccess);
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator_CubeGrid(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings);
    }



}

