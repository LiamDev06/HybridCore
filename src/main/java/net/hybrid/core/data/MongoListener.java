package net.hybrid.core.data;

import com.mongodb.client.model.Filters;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.managers.NetworkLevelingManager;
import net.hybrid.core.managers.tabmanagers.NetworkTabManager;
import net.hybrid.core.rank.RankManager;
import net.hybrid.core.utility.DisguiseManager;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.enums.*;
import net.hybrid.core.utility.nick.Nick;
import net.hybrid.core.utility.nick.NickUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class MongoListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document document;

        Document playerListDocument = mongo.getCoreDatabase()
                .getCollection("serverData").find(
                        Filters.eq("serverDataType", "playerDataList")).first();

        if (playerListDocument == null) {
            playerListDocument = new Document().append("serverDataType", "playerDataList");
        }

        if (!playerListDocument.containsKey(player.getUniqueId().toString())) {
            playerListDocument.append(player.getUniqueId().toString(), player.getName());
            playerListDocument.replace("highestUniqueReached", playerListDocument.getInteger("highestUniqueReached") + 1);
        } else {
            if (!playerListDocument.getString(player.getUniqueId().toString()).equalsIgnoreCase(player.getName())) {
                playerListDocument.replace(player.getUniqueId().toString(), player.getName());
            }
        }

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.hasJoinedServerBefore()) {
            document = new Document();

            document.append("playerName", player.getName());
            document.append("playerUuid", player.getUniqueId().toString());
            document.append("firstLogin", System.currentTimeMillis());
            document.append("lastLogin", System.currentTimeMillis());
            document.append("lastLogout", "");

            document.append("playerRank", PlayerRank.MEMBER.name());
            document.append("specialRank", "");
            document.append("staffRank", "");

            document.append("networkLevel", 1);
            document.append("networkLevelExp", 0);
            document.append("chatChannel", ChatChannel.ALL.name());
            document.append("chatColor", ChatColor.WHITE.name());
            document.append("userLanguage", LanguageType.ENGLISH.name());
            document.append("messageLastReplyUuid", "");
            document.append("banned", false);
            document.append("banId", "");
            document.append("muted", false);
            document.append("muteId", "");
            document.append("waitingOnSeeWarning", false);
            document.append("warningWaitingId", "");
            document.append("vanished", false);
            document.append("nicked", false);
            document.append("nickNickname", "");
            document.append("nickRank", "");
            document.append("nickSkinFileName", "");

            document.append("friendRequestPrivacy", FriendsPrivacy.ALL.name());
            document.append("gangInvitePrivacy", GangPrivacy.ALL.name());
            document.append("partyInvitePrivacy", PartyPrivacy.ALL.name());
            document.append("directMessagesPrivacy", DirectMessagePrivacy.ALL.name());

            document.append("joinNotification", true);
            document.append("playerVisibility", true);
            document.append("allChat", true);
            document.append("pingAlert", true);

            document.append("staffBuildMode", false);
            document.append("staffNotifyMode", false);
            document.append("staffAdminDebug", false);

        } else {
            document = mongo.getCoreDatabase().getCollection("playerData")
                    .find(Filters.eq("playerUuid",
                            player.getUniqueId().toString())).first();

            document.replace("playerName", player.getName());
        }

        if (document.containsKey("nickSkinTexture") && !document.containsKey("nickSkinFileName")) {
            document.append("nickSkinFileName", "");
        }

        mongo.saveDocument("serverData", playerListDocument, "serverDataType", "playerDataList");
        mongo.saveDocument("playerData", document, player.getUniqueId());

        if (RankManager.getRankCache().containsKey(player.getUniqueId())) {
            RankManager.getRankCache().replace(player.getUniqueId(), hybridPlayer.getRankManager().getRank());
        } else {
            RankManager.getRankCache().put(player.getUniqueId(), hybridPlayer.getRankManager().getRank());
        }

        if (NetworkLevelingManager.levelCache.containsKey(player.getUniqueId())) {
            NetworkLevelingManager.levelCache.replace(player.getUniqueId(), hybridPlayer.getNetworkLevelingManager().getLevel());
        } else {
            NetworkLevelingManager.levelCache.put(player.getUniqueId(), hybridPlayer.getNetworkLevelingManager().getLevel());
        }

        if (NetworkLevelingManager.expCache.containsKey(player.getUniqueId())) {
            NetworkLevelingManager.expCache.replace(player.getUniqueId(), hybridPlayer.getNetworkLevelingManager().getExp());
        } else {
            NetworkLevelingManager.expCache.put(player.getUniqueId(), hybridPlayer.getNetworkLevelingManager().getExp());
        }

        if (document.getBoolean("vanished")) {
            if (!DisguiseManager.vanishedPlayersCache.contains(player.getUniqueId())) {
                DisguiseManager.vanishedPlayersCache.add(player.getUniqueId());
            }
        }

        if (document.getBoolean("nicked")) {
            Nick nick = new Nick(
                    player.getUniqueId(),
                    document.getString("nickNickname"),
                    NickRank.valueOf(document.getString("nickRank").toUpperCase()),
                    NickSkinType.RANDOM,
                    new File(NickUtils.skinsPath(), document.getString("nickSkinFileName"))
            );

            if (document.getString("nickSkinFileName").equalsIgnoreCase("steve.png")) {
                nick.setNickSkinType(NickSkinType.STEVE);
            }

            if (document.getString("nickSkinFileName").equalsIgnoreCase("self")) {
                nick.setNickSkinType(NickSkinType.OWN);
            }


            if (DisguiseManager.nicksCache.containsKey(player.getUniqueId())) {
                DisguiseManager.nicksCache.replace(player.getUniqueId(), nick);
            } else {
                DisguiseManager.nicksCache.put(player.getUniqueId(), nick);
            }

            if (!DisguiseManager.nickedPlayersCache.contains(player.getUniqueId())) {
                DisguiseManager.nickedPlayersCache.add(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onLogQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        RankManager.getRankCache().remove(player.getUniqueId());
        NetworkLevelingManager.levelCache.remove(player.getUniqueId());
        NetworkLevelingManager.expCache.remove(player.getUniqueId());
        DisguiseManager.vanishedPlayersCache.remove(player.getUniqueId());
        DisguiseManager.nicksCache.remove(player.getUniqueId());
        DisguiseManager.nickedPlayersCache.remove(player.getUniqueId());

        if (NetworkTabManager.scoreboards.get(player.getUniqueId()).getObjective("sidebar") != null) {
            NetworkTabManager.scoreboards.get(player.getUniqueId()).getObjective("sidebar").unregister();
        }

        NetworkTabManager.scoreboards.remove(player.getUniqueId());
    }

}











