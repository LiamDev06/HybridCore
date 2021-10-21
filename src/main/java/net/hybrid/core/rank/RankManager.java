package net.hybrid.core.rank;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Mongo;
import net.hybrid.core.managers.tabmanagers.NetworkTabManager;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RankManager {

    private final UUID uuid;
    private final Mongo mongo = CorePlugin.getInstance().getMongo();
    private static final HashMap<UUID, PlayerRank> rankCache = new HashMap<>();

    public RankManager(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(PlayerRank playerRank) {
        Player player1 = Bukkit.getPlayer(uuid);
        if (player1 != null) {
            NetworkTabManager.setTabRank(player1, playerRank);
        }

        if (rankCache.containsKey(uuid)) {
            rankCache.replace(uuid, playerRank);
        } else {
            rankCache.put(uuid, playerRank);
        }

        Document document = mongo.loadDocument("playerData", uuid);

        if (playerRank.isStaffRank()) {
            document.replace("staffRank", playerRank.name());
            document.replace("staffNotifyMode", true);

        } else if (playerRank == PlayerRank.YOUTUBER || playerRank == PlayerRank.TWITCH_STREAMER || playerRank == PlayerRank.PARTNER) {
            document.replace("specialRank", playerRank.name());
            document.replace("staffRank", "");
            document.replace("staffNotifyMode", false);
            document.replace("staffBuildMode", false);

            if (document.getString("chatChannel").equalsIgnoreCase("staff") ||
                    document.getString("chatChannel").equalsIgnoreCase("builder") ||
                    document.getString("chatChannel").equalsIgnoreCase("admin") ||
                    document.getString("chatChannel").equalsIgnoreCase("owner")) {
                document.replace("chatChannel", "ALL");
            }

        } else {
            document.replace("playerRank", playerRank.name());
            document.replace("specialRank", "");
            document.replace("staffRank", "");
            document.replace("staffNotifyMode", false);
            document.replace("staffBuildMode", false);

            if (document.getString("chatChannel").equalsIgnoreCase("staff") ||
                    document.getString("chatChannel").equalsIgnoreCase("builder") ||
                    document.getString("chatChannel").equalsIgnoreCase("admin") ||
                    document.getString("chatChannel").equalsIgnoreCase("owner")) {
                document.replace("chatChannel", "ALL");
            }
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF("RankUpdate");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(playerRank.name());
            msgOut.writeUTF(uuid.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        document.replace("adminDebugMode", false);
        document.replace("chatColor", ChatColor.WHITE.name());

        mongo.saveDocument("playerData", document, uuid);
    }

    public PlayerRank getRank() {
        if (rankCache.containsKey(uuid)) {
            return rankCache.get(uuid);
        }

        Document document = mongo.loadDocument("playerData", uuid);

        if (!document.getString("staffRank").equalsIgnoreCase("")) {
            return PlayerRank.valueOf(document.getString("staffRank").toUpperCase());

        } else if (!document.getString("specialRank").equalsIgnoreCase("")) {
            return PlayerRank.valueOf(document.getString("specialRank").toUpperCase());

        } else {
            return PlayerRank.valueOf(document.getString("playerRank").toUpperCase());
        }
    }

    public boolean hasRank(PlayerRank playerRank) {
        return (getRank().getOrdering() >= playerRank.getOrdering());
    }

    public boolean hasRankOnly(PlayerRank playerRank) {
        return getRank() == playerRank;
    }

    public boolean isStaff() {
        return getRank().isStaffRank();
    }

    public boolean isModerator() {
        return hasRank(PlayerRank.MODERATOR);
    }

    public boolean isSeniorModerator() {
        return hasRank(PlayerRank.SENIOR_MODERATOR);
    }

    public boolean isAdmin() {
        return hasRank(PlayerRank.ADMIN);
    }

    public static HashMap<UUID, PlayerRank> getRankCache() {
        return rankCache;
    }

}










