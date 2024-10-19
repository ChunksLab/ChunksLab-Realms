package com.chunkslab.realms.api.schematic;

import java.io.File;
import java.util.Map;

public interface ISchematicManager {

    Map<String, File> getSchematics();

    SchematicPaster getSchematicPaster();

    SchematicPaster getDefaultSchematicPaster();

}