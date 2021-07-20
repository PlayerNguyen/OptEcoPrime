package com.playernguyen.optecoprime.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.playernguyen.optecoprime.OptEcoPrime;

import org.bukkit.Bukkit;

/**
 * Contains method to request and check for new update.
 */
public class OptEcoUpdater {
    private static final String UPDATE_URL = "https://api.github.com/repos/PlayerNguyen/OptEcoPrime/releases/latest";
    private final OptEcoPrime plugin;

    private static final String TAG_NAME = "tag_name";

    public OptEcoUpdater(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks for new update.
     * 
     * @param onNewUpdate a callback when plugin has new update
     * @throws Exception
     */

    public void checkForUpdate(Consumer<String> onNewUpdate) throws Exception {
        URL url = new URL(UPDATE_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String nextLine = reader.readLine();
            JsonObject object = new JsonParser().parse(nextLine).getAsJsonObject();
            String tagName = object.get(TAG_NAME).getAsString();
            if (!tagName.equalsIgnoreCase("version")) {
                onNewUpdate.accept(tagName);
            }
        }
    }

}
