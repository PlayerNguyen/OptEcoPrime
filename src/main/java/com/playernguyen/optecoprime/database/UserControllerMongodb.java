package com.playernguyen.optecoprime.database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.operation.OrderBy;
import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.database.mongodb.MongoDispatch;
import com.playernguyen.optecoprime.events.OptEcoBalanceChangeEvent;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserControllerMongodb implements UserController {
    private final OptEcoPrime plugin;
    private final MongoDispatch dispatch;

    public UserControllerMongodb(OptEcoPrime plugin, MongoDispatch dispatch) {
        this.plugin = plugin;
        this.dispatch = dispatch;
    }

    private void getToCollection(Consumer<MongoCollection<Document>> callback) {
        dispatch.getClient(client -> {
            MongoCollection<Document> collection = client
                    .getDatabase(plugin.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MONGODB_DATABASE))
                    .getCollection(plugin.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MONGODB_COLLECTION_USER));
            callback.accept(collection);
        });
    }

    private List<OptEcoPlayer> fetchAllUsers() {
        // Create empty list of player first
        List<OptEcoPlayer> players = new ArrayList<>();

        // Fetch items
        this.getToCollection(collection -> {
            MongoCursor<Document> iterator = collection.find().iterator();
            if (iterator.hasNext()) {
                Document document = iterator.next();
                players.add(new OptEcoPlayerInstance(
                        UUID.fromString(document.getString("_id")),
                        document.getDouble("balance")
                ));
            }
        });

        // Return items
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws Exception {
        return Optional.ofNullable(fetchAllUsers().get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayer(@NotNull UUID uuid, double balance) throws Exception {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        this.getToCollection(c -> {
            Document document = new Document();
            document.put("_id", uuid.toString());
            document.put("balance", balance);
            document.put("username", player.getName());
            c.insertOne(document);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayer(@NotNull UUID uuid) throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.getToCollection(c -> {
            FindIterable<Document> documents = c.find(Filters.eq("_id", uuid.toString()));
            atomicBoolean.set(documents.iterator().hasNext());
        });
        return atomicBoolean.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlayer(UUID uuid, double balance) throws Exception {
        OptEcoPlayer persistedPlayer = this.getPlayerByUUID(uuid).orElseThrow(() -> new NullPointerException(
                "cannot update non-existed database player causes player not found " + uuid));

        // Call event after update
        Bukkit.getPluginManager().callEvent(
                new OptEcoBalanceChangeEvent(plugin, persistedPlayer, persistedPlayer.getBalance(), balance));

        plugin.getTrackers().describeNothing(
                String.format("Update &c[%s] &m%s &8-> &e%s", uuid, persistedPlayer.getBalance(), balance));
        // Then update
        this.getToCollection(c -> c.findOneAndUpdate(Filters.eq("_id", uuid.toString()),
                Updates.set("balance", balance)
        ));
    }

    @Override
    public List<OptEcoPlayer> getHighestBalancePlayers(int limit) throws Exception {
        List<OptEcoPlayer> players = new ArrayList<>();

        this.getToCollection(collection -> {
            FindIterable<Document> element = collection
                    .find()
                    .sort(new BasicDBObject("balance", -1))
                    .limit(limit);
            for (Document next : element) {
                players.add(new OptEcoPlayerInstance(
                        UUID.fromString(next.getString("_id")),
                        next.getDouble("balance")
                ));
            }
        });

        return players;
    }

    @Override
    public Optional<OptEcoPlayer> getHighestBalancePlayer(int offset) throws Exception {
        List<OptEcoPlayer> players = new ArrayList<>();

        this.getToCollection(collection -> {
            FindIterable<Document> element = collection
                    .find()
                    .sort(new BasicDBObject("balance", -1));
            for (Document next : element) {
                players.add(new OptEcoPlayerInstance(
                        UUID.fromString(next.getString("_id")),
                        next.getDouble("balance")
                ));
            }
        });

        return Optional.ofNullable(players.get(offset));
    }
}
