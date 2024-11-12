package com.chunkslab.realms.schematic;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.schematic.ISchematicManager;
import com.chunkslab.realms.api.schematic.SchematicPaster;
import com.chunkslab.realms.schematic.impl.WorldEditPaster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SchematicManager implements ISchematicManager {

    private final RealmsPlugin plugin;

    @Override
    public SchematicPaster getSchematicPaster() {
        String recommendedPaster = plugin.getPluginConfig().getPaster().getName();

        if(!Bukkit.getPluginManager().isPluginEnabled(recommendedPaster))
            return getDefaultSchematicPaster();

        return switch (recommendedPaster) {
            case "WorldEdit" -> new WorldEditPaster();
            default -> getDefaultSchematicPaster();
        };
    }

    @Override
    public SchematicPaster getDefaultSchematicPaster() {
        return new WorldEditPaster();
    }
}