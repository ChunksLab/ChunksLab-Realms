package com.chunkslab.realms.schematic.impl;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.schematic.SchematicPaster;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.chunkslab.realms.api.util.Pair;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class WorldEditPaster implements SchematicPaster {

    @Override
    public CompletableFuture<Pair<Location, Location>> paste(Location location, File schematic) {
        return CompletableFuture.supplyAsync(() -> {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            assert format != null;
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematic));
                 EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {

                Clipboard clipboard = reader.read();

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BukkitAdapter.asBlockVector(location))
                        .copyBiomes(true)
                        .copyEntities(true)
                        .ignoreAirBlocks(true)
                        .build();
                Operations.complete(operation);
                Location center = getCenter(location, clipboard);
                Location surface = getRandomSurfaceLocationNearCenter(center);
                return new Pair<>(center, surface);
            } catch (IOException | WorldEditException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Location getCenter(Location loc, Clipboard cd) {
        return new Location(loc.getWorld(),
                loc.getBlockX() + (cd.getRegion().getCenter().getX() - cd.getOrigin().getBlockX()),
                loc.getBlockY() + (cd.getRegion().getCenter().getY() - cd.getOrigin().getBlockY()),
                loc.getBlockZ() + (cd.getRegion().getCenter().getZ() - cd.getOrigin().getBlockZ()));
    }

    private Location getRandomSurfaceLocationNearCenter(Location center) {
        Location location;
        Random random = new Random();
        World world = center.getWorld();

        int maxDistance = RealmsPlugin.getInstance().getUpgradeManager().getUpgrade(Upgrade.Type.SIZE, 0).value() * 16;
        List<String> forbiddenBlocks = RealmsPlugin.getInstance().getPluginConfig().getSettings().getForbiddenBlocks();

        do {
            int offsetX = random.nextInt(maxDistance * 2 + 1) - maxDistance;
            int offsetZ = random.nextInt(maxDistance * 2 + 1) - maxDistance;

            int newX = center.getBlockX() + offsetX;
            int newZ = center.getBlockZ() + offsetZ;

            Location surface = world.getHighestBlockAt(newX, newZ).getLocation();
            Material groundMaterial = surface.getBlock().getType();

            if (surface.getBlockY() <= world.getMinHeight()) continue;

            if (forbiddenBlocks.contains(groundMaterial.name())) continue;

            location = surface.add(0, 1, 0);
            break;
        } while (true);

        return location;
    }
}