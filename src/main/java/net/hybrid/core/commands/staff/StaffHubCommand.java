package net.hybrid.core.commands.staff;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class StaffHubCommand extends PlayerCommand {

    public StaffHubCommand() {
        super("staffhub", "build", "buildserver", "staffserver");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        hybridPlayer.sendMessage("&7Sending you to the staff server...");
        hybridPlayer.sendToServer("build");

    }
}












