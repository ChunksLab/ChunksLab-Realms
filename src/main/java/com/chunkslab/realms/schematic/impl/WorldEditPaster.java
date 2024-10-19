package com.chunkslab.realms.schematic.impl;

import com.chunkslab.realms.api.schematic.SchematicPaster;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditPaster implements SchematicPaster {

    @Override
    public void paste(Location location, File schematic) {
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
        } catch (IOException | WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

}