package com.playernguyen.optecoprime.commands;

import java.util.Arrays;
import java.util.List;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.NumberUtil.NumberFilter;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Create a new transaction, transact a balance to target
 */
public class SubOptEcoPay extends CommandSub {

    private static final String COMMAND_NAME = "pay";
    private static final List<String> COMMAND_ALIASES = Arrays.asList("p", "transact");
    private final OptEcoPrime plugin;

    public SubOptEcoPay(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME, Arrays.asList(
                CommandParameter.newParameter(
                        plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_PLAYER).toString(),
                        true),
                CommandParameter.newParameter(
                        plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_AMOUNT).toString(),
                        true)),
                plugin.getLanguageConfiguration().getWithoutPrefix(LanguageConfigurationModel.COMMAND_PAY_DESCRIPTION)
                        .toString(),
                COMMAND_ALIASES);

        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        // Match sender
        if (!(sender instanceof Player)) {
            onInvalidSender(sender, params);
            return CommandResult.NOTHING;
        }

        // Missing parameters
        if (params.size() != 2) {
            Teller.init(sender).next(toGuidelineText());
            return CommandResult.MISSING_ARGUMENTS;
        }

        // Find online player (yes, must be online player)
        Player target = Bukkit.getPlayer(params.get(0));

        // Not found player
        if (target == null) {
            Teller.init(sender).next(plugin.getLanguageConfiguration()
                    .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_PLAYER_NOT_FOUND).toString());
            return CommandResult.NOTHING;
        }

        // Get and filter a number data
        NumberFilter filter = new NumberFilter(params.get(1));

        // If a parameter is not a number (invalid number)
        if (!filter.isNumber()) {
            Teller.init(sender).next(plugin.getLanguageConfiguration()
                    .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_INVALID_NUMBER).toString());
            return CommandResult.NOTHING;
        }

        // And must not be negative
        if (filter.isNegative()) {
            Teller.init(sender).next(plugin.getLanguageConfiguration()
                    .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_NUMBER_MUST_POSITIVE).toString());
            return CommandResult.NOTHING;
        }

        // Cannot send to themselves
        Player _sender = (Player) sender;

        // Match uuid, response fail
        if (target.getUniqueId().equals(_sender.getUniqueId())) {
            Teller.init(sender).next(plugin.getLanguageConfiguration()
                    .getWithPrefix(LanguageConfigurationModel.COMMAND_PAY_ONESELF_PAY).toString());
            return CommandResult.NOTHING;
        }

        // If passed all tests, continue creating transact
        try {
            // Check whether player have enough money to pay
            if (plugin.getPlayerManager().getPlayer(_sender.getUniqueId()).getBalance() < filter.asNumber()) {
                Teller.init(sender)
                        .next(plugin.getLanguageConfiguration()
                        .getWithPrefix(LanguageConfigurationModel.COMMAND_PAY_BALANCE_NOT_ENOUGH).toString()); 
                return CommandResult.NOTHING;
            }

            // Transact an amount of balance to target
            plugin.getPlayerManager().transact(_sender.getUniqueId(), target.getUniqueId(), filter.asNumber());
            // Send message to response to player
            Teller.init(sender)
                    .next(plugin.getLanguageConfiguration()
                            .getWithPrefix(LanguageConfigurationModel.COMMAND_PAY_RESPONSE)
                            .changeFlex("%amount%", filter.asNumber()).change("%target%", target.getName()).toString());
        } catch (Exception e) {
            // Tell sender causing error
            Teller.init(sender).next(plugin.getLanguageConfiguration()
                    .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_UNEXPECTED_ERROR).toString());
            // Print stack trance
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }

}
