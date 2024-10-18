package com.chunkslab.realms.api.util;

import com.chunkslab.realms.api.RealmsAPI;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LogUtils {

    /**
     * Log an informational message.
     *
     * @param message The message to log.
     */
    public static void info(@NotNull String message) {
        RealmsAPI.getInstance().getLogger().info(message);
    }

    /**
     * Log a warning message.
     *
     * @param message The message to log.
     */
    public static void warn(@NotNull String message) {
        RealmsAPI.getInstance().getLogger().warning(message);
    }

    /**
     * Log a severe error message.
     *
     * @param message The message to log.
     */
    public static void severe(@NotNull String message) {
        RealmsAPI.getInstance().getLogger().severe(message);
    }

    /**
     * Log a debug message.
     *
     * @param message The message to log.
     */
    public static void debug(@NotNull String message) {
        if (!RealmsAPI.isDebugMode()) return;
        RealmsAPI.getInstance().getLogger().log(Level.INFO, "[DEBUG] " + message);
    }

    /**
     * Log a warning message with a throwable exception.
     *
     * @param message    The message to log.
     * @param throwable  The throwable exception to log.
     */
    public static void warn(@NotNull String message, Throwable throwable) {
        RealmsAPI.getInstance().getLogger().log(Level.WARNING, message, throwable);
    }

    /**
     * Log a severe error message with a throwable exception.
     *
     * @param message    The message to log.
     * @param throwable  The throwable exception to log.
     */
    public static void severe(@NotNull String message, Throwable throwable) {
        RealmsAPI.getInstance().getLogger().log(Level.SEVERE, message, throwable);
    }
}