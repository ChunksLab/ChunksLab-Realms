package com.chunkslab.realms.command;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.gui.RealmsGui;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class MainCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @Default
    public void defaultCommand(Player player) {
        RealmsGui.open(player, plugin);
    }
}