package com.playernguyen.optecoprime.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;

import org.bukkit.command.CommandSender;

/**
 * SubOptEcoHelp
 */
public class SubOptEcoHelp extends CommandSub {

    private static final String HEADER = "&7--------- &c&lOptEcoPrime &7---------";
    private static final String PREFIX_GUIDELINE_TEXT = "&c&l/point ";

    private static final String COMMAND_NAME = "help";
    private static final List<String> COMMAND_ALIASES = Arrays.asList("h");
    private static final List<CommandParameter> COMMAND_PARAMETER_LIST = Collections.emptyList();

    public SubOptEcoHelp(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME, COMMAND_PARAMETER_LIST, plugin.getLanguageConfiguration()
                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_HELP_DESCRIPTION).toString(), COMMAND_ALIASES);

    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        // Send help to sender
        sendHelp(sender);
        // Break code without error
        return CommandResult.NOTHING;
    }

    /**
     * Send a help of OptEco command to player
     * 
     * @param sender a sender to send
     */
    private void sendHelp(CommandSender sender) {
        SenderUtil.reveal(sender, HEADER);
        Objects.requireNonNull(this.getParent()).getChildren().forEach(e -> {
            // Has permission, send message to sender. Otherwise do nothing
            if (sender.hasPermission(e.getPermissions())) {
                SenderUtil.reveal(sender, PREFIX_GUIDELINE_TEXT + e.toGuidelineText());
            }
        });
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }

}