package com.chunkslab.realms.api.player;

/**
 * Used to determine what's the color of the border of players.
 */
public enum BorderColor {
    BLUE, GREEN, RED;

    public static BorderColor safeValue(String name, BorderColor def) {
        try {
            return BorderColor.valueOf(name);
        } catch (NullPointerException ex) {
            return def;
        }
    }
}