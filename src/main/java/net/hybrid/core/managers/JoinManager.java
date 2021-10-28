package net.hybrid.core.managers;

import net.hybrid.core.managers.tabmanagers.NetworkTabManager;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.Bukkit;
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
        NetworkTabManager.setTabRank(player, hybridPlayer.getRankManager().getRank(), scoreboard);
        NetworkTabManager.scoreboards.put(player.getUniqueId(), scoreboard);

        for (Player target : Bukkit.getOnlinePlayers()) {
            HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());

            if (target.getUniqueId() != player.getUniqueId()) {
                NetworkTabManager.setTabRank(target, hybridTarget.getRankManager().getRank(), NetworkTabManager.scoreboards.get(target.getUniqueId()));
            }
        }
    }
}
