package com.chunkslab.realms.upgrade;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.upgrade.IUpgradeManager;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.google.common.collect.HashBasedTable;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

@RequiredArgsConstructor
public class UpgradeManager implements IUpgradeManager {

    // not quite sure if this the best way to do something like this but this is the easiest way imo
    private final HashBasedTable<Upgrade.Type, Integer, Upgrade> upgrades = HashBasedTable.create();
    private final RealmsPlugin plugin;

    @Override
    public void enable() {
        for (Upgrade.Type type : Upgrade.Type.VALUES) {
            String s = type.name().toLowerCase(Locale.ENGLISH);
            ConfigurationSection section = plugin.getUpgradesFile().getConfigurationSection("upgrades." + s);
            assert section != null;
            for (String key : section.getKeys(false)) {
                int level = section.getInt(key + ".level");
                int value = section.getInt(key + ".value");
                double price = section.getDouble(key + ".price");
                this.upgrades.put(type, level, new Upgrade("", type, level, value, price));
            }
        }
    }

    @Override
    public Upgrade getUpgrade(Upgrade.Type type, int level) {
        return this.upgrades.get(type, level);
    }

}