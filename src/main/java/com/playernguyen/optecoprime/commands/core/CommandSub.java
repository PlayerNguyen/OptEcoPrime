package com.playernguyen.optecoprime.commands.core;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public abstract class CommandSub implements CommandInterface {
    private final String name;
    private final List<String> aliases;
    private final CommandInterface parent;
    private final String guideline;
    private final List<CommandParameter> parameters;
    private final Set<CommandInterface> children = new HashSet<>();

    public CommandSub(CommandInterface parent, String name, List<CommandParameter> parameters, String guideline,
            List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        this.parent = parent;
        this.guideline = guideline;
        this.parameters = parameters;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public Set<CommandInterface> getChildren() {
        return children;
    }

    @Override
    public String getGuideline() {
        return guideline;
    }

    @Override
    public List<CommandParameter> getParameters() {
        return parameters;
    }

    @Override
    public @Nullable CommandInterface getParent() {
        return parent;
    }

    @Override
    public String getPermissions() {
        return Objects.requireNonNull(this.getParent()).getPermissions().concat(".").concat(getName());
    }

    @Override
    public void onInvalidSender(CommandSender sender, List<String> params) {
        this.parent.onInvalidSender(sender, params);
    }

    @Override
    public void onNoPermissionAccess(CommandSender sender, List<String> params) {
        // Call to parent (execute)
        parent.onNoPermissionAccess(sender, params);
    }

}
