package net.hybrid.core.managers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class NetworkLevelingManager {

    private final Mongo mongo = CorePlugin.getInstance().getMongo();

    public static HashMap<UUID, Integer> levelCache = new HashMap<>();
    public static HashMap<UUID, Double> expCache = new HashMap<>();

    private final UUID uuid;
    public NetworkLevelingManager(UUID uuid) {
        this.uuid = uuid;
    }

    public int getLevel() {
        if (levelCache.containsKey(uuid)) {
            return levelCache.get(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        return document.getInteger("networkLevel");
    }

    public void setLevel(int level) {
        if (levelCache.containsKey(uuid)) {
            levelCache.replace(uuid, level);
        } else {
            levelCache.put(uuid, level);
            return;
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("networkLevel", level);
        mongo.saveDocument("playerData", document, uuid);
    }

    public double getExp() {
        if (expCache.containsKey(uuid)) {
            return expCache.get(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        return document.getDouble("networkLevelExp");
    }

    public void setExp(double exp) {
        if (expCache.containsKey(uuid)) {
            expCache.replace(uuid, exp);
        } else {
            expCache.put(uuid, exp);
            return;
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("networkLevelExp", exp);
        mongo.saveDocument("playerData", document, uuid);
    }

    public double getExpRequiredForNextLevel(int level) {
        double required = 0;

        for (int i = 0; i == level; i++) {
            required = required + 1500;
        }

        return required;
    }

    public double getExpRequiredForNextLevel() {
        double required = 0;

        for (int i = 0; i == getLevel(); i++) {
            required = required + 1500;
        }

        return required;
    }

}











