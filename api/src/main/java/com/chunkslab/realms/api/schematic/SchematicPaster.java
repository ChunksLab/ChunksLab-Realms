package com.chunkslab.realms.api.schematic;

import com.chunkslab.realms.api.util.Pair;
import org.bukkit.Location;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface SchematicPaster {

    CompletableFuture<Pair<Location, Location>> paste(Location location, File schematic);

}