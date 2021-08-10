package com.playernguyen.optecoprime.commands.core;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A command interface.
 */
public interface CommandInterface {

    public static final String PARAMETER_REQUIRED_COLOR = "&7";
    public static final String PARAMETER_NON_REQUIRED_COLOR = "&8";
    public static final String GUIDELINE_COLOR = "&7";
    public static final String COMMAND_NAME_COLOR = "&6";

    /**
     * A command name, this name must be immutable and unique.
     *
     * @return a unique and immutable name.
     */
    String getName();

    /**
     * Aliases list string, contains abbreviation of this command.
     * 
     * @return abbreviation list of command
     */
    List<String> getAliases();

    /**
     * A guideline string to help player use command.
     *
     * @return a guideline string.
     */
    String getGuideline();

    /**
     * A parameter list to guide player to user command.
     *
     * @return a parameter list
     */
    List<CommandParameter> getParameters();

    /**
     * A parent command to persist parent command data. In order to get relative
     * node in children command.
     *
     * @return a parent list
     */
    @Nullable
    CommandInterface getParent();

    /**
     * A children command set contains child node of this command.
     *
     * @return a children command set
     */
    Set<CommandInterface> getChildren();

    /**
     * Call when sender triggers command via types.
     * 
     * @param sender a sender who sends this command
     * @param params parameters
     * @return command result to response.
     */
    CommandResult onExecute(CommandSender sender, List<String> params);

    /**
     * Call when sender triggers tab on command.
     * 
     * @param sender a sender who triggers tab
     * @param params parameters
     * @return suggestion list
     */
    List<String> onTab(CommandSender sender, List<String> params);

    /**
     * A permission to access command.
     * 
     * @return a permission as string
     */
    String getPermissions();

    /**
     * Call when sender has no permission to access to command.
     * 
     * @param sender a sender who has no permission to access
     * @param params parameters used this command
     */
    void onNoPermissionAccess(CommandSender sender, List<String> params);

    /**
     * Guideline text illustrates a current help for player to understand command as
     * string. A guideline text will be shows as follow:
     * 
     * <pre>
     * {command_name} {command_parameters}: {command_guideline}
     * </pre>
     * 
     * <br>
     * i.e:<br>
     * <ul>
     * <li>tp player x y z: Teleport player to another place</li>
     * </ul>
     * 
     * @return a unformatted text of guideline.
     */
    default String toGuidelineText() {
        StringBuilder builder = new StringBuilder();

        // First append command name
        builder.append(COMMAND_NAME_COLOR).append(this.getName()).append((this.getParameters().size() > 0 ? " " : ""));

        // Then, append all parameters
        Iterator<CommandParameter> iterator = this.getParameters().iterator();
        while (iterator.hasNext()) {
            CommandParameter parameter = iterator.next();
            builder.append((parameter.isRequire() ? PARAMETER_REQUIRED_COLOR : PARAMETER_NON_REQUIRED_COLOR))
                    .append("<").append(parameter.getName()).append(">");
            if (iterator.hasNext()) {
                builder.append(" ");
            }
        }

        // Append guideline
        builder.append(GUIDELINE_COLOR).append(": ").append(this.getGuideline());

        // Return
        return builder.toString();
    }

    /**
     * Call when the sender is not compatible with command.
     * 
     * @param sender a sender
     * @param params params
     */
    void onInvalidSender(CommandSender sender, List<String> params);
}
