package com.chunkslab.realms.api.schematic;

import org.bukkit.Location;

import java.io.File;

public interface SchematicPaster {

    void paste(Location location, File schematic);

}