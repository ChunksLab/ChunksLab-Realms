package com.chunkslab.realms.item.impl;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.item.IItemManager;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import org.bukkit.inventory.ItemStack;

public class Oraxen extends CompatibilityProvider<RealmsPlugin> implements IItemManager {

    public Oraxen() {
        addCompatibility();
    }

    private void addCompatibility() {
        CompatibilitiesManager.addCompatibility("ChunksLab-Realms", Oraxen.class);
    }

    @Override
    public ItemStack getItemWithId(String id) {
        if (!OraxenItems.exists(id))
            return null;
        return OraxenItems.getItemById(id).build();
    }
}
