package com.chunkslab.realms.listener.realm;

import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.listener.RealmPermissionListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BreakPermission extends RealmPermissionListener {

    public BreakPermission(Permission permission) {
        super(permission);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        final Block block = event.getBlock();
        final Material blockMat = block.getType();
        final Entity entity = event.getEntity();
        final EntityType entityType = event.getEntityType();

        if (blockMat.equals(Material.FARMLAND)) {
            if (entity instanceof Player player)
                checkAndCancel(event, player);
            else
                event.setCancelled(true);
        }

        if (entityType == EntityType.ENDERMAN) {
            event.setCancelled(true);
        } else if (entityType == EntityType.RAVAGER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragonEggLeftClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();
        if (block.getType() != Material.DRAGON_EGG)
            return;

        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (event.getBlockClicked().getType().equals(Material.AIR))
            return;

        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onHitMob(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Mob)
            checkAndCancel(event, player);
    }
}