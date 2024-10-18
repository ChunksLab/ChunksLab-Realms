package com.chunkslab.realms.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class RealmsAPI extends JavaPlugin {

    private static RealmsAPI api;

    @Getter @Setter
    private static boolean debugMode;

    public static void setInstance(RealmsAPI api) {
        if (api == null)
            throw new IllegalStateException("Plugin instance is already set!");

        RealmsAPI.api = api;
    }

    public static RealmsAPI getInstance() {
        return api;
    }

}
