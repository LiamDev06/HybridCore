package net.hybrid.core.commands;

import net.hybrid.core.managers.ChatManager;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllChatCommand extends PlayerCommand {

    public AllChatCommand() {
        super("allchat", "a", "all", "main", "mainchat", "ac");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.performCommand("/chat all");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        ChatManager.sendAllChatMessage(player, message.toString().trim());
    }
}













