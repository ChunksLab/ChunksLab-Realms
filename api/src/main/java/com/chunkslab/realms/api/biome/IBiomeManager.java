package com.chunkslab.realms.api.biome;

public interface IBiomeManager {

    void enable();

    Biome getBiome(String name);

    Biome getDefaultBiome();

}
