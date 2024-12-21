package com.chunkslab.realms.util;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.upgrade.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class WorldBorderUtils {

    public static void send(RealmPlayer player, Realm realm) {
        if (player.getData().isBypass()) return;

        Player bukkitPlayer = player.getBukkitPlayer();
        WorldBorder border = Bukkit.createWorldBorder();
        border.setCenter(realm.getCenterLocation().getLocation());
        double size = realm.getUpgrade(Upgrade.Type.SIZE).value() * 16 * 2;
        double decrement = size % 2 == 0 ? 1 : 0;
        border.setSize(size - decrement);
        border.setDamageAmount(0);
        border.setDamageBuffer(0);
        border.setWarningDistance(0);
        border.setWarningTime(0);
        switch (player.getData().getBorderColor()) {
            case BLUE -> border.setSize(size - decrement);
            case GREEN -> border.setSize((size - decrement) + 1.001D, Long.MAX_VALUE);
            case RED -> border.setSize((size - decrement) - 0.001D, Long.MAX_VALUE);
        }
        bukkitPlayer.setWorldBorder(border);
    }

    public static void reset(Player player) {
        player.setWorldBorder(null);
    }

}
