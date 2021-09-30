package net.hybrid.core.utility;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.rank.RankManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class HybridPlayer {

    private final UUID uuid;
    private final RankManager rankManager;

    public HybridPlayer(UUID uuid) {
        this.uuid = uuid;
        this.rankManager = new RankManager(uuid);
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        try {
            out.writeUTF("Message");
            out.writeUTF(getName());
            out.writeUTF(CC.translate(message));

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", outputStream.toByteArray());
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
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

}




