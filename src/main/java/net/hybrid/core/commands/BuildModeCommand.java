package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class BuildModeCommand extends PlayerCommand {

    public BuildModeCommand() {
        super("buildmode");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (hybridPlayer.getMetadataManager().isInBuildMode()) {
            hybridPlayer.sendMessage("&bYou turned &c&lOFF &bbuild mode.");
            hybridPlayer.getMetadataManager().setBuildMode(false);
        } else {
            hybridPlayer.sendMessage("&bYou turned &a&lON &bbuild mode.");
            hybridPlayer.getMetadataManager().setBuildMode(true);
        }

    }
}












