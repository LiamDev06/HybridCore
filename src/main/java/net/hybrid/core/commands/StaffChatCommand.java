package net.hybrid.core.commands;

import net.hybrid.core.managers.ChatManager;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class StaffChatCommand extends PlayerCommand {

    public StaffChatCommand() {
        super("staffchat", "sc", "s", "schat");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.performCommand("/chat staff");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        ChatManager.sendStaffChatMessage(player, message.toString().trim());
    }
}
