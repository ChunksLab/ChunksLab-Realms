package com.chunkslab.realms.api.player.data;

import com.chunkslab.realms.api.player.BorderColor;
import com.chunkslab.realms.api.player.MessagePreference;

import java.util.UUID;

public interface RealmPlayerData {
    UUID getUniqueId();

    long getLastLogout();

    MessagePreference getMessagePreference();

    BorderColor getBorderColor();

    boolean isBypass();

    void setLastLogout(long lastLogout);

    void setMessagePreference(MessagePreference messagePreference);

    void setBorderColor(BorderColor borderColor);

    void setBypass(boolean bypass);
}
