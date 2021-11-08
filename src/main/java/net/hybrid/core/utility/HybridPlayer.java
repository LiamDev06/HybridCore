package net.hybrid.core.utility;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.managers.NetworkLevelingManager;
import net.hybrid.core.rank.RankManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HybridPlayer {

    private final UUID uuid;
    private final Mongo mongo = CorePlugin.getInstance().getMongo();

    private final RankManager rankManager;
    private final MetadataManager metadataManager;
    private final NetworkLevelingManager networkLevelingManager;
    private final DisguiseManager disguiseManager;
    private final ServerManager serverManager;

    public HybridPlayer(UUID uuid) {
        this.uuid = uuid;
        this.rankManager = new RankManager(uuid);
        this.metadataManager = new MetadataManager(uuid);
        this.networkLevelingManager = new NetworkLevelingManager(uuid);
        this.disguiseManager = new DisguiseManager(uuid);
        this.serverManager = new ServerManager(uuid);
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public MetadataManager getMetadataManager() {
        return metadataManager;
    }

    public NetworkLevelingManager getNetworkLevelingManager() {
        return networkLevelingManager;
    }

    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        String[] args = message.split(" ");
        StringBuilder newString = new StringBuilder();
        for (String s : args) {
            newString.append(replaceColor(s)).append(" ");
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', newString.toString().trim()));
    }

    private String replaceColor(String input) {
        for (ChatColor cc : ChatColor.values()) {
             input = input.replace("{" + cc.name().toLowerCase() + "}", cc.toString());
        }

        return input;
    }

    public Player getPlayer() {
        Player player = Bukkit.getPlayer(uuid);

        if (player.isOnline()) {
            return player;
        }

        return null;
    }

    public void sendToServer(String serverName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", outputStream.toByteArray());
    }

    public void sendBungeeMessage(String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF(getName());
        out.writeUTF(CC.translate(message));

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void sendBungeeMessage(String name, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF(name);
        out.writeUTF(CC.translate(message));

        getPlayer().sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void sendBungeeMessageRaw(String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("MessageRaw");
        out.writeUTF(getName());
        out.writeUTF(CC.translate(message));

        getPlayer().sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public String getColoredName() {
        return rankManager.getRank().getColor() + getName();
    }

    public boolean isBanned() {
        Document document = mongo.loadDocument("playerData", uuid);
        return document.getBoolean("banned");
    }

    public boolean isMuted() {
        Document document = mongo.loadDocument("playerData", uuid);
        if (document.getString("muteId").equalsIgnoreCase("")) {
            return false;
        }

        Document muteDoc = mongo.loadDocument("punishments", "punishmentId",
                document.getString("muteId"));

        if (System.currentTimeMillis() > muteDoc.getLong("expires")) {
            document.replace("muted", false);
            document.replace("muteId", "");
            mongo.saveDocument("playerData", document, uuid);

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(CC.translate("&7&m---------------------------"));
                player.sendMessage(CC.translate("&a&lMUTE EXPIRED!"));
                player.sendMessage(CC.translate("&aYour mute has now expired meaning you can send messages again."));
                player.sendMessage(CC.translate(""));
                player.sendMessage(CC.translate("&7Please get familiar with our rules to avoid more punishments in the future."));
                player.sendMessage(CC.translate("&7&m---------------------------"));
            }

            return false;
        } else {
            return true;
        }
    }

    public String getMuteExpiresNormal() {
        Document document = mongo.loadDocument("playerData", uuid);
        if (document.getString("muteId").equalsIgnoreCase("")) {
            return "NO ACTIVE MUTE [ERROR-4CG6Z]";
        }

        Document muteDoc = mongo.loadDocument("punishments", "punishmentId",
                document.getString("muteId"));
        long millisLeft = muteDoc.getLong("expires") - System.currentTimeMillis();

        return timeAsString(millisLeft);
    }

    public String getBanId() {
        Document document = mongo.loadDocument("playerData", uuid);
        return document.getString("banId");
    }

    public String getMuteId() {
        Document document = mongo.loadDocument("playerData", uuid);
        return document.getString("muteId");
    }

    public boolean hasJoinedServerBefore() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument(
                "serverData", "serverDataType", "playerDataList"
        );

        try {
            if (!document.getString(uuid.toString()).equalsIgnoreCase("")) {
                return true;
            }

            if (document.getString(uuid.toString()).equalsIgnoreCase("")) {
                return false;
            }
        } catch (NullPointerException ignored) {}

        return false;
    }

    public boolean isOnline() {
        Document document = CorePlugin.getInstance().getMongo().loadDocument("playerData", uuid);

        long login = document.getLong("lastLogin");
        long logout = document.getLong("lastLogout");

        return (login > logout);
    }

    private static String timeAsString(long timePeriod){

        long millis = timePeriod;

        String output = "";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 1) output += days + " days ";
        else if (days == 1) output += days + " day ";

        if (hours > 1) output += hours + " hours ";
        else if (hours == 1) output += hours + " hour ";

        if (minutes > 1) output += minutes + " minutes ";
        else if (minutes == 1) output += minutes + " minute ";

        if (seconds > 1) output += seconds + " seconds ";
        else if (seconds == 1) output += seconds + " second ";

        if (seconds < 1) output += " now ";
        return output.trim();
    }

}




