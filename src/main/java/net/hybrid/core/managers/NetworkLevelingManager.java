package net.hybrid.core.managers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.events.PlayerNetworkExpChangeEvent;
import net.hybrid.core.events.PlayerNetworkLevelUpEvent;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class NetworkLevelingManager {

    private final Mongo mongo = CorePlugin.getInstance().getMongo();

    public static HashMap<UUID, Integer> levelCache = new HashMap<>();
    public static HashMap<UUID, Integer> expCache = new HashMap<>();

    private final UUID uuid;
    public NetworkLevelingManager(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getLevel() {
        if (levelCache.containsKey(uuid)) {
            return levelCache.get(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        return document.getInteger("networkLevel");
    }

    public void setLevel(int level) {
        PlayerNetworkLevelUpEvent playerNetworkLevelUpEvent =
                new PlayerNetworkLevelUpEvent(uuid, level, getLevel());
        Bukkit.getPluginManager().callEvent(playerNetworkLevelUpEvent);

        if (levelCache.containsKey(uuid)) {
            levelCache.replace(uuid, level);
        } else {
            levelCache.put(uuid, level);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("networkLevel", level);
        mongo.saveDocument("playerData", document, uuid);
    }

    public Integer getExp() {
        if (expCache.containsKey(uuid)) {
            return expCache.get(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        return document.getInteger("networkLevelExp");
    }

    public void setExp(int exp) {
        boolean causedLevelUp = false;
        int expSave = exp;

        if (exp >= getExpRequiredForNextLevel()) {
            exp = (exp + getExp()) - getExpRequiredForNextLevel();
            setLevel(getLevel() + 1);
            causedLevelUp = true;
        }

        PlayerNetworkExpChangeEvent expChangeEvent =
                new PlayerNetworkExpChangeEvent(uuid, expSave, getExp(), causedLevelUp);
        Bukkit.getPluginManager().callEvent(expChangeEvent);

        if (expCache.containsKey(uuid)) {
            expCache.replace(uuid, exp);
        } else {
            expCache.put(uuid, exp);
        }

        Document document = mongo.loadDocument("playerData", uuid);
        document.replace("networkLevelExp", exp);
        mongo.saveDocument("playerData", document, uuid);
    }

    public Integer getExpRequiredForNextLevel(int level) {
        return 1500 * level;
    }

    public Integer getExpRequiredForNextLevel() {
        return 1500 * getLevel();
    }

}












