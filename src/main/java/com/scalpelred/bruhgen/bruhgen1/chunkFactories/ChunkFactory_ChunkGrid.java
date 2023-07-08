package com.scalpelred.bruhgen.bruhgen1.chunkFactories;

import com.mojang.serialization.Lifecycle;
import com.scalpelred.bruhgen.bruhgen1.chunkGenerators.ChunkGenerator_ChunkGrid;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.common.world.ForgeWorldPreset;

public record ChunkFactory_ChunkGrid() implements ForgeWorldPreset.IChunkGeneratorFactory {

    public ChunkGenerator createChunkGenerator(RegistryAccess registryAccess,
                                               long seed,
                                               String generatorSettings){

        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<StructureSet> structureSets = registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        Registry<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        Registry<NormalNoise.NoiseParameters> noiseParameters = registryAccess.registryOrThrow(Registry.NOISE_REGISTRY);

        return new ChunkGenerator_ChunkGrid(
                structureSets,
                noiseParameters,
                MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(
                        biomeRegistry, true),
                seed, noiseSettings.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD));
    }

    public WorldGenSettings createSettings(RegistryAccess registryAccess, long seed,
                                           boolean generateStructures, boolean bonusChest,
                                           String generatorSettings) {

        Registry<DimensionType> dimTypeRegistry = registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        Registry<LevelStem> defaultDimRegistry = DimensionType.defaultDimensions(registryAccess, seed);
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<StructureSet> structureSetRegistry = registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        Registry<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        Registry<NormalNoise.NoiseParameters> noiseParameters = registryAccess.registryOrThrow(Registry.NOISE_REGISTRY);

        LevelStem nether = defaultDimRegistry.get(LevelStem.NETHER);
        LevelStem end = defaultDimRegistry.get(LevelStem.END);
        MappedRegistry<LevelStem> dimensions = new MappedRegistry(Registry.DIMENSION_REGISTRY,
                Lifecycle.stable(), null);

        dimensions.register(
                LevelStem.NETHER,
                new LevelStem(nether.typeHolder(),
                        new ChunkGenerator_ChunkGrid(
                                structureSetRegistry,
                                noiseParameters,
                                MultiNoiseBiomeSource.Preset.NETHER.biomeSource(
                                        biomeRegistry, true),
                                seed, noiseSettings.getOrCreateHolder(
                                        NoiseGeneratorSettings.NETHER))),
                Lifecycle.stable());

        dimensions.register(
                LevelStem.END,
                new LevelStem(end.typeHolder(),
                        new ChunkGenerator_ChunkGrid(
                                structureSetRegistry,
                                noiseParameters,
                                new FixedBiomeSource(
                                        biomeRegistry.getOrCreateHolder(Biomes.THE_END)),
                                seed, noiseSettings.getOrCreateHolder(
                                        NoiseGeneratorSettings.END))),
                Lifecycle.stable());

        return new WorldGenSettings(seed, generateStructures, bonusChest,
                WorldGenSettings.withOverworld(dimTypeRegistry, dimensions,
                        new ChunkGenerator_ChunkGrid(
                                structureSetRegistry,
                                noiseParameters,
                                MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(
                                        biomeRegistry, true),
                                seed, noiseSettings.getOrCreateHolder(
                                        NoiseGeneratorSettings.OVERWORLD))));
    }
}
