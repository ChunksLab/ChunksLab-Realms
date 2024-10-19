package com.chunkslab.realms.api.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final File file;
    private final boolean save;

    public ConfigFile(JavaPlugin plugin, String name, boolean save) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name);
        this.save = save;
    }

    public void create() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            if (!save) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                plugin.saveResource(file.getName(), false);
            }
        }

        reload();
    }

    public void reload() {
        try {
            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}