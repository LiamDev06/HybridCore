package net.hybrid.core.commands;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSVersionCommand extends PlayerCommand {

    public NMSVersionCommand() {
        super("nmsversion");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        hybridPlayer.sendMessage("&fServer NMS Version: &6" + version);
        hybridPlayer.sendMessage("&fCore Plugin NMS Detected: &6" + CorePlugin.VERSION.name());

    }
}











