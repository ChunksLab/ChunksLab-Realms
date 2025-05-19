package com.chunkslab.realms.util;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.api.util.MinecraftVersion;
import com.chunkslab.realms.api.util.OS;
import com.chunkslab.realms.api.util.VersionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

public class NoticeUtil {

    private final RealmsPlugin plugin;

    public NoticeUtil(RealmsPlugin plugin) {
        this.plugin = plugin;
        handleVersionCheck();
        printStartupMessages();
    }

    private void handleVersionCheck() {
        if (VersionUtil.isCompiled()) {
            printCompileNotice();
        }

        if (VersionUtil.isLeaked()) {
            printLeakNotice();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private void printCompileNotice() {
        LogUtils.severe("This is a compiled version of ChunksLab-Realms.");
        LogUtils.warn("Compiled versions come without default assets and support is not provided.");
        LogUtils.warn("Please purchase from BuiltByBit, Polymart, or SpigotMC to access the full version.");
    }

    private void printLeakNotice() {
        LogUtils.severe("This is a leaked version of ChunksLab-Realms.");
        LogUtils.severe("Piracy is not supported. Disabling plugin.");
        LogUtils.severe("Purchase the plugin legally via BuiltByBit, Polymart, or SpigotMC.");
    }

    private void printStartupMessages() {
        if (VersionUtil.isLeaked()) return;

        String nmsVersionName = VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()).name();

        Component[] messages = {
                format("Paper server detected, enabling paper support.", VersionUtil.isPaperServer()),
                format("Folia server detected, enabling folia support.", VersionUtil.isFoliaServer()),
                format("Version <version> has been detected.", "version", nmsVersionName),
                format("NMS will be use <version>.", "version", nmsVersionName),
                format("Plugin \"PlaceholderAPI\" detected, enabling hooks."),
                format("Thank you for choosing us, <white><username> (<user>)<#529ced>! We hope you enjoy using our plugin.",
                        "username", plugin.getUsername(), "user", plugin.getUser()),
                format("Successfully loaded on <os>", "os", OS.getOs().getPlatformName())
        };

        for (Component msg : messages) {
            if (msg != null) ChatUtils.sendMessage(Bukkit.getConsoleSender(), msg);
        }
    }

    // overloads for various message types
    private Component format(String message, boolean condition) {
        return condition ? ChatUtils.format("<prefix> <#55ffa4>" + message) : null;
    }

    private Component format(String message) {
        return ChatUtils.format("<prefix> <#55ffa4>" + message);
    }

    private Component format(String message, String key, String value) {
        if (value == null) return null;
        return ChatUtils.format("<prefix> <#55ffa4>" + message, Placeholder.parsed(key, value));
    }

    private Component format(String message, String key1, String val1, String key2, String val2) {
        if (val1 == null || val2 == null) return null;
        return ChatUtils.format("<prefix> <#529ced>" + message,
                Placeholder.parsed(key1, val1),
                Placeholder.parsed(key2, val2));
    }

    private String safeName(Object obj) {
        return obj != null ? obj.getClass().getSimpleName() : null;
    }

    private String getSimpleName(String className) {
        if (className == null) return "Unknown";
        String simple = className.substring(className.lastIndexOf('.') + 1);
        return simple.replace("Hook", "");
    }
}