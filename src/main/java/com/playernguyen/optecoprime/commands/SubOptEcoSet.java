package com.playernguyen.optecoprime.commands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.StringUtil;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubOptEcoSet extends CommandSub {
    private static final String COMMAND_NAME = "set";
    private OptEcoPrime plugin;

    public SubOptEcoSet(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME, Arrays.asList(
                CommandParameter.newParameter(plugin.getLanguageConfiguration()
                        .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_PLAYER_OR_UUID).toString(),
                        true),
                CommandParameter.newParameter(
                        plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_AMOUNT).toString(),
                        true)),
                plugin.getLanguageConfiguration().getWithoutPrefix(LanguageConfigurationModel.COMMAND_SET_DESCRIPTION)
                        .toString(),
                Arrays.asList("s"));

        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        // Missing arguments
        if (params.size() < 2) {
            return CommandResult.MISSING_ARGUMENTS;
        }

        try {

            UUID currentUUID = null;
            // If using username, the player must online
            if (!StringUtil.isUUID(params.get(0))) {
                Player alternativePlayer = Bukkit.getPlayer(params.get(0));

                // Send player not found
                if (alternativePlayer == null) {
                    plugin.getLanguageConfiguration().sendWithPrefix(sender,
                            LanguageConfigurationModel.COMMAND_RESPONSE_PLAYER_NOT_FOUND);

                    return CommandResult.NOTHING;
                }

                currentUUID = alternativePlayer.getUniqueId();
            } else {
                currentUUID = UUID.fromString(params.get(0));
            }

            // Set a current uuid
            double amount = Double.parseDouble(params.get(1));
            plugin.getPlayerManager().setPlayerBalance(currentUUID, amount);

            // Send message to sender
            Teller.init(sender)
                    .next(plugin.getLanguageConfiguration()
                            .getWithPrefix(LanguageConfigurationModel.COMMAND_SET_RESPONSE)
                            .change("%player%", Bukkit.getOfflinePlayer(currentUUID).getName())
                            .changeFlex("%amount%", amount).toString());

        } catch (NumberFormatException exception) {
            // Number invalid
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_INVALID_NUMBER);

            // Print stack trace
            exception.printStackTrace();

            // Return
            return CommandResult.NOTHING;
        } catch (Exception exception) {
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_INVALID_NUMBER);
            exception.printStackTrace();
        }
        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        // TODO Auto-generated method stub
        return null;
    }

}
