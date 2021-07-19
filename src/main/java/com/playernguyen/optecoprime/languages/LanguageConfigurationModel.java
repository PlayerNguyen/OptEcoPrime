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

    COMMAND_ME_DESCRIPTION("Command.Me.Description", "Reveal a current balance that player are having"),
    COMMAND_ME_RESPONSE("Command.Me.Response", "&7You currently have &c%point% %currency_symbol% "),

    COMMAND_SET_DESCRIPTION("Command.Set.Description", "Define or set a new balance to player"),
    COMMAND_SET_RESPONSE("Command.Set.Response", "&aSet the balance of &6%player% &ato &6%amount% %currency_symbol% "),

    COMMAND_HELP_DESCRIPTION("Command.Help.Description", "Show a plugin commands"),

    COMMAND_RESPONSE_PLAYER_NOT_FOUND("Command.GeneralResponse.PlayerNotFound", "&cPlayer not found."),
    COMMAND_RESPONSE_INVALID_NUMBER("Command.GeneralResponse.InvalidNumber",
            "&cA number that you inputted was invalid"),
    COMMAND_RESPONSE_UNEXPECTED_ERROR("Command.GeneralResponse.UnexpectedError",
            "&cUnexpected error when executing command."),
    COMMAND_RESPONSE_COMMAND_NOT_FOUND("Command.GeneralResponse.CommandNotFound",
            "&cCommand not found. Please use &7/point help&c for any assistance"),

    COMMAND_PARAMETER_PLAYER_OR_UUID("Command.Parameter.PlayerOrUUID", "player/uuid"),
    COMMAND_PARAMETER_AMOUNT("Command.Parameter.Amount", "amount"),

    PREFERENCES_SUB_COMMAND("Preferences.SubCommand", "sub-command"),;

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
