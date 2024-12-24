package com.chunkslab.realms.listener.realm;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.util.AbstractRegistryList;
import com.chunkslab.realms.api.util.ItemLists;
import com.chunkslab.realms.listener.RealmPermissionListener;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsePermission extends RealmPermissionListener {

    private static final Set<Material> itemUseMaterials = new HashSet<>();

    public UsePermission(Permission permission) {
        super(permission);

        itemUseMaterials.clear();
        List<String> items = RealmsPlugin.getInstance().getPluginConfig().getSettings().getUseItems();
        for (String matName : items) {
            if (ItemLists.GROUPS.contains(matName)) {
                itemUseMaterials.addAll(ItemLists.getGrouping(matName));
            } else {
                Material material = AbstractRegistryList.matchRegistry(Registry.MATERIAL, matName);
                if (material != null)
                    itemUseMaterials.add(material);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem())
            if (itemUseMaterials.contains(event.getItem().getType()))
                checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player)
            checkAndCancel(event, player);
    }

    @EventHandler
    public void onEggLand(PlayerEggThrowEvent event) {
        if (itemUseMaterials.contains(Material.EGG) && check(event.getPlayer()))
            event.setHatching(false);
    }
}