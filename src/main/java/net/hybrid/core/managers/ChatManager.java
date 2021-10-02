package net.hybrid.core.managers;

import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.PlayerRank;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

    /**
     * TODO
     *
     * - Check if player is muted; disable all chatting
     * - Check for all chat channels
     * - Add profanity filter, check for blacklisted words
     * - Check for links, spamming, same word, etc
     * - Add ping system for pinging users in chat
     * - Add ping alert sound for pinging users in chat
     *
     */

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        final String startFormatAll = hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName();

        if (hybridPlayer.getMetadataManager().getChatChannel() == ChatChannel.ALL) {
            event.setCancelled(true);

            for (Player targetPlayer : Bukkit.getWorld(player.getWorld().getName()).getPlayers()) {
                if (hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
                    targetPlayer.sendMessage(startFormatAll + "§f ➤ " + hybridPlayer.getMetadataManager().getChatColor() +
                            CC.translate(event.getMessage()));

                } else if (hybridPlayer.getRankManager().hasRank(PlayerRank.IRON) && !hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
                    targetPlayer.sendMessage(startFormatAll + "§f ➤ " + event.getMessage());

                } else {
                    targetPlayer.sendMessage(startFormatAll + ": §f" + event.getMessage());
                }
            }
        }
    }
}











