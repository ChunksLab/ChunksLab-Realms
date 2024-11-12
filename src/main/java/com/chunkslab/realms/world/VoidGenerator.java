package com.chunkslab.realms.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new BiomeProvider() {
            @Override
            public @NotNull
            Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
                return switch (worldInfo.getEnvironment()) {
                    case NORMAL, CUSTOM -> Biome.PLAINS;
                    case NETHER -> Biome.NETHER_WASTES;
                    case THE_END -> Biome.THE_END;
                };
            }

            @Override
            public @NotNull
            List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                return Arrays.asList(Biome.PLAINS, Biome.NETHER_WASTES, Biome.THE_END);
            }
        };
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

    @Override
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world, 0, 0, 0);
    }

}