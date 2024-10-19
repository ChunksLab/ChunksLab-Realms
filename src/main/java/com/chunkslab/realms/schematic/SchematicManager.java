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

    @Getter private final Map<String, File> schematics = new HashMap<>();

    @Override
    public void enable() {
        File schematicsFolder = new File(plugin.getDataFolder(), "schematics");

        if (!schematicsFolder.exists()) {
            plugin.saveResource("schematics/plains_1.schem", false);
        }

        File[] files = schematicsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    schematics.put(file.getName(), file);
                }
            }
        }
    }

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