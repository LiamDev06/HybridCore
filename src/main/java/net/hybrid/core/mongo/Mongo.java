package net.hybrid.core.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import net.hybrid.core.CorePlugin;
import org.bson.Document;

import java.util.UUID;

public class Mongo {

    private final CorePlugin plugin;
    private final MongoClient mongoClient;

    private final MongoDatabase coreDatabase;

    public Mongo(CorePlugin plugin) {
        this.plugin = plugin;

        String connectionString = "mongodb://admin:admin@localhost:27017/?authSource=test&connectTimeoutMS=30000";
        mongoClient = new MongoClient(new MongoClientURI(connectionString));

        this.coreDatabase = mongoClient.getDatabase("coredata");

        this.plugin.getLogger().info("The core database has been CONNECTED.");
    }

    public Document loadDocument(String collectionName, UUID uuid) {
        Document document = coreDatabase.getCollection(collectionName)
                .find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            document = new Document().append("uuid", uuid.toString());
            coreDatabase.getCollection(collectionName)
                    .replaceOne(Filters.eq("uuid", uuid.toString()), document,
                            new UpdateOptions().upsert(true));
        }

        return document;
    }

    public void saveDocument(String collectionName, Document document, UUID uuid) {
        coreDatabase.getCollection(collectionName)
                .replaceOne(Filters.eq("uuid", uuid.toString()), document
                , new UpdateOptions().upsert(true));
    }

}











