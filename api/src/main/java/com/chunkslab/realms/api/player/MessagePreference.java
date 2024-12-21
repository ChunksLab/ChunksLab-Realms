package com.chunkslab.realms.api.player;

public enum MessagePreference {
    CHAT, TITLE, SUBTITLE, ACTION_BAR, BOSS_BAR;

    public static MessagePreference safeValue(String name, MessagePreference def) {
        try {
            return MessagePreference.valueOf(name);
        } catch (NullPointerException ex) {
            return def;
        }
    }
}