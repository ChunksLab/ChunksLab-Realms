package com.chunkslab.realms.paper;

import com.chunkslab.realms.RealmsPlugin;
import xyz.xenondevs.invui.InvUI;

public class PaperRealmsPlugin extends RealmsPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        InvUI.getInstance().setPlugin(this);
    }
}