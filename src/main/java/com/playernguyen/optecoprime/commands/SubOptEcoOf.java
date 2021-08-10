package com.playernguyen.optecoprime.commands;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;
import com.playernguyen.optecoprime.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SubOptEcoOf extends CommandSub {

    private static final String COMMAND_NAME = "look";
    private static final List<String> COMMAND_ALIASES = Arrays.asList("of", "l");
    private final OptEcoPrime plugin;

    public SubOptEcoOf(OptEcoPrime plugin, CommandInterface parent) {
        super(parent, COMMAND_NAME,
                Collections.singletonList(CommandParameter.newParameter(plugin.getLanguageConfiguration()
                                .getWithoutPrefix(LanguageConfigurationModel.COMMAND_PARAMETER_PLAYER_OR_UUID).toString(),
                        true)),
                plugin.getLanguageConfiguration().getWithoutPrefix(LanguageConfigurationModel.COMMAND_LOOK_DESCRIPTION)
                        .toString(),
                COMMAND_ALIASES);

        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {
        // Missing parameter
        if (params.size() < 1) {
            Teller.init(sender).next(toGuidelineText());
            return CommandResult.MISSING_ARGUMENTS;
        }

        // Extract parameter
        String rawTarget = params.get(0);
        UUID uuid = null;

        // Is not an uuid
        if (!StringUtil.isUUID(rawTarget)) {
            Player player = Bukkit.getPlayer(rawTarget);
            // Not found player
            if (player == null) {
                Teller.init(sender)
                        .next(plugin.getLanguageConfiguration()
                                .getWithPrefix(LanguageConfigurationModel.COMMAND_RESPONSE_PLAYER_NOT_FOUND)
                                .change("%target%", params.get(0)).toString());
                return CommandResult.NOTHING;
            }
            // Set a unique id of the player was found
            uuid = player.getUniqueId();
        } else
            uuid = UUID.fromString(rawTarget);

        try {
            OptEcoPlayer player = plugin.getPlayerManager().getPlayer(uuid);

            Teller.init(sender)
                    .next(plugin.getLanguageConfiguration()
                            .getWithPrefix(LanguageConfigurationModel.COMMAND_LOOK_RESPONSE)
                            .change("%target%", params.get(0)).changeFlex("%amount%", player.getBalance()).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }

}
