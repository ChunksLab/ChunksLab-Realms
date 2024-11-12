package com.chunkslab.realms.api.biome;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
public class Biome {
    private final String id;
    private final String displayName;
    private final String permission;
    private final List<String> description;
    private final ItemStack icon;
    private final List<File> schematics;
}