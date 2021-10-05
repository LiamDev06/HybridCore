package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.managers.NetworkChatManager;
import net.hybrid.core.utility.BadWordsFilter;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.enums.ChatChannel;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.entity.Player;

public class AllChatCommand extends PlayerCommand {

    public AllChatCommand() {
        super("allchat", "a", "all", "main", "mainchat", "ac");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.chat("/chat all");
            return;
        }

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(ChatChannel.ALL.getRequiredRank())) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        String sendMessage;

        final String start = hybridPlayer.getRankManager().getRank().getPrefixSpace() + hybridPlayer.getColoredName();

        if (hybridPlayer.getRankManager().getRank() == PlayerRank.MEMBER) {
            if (NetworkChatManager.canSendMessageAllFilters(player, message.toString().trim())) {
                sendMessage = start + "§f: " + message;

                NetworkChatManager.lastMessage.remove(player.getUniqueId());
                NetworkChatManager.chatCooldown.remove(player.getUniqueId());
                NetworkChatManager.lastMessage.put(player.getUniqueId(), message.toString().toLowerCase().trim());
                NetworkChatManager.chatCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 2000);
            } else {
                return;
            }
        }

        else if (hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
            sendMessage = start + "§f ➤ " + CC.translate(message.toString().trim());
        }

        else {
            if (NetworkChatManager.canSendMessageBlackListOnly(player, message.toString().trim())) {
                sendMessage = start + "§f ➤ " + message;
            } else {
                return;
            }
        }

        for (Player targetPlayer : player.getWorld().getPlayers()) {
            targetPlayer.sendMessage(sendMessage);
        }
    }
}













