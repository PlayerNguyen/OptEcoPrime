package com.playernguyen.optecoprime.commands;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.NumberUtil.NumberFilter;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;
import com.playernguyen.optecoprime.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A command to take player balance in their account.
 */
public class SubOptEcoTake extends CommandSub {
    private static final String COMMAND_NAME = "take";
    private static final List<String> COMMAND_ALIASES = Arrays.asList("t");
    private final OptEcoPrime plugin;

    public SubOptEcoTake(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME, Arrays.asList(
                CommandParameter.newParameter(plugin.getLanguageConfiguration()
                        .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_PLAYER_OR_UUID).toString(),
                        true),
                CommandParameter.newParameter(
                        plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_AMOUNT).toString(),
                        true)),
                plugin.getLanguageConfiguration().getWithoutPrefix(LanguageConfigurationModel.COMMAND_TAKE_DESCRIPTION)
                        .toString(),
                COMMAND_ALIASES);
        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {
        // Missing parameters
        if (params.size() != 2) {
            Teller.init(sender).next(toGuidelineText());
            return CommandResult.MISSING_ARGUMENTS;
        }

        // Target finder
        UUID uuid;

        // Is not an uuid
        if (!StringUtil.isUUID(params.get(0))) {
            Player player = Bukkit.getPlayer(params.get(0));

            // Not found a player
            if (player == null) {
                Teller.init(sender)
                        .next(plugin.getLanguageConfiguration()
                                .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_PLAYER_NOT_FOUND)
                                .change("%target%", params.get(0)).toString());
                return CommandResult.NOTHING;
            }

            // Otherwise, set the uuid of that player to uuid variable
            uuid = player.getUniqueId();
        } else
            uuid = UUID.fromString(params.get(0));

        // Filter a number
        NumberFilter filter = new NumberFilter(params.get(1));

        // Is not a number or invalid
        if (!filter.isNumber()) {
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_INVALID_NUMBER);
            return CommandResult.NOTHING;
        }

        // Add value cannot be a negative number
        if (filter.isNegative()) {
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_NUMBER_MUST_POSITIVE);
            return CommandResult.NOTHING;
        }

        try {
            // Paying in action
            plugin.getPlayerManager().takePlayerBalance(uuid, filter.asNumber());

            // Then response to the sender
            Teller.init(sender)
                    .next(plugin.getLanguageConfiguration()
                            .getWithPrefix(LanguageConfigurationModel.COMMAND_TAKE_RESPONSE)
                            .change("%target%", Bukkit.getOfflinePlayer(uuid).getName())
                            .changeFlex("%amount%", filter.asNumber()).toString());

        } catch (Exception e) {
            plugin.getLanguageConfiguration().sendWithPrefix(sender,
                    LanguageConfigurationModel.COMMAND_RESPONSE_UNEXPECTED_ERROR);
            e.printStackTrace();
        }
        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        // TODO Auto-generated method stub
        return null;
    }

}