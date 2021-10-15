package net.hybrid.core.managers;

import net.hybrid.core.managers.tabmanagers.NetworkTabManager;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinManager implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        NetworkTabManager.setTabRank(player, hybridPlayer.getRankManager().getRank());
    }

}
