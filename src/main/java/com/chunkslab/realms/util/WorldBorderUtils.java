package com.chunkslab.realms.util;

import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.upgrade.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class WorldBorderUtils {

    public static void send(Player player, Realm realm) {
        if (player.hasPermission("chunkslab.realms.permission.bypass.border")) return;

        WorldBorder border = Bukkit.createWorldBorder();
        border.setCenter(realm.getCenterLocation().getLocation());
        double size = realm.getUpgrade(Upgrade.Type.SIZE).value();
        double decrement = size % 2 == 0 ? 1 : 0;
        border.setSize(size - decrement);
        border.setDamageAmount(0);
        border.setDamageBuffer(0);
        border.setWarningDistance(0);
        border.setWarningTime(0);
        player.setWorldBorder(border);
    }

    public static void reset(Player player) {
        player.setWorldBorder(null);
    }

}
