package com.chunkslab.realms;

import com.chunkslab.realms.api.RealmsAPI;
import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.biome.IBiomeManager;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.database.Database;
import com.chunkslab.realms.api.invite.IInviteManager;
import com.chunkslab.realms.api.item.IItemManager;
import com.chunkslab.realms.api.listener.IListenerManager;
import com.chunkslab.realms.api.module.ModuleManager;
import com.chunkslab.realms.api.player.IPlayerManager;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.rank.IRankManager;
import com.chunkslab.realms.api.realm.IRealmManager;
import com.chunkslab.realms.api.scheduler.IScheduler;
import com.chunkslab.realms.api.schematic.ISchematicManager;
import com.chunkslab.realms.api.server.IServerManager;
import com.chunkslab.realms.api.upgrade.IUpgradeManager;
import com.chunkslab.realms.api.world.IWorldManager;
import com.chunkslab.realms.biome.BiomeManager;
import com.chunkslab.realms.command.MainCommand;
import com.chunkslab.realms.command.player.*;
import com.chunkslab.realms.config.Config;
import com.chunkslab.realms.config.messages.MessagesEN;
import com.chunkslab.realms.database.impl.yaml.YamlDatabase;
import com.chunkslab.realms.invite.InviteManager;
import com.chunkslab.realms.listener.ListenerManager;
import com.chunkslab.realms.papi.PapiHook;
import com.chunkslab.realms.player.PlayerManager;
import com.chunkslab.realms.rank.RankManager;
import com.chunkslab.realms.realm.RealmManager;
import com.chunkslab.realms.scheduler.Scheduler;
import com.chunkslab.realms.schematic.SchematicManager;
import com.chunkslab.realms.server.ServerManager;
import com.chunkslab.realms.upgrade.UpgradeManager;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.world.WorldManager;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.CompactNumberFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

@Getter
public final class RealmsPlugin extends RealmsAPI {

    @Getter private static RealmsPlugin instance;

    private BukkitAudiences adventure;
    private Config pluginConfig;
    private MessagesEN pluginMessages;
    private BukkitCommandManager<CommandSender> commandManager;

    // config
    private final ConfigFile upgradesFile = new ConfigFile(this, "upgrades.yml", true);
    private final ConfigFile permissionsFile = new ConfigFile(this, "permissions.yml", true);
    private final ConfigFile biomesFile = new ConfigFile(this, "biomes.yml", true);
    private final ConfigFile createMenuConfig = new ConfigFile(this, "menus", "create-menu.yml");
    private final ConfigFile realmsMenuConfig = new ConfigFile(this, "menus", "realms-menu.yml");
    private final ConfigFile settingsMenuConfig = new ConfigFile(this, "menus", "settings-menu.yml");
    private final ConfigFile membersMenuConfig = new ConfigFile(this, "menus", "members-menu.yml");
    private final ConfigFile inviteMenuConfig = new ConfigFile(this, "menus", "invite-menu.yml");
    private final ConfigFile rankMenuConfig = new ConfigFile(this, "menus", "rank-menu.yml");
    private final ConfigFile bansMenuConfig = new ConfigFile(this, "menus", "bans-menu.yml");

    // database
    @Setter private Database database;

    // managers
    @Setter private IWorldManager worldManager = new WorldManager(this);
    @Setter private ISchematicManager schematicManager = new SchematicManager(this);
    @Setter private IUpgradeManager upgradeManager = new UpgradeManager(this);
    @Setter private IRankManager rankManager = new RankManager(this);
    //@Setter private IItemManager itemManager = new ItemManager(this).getManager();
    @Setter private IBiomeManager biomeManager= new BiomeManager(this);
    @Setter private IRealmManager realmManager = new RealmManager(this);
    @Setter private IInviteManager inviteManager = new InviteManager(this);
    @Setter private IListenerManager listenerManager = new ListenerManager(this);
    @Setter private IServerManager serverManager = new ServerManager();
    @Setter private IPlayerManager playerManager = new PlayerManager();
    @Setter private IScheduler scheduler = new Scheduler(this);
    @Setter private ModuleManager moduleManager = new ModuleManager(this);

    @Override
    public void onLoad() {
        instance = this;
        RealmsAPI.setInstance(this);
        RealmsAPI.setDebugMode(true);

        this.moduleManager.loadModules();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        registerCommands();
        createConfig();

        upgradesFile.create();
        permissionsFile.create();
        biomesFile.create();
        createMenuConfig.create();
        realmsMenuConfig.create();
        settingsMenuConfig.create();
        membersMenuConfig.create();
        inviteMenuConfig.create();
        rankMenuConfig.create();
        bansMenuConfig.create();

        ChatUtils.setCompactNumberFormat(
                CompactNumberFormat.getCompactNumberInstance(
                        Locale.forLanguageTag(pluginConfig.getNumberSettings().getLocale()),
                        NumberFormat.Style.valueOf(pluginConfig.getNumberSettings().getStyle())
                )
        );

        worldManager.loadWorlds();
        upgradeManager.enable();
        rankManager.enable();
        biomeManager.enable();
        listenerManager.enable();
        scheduler.enable();

        database = new YamlDatabase(this);

        new PapiHook(this).register();

        this.getModuleManager().enableModules();
        database.enable();
    }

    @Override
    public void onDisable() {
        this.getModuleManager().disableModules();

        listenerManager.disable();
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        if (database != null) {
            database.disable();
            database = null;
        }
        if (scheduler != null) {
            scheduler.disable();
            scheduler = null;
        }

        getBukkitCommands(getCommandMap()).remove("realm");
        getBukkitCommands(getCommandMap()).remove("realms");
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
    }

    private void registerCommands() {
        commandManager = BukkitCommandManager.create(this);

        commandManager.registerSuggestion(SuggestionKey.of("players"), (sender, context) ->
                new ArrayList<>(this.serverManager.getAllOnlinePlayers())
        );
        commandManager.registerSuggestion(SuggestionKey.of("biomes"), (sender, context) ->
                biomeManager.getBiomes().stream().map(Biome::getId).toList()
        );

        commandManager.registerCommand(new MainCommand(this, Permission.REALM_COMMAND));

        // Player Commands
        commandManager.registerCommand(
                new CreateCommand(this, Permission.REALM_COMMAND_CREATE),
                new DeleteCommand(this, Permission.REALM_COMMAND_DELETE),
                new TeleportCommand(this, Permission.REALM_COMMAND_TELEPORT),
                new SettingsCommand(this, Permission.REALM_COMMAND_SETTINGS),
                new SetSpawnCommand(this, Permission.REALM_COMMAND_SETSPAWN),
                new MembersCommand(this, Permission.REALM_COMMAND_MEMBERS),
                new MemberCommand(this, Permission.REALM_COMMAND_MEMBER),
                new RemoveCommand(this, Permission.REALM_COMMAND_REMOVE),
                new InviteCommand(this, Permission.REALM_COMMAND_INVITE),
                new AcceptCommand(this, Permission.REALM_COMMAND_ACCEPT),
                new DenyCommand(this, Permission.REALM_COMMAND_DENY),
                new BanCommand(this, Permission.REALM_COMMAND_BAN),
                new UnBanCommand(this, Permission.REALM_COMMAND_UNBAN),
                new BorderCommand(this, Permission.REALM_COMMAND_BORDER)
        );

        // Admin Commands

        commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getInvalidArgument())));
        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getUnknownCommand())));
        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getNotEnoughArguments())));
        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getTooManyArguments())));
        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getNotEnoughPermission())));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void createConfig() {
        pluginConfig = ConfigManager.create(Config.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        Class<MessagesEN> langClass = (Class<MessagesEN>) Class.forName("com.chunkslab.realms.config.messages.Messages" + pluginConfig.getLanguage().name());
        pluginMessages = ConfigManager.create(langClass, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(this.getDataFolder(), pluginConfig.getLanguage().getFileName()));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }
}
