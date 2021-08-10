package com.playernguyen.optecoprime.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubOptEcoMe extends CommandSub {

    private static final String COMMAND_NAME = "me";
    private final OptEcoPrime plugin;

    public SubOptEcoMe(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME, Collections.emptyList(), plugin.getLanguageConfiguration()
                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_ME_DESCRIPTION).toString(), Arrays.asList("m"));

        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {
        if (!(sender instanceof Player)) {
            onInvalidSender(sender, params);
            return CommandResult.NOTHING;
        }

        Player player = (Player) sender;
        try {
            OptEcoPlayer optEcoPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

            Teller.init(player)
                    .next(plugin.getLanguageConfiguration()
                            .getWithPrefix(LanguageConfigurationModel.COMMAND_ME_RESPONSE)
                            .changeFlex("%point%", optEcoPlayer.getBalance())
                            .toString());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }

}
