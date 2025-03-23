package com.chunkslab.realms.api.biome;

import java.util.Collection;

public interface IBiomeManager {

    void enable();

    Biome getBiome(String name);

    Biome getDefaultBiome();

    Collection<Biome> getBiomes();

}
