package com.chunkslab.realms.command;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.gui.RealmsGui;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class MainCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @Default
    public void defaultCommand(Player player) {
        if (!plugin.getPlayerManager().getPlayer(player).hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        RealmsGui.open(player, plugin);
    }
}