package com.chunkslab.realms.player;

import com.chunkslab.realms.api.player.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class RealmPlayerImpl implements RealmPlayer {
    private final UUID uniqueId;
    private String name;
    private long lastLogout;
    private boolean online;
    @Nullable private Realm realm;
}