package net.hybrid.core.data;

import com.mongodb.client.model.Filters;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.enums.*;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        } else {
            if (!playerListDocument.getString(player.getUniqueId().toString()).equalsIgnoreCase(player.getName())) {
                playerListDocument.replace(player.getUniqueId().toString(), player.getName());
            }
        }

        if (!new HybridPlayer(player.getUniqueId()).hasJoinedServerBefore()) {
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
            document.append("totalNetworkExp", 0);
            document.append("chatChannel", ChatChannel.ALL.name());
            document.append("chatColor", ChatColor.WHITE.name());
            document.append("userLanguage", LanguageType.ENGLISH.name());
            document.append("messageLastReplyUuid", "");
            document.append("banned", false);
            document.append("banExpires", "");
            document.append("muted", false);
            document.append("muteExpires", "");

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

        mongo.saveDocument("serverData", playerListDocument, "serverDataType", "playerDataList");
        mongo.saveDocument("playerData", document, player.getUniqueId());
    }

    @EventHandler
    public void onLogQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document document = mongo.loadDocument("playerData", player.getUniqueId());

        document.append("lastLogout", System.currentTimeMillis());

        mongo.saveDocument("playerData", document, player.getUniqueId());
    }

}










