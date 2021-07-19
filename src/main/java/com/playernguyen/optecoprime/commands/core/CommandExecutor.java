package com.playernguyen.optecoprime.commands.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CommandExecutor extends BukkitCommand implements CommandInterface, TabExecutor {
    private final Plugin plugin;
    private final String name;
    private final String guideline;
    private final List<CommandParameter> parameters;
    private final List<String> aliases;
    private final Set<CommandInterface> children = new HashSet<>();

    public CommandExecutor(Plugin plugin, String name, String guideline, List<String> aliases) {
        super(name);

        // CommandInterface
        this.plugin = plugin;
        this.name = name;
        this.guideline = guideline;
        this.parameters = new ArrayList<>();
        this.aliases = aliases;

        // BukkitCommand hierarchy
        this.description = guideline;
        this.usageMessage = "/" + name + " <sub command>";
        this.setAliases(aliases);
        this.setPermission(this.getPermission());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAliases() {
        // Aliases of executor can be replaced in plugin.yml
        return aliases;
    }

    /**
     * A command name, this name must be immutable and unique.
     *
     * @return a unique and immutable name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * A guideline string to help player use command.
     *
     * @return a guideline string.
     */
    @Override
    public String getGuideline() {
        return guideline;
    }

    /**
     * A parameter list to guide player to user command.
     *
     * @return a parameter list
     */
    @Override
    public List<CommandParameter> getParameters() {
        return parameters;
    }

    /**
     * A parent command to persist parent command data. In order to get relative
     * node in children command.
     *
     * @return a parent list
     */
    @Override
    public @Nullable CommandInterface getParent() {
        return null;
    }

    /**
     * A children command set contains child node of this command.
     *
     * @return a children command set
     */
    @Override
    public Set<CommandInterface> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return onTab(sender, Arrays.asList(args));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check permission, whether right, call onExecute
        if (!sender.hasPermission(getPermissions())) {

            // Handle a result
            this.onNoPermissionAccess(sender, Arrays.asList(args));
            return true;
        }

        // Has no permission to access command
        this.onReceiveResult(onExecute(sender, Arrays.asList(args)), sender,
                Arrays.asList(args));

        // Always return true, Bukkit command return is ****
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract CommandResult onExecute(CommandSender sender, List<String> params);

    /**
     * Call when sender is invalid. This method is pre-processing code in order to
     * validate parameters.
     * 
     * @param sender a sender triggers this command
     * @param params parameters
     */
    public abstract void onInvalidSender(CommandSender sender, List<String> params);

    /**
     * A post-processing made to response all results after call
     * {@link #onExecute(CommandSender, List)}.
     * 
     * @param result a result after call {@link #onExecute(CommandSender, List)}
     * @param sender a sender who sent this command
     * @param args   arguments of sender
     */
    public abstract void onReceiveResult(CommandResult result, CommandSender sender, List<String> args);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermissions() {
        return plugin.getName().concat(".command.").concat(this.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return onCommand(sender, null, commandLabel, args);
    }
}