package com.chunkslab.realms.listener.realm;

import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.listener.RealmPermissionListener;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlacePermission extends RealmPermissionListener {

    public PlacePermission(Permission permission) {
        super(permission);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractWithArmourStand(PlayerArmorStandManipulateEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.PAINTING || entity.getType() == EntityType.ARMOR_STAND) {
            checkAndCancel(event, player);
        }
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)
            return;
        for (BlockState ignored : event.getBlocks()) {
            checkAndCancel(event, event.getPlayer());
        }
    }

    @EventHandler
    public void onFrostWalkerFreezeWater(EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player player)
            checkAndCancel(event, player);
    }
}