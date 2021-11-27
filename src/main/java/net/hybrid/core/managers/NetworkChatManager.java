package net.hybrid.core.managers;

import net.hybrid.core.utility.BadWordsFilter;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.NickRank;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class NetworkChatManager implements Listener {

    public static final HashMap<UUID, String> lastMessage = new HashMap<>();
    public static final HashMap<UUID, Long> chatCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        String message = event.getMessage();
        String sendMessage;

        if (hybridPlayer.isMuted()) {
            Bukkit.getConsoleSender().sendMessage("[MUTED] (ALL) " + player.getName() + ": " + event.getMessage());
        } else {
            Bukkit.getConsoleSender().sendMessage("(ALL) " + player.getName() + ": " + event.getMessage());
        }

        if (hybridPlayer.isMuted()) {
            event.setCancelled(true);

            hybridPlayer.sendMessage("&7&m-------------------------------------");
            hybridPlayer.sendMessage("&c&lYOU ARE CURRENTLY MUTED!");
            hybridPlayer.sendMessage("&cYour mute expires in &f" + hybridPlayer.getMuteExpiresNormal());
            hybridPlayer.sendMessage("  ");
            hybridPlayer.sendMessage("&7Punished falsely? Create a ticket at &b&nhttps://hybridplays.com/discord&7 and explain the situation.");
            hybridPlayer.sendMessage("&7&m-------------------------------------");
            return;
        }

        String start = hybridPlayer.getRankManager().getRank().getPrefixSpace() + hybridPlayer.getColoredName();
        if (hybridPlayer.getDisguiseManager().isNicked()) {
            start = hybridPlayer.getDisguiseManager().getNick().getNickRank().getPrefixSpace() + hybridPlayer.getDisguiseManager().getNick().getNickname();
        }

        if (hybridPlayer.getMetadataManager().getChatChannel() == ChatChannel.ALL) {
            event.setCancelled(true);

            if (hybridPlayer.getRankManager().getRank() == PlayerRank.MEMBER || (hybridPlayer.getDisguiseManager().getNick().getNickRank() == NickRank.MEMBER && hybridPlayer.getDisguiseManager().isNicked())) {
                if (canSendMessageAllFilters(player, hybridPlayer, message)) {
                    sendMessage = start + "§f: " + message;

                    lastMessage.remove(player.getUniqueId());
                    chatCooldown.remove(player.getUniqueId());
                    lastMessage.put(player.getUniqueId(), event.getMessage().toLowerCase());
                    chatCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 2000);
                } else {
                    return;
                }
            }

            else if (hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN) && !hybridPlayer.getDisguiseManager().isNicked()) {
                sendMessage = start + "§f ➤ " + CC.translate(message);
                sendMessage = replaceWithEmote(sendMessage);
            }

            else {
                if (canSendMessageBlackListOnly(player, hybridPlayer, message)) {
                    sendMessage = start + "§f ➤ " + message;
                    sendMessage = replaceWithEmote(sendMessage);
                } else {
                    return;
                }
            }

            for (Player targetPlayer : player.getWorld().getPlayers()) {
                targetPlayer.sendMessage(sendMessage);
            }
        }
    }

    public static boolean canSendMessageAllFilters(Player player, HybridPlayer hybridPlayer, String message) {
        boolean containsBadWord = false;
        for (String word : BadWordsFilter.getBadWords()) {
            if (message.toLowerCase().equalsIgnoreCase(word)) {
                containsBadWord = true;
                break;
            }
        }

        boolean lastMessageBlock = false;
        if (message.toLowerCase().equalsIgnoreCase(lastMessage.get(player.getUniqueId()))) {
            lastMessageBlock = true;
        }

        boolean chatCooldownBlock = false;
        if (chatCooldown.containsKey(player.getUniqueId()) && chatCooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            chatCooldownBlock = true;
        }

        if (containsBadWord && !hybridPlayer.getRankManager().isAdmin()) {
            player.sendMessage("§7§m-------------------------------------------");
            player.sendMessage("§c§lMESSAGE BLOCKED! §cYour message contains blacklisted words!");
            player.sendMessage("§7§m-------------------------------------------");
            return false;
        }

        if (lastMessageBlock && !hybridPlayer.getDisguiseManager().isNicked()) {
            player.sendMessage("§7§m-------------------------------------------");
            player.sendMessage("§c§lSPAM! §cPlease do not say the same thing twice!");
            player.sendMessage("§7§m-------------------------------------------");
            return false;
        }

        if (chatCooldownBlock && !hybridPlayer.getDisguiseManager().isNicked()) {
            player.sendMessage("§7§m-------------------------------------------");
            player.sendMessage("§c§lCOOLDOWN! §cYou can only chat once every 2 seconds!");
            player.sendMessage("§7§m-------------------------------------------");
            return false;
        }

        return true;
    }

    public static boolean canSendMessageBlackListOnly(Player player, HybridPlayer hybridPlayer, String message) {
        boolean containsBadWord = false;
        for (String word : BadWordsFilter.getBadWords()) {
            if (message.toLowerCase().contains(word) && !word.toLowerCase().equalsIgnoreCase("bypass")) {
                containsBadWord = true;
                break;
            }
        }

        if (containsBadWord && !hybridPlayer.getRankManager().isAdmin()) {
            player.sendMessage("§7§m-------------------------------------------");
            player.sendMessage("§c§lMESSAGE BLOCKED! §cYour message contains blacklisted words!");
            player.sendMessage("§7§m-------------------------------------------");
            return false;
        }

        return true;
    }

    public static String replaceWithEmote(String input) {

        input = input.replace(":cool:", CC.translate("&a&lCool&r"));
        input = input.replace(":shrug:", CC.translate("&d¯\\_(ツ)_/¯&r"));
        input = input.replace(":wow:", CC.translate("&b&lW&4&lO&b&lW&r"));
        input = input.replace("o/", CC.translate("&5(o_o)/&r"));
        input = input.replace(":hybrid:", CC.translate("&2&lHYBRID&r"));
        input = input.replace(":L:", CC.translate("&c&lL&r"));
        input = input.replace(":sad:", CC.translate("&e◕︵◕&r"));
        input = input.replace(":happy:", CC.translate("&6&l◕◡◕&r"));
        input = input.replace(":embarrassed:", CC.translate("&b⊙﹏⊙&r"));
        input = input.replace(":eyes:", CC.translate("&aʘ.ʘ&r"));
        input = input.replace(":hehe:", CC.translate("&0hehe&r"));

        return input;
    }

}










