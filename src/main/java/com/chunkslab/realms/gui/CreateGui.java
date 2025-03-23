package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class CreateGui {

    public static void open(RealmPlayer player, RealmsPlugin plugin) {
        if (!player.hasPermission(Permission.REALM_MENU_CREATE)) {
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        ConfigFile config = plugin.getCreateMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem(config))
                .addIngredient('>', new ForwardItem(config))
                .build();

        Window window = Window.single()
                .setViewer(player.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }
}