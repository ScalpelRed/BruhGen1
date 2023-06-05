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
import java.util.*;
import net.minecraft.server.level.WorldGenRegion;
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_AverageBlock extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_AverageBlock> CODEC =
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
                    .apply(instance, ChunkGenerator_AverageBlock::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;

    public ChunkGenerator_AverageBlock(Registry<StructureSet> structureSetRegistry,
                                       Registry<NormalNoise.NoiseParameters> noiseParameters,
                                       BiomeSource biomeSource, long seed,
                                       Holder<NoiseGeneratorSettings> settings) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;

        coefX = Config.AverageBlock_coefX.get();
        coefY = Config.AverageBlock_coefY.get();
        coefZ = Config.AverageBlock_coefZ.get();
        affectStructures = Config.AverageBlock_affectStructures.get();
    }

    private int coefX;
    private int coefY;
    private int coefZ;
    private boolean affectStructures;

    @Override
    public Codec<ChunkGenerator_AverageBlock> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long seed, BiomeManager biomeManager,
                             StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess,
                             GenerationStep.Carving carving){

        super.applyCarvers(worldGenRegion, seed, biomeManager, structureFeatureManager, chunkAccess, carving);

        if (!affectStructures) {
            for (int x = 0; x < 16; x += coefX){
                for (int y = -64; y < 320; y += coefY){
                    for (int z = 0; z < 16; z += coefZ){
                        fillAt(x, y, z, chunkAccess, getMostFrequent(x, y, z, chunkAccess));
                    }
                }
            }
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess,
                                     StructureFeatureManager structureFeatureManager) {

        super.applyBiomeDecoration(worldGenLevel, chunkAccess, structureFeatureManager);

        if (affectStructures) {
            for (int x = 0; x < 16; x += coefX){
                for (int y = -64; y < 320; y += coefY){
                    for (int z = 0; z < 16; z += coefZ){
                        fillAt(x, y, z, chunkAccess, getMostFrequent(x, y, z, chunkAccess));
                    }
                }
            }
        }
    }

    private BlockState getMostFrequent(int x0, int y0, int z0, ChunkAccess chunkAccess) {
        HashMap<BlockState, Integer> bls = new HashMap<>();
        BlockPos.MutableBlockPos mbp = BlockPos.ZERO.mutable();

        for (int x = x0; x < x0 + coefX; x++){
            mbp.setX(x);
            for (int y = y0; y < y0 + coefY; y++){
                mbp.setY(y);
                for (int z = z0; z < z0 + coefZ; z++){
                    BlockState t = chunkAccess.getBlockState(mbp.setZ(z));
                    if (bls.containsKey(t)) bls.put(t, bls.get(t) + 1);
                    else bls.put(t, 1);
                }
            }
        }

        Map.Entry<BlockState, Integer> max = null;
        for (Map.Entry<BlockState, Integer> v : bls.entrySet())
        {
            if (max == null || v.getValue().compareTo(max.getValue()) > 0) max = v;
        }

        return max.getKey();
    }

    private void fillAt(int x0, int y0, int z0, ChunkAccess chunkAccess, BlockState block){
        BlockPos.MutableBlockPos mbp = BlockPos.ZERO.mutable();

        for (int x = x0; x < x0 + coefX; x++){
            mbp.setX(x);
            for (int y = y0; y < y0 + coefY; y++){
                mbp.setY(y);
                for (int z = z0; z < z0 + coefZ; z++){
                    chunkAccess.setBlockState(mbp.setZ(z), block, false);
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
        return new ChunkGenerator_AverageBlock(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings);
    }
}

