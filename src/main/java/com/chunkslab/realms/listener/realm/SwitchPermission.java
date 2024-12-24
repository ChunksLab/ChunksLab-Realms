package com.chunkslab.realms.listener.realm;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.util.AbstractRegistryList;
import com.chunkslab.realms.api.util.ItemLists;
import com.chunkslab.realms.listener.RealmPermissionListener;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwitchPermission extends RealmPermissionListener {

    private static final Set<Material> switchUseMaterials = new HashSet<>();

    public SwitchPermission(Permission permission) {
        super(permission);

        switchUseMaterials.clear();
        List<String> items = RealmsPlugin.getInstance().getPluginConfig().getSettings().getSwitchItems();
        for (String matName : items) {
            if (ItemLists.GROUPS.contains(matName)) {
                switchUseMaterials.addAll(ItemLists.getGrouping(matName));
            } else {
                Material material = AbstractRegistryList.matchRegistry(Registry.MATERIAL, matName);
                if (material != null)
                    switchUseMaterials.add(material);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock())
            if (switchUseMaterials.contains(event.getItem().getType()))
                checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        Block block = event.getBlock();
        Entity entity = event.getEntity();
        List<Entity> passengers = entity.getPassengers();

        for (Entity passenger : passengers) {
            if (!(passenger instanceof Player player))
                continue;

            if (switchUseMaterials.contains(block.getType())) {
                checkAndCancel(event, player);
                return;
            }
        }

        if (!(entity instanceof Creature))
            return;

        if (entity instanceof Villager && ItemLists.WOOD_DOORS.contains(block.getType()))
            return;

        if (block.getType() == Material.STONE_PRESSURE_PLATE) {
            event.setCancelled(true);
            return;
        }

        if (RealmsPlugin.getInstance().getRealmManager().getRealm(block.getLocation()) == null)
            return;

        if (switchUseMaterials.contains(block.getType())) {
            event.setCancelled(true);
        }
    }
}