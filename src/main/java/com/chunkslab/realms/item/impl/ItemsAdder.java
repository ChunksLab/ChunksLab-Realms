package com.chunkslab.realms.item.impl;

import com.chunkslab.realms.api.item.IItemManager;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdder implements IItemManager {
    @Override
    public ItemStack getItemWithId(String id) {
        return CustomStack.getInstance(id).getItemStack();
    }
}