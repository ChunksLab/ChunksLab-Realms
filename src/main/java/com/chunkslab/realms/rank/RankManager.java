package com.chunkslab.realms.rank;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.rank.IRankManager;
import com.chunkslab.realms.util.ChatUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.*;

@RequiredArgsConstructor
public class RankManager implements IRankManager {

    private final RealmsPlugin plugin;

    @Getter
    private final Set<Rank> ranks = new HashSet<>();

    @Override
    public void enable() {
        List<Map<?, ?>> rankMaps = plugin.getPermissionsFile().getMapList("ranks");
        for (Map<?, ?> rankMap : rankMaps) {
            String id = (String) rankMap.get("id");
            int power = (int) rankMap.get("power");
            Component display = ChatUtils.format((String) rankMap.get("display"));
            Rank.Assignment assignment = Rank.Assignment.valueOf((String) rankMap.get("assignment"));
            List<Permission> permissions = new ArrayList<>();
            for (String perm : (List<String>) rankMap.get("permissions")) {
                permissions.add(Permission.valueOf(perm));
            }
            ranks.add(new Rank(id, power, display, assignment, permissions));
        }
    }

    @Override
    public Rank getRank(Rank.Assignment assignment) {
        return ranks.stream().filter(rank -> rank.assignment() == assignment).findFirst().get();
    }
}
