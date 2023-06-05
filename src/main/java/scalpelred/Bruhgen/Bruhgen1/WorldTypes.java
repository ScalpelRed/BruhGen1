package scalpelred.Bruhgen.Bruhgen1;

import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import scalpelred.Bruhgen.Bruhgen1.chunkFactories.*;

public class WorldTypes {

    public static final DeferredRegister<ForgeWorldPreset> CUSTOM_WORLD_TYPES
            = DeferredRegister.create(ForgeRegistries.Keys.WORLD_TYPES, "bruhgen1");

    public static final RegistryObject<ForgeWorldPreset> PARABOLA =
            CUSTOM_WORLD_TYPES.register("parabola",
                    () -> new ForgeWorldPreset(new ChunkFactory_AverageBlock()));

    public static final RegistryObject<ForgeWorldPreset> SINE =
            CUSTOM_WORLD_TYPES.register("sine",
                    () -> new ForgeWorldPreset(new ChunkFactory_Sine()));

    public static final RegistryObject<ForgeWorldPreset> RADIALSINE =
            CUSTOM_WORLD_TYPES.register("radialsine",
                    () -> new ForgeWorldPreset(new ChunkFactory_RadialSine()));

    public static final RegistryObject<ForgeWorldPreset> CHUNKGRID =
            CUSTOM_WORLD_TYPES.register("chunkgrid",
                    () -> new ForgeWorldPreset(new ChunkFactory_ChunkGrid()));

    public static final RegistryObject<ForgeWorldPreset> CUBEGRID =
            CUSTOM_WORLD_TYPES.register("cubegrid",
                    () -> new ForgeWorldPreset(new ChunkFactory_CubeGrid()));

    public static final RegistryObject<ForgeWorldPreset> HEIGHTBYPOLARDATA =
            CUSTOM_WORLD_TYPES.register("heightbypolardata",
                    () -> new ForgeWorldPreset(new ChunkFactory_HeightByPolarData()));

    public static final RegistryObject<ForgeWorldPreset> SECTIONS =
            CUSTOM_WORLD_TYPES.register("sections",
                    () -> new ForgeWorldPreset(new ChunkFactory_Sections()));

    public static final RegistryObject<ForgeWorldPreset> ROUNDCHUNKS =
            CUSTOM_WORLD_TYPES.register("roundchunks",
                    () -> new ForgeWorldPreset(new ChunkFactory_RoundChunks()));

    public static final RegistryObject<ForgeWorldPreset> AVERAGEBLOCK =
            CUSTOM_WORLD_TYPES.register("averageblock",
                    () -> new ForgeWorldPreset(new ChunkFactory_AverageBlock()));

    public static final RegistryObject<ForgeWorldPreset> SCALEDY =
            CUSTOM_WORLD_TYPES.register("scaledy",
                    () -> new ForgeWorldPreset(new ChunkFactory_ScaledY()));
}