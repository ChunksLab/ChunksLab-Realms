package com.chunkslab.realms.api;

import com.chunkslab.realms.api.biome.IBiomeManager;
import com.chunkslab.realms.api.module.ModuleManager;
import com.chunkslab.realms.api.player.IPlayerManager;
import com.chunkslab.realms.api.rank.IRankManager;
import com.chunkslab.realms.api.realm.IRealmManager;
import com.chunkslab.realms.api.scheduler.IScheduler;
import com.chunkslab.realms.api.schematic.ISchematicManager;
import com.chunkslab.realms.api.server.IServerManager;
import com.chunkslab.realms.api.world.IWorldManager;
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

    public abstract IServerManager getServerManager();

    public abstract IPlayerManager getPlayerManager();

    public abstract IRealmManager getRealmManager();

    public abstract ISchematicManager getSchematicManager();

    public abstract IWorldManager getWorldManager();

    public abstract IRankManager getRankManager();

    public abstract IBiomeManager getBiomeManager();

    public abstract ModuleManager getModuleManager();

    public abstract void setScheduler(IScheduler scheduler);

    public abstract void setServerManager(IServerManager serverManager);

    public abstract void setPlayerManager(IPlayerManager playerManager);

    public abstract void setRealmManager(IRealmManager realmManager);

    public abstract void setSchematicManager(ISchematicManager schematicManager);

    public abstract void setWorldManager(IWorldManager worldManager);

    public abstract void setRankManager(IRankManager rankManager);

    public abstract void setBiomeManager(IBiomeManager biomeManager);

    public abstract void setModuleManager(ModuleManager manager);

}
