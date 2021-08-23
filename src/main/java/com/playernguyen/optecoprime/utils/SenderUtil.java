package com.playernguyen.optecoprime.utils;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.dependencies.OptEcoPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sender util contains utilities in order to send message to CommandSender
 */
public class SenderUtil {
    private static final char COLOR_CHARACTER = '&';

    /**
     * Reveal a secret to command sender.
     * 
     * @param target  a target as {@link CommandSender} to reveal
     * @param context a context with formatted code
     */
    public static void reveal(CommandSender target, String context) {
        if (Bukkit.getPluginManager().getPlugin(OptEcoPrime.PLUGIN_PLACEHOLDER_API_NAME) != null) {
            if (target instanceof Player || target instanceof OfflinePlayer) {
                target.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CHARACTER,
                        OptEcoPlaceholder.requestPlaceholder((OfflinePlayer) target, context)));
                return;
            }
        }
        target.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CHARACTER, context));
    }

    /**
     * Teller is a messaging sends class, and enhances multiple lines message send.
     * Use teller whenever you want to send multiline message. i.e: <br>
     * Teller.init(sender).next(line1).next(line2).next(line3)... <br>
     * new Teller(sender).next(line1).next(line2).next(line3)...
     */
    public static class Teller {
        private final CommandSender sender;

        public Teller(CommandSender sender) {
            this.sender = sender;
        }

        /**
         * Initialize a new teller class
         *
         * @param sender a sender to get instance
         * @return new instance of this class
         */
        public static Teller init(CommandSender sender) {
            return new Teller(sender);
        }

        /**
         * Send a message with next middleware
         *
         * @param context a context to send, as message with translated color chars
         * @return a next teller, current instance
         */
        public Teller next(String context) {
            reveal(sender, context);
            return this;
        }

    }
}
