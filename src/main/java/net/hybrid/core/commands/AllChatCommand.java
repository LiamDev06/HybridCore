package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.managers.NetworkChatManager;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.NickRank;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.entity.Player;

import static net.hybrid.core.managers.NetworkChatManager.replaceWithEmote;

public class AllChatCommand extends PlayerCommand {

    public AllChatCommand() {
        super("allchat", "a", "all", "main", "mainchat", "ac");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (hybridPlayer.isMuted()) {
            hybridPlayer.sendMessage("&7&m-------------------------------------");
            hybridPlayer.sendMessage("&c&lYOU ARE CURRENTLY MUTED!");
            hybridPlayer.sendMessage("&cYour mute expires in &f" + hybridPlayer.getMuteExpiresNormal());
            hybridPlayer.sendMessage("  ");
            hybridPlayer.sendMessage("&7Punished falsely? Create a ticket at &b&nhttps://hybridplays.com/discord&7 and explain the situation.");
            hybridPlayer.sendMessage("&7&m-------------------------------------");
            return;
        }

        if (args.length == 0) {
            player.chat("/chat all");
            return;
        }

        if (!hybridPlayer.getRankManager().hasRank(ChatChannel.ALL.getRequiredRank())) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        String sendMessage;

        String start = hybridPlayer.getRankManager().getRank().getPrefixSpace() + hybridPlayer.getColoredName();
        if (hybridPlayer.getDisguiseManager().isNicked()) {
            start = hybridPlayer.getDisguiseManager().getNick().getNickRank().getPrefixSpace() + hybridPlayer.getDisguiseManager().getNick().getNickname();
        }

        if (hybridPlayer.getRankManager().getRank() == PlayerRank.MEMBER || (hybridPlayer.getDisguiseManager().getNick().getNickRank() == NickRank.MEMBER && hybridPlayer.getDisguiseManager().isNicked())) {
            if (NetworkChatManager.canSendMessageAllFilters(player, hybridPlayer, message.toString().trim())) {
                sendMessage = start + "§f: " + message;

                NetworkChatManager.lastMessage.remove(player.getUniqueId());
                NetworkChatManager.chatCooldown.remove(player.getUniqueId());
                NetworkChatManager.lastMessage.put(player.getUniqueId(), message.toString().toLowerCase().trim());
                NetworkChatManager.chatCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 2000);
            } else {
                return;
            }
        }

        else if (hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN) && !hybridPlayer.getDisguiseManager().isNicked()) {
            sendMessage = start + "§f ➤ " + CC.translate(message.toString().trim());
            sendMessage = replaceWithEmote(sendMessage);
        }

        else {
            if (NetworkChatManager.canSendMessageBlackListOnly(player, hybridPlayer, message.toString().trim())) {
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













