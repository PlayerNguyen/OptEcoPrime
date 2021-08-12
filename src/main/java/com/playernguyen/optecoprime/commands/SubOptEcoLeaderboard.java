package com.playernguyen.optecoprime.commands;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubOptEcoLeaderboard extends CommandSub {

    private static final String COMMAND_NAME = "leaderboard";
    private final OptEcoPrime prime;

    public SubOptEcoLeaderboard(OptEcoPrime prime, CommandInterface parent) {
        super(parent,
                COMMAND_NAME,
                Collections.emptyList(),
                prime.getLanguageConfiguration()
                        .getWithoutPrefix(LanguageConfigurationModel.COMMAND_LEADERBOARD_DESCRIPTION)
                        .toString(),
                Arrays.asList("lb", "top"));
        this.prime = prime;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        try {
            List<OptEcoPlayer> highestBalancePlayers = prime.getUserController()
                    .getHighestBalancePlayers(
                            prime
                                    .getSettingConfiguration()
                                    .get(SettingConfigurationModel.LEADERBOARD_LIMIT_AMOUNT)
                                    .asInt()
                    );
            highestBalancePlayers.forEach(player -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
                SenderUtil.Teller.init(sender)
                        .next(String.format(
                                "&7#  &c%s  %s",
                                offlinePlayer.getName(),
                                player.getBalance()
                        ));
            });
        } catch (Exception e) {
            SenderUtil.Teller.init(sender)
                    .next(
                            prime.getLanguageConfiguration()
                                    .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_UNEXPECTED_ERROR)
                                    .toString()
                    );
        }

        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }
}
