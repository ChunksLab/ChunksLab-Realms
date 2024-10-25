package com.chunkslab.realms.api.realm.member.data;

import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;

import java.util.Set;

public interface MembersData {
    Set<RankedPlayer> getMembers();
}