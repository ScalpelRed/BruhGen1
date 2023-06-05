package scalpelred.Bruhgen.Bruhgen1.chunkGenerators;

import scalpelred.Bruhgen.Bruhgen1.Config;
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
import net.minecraft.world.level.block.Blocks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_ScaledY extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_ScaledY> CODEC =
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
                    .apply(instance, ChunkGenerator_ScaledY::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;

    public ChunkGenerator_ScaledY(Registry<StructureSet> structureSetRegistry,
                                      Registry<NormalNoise.NoiseParameters> noiseParameters,
                                      BiomeSource biomeSource, long seed,
                                      Holder<NoiseGeneratorSettings> settings) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;

        coef = Config.ScaledY_coef.get();
        fill = Config.ScaledY_fill.get();
    }

    private double coef;
    private boolean fill;

    @Override
    public Codec<ChunkGenerator_ScaledY> codec() {
        return CODEC;
    }

    private int minY = getMinY();

    public void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess,
                                     StructureFeatureManager structureFeatureManager) {

        super.applyBiomeDecoration(worldGenLevel, chunkAccess, structureFeatureManager);

        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++){
            mbp.setX(x);
            for (int z = 0; z < 16; z++){
                mbp.setZ(z);
                for (int y = 384; y >= 0; y--){
                    if (y * coef > 384) continue;
                    BlockState t = chunkAccess.getBlockState(mbp.setY(y + minY));
                    if (t.is(Blocks.VOID_AIR)) continue;

                    if (fill) {
                        double lower = y * coef + minY;
                        double upper = (y + 1) * coef + minY;

                        for (double i = lower; i <= upper; i++) {
                            chunkAccess.setBlockState(mbp.setY((int)i), t, false);
                        }
                    }
                    else {
                        chunkAccess.setBlockState(
                                mbp.setY((int)(y * coef + minY)), t, false);
                    }
                }
            }
        }
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long seed, BiomeManager biomeManager,
                                      StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess,
                                      GenerationStep.Carving carving){
        super.applyCarvers(worldGenRegion, seed, biomeManager, structureFeatureManager, chunkAccess, carving);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
                                                        StructureFeatureManager structureFeatureManager,
                                                        ChunkAccess chunkAccess) {

        return super.fillFromNoise(executor, blender, structureFeatureManager, chunkAccess);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator_ScaledY(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings);
    }



}

