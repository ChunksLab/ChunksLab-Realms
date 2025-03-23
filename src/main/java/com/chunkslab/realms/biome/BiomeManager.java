package com.chunkslab.realms.biome;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.biome.IBiomeManager;
import com.chunkslab.realms.util.ItemUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

@RequiredArgsConstructor
public class BiomeManager implements IBiomeManager {

    private final RealmsPlugin plugin;

    private final Map<String, Biome> biomeMap = new HashMap<>();

    @Override
    public void enable() {
        biomeMap.clear();

        File schematicsFolder = new File(plugin.getDataFolder(), "schematics");
        if (!schematicsFolder.exists()) {
            plugin.saveResource("schematics/plains_1.schem", false);
        }

        for (String s : plugin.getBiomesFile().getConfigurationSection("biomes").getKeys(false)) {
            ConfigurationSection section = plugin.getBiomesFile().getConfigurationSection("biomes." + s);
            String displayName = section.getString("display-name");
            List<String> description = section.getStringList("description");
            String permission = section.getString("permission");
            ItemStack icon = ItemUtils.build(plugin.getBiomesFile(), section.getString("icon"));
            FilenameFilter filter = (dir, name) -> name.startsWith(s + "_");
            File[] files = schematicsFolder.listFiles(filter);
            List<File> schematics = new ArrayList<>();
            if (files != null) Collections.addAll(schematics, files);
            Biome biome = new Biome(s, displayName, permission, description, icon, schematics);
            biomeMap.put(s, biome);
        }

    }

    @Override
    public Biome getBiome(String name) {
        return this.biomeMap.get(name);
    }

    @Override
    public Biome getDefaultBiome() {
        for (Biome biome : biomeMap.values()) {
            if (biome.getPermission() == null)
                return biome;
        }

        return null;
    }

    @Override
    public Collection<Biome> getBiomes() {
        return biomeMap.values();
    }
}
