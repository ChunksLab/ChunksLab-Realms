package com.chunkslab.realms.api.player.data;

import com.chunkslab.realms.api.player.MessagePreference;

import java.util.UUID;

public interface RealmPlayerData {
    UUID getUniqueId();

    long getLastLogout();

    MessagePreference getMessagePreference();

    boolean isBypass();

    void setLastLogout(long lastLogout);

    void setMessagePreference(MessagePreference messagePreference);

    void setBypass(boolean bypass);
}
