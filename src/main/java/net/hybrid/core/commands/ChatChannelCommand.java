package net.hybrid.core.commands;

import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import net.hybrid.core.utility.enums.ChatChannel;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatChannelCommand extends PlayerCommand {

    public ChatChannelCommand() {
        super("chat", "chatchannel");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (hybridPlayer.isMuted()) {
            hybridPlayer.sendMessage("&c&lCURRENTLY MUTED! &cYou are currently muted meaning you cannot modify your chat channel!");
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /chat <chatchannel>");
            hybridPlayer.sendMessage("&cAvailable channels: " + getChannels(hybridPlayer));
            return;
        }

        ChatChannel channel = ChatChannel.ALL;
        boolean shouldContinue = true;

        try {
            channel = ChatChannel.valueOf(args[0].toUpperCase());

        } catch (Exception ignored) {

            for (ChatChannel target : ChatChannel.values()) {
                if (!shouldContinue) break;

                for (String s : target.getAliases()) {
                    if (args[0].equalsIgnoreCase(s)) {
                        shouldContinue = false;
                        channel = target;
                        break;
                    }
                }
            }

            if (shouldContinue) {
                hybridPlayer.sendMessage("&cInvalid chat channel!");
                hybridPlayer.sendMessage("&cAvailable channels: " + getChannels(hybridPlayer));
                return;
            }
        }


        try {
            if (!hybridPlayer.getRankManager().hasRank(channel.getRequiredRank())) {
                hybridPlayer.sendMessage("&cInvalid chat channel!");
                hybridPlayer.sendMessage("&cAvailable channels: " + getChannels(hybridPlayer));
                return;
            }

            if (hybridPlayer.getMetadataManager().getChatChannel() == channel) {
                hybridPlayer.sendMessage("&cYou are already in this chat channel!");
                return;
            }

            hybridPlayer.getMetadataManager().setChatChannel(channel);
            hybridPlayer.sendMessage("&bYou toggled on the &e" + channel.name() + " &bchat channel.");
            hybridPlayer.sendMessage("&bUse &c/chat all &bto return to all chat again.");
            SoundManager.playSound(player, "NOTE_BASS", 10, 3);

        } catch (Exception exception) {
            hybridPlayer.sendMessage("&cInvalid chat channel!");
            hybridPlayer.sendMessage("&cAvailable channels: " + getChannels(hybridPlayer));
        }

    }

    private String getChannels(HybridPlayer hybridPlayer) {
        StringBuilder builder = new StringBuilder();

        for (ChatChannel channel : ChatChannel.values()) {
            if (hybridPlayer.getRankManager().hasRank(channel.getRequiredRank())) {
                builder.append(channel.name().toLowerCase()).append(", ");
            }
        }

        return builder.substring(0, builder.length() - 2);
    }

}













