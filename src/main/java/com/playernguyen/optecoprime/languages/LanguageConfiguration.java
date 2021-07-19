package com.playernguyen.optecoprime.languages;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.configurations.ConfigurationAbstract;
import com.playernguyen.optecoprime.utils.ReplaceableString;
import com.playernguyen.optecoprime.utils.SenderUtil;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Language configuration object.
 */
public class LanguageConfiguration extends ConfigurationAbstract<LanguageConfigurationModel> {
    private static final String SEPARATOR_CHAR = " ";
    private static final String DIRECTORY_NAME = "Language";
    private static final String FILE_NAME = "Language.yml";
    private OptEcoPrime plugin;

    public LanguageConfiguration(OptEcoPrime plugin) throws Exception {
        super(plugin, FILE_NAME, LanguageConfigurationModel.class, DIRECTORY_NAME);
        this.plugin = plugin;
    }

    /**
     * Get a current model as Replaceable string with prefix.
     * 
     * @param model a model contains configured language.
     * @return a replaceable string object
     * @see ReplaceableString
     */
    public ReplaceableString getWithoutPrefix(@NotNull LanguageConfigurationModel model) {
        return new ReplaceableString(plugin, this.get(model).asString());
    }

    /**
     * Get a configured item with prefix as ReplaceableString.
     * 
     * @param model a model contains configured language
     * @return a prefixed string with replaceable string
     * @see ReplaceableString
     */
    public ReplaceableString getWithPrefix(@NotNull LanguageConfigurationModel model) {
        return new ReplaceableString(plugin,
                this.get(LanguageConfigurationModel.PREFIX).asString() + SEPARATOR_CHAR + this.get(model).asString());
    }

    /**
     * Send with a configured prefix .
     * 
     * @param target  a target to send
     * @param message a message to send
     */
    private void sendWithPrefix(@NotNull CommandSender target, @NotNull String message) {
        SenderUtil.reveal(target,
                this.get(LanguageConfigurationModel.PREFIX).asString().concat(SEPARATOR_CHAR).concat(message));
    }

    /**
     * Send with a configured prefix, message as model.
     * 
     * @param target a target to send
     * @param model  a model to send as message
     */
    public void sendWithPrefix(@NotNull CommandSender target, @NotNull LanguageConfigurationModel model) {
        this.sendWithPrefix(target, this.get(model).asString());
    }

}
