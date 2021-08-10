package com.playernguyen.optecoprime.commands;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubOptEcoReload extends CommandSub {
    // Constants
    private static final String COMMAND_NAME = "reload";
    private static final List<CommandParameter> COMMAND_PARAMETERS = Collections.emptyList();
    private static final List<String> COMMAND_ALIASES = Arrays.asList("re", "rl", "r");
    // Variables
    private final OptEcoPrime plugin;

    public SubOptEcoReload(OptEcoPrime plugin, CommandInterface parent) {
        super(parent,
                COMMAND_NAME,
                COMMAND_PARAMETERS,
                plugin.getLanguageConfiguration().getWithoutPrefix(LanguageConfigurationModel.COMMAND_RELOAD_DESCRIPTION).toString(),
                COMMAND_ALIASES);
        this.plugin = plugin;
    }

    /**
     * Call when sender triggers command via types.
     *
     * @param sender a sender who sends this command
     * @param params parameters
     * @return command result to response.
     */
    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {
        // Reload plugin
        plugin.reload();
        SenderUtil.Teller.init(sender)
                .next("&aSuccessfully reload &6OptEcoPrime &aversion " + this.plugin.getDescription().getVersion());

        // Nothing
        return CommandResult.NOTHING;
    }

    /**
     * Call when sender triggers tab on command.
     *
     * @param sender a sender who triggers tab
     * @param params parameters
     * @return suggestion list
     */
    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }
}
