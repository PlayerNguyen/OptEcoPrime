package com.playernguyen.optecoprime.commands;

import com.playernguyen.dbcollective.sqlite.SQLiteDispatch;
import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.commands.core.CommandDefaultParameter;
import com.playernguyen.optecoprime.commands.core.CommandInterface;
import com.playernguyen.optecoprime.commands.core.CommandResult;
import com.playernguyen.optecoprime.commands.core.CommandSub;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SubOptEcoToPrime extends CommandSub {

    private static final String COMMAND_NAME = "prime";
    private static final List<String> COMMAND_ALIASES = Arrays.asList("toprime", "pr");
    private final OptEcoPrime prime;

    public SubOptEcoToPrime(OptEcoPrime prime, CommandInterface parent) {
        super(
                parent,
                COMMAND_NAME,
                Arrays.asList(
                        new CommandDefaultParameter(
                                prime.getLanguageConfiguration()
                                        .getWithoutPrefix(LanguageConfigurationModel
                                                .COMMAND_SQLITE)
                                        .toString(),
                                true
                        ),
                        new CommandDefaultParameter(
                                prime.getLanguageConfiguration()
                                        .getWithoutPrefix(LanguageConfigurationModel
                                                .COMMAND_TABLE_NAME)
                                        .toString(),
                                true
                        )
                ),
                prime.getLanguageConfiguration().getWithoutPrefix(
                        LanguageConfigurationModel.COMMAND_TO_PRIME_DESCRIPTION
                ).toString(),
                COMMAND_ALIASES
        );
        this.prime = prime;
    }

    @Override
    public CommandResult onExecute(CommandSender sender, List<String> params) {
        SenderUtil.Teller teller = SenderUtil.Teller.init(sender);
        // Command sender is invalid
        if (!(sender instanceof ConsoleCommandSender)) {
            teller.next("Invalid sender, must be a console sender");
            return CommandResult.NOTHING;
        }

        // Missing arguments
        if (params.size() < 2) {
            return CommandResult.MISSING_ARGUMENTS;
        }

        String type = params.get(0);
        String name = params.get(1);
        // sqlite
        if (type.contains("sqlite")) {
            File parent = new File(prime.getDataFolder().getParent(), "OptEco");

            // Not exists
            if (!parent.exists()) {
                prime.getConsoleTeller().send("&cFolder not found: &7"
                        + parent.getAbsolutePath());
                return CommandResult.NOTHING;
            }

            File file = new File(parent, "account.sqlite");
            try {
                SQLiteDispatch dispatch = new SQLiteDispatch(file.getAbsolutePath());
                dispatch.executeQuery((resultSetCallback) -> {
                    while (resultSetCallback.next()) {
                        UUID uuid = UUID.fromString(resultSetCallback.getString("uuid"));
                        double balance = resultSetCallback.getDouble("balance");

                        try {
                            // Ignore existed user
                            if (prime.getUserController().hasPlayer(uuid)) {
                                teller.next(String.format(
                                        "&cIgnore user %s",
                                        uuid
                                ));
                                return;
                            }
                            // Importing
                            teller.next(String.format(
                                    "&6Importing %s -> %s", uuid, balance
                            ));
                            prime.getUserController().addPlayer(uuid, balance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    teller.next("&aCompletely move your data!");
                }, "SELECT * FROM " + name + ";");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return CommandResult.NOTHING;
        }

        teller.next("Unsupported type");
        return CommandResult.NOTHING;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> params) {
        return null;
    }
}
