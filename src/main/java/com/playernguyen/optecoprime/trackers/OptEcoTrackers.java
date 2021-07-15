package com.playernguyen.optecoprime.trackers;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

/**
 * Trackers do many debug stuffs
 */
public class OptEcoTrackers {
    private final OptEcoPrime plugin;

    public OptEcoTrackers(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Logging as database level (prefix) before run something in runnable parameter
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param runnable a runnable after logged or not.
     */
    public void describeDatabase(@NotNull String message, Runnable runnable) {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "|-- [Database] " + message);
        }
        runnable.run();
    }

    /**
     * Logging as database level (prefix) before run something in callable parameter and return it.
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param callable a callable after logged or not.
     * @return value in callable.
     */
    public Object describeDatabase(@NotNull String message, Callable<?> callable) throws Exception {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "|-- [Database] " + message);
        }
        return callable.call();
    }

    /**
     * Logging as tanker level (prefix) before run something in runnable parameter
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param runnable a runnable after logged or not.
     */
    public void describeTanker(@NotNull String message, Runnable runnable) {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "|-- [Tanker] " + message);
        }
        runnable.run();
    }

    /**
     * Logging as tanker level (prefix) before run something in callable parameter and return it.
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param callable a callable after logged or not.
     * @return value in callable.
     */
    public Object describeTanker(@NotNull String message, Callable<?> callable) throws Exception {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "|-- [Tanker] " + message);
        }
        return callable.call();
    }
}
