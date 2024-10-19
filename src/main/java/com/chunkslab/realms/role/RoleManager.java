package com.chunkslab.realms.role;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.role.IRoleManager;
import com.chunkslab.realms.api.role.RealmPermission;
import com.chunkslab.realms.api.role.RealmRole;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@RequiredArgsConstructor
public class RoleManager implements IRoleManager {

    private final RealmsPlugin plugin;

    private final TreeMap<String, RealmRole> realmRoleMap = new TreeMap<>();

    @Override
    public void enable() {
        realmRoleMap.clear();
        List<RealmRole> rolesByWeight = plugin.getRolesFile().getKeys(false)
                .stream()
                .map(id -> {
                    ConfigurationSection section = plugin.getRolesFile().getConfigurationSection(id);
                    String name = section.getString("name");
                    int weight = section.getInt("weight");
                    List<RealmPermission> permissions = section.getStringList("permissions").stream().map(RealmPermission::valueOf).toList();
                    return new RealmRole(id, name, weight, permissions);
                }).toList();
        rolesByWeight.sort(Comparator.comparingInt(RealmRole::getWeight));

        for (RealmRole realmRole : rolesByWeight)
            realmRoleMap.put(realmRole.getId(), realmRole);
    }

    @Override
    public RealmRole getRealmRole(String id) {
        return realmRoleMap.get(id);
    }

    @Override
    public RealmRole getDefaultRole() {
        return realmRoleMap.firstEntry().getValue();
    }

    @Override
    public RealmRole getLastRole() {
        return realmRoleMap.lastEntry().getValue();
    }
}
