package net.hybrid.core.managers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

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
     * - Make sure to all of the things above to the message commands
     * - Use the methods used in this class, scroll down
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
            return;
        }

        if (hybridPlayer.getMetadataManager().getChatChannel() == ChatChannel.OWNER) {
            event.setCancelled(true);

            for (UUID target : CorePlugin.getInstance().getMongo().getOwners()) {
                HybridPlayer hybridTarget = new HybridPlayer(target);
                hybridTarget.sendBungeeMessage(ChatChannel.OWNER.getPrefix() + " " + hybridPlayer.getColoredName() + player.getName() + "&f: " + CC.translate(event.getMessage()));
            }
            return;
        }

        if (hybridPlayer.getMetadataManager().getChatChannel() == ChatChannel.ADMIN) {
            event.setCancelled(true);

            for (UUID target : CorePlugin.getInstance().getMongo().getAdmins()) {
                HybridPlayer hybridTarget = new HybridPlayer(target);
                hybridTarget.sendBungeeMessage(ChatChannel.ADMIN.getPrefix() + " " + hybridPlayer.getColoredName() + "&f: " + CC.translate(event.getMessage()));
            }
            return;
        }

        if (hybridPlayer.getMetadataManager().getChatChannel() == ChatChannel.STAFF) {
            event.setCancelled(true);

            for (UUID target : CorePlugin.getInstance().getMongo().getStaff()) {
                HybridPlayer hybridTarget = new HybridPlayer(target);
                hybridTarget.sendBungeeMessage(ChatChannel.STAFF.getPrefix() + " " + hybridPlayer.getColoredName() + "&f: " + CC.translate(event.getMessage()));
            }
        }
    }

    public static void sendAllChatMessage(Player player, String message) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        String startFormatAll = hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName();

        for (Player targetPlayer : Bukkit.getWorld(player.getWorld().getName()).getPlayers()) {
            if (hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
                targetPlayer.sendMessage(startFormatAll + "§f ➤ " + hybridPlayer.getMetadataManager().getChatColor() +
                        CC.translate(message));

            } else if (hybridPlayer.getRankManager().hasRank(PlayerRank.IRON) && !hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
                targetPlayer.sendMessage(startFormatAll + "§f ➤ " + message);

            } else {
                targetPlayer.sendMessage(startFormatAll + ": §f" + message);
            }
        }
    }

    public static void sendStaffChatMessage(Player player, String message) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        for (UUID target : CorePlugin.getInstance().getMongo().getStaff()) {
            HybridPlayer hybridTarget = new HybridPlayer(target);
            hybridTarget.sendBungeeMessage(ChatChannel.STAFF.getPrefix() + " " + hybridPlayer.getColoredName() + "&f: " + CC.translate(message));
        }
    }

    public static void sendAdminChatMessage(Player player, String message) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        for (UUID target : CorePlugin.getInstance().getMongo().getAdmins()) {
            HybridPlayer hybridTarget = new HybridPlayer(target);
            hybridTarget.sendBungeeMessage(ChatChannel.ADMIN.getPrefix() + " " + hybridPlayer.getColoredName() + "&f: " + CC.translate(message));
        }
    }

    public static void sendOwnerChatMessage(Player player, String message) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        for (UUID target : CorePlugin.getInstance().getMongo().getOwners()) {
            HybridPlayer hybridTarget = new HybridPlayer(target);
            hybridTarget.sendBungeeMessage(ChatChannel.OWNER.getPrefix() + " " + hybridPlayer.getRankManager().getRank().getColor() + player.getName() + "&f: " + CC.translate(message));
        }
    }



}











