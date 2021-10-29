package net.hybrid.core.commands.staff;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class StaffNotifyCommand extends PlayerCommand {

    public StaffNotifyCommand() {
        super("staffnotify", "staffnotifications", "staffnotification");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (hybridPlayer.getMetadataManager().hasStaffNotify()) {
            hybridPlayer.sendMessage("&eYou turned &c&lOFF &estaff notification mode.");
            hybridPlayer.getMetadataManager().setStaffNotify(false);
        } else {
            hybridPlayer.sendMessage("&eYou turned &a&lON &estaff notification mode.");
            hybridPlayer.getMetadataManager().setStaffNotify(true);
        }

    }
}










