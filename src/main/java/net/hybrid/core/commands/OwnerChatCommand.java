package net.hybrid.core.commands;

import net.hybrid.core.managers.ChatManager;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class OwnerChatCommand extends PlayerCommand {

    public OwnerChatCommand() {
        super("ownerchat", "ochat", "oc");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.performCommand("/chat owner");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        ChatManager.sendOwnerChatMessage(player, message.toString().trim());
    }
}
