package com.playernguyen.optecoprime.commands;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandParameter;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.utils.NumberUtil;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.command.CommandSender;

import java.util.*;

public class SubOptEcoPerformance extends CommandSub {
    private static final String COMMAND_GUIDELINE = "Creates a performance";
    private static final String COMMAND_NAME = "performance";
    private final OptEcoPrime plugin;

    public SubOptEcoPerformance(OptEcoPrime plugin, CommandInterface parent) {
        super(parent,
                COMMAND_NAME,
                Collections.singletonList(CommandParameter.newParameter("request", true)),
                COMMAND_GUIDELINE,
                Collections.singletonList("test"));
        this.plugin = plugin;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {

        if (params.size() != 1) {
            return CommandResult.MISSING_ARGUMENTS;
        }

        String request = params.get(0);
        double requestAsNumber = new NumberUtil.NumberFilter(request).asNumber();
        try {
            // Record times
            long start = System.currentTimeMillis();
            for (int i = 0; i < requestAsNumber; i++) {
                plugin.getUserController()
                        .addPlayer(UUID.randomUUID(),
                                new Random().nextDouble() * 10000);
            }
            long total = System.currentTimeMillis() - start;
            SenderUtil.Teller.init(sender)
                    .next(String.format("&a Took %s ms to request", total));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }
}
