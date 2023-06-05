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
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenerator_HeightByPolarData extends NoiseBasedChunkGenerator {

    public static final Codec<ChunkGenerator_HeightByPolarData> CODEC =
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
                    .apply(instance, ChunkGenerator_HeightByPolarData::new));

    public final Registry<NormalNoise.NoiseParameters> noiseParameters;
    public final BlockState filler;

    public ChunkGenerator_HeightByPolarData(Registry<StructureSet> structureSetRegistry,
                                            Registry<NormalNoise.NoiseParameters> noiseParameters,
                                            BiomeSource biomeSource, long seed,
                                            Holder<NoiseGeneratorSettings> settings,
                                            BlockState filler) {

        super(structureSetRegistry, noiseParameters, biomeSource, seed, settings);

        this.noiseParameters = noiseParameters;
        this.filler = filler;

        a = Config.HeightByPolarData_A.get();
        b = Config.HeightByPolarData_B.get();
        c = Config.HeightByPolarData_C.get();
        d = Config.HeightByPolarData_D.get();
        e = Config.HeightByPolarData_E.get();
    }

    private double a;
    private double b;
    private double c;
    private double d;
    private double e;


    @Override
    public Codec<ChunkGenerator_HeightByPolarData> codec() {
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
                double dx = (cp.x * 16 + x);
                double dz = (cp.z * 16 + z);

                double r = Math.sqrt(dx * dx + dz * dz);
                double ang = Math.atan2(dz, dx);

                int vl = (int)(a * (r + b) + c * (ang + d) + e);
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
        return new ChunkGenerator_HeightByPolarData(structureSets, noiseParameters,
                biomeSource.withSeed(seed), seed, settings, filler);
    }



}

