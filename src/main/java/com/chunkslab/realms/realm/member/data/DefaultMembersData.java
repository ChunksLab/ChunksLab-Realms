package com.chunkslab.realms.realm.member.data;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.member.data.MembersData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultMembersData implements MembersData {
    @Getter
    private final Set<RankedPlayer> members = ConcurrentHashMap.newKeySet();
    @Getter
    private final Set<RealmPlayer> visitors = ConcurrentHashMap.newKeySet();
    @Getter
    private final Set<RankedPlayer> bans = ConcurrentHashMap.newKeySet();
}