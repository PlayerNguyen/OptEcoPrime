package com.playernguyen.optecoprime.trackers;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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
     * @throws Exception an exceptions causes when trackers cannot returns value
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
     * Logging as tanker level (prefix) before run something in callable parameter and then return it.
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param callable a callable after logged or not.
     * @param <T>      a datatype of callable
     * @return an object in callable
     * @throws ExecutionException could not execute error
     * @throws InterruptedException thread interruption error
     */
    public <T> T describeTankerAsync(@NotNull String message, Callable<T> callable)
            throws ExecutionException, InterruptedException {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "|-- [Tanker] " + message);
        }
        return plugin.getExecutorService().submit(callable).get();
    }

    /**
     * Logging as debug prefix before run something in callable parameter and return it.
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param callable a callable after logged or not.
     * @param <T>      a type of return value
     * @return value in callable.
     * @throws Exception an exceptions causes when trackers cannot returns value
     */
    public <T> T describe(@NotNull String message, Callable<T> callable) throws Exception {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(String.format("%s|-- [Debugger] %s%s",
                    ChatColor.GRAY,
                    ChatColor.RESET,
                    ChatColor.translateAlternateColorCodes('&', message)));
        }
        return callable.call();
    }

    /**
     * Asynchronously logging as debug prefix before run something in callable parameter and return it.
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param callable a callable after logged or not.
     * @param <T>      a type of return value
     * @return value in callable.
     * @throws Exception an exceptions causes when trackers cannot returns value, and cannot submit Future
     *                   to executor service.
     */
    public <T> T describeAsync(@NotNull String message, Callable<T> callable) throws Exception {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(String.format("%s|-- [Debugger] %s%s",
                    ChatColor.GRAY,
                    ChatColor.RESET,
                    ChatColor.translateAlternateColorCodes('&', message)));
        }
        return plugin.getExecutorService().submit(callable).get();
    }

    /**
     * Asynchronously logging as debug prefix before run something in callable parameter.
     * <p>
     * Only log when the debug mode is on.
     *
     * @param message  a message to transmit
     * @param runnable a runnable after logged or not.
     */
    public void describeAsync(@NotNull String message, Runnable runnable) {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(String.format("%s|-- [Debugger] %s%s",
                    ChatColor.GRAY,
                    ChatColor.RESET,
                    ChatColor.translateAlternateColorCodes('&', message)));
        }
        plugin.getExecutorService().submit(runnable);
    }

    /**
     * Logging only as debug prefix.
     *
     * @param message a message to transmit
     */
    public void describeNothing(@NotNull String message) {
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean()) {
            Bukkit.getConsoleSender().sendMessage(String.format("%s|-- [Debugger] %s%s",
                    ChatColor.GRAY,
                    ChatColor.RESET,
                    ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
}
