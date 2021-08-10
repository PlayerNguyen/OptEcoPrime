package com.playernguyen.optecoprime.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.database.mongodb.MongoDispatch;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bson.Document;
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

    public void getToCollection(Consumer<MongoCollection<Document>> callback) {
        dispatch.getClient(client -> {
            MongoCollection<Document> collection = client
                    .getDatabase(plugin.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MONGODB_DATABASE))
                    .getCollection(plugin.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MONGODB_COLLECTION));
            callback.accept(collection);
        });
    }

    public List<OptEcoPlayer> fetchAllUsers() {
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

    @Override
    public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws Exception {
        return Optional.ofNullable(fetchAllUsers().get(0));
    }

    @Override
    public void addPlayer(@NotNull UUID uuid, double balance) throws Exception {
        this.getToCollection(c -> {
            Document document = new Document();
            document.put("_id", uuid.toString());
            document.put("balance", balance);
            c.insertOne(document);
        });
    }

    @Override
    public boolean hasPlayer(@NotNull UUID uuid) throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.getToCollection(c -> {
            FindIterable<Document> documents = c.find(Filters.eq("_id", uuid.toString()));
            atomicBoolean.set(documents.iterator().hasNext());
        });
        return atomicBoolean.get();
    }

    @Override
    public void updatePlayer(UUID uuid, double balance) throws Exception {
        this.getToCollection(c -> {
            c.findOneAndUpdate(Filters.eq("_id", uuid.toString()),
                    Updates.set("balance", balance)
            );
        });
    }

    @Override
    public void updatePlayerIgnoreNull(UUID uuid, double balance) throws Exception {
        this.getToCollection(c -> {
            Document document = new Document();
            document.put("balance", balance);
            c.findOneAndUpdate(Filters.eq("_id", uuid.toString()), document);
        });
    }
}