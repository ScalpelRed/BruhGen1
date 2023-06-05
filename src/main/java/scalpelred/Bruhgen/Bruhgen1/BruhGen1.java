package scalpelred.Bruhgen.Bruhgen1;

import scalpelred.Bruhgen.Bruhgen1.chunkGenerators.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("bruhgen1")
public class BruhGen1 {

    private static final Logger LOGGER = LogUtils.getLogger();

    public BruhGen1() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.Config,
                "BruhGen1.toml");

        WorldTypes.CUSTOM_WORLD_TYPES.register(modBus);

        LOGGER.info("Yowza! It's all so weird!");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_parabola",
                    ChunkGenerator_Parabola.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_sine",
                    ChunkGenerator_Sine.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_radialsine",
                    ChunkGenerator_RadialSine.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_chunkgrid",
                    ChunkGenerator_ChunkGrid.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_cubegrid",
                    ChunkGenerator_CubeGrid.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_heightbypolardata",
                    ChunkGenerator_HeightByPolarData.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_sections",
                    ChunkGenerator_Sections.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_roundchunks",
                    ChunkGenerator_RoundChunks.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_averageblock",
                    ChunkGenerator_AverageBlock.CODEC);

            Registry.register(Registry.CHUNK_GENERATOR, "bruhgen1_scaledy",
                    ChunkGenerator_ScaledY.CODEC);
        }
    }
}
