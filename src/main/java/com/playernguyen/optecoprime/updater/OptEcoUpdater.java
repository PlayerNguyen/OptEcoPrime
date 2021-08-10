package com.playernguyen.optecoprime.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.playernguyen.optecoprime.OptEcoPrime;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Contains method to request and check for new update.
 */
public class OptEcoUpdater {
    private static final String UPDATE_URL = "https://api.github.com/repos/PlayerNguyen/OptEcoPrime/releases/latest";
    private static final String TAG_NAME = "tag_name";

    private final OptEcoPrime plugin;

    public OptEcoUpdater(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks for new update.
     * 
     * @param onNewUpdate a callback when plugin has new update
     * @throws Exception errors when update
     */
    public void checkForUpdate(Consumer<String> onNewUpdate) throws Exception {
        plugin.getExecutorService().submit(() -> {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
