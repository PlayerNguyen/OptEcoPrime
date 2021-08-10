package com.playernguyen.optecoprime.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandExecutor;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;

import org.bukkit.command.CommandSender;

/**
 * ExecutorOptEco
 */
public class ExecutorOptEco extends CommandExecutor {

    private static final String EXECUTOR_NAME = "opteco";
    private static final String EXECUTOR_GUIDELINE = "An OptEco plugin commands";
    private static final String HEADER = "&7--------- &c&lOptEcoPrime &7---------";
    private static final String PREFIX_GUIDELINE_TEXT = "&c&l/point ";
    private final List<String> CHILDREN_NAME = this.getChildren().stream().map(e -> e.getName())
            .collect(Collectors.toList());
    private final OptEcoPrime plugin;

    public ExecutorOptEco(OptEcoPrime plugin) {
        super(plugin, EXECUTOR_NAME, EXECUTOR_GUIDELINE, Arrays.asList("point", "p"));
        // Extends plugin instance
        this.plugin = plugin;
        // Add sub-command parameter to show
        this.getParameters()
                .add(CommandParameter.newParameter(
                        plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.PREFERENCES_SUB_COMMAND).toString(),
                        true));

        // Add mortal sub command 
        this.getChildren().add(new SubOptEcoMe(plugin, this));
        this.getChildren().add(new SubOptEcoSet(plugin, this));
        this.getChildren().add(new SubOptEcoHelp(plugin, this));
        this.getChildren().add(new SubOptEcoAdd(plugin, this));
        this.getChildren().add(new SubOptEcoTake(plugin, this));
        this.getChildren().add(new SubOptEcoOf(plugin, this));
        this.getChildren().add(new SubOptEcoPay(plugin, this));
        this.getChildren().add(new SubOptEcoReload(plugin, this));
        this.getChildren().forEach(e -> {
            plugin.getTrackers().describeNothing(e.getName() + " -> " +e.getPermissions());
        });
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        // Missing parameters (arguments)
        if (params.size() <= 0) {
            sendHelp(sender);
            return CommandResult.NOTHING;
        }

        // Find sub-command and executor
        CommandInterface subCommand = getChildren().stream().filter(e -> e.getName().equalsIgnoreCase(params.get(0)))
                .findFirst().orElse(null);

        // Sub command not found
        if (subCommand == null) {
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_COMMAND_NOT_FOUND);
            return CommandResult.NOTHING;
        }

        // Execute and return
        return subCommand.onExecute(sender, params.subList(1, params.size()));
    }

    /**
     * Send a help of OptEco command to player
     * 
     * @param sender a sender to send
     */
    private void sendHelp(CommandSender sender) {
        SenderUtil.reveal(sender, HEADER);
        this.getChildren().forEach(e -> {
            // Has permission, send message to sender. Otherwise do nothing
            if (sender.hasPermission(e.getPermissions())) {
                SenderUtil.reveal(sender, PREFIX_GUIDELINE_TEXT + e.toGuidelineText());
            }
        });
    }

    @Override
    public void onNoPermissionAccess(CommandSender sender, List<String> params) {
        // Send a no permission to player
        plugin.getLanguageConfiguration().sendWithPrefix(sender,
                LanguageConfigurationModel.COMMAND_SENDER_NO_PERMISSIONS);
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        
        return CHILDREN_NAME;
    }

    @Override
    public void onInvalidSender(CommandSender sender, List<String> params) {
        // Send a no permission to player
        plugin.getLanguageConfiguration().sendWithPrefix(sender, LanguageConfigurationModel.COMMAND_SENDER_UNSUPPORTED);
    }

    @Override
    public void onReceiveResult(CommandResult result, CommandSender sender, List<String> args) {
        // Null can ignore the receive
        if (result == null) {
            // Null response admonishment
            plugin.getTrackers().describeNothing("Command result is null. It is acceptable. However, "
                    + "I advice you to have another response type to enhance your code. NOTHING is better");
            return;
        }

        switch (result) {
            case NOTHING: {
                break;
            }
            case MISSING_ARGUMENTS: {
                
                plugin.getLanguageConfiguration().sendWithPrefix(sender,
                        LanguageConfigurationModel.COMMAND_SENDER_MISSING_ARGUMENT);
                break;
            }
            // Invalid result type
            default: {
                throw new IllegalStateException("Invalid result type");
            }
        }

    }

}