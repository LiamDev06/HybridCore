package net.hybrid.core.commands;

import net.hybrid.core.managers.ChatManager;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class AdminChatCommand extends PlayerCommand {

    public AdminChatCommand() {
        super("adminchat", "adchat", "ad", "adc");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.performCommand("/chat admin");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        ChatManager.sendAdminChatMessage(player, message.toString().trim());
    }
}
