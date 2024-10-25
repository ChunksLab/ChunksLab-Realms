package com.chunkslab.realms.api.player.data;

import com.chunkslab.realms.api.player.MessagePreference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultRealmPlayerData implements RealmPlayerData {
    @Getter
    private final UUID uniqueId;
    @Getter @Setter
    private long lastLogout = System.currentTimeMillis();
    @Getter @Setter
    private MessagePreference messagePreference = MessagePreference.CHAT;
    @Getter @Setter
    private boolean bypass = false;
}
