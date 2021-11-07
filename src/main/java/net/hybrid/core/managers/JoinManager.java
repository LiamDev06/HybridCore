package net.hybrid.core.managers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.managers.tabmanagers.NetworkTabManager;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

public class JoinManager implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        NetworkTabManager.scoreboards.put(player.getUniqueId(), scoreboard);

        if (CorePlugin.VERSION != ServerVersion.v1_17_R1) {
            CommandSender sender = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " clear");
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " prefix '" + hybridPlayer.getRankManager().getRank().getPrefixSpace() + "'");
            Bukkit.dispatchCommand(sender, "nte player " + player.getName() + " priority " + hybridPlayer.getRankManager().getRank().getNtePriority());
        }
    }
}



