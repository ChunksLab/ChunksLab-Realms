package com.chunkslab.realms.item;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.item.IItemManager;
import com.chunkslab.realms.item.impl.ItemsAdder;
import com.chunkslab.realms.item.impl.Oraxen;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class ItemManager {

    private final RealmsPlugin plugin;

    public IItemManager getManager() {
        if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            ItemsAdder itemsAdder = new ItemsAdder();
            plugin.setItemManager(itemsAdder);
            return itemsAdder;
        } else if (Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {
            Oraxen oraxen = new Oraxen();
            plugin.setItemManager(oraxen);
            return oraxen;
        }
        return null;
    }
}