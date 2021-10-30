package net.hybrid.core.utility;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class DisguiseManager {

    public static final ArrayList<UUID> vanishedPlayersCache = new ArrayList<>();

    private final Mongo mongo = CorePlugin.getInstance().getMongo();
    private final UUID uuid;

    public DisguiseManager(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isVanished() {
        if (vanishedPlayersCache.contains(uuid)) {
            return vanishedPlayersCache.contains(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        return document.getBoolean("vanished");
    }

    public void setVanished(boolean value) {
        if (value) {
            vanishedPlayersCache.remove(uuid);
            vanishedPlayersCache.add(uuid);
        } else {
            vanishedPlayersCache.remove(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("vanished", value);
        mongo.saveDocument("playerData", document, uuid);
    }

}
