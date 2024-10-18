package com.chunkslab.realms.api;

import com.chunkslab.realms.api.scheduler.IScheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RealmsAPI extends JavaPlugin {

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

    // abstract

    public abstract IScheduler getScheduler();

    public abstract void setScheduler(IScheduler scheduler);

}
