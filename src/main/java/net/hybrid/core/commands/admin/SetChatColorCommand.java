package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetChatColorCommand extends PlayerCommand {

    public SetChatColorCommand() {
        super("setchatcolor");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /setchatcolor <color>");
            return;
        }

        if (args[0].equalsIgnoreCase("pink")) {
            hybridPlayer.getMetadataManager().setChatColor(ChatColor.LIGHT_PURPLE);
            hybridPlayer.sendMessage("&aYou have updated your chat color to " + ChatColor.LIGHT_PURPLE + ChatColor.LIGHT_PURPLE.name());
            return;
        }

        if (args[0].equalsIgnoreCase("purple")) {
            hybridPlayer.getMetadataManager().setChatColor(ChatColor.DARK_PURPLE);
            hybridPlayer.sendMessage("&aYou have updated your chat color to " + ChatColor.DARK_PURPLE + ChatColor.DARK_PURPLE.name());
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            StringBuilder message = new StringBuilder();
            message.append(CC.translate("&aAvailable Colors: "));

            for (ChatColor chatColor : ChatColor.values()) {
                message.append(chatColor).append(chatColor.name()).append(ChatColor.GREEN).append(", ");
            }

            hybridPlayer.sendMessage(message.toString());
            return;
        }

        try {
            ChatColor chatColor = ChatColor.valueOf(args[0].toUpperCase());
            hybridPlayer.getMetadataManager().setChatColor(chatColor);
            hybridPlayer.sendMessage("&aYou have updated your chat color to " + chatColor + chatColor.name());

        } catch (Exception exception) {
            hybridPlayer.sendMessage("&cInvalid chat color! Please insert a valid one!");
        }
    }
}
