package com.chunkslab.realms.api.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Locale;

public class PermissionUtils {

    /**
     * Get the maximum value of a permission node.
     *
     * @param player The player to check.
     * @param s The permission node.
     * @param def The default value.
     * @return The maximum value of the permission node.
     */
    public static int getMax(Player player, String s, int def) {
        int max = -1;

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            String perm = permission.getPermission().toLowerCase(Locale.ENGLISH);
            if (perm.startsWith(s)) {
                int index = perm.lastIndexOf('.');
                int i = Integer.parseInt(perm.substring(index));

                if (i >= max) max = i;
            }
        }

        return max == -1 ? def : max;
    }

}