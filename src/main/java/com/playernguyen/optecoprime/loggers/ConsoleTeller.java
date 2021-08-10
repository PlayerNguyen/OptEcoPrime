package com.playernguyen.optecoprime.loggers;

import com.playernguyen.optecoprime.utils.SenderUtil.Teller;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Enhances the way we send message to console (not a bored logger).
 */
public class ConsoleTeller {

    private Teller teller = new Teller(Bukkit.getConsoleSender());
    private final String prefix;

    public ConsoleTeller(Plugin plugin) {
        this.prefix = "&7[&c" + plugin.getName() + "&7]&r ";
    }

    /**
     * Sends a new message by using teller, includes a prefix (plugin name).
     * 
     * @param message a message to send
     * @return the current class instance. For chain execute
     */
    public ConsoleTeller send(String message) {
        teller.next(prefix + message);
        return this;
    }

}
