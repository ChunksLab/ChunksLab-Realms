package com.chunkslab.realms.gui.item;

import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.util.ItemUtils;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ForwardItem extends PageItem {

    private final ConfigFile config;

    public ForwardItem(ConfigFile config) {
        super(true);

        this.config = config;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        return new ItemBuilder(ItemUtils.build(config, "items.<"));
    }

}