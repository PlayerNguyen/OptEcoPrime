package com.playernguyen.optecoprime.languages;

import com.playernguyen.optecoprime.configurations.ConfigurationSectionModel;

/**
 * Language configuration model.
 */
public enum LanguageConfigurationModel implements ConfigurationSectionModel {

    PREFIX("Prefix", "&8[&cOptEcoPrime&8] ", "A language prefix, bare with message when send to player or system."),
    CURRENCY_SYMBOL("CurrencySymbol", "$"),
    COMMAND_SENDER_UNSUPPORTED("Command.SenderUnsupported", "&cThis sender cannot execute this command."),
    COMMAND_SENDER_MISSING_ARGUMENT("Command.SenderMissingArguments", "&cYou are executing without argument."),
    COMMAND_SENDER_NO_PERMISSIONS("Command.SenderNoPermissionToExecute",
            "&cYou have no permission to access this command."),

    COMMAND_ME_DESCRIPTION("Command.Me.Description", "Reveal a current balance of player"),
    COMMAND_ME_RESPONSE("Command.Me.Response", "&7You currently have &c%point% %currency_symbol% "),

    COMMAND_SET_DESCRIPTION("Command.Set.Description", "Set a new balance to player"),
    COMMAND_SET_RESPONSE("Command.Set.Response", "&aSet the balance of &6%target% &ato &6%amount% %currency_symbol% "),

    COMMAND_HELP_DESCRIPTION("Command.Help.Description", "Show a plugin commands"),

    COMMAND_ADD_DESCRIPTION("Command.Add.Description", "Add a player current balance"),
    COMMAND_ADD_RESPONSE("Command.Add.Response", "&aAdd &6%amount% %currency_symbol%&a into &6%target%&a account"),

    COMMAND_TAKE_DESCRIPTION("Command.Take.Description", "Take a player current balance"),
    COMMAND_TAKE_RESPONSE("Command.Take.Response", "&aTake &6%amount% %currency_symbol%&a from &6%target%&a account"),

    COMMAND_LOOK_DESCRIPTION("Command.Look.Description", "Reveal a balance of specific player."),
    COMMAND_LOOK_RESPONSE("Command.Look.Response", "&aA player &6%target%&a currently have &6%amount% %currency_symbol%&a."),

    COMMAND_PAY_DESCRIPTION("Command.Pay.Description", "Transact your balance to another online player"),
    COMMAND_PAY_RESPONSE("Command.Pay.Response", "&aTransact %amount% %currency_symbol% to %target%"),
    COMMAND_PAY_ONESELF_PAY("Command.Pay.OneselfPay", "&cYou cannot pay to yourself."),
    COMMAND_PAY_BALANCE_NOT_ENOUGH("Command.Pay.BalanceNotEnough", "&cYou have not have enough in your account to pay"),

    COMMAND_LEADERBOARD_DESCRIPTION("Command.Leaderboard.Description", "Show the highest balance players"),

    COMMAND_RELOAD_DESCRIPTION("Command.Reload.Description", "Reload your configurations of OptEcoPrime"),

    COMMAND_RESPONSE_PLAYER_NOT_FOUND("Command.GeneralResponse.PlayerNotFound", "&cPlayer &6%target%&c not found."),
    COMMAND_RESPONSE_INVALID_NUMBER("Command.GeneralResponse.InvalidNumber",
            "&cA number that you inputted was invalid."),
    COMMAND_RESPONSE_NUMBER_MUST_POSITIVE("Command.GeneralResponse.NumberMustPositive",
            "&cA number must be a positive value."),
    COMMAND_RESPONSE_UNEXPECTED_ERROR("Command.GeneralResponse.UnexpectedError",
            "&cUnexpected error when executing command."),
    COMMAND_RESPONSE_COMMAND_NOT_FOUND("Command.GeneralResponse.CommandNotFound",
            "&cCommand not found. Please use &7/point help&c for any assistance."),

    COMMAND_PARAMETER_PLAYER_OR_UUID("Command.Parameter.PlayerOrUUID", "player/uuid"),
    COMMAND_PARAMETER_PLAYER("Command.Parameter.Player", "player"),
    COMMAND_PARAMETER_AMOUNT("Command.Parameter.Amount", "amount"),

    PREFERENCES_SUB_COMMAND("Preferences.SubCommand", "sub-command"),

    EVENT_UPDATE_BALANCE_MESSAGE("Event.UpdateBalanceMessage", "&6Your current balance now is %amount% %currency_symbol%"),

    COMMAND_TO_PRIME_DESCRIPTION("Command.ToPrime.Description",
            "Change a data from OptEco to OptEcoPrime"
    ),

    COMMAND_SQLITE("Command.SQLite",
            "sqlite"),
    COMMAND_TABLE_NAME("Command.TableName",
            "table_name");

    private final String path;
    private final Object instance;
    private final String[] comments;

    LanguageConfigurationModel(String path, Object instance, String... comments) {
        this.path = path;
        this.instance = instance;
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getComments() {
        return comments;
    }
}
