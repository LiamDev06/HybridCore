package net.hybrid.core.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import net.hybrid.core.CorePlugin;
import org.bson.Document;

import java.util.UUID;

public class Mongo {

    private final MongoDatabase coreDatabase;
    private final MongoClient mongoClient;

    public Mongo(CorePlugin plugin) {
        String connectionString = "mongodb+srv://HybridNetwork:jdfsdsf879hjgfdg5@cluster0.0bfk6.mongodb.net/test?retryWrites=true&w=majority";

        this.mongoClient = new MongoClient(new MongoClientURI(connectionString));
        this.coreDatabase = mongoClient.getDatabase("coredata");

        plugin.getLogger().info("The core database has been CONNECTED.");
    }

    public Document loadDocument(String collectionName, UUID uuid) {
        Document document = coreDatabase.getCollection(collectionName)
                .find(Filters.eq("playerUuid", uuid.toString())).first();

        if (document == null) {
            document = new Document().append("playerUuid", uuid.toString());
            coreDatabase.getCollection(collectionName)
                    .replaceOne(Filters.eq("playerUuid", uuid.toString()), document,
                            new UpdateOptions().upsert(true));
        }

        return document;
    }

    public Document loadDocument(String collectionName, String find, Object value) {
        return coreDatabase.getCollection(collectionName)
                .find(Filters.eq(find, value)).first();
    }

    public void saveDocument(String collectionName, Document document, UUID uuid) {
        coreDatabase.getCollection(collectionName)
                .replaceOne(Filters.eq("playerUuid", uuid.toString()), document
                , new UpdateOptions().upsert(true));
    }

    public void saveDocument(String collectionName, Document document) {
        coreDatabase.getCollection(collectionName).insertOne(document);
    }

    public void saveDocument(String collectionName, Document document, String find, Object value) {
        coreDatabase.getCollection(collectionName)
                .replaceOne(Filters.eq(find, value), document
                        , new UpdateOptions().upsert(true));
    }

    public void deleteDocument(String collectionName, Document document) {
        coreDatabase.getCollection(collectionName).deleteOne(document);
    }

    public MongoDatabase getCoreDatabase() {
        return coreDatabase;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}











