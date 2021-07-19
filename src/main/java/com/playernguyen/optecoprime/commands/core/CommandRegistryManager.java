package com.playernguyen.optecoprime.commands.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

/**
 * CommandRegistryManager helps you to register your command without plugin.yml.
 * It contains all command inside and register to CraftServer object.
 */
public class CommandRegistryManager {

    private final List<CommandExecutor> executors = new ArrayList<>();

    /**
     * Add new command into container
     * 
     * @param executor an executor you want to register
     */
    public void addCommand(CommandExecutor executor) {
        this.executors.add(executor);
    }

    /**
     * Clean up anything
     */
    public void clearAll() {
        this.executors.clear();
    }

    /**
     * A method that register new command.
     * 
     * @throws NoSuchFieldException     bukkit exception
     * @throws SecurityException        bukkit exception
     * @throws IllegalArgumentException bukkit exception
     * @throws IllegalAccessException   bukkit exception
     */
    public void load()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

        executors.forEach(executor -> {
            commandMap.register(executor.getName(), executor);
        });
    }

}