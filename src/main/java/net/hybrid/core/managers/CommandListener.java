package net.hybrid.core.managers;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (event.getMessage().toLowerCase().startsWith("/plugins")
                || event.getMessage().toLowerCase().startsWith("/pl")
                || event.getMessage().toLowerCase().startsWith("/bukkit:plugins")
                || event.getMessage().toLowerCase().startsWith("/bukkit:pl")) {

            if (!hybridPlayer.getRankManager().isAdmin() && !event.getMessage().toLowerCase().startsWith("/play")) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                event.setCancelled(true);
            }
        }

        if (event.getMessage().toLowerCase().startsWith("/version")
                || event.getMessage().toLowerCase().startsWith("/ver")) {
            if (!hybridPlayer.getRankManager().isAdmin()) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                event.setCancelled(true);
            }
        }

        if (event.getMessage().toLowerCase().startsWith("/bukkit")) {
            if (!hybridPlayer.getRankManager().isAdmin()) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                event.setCancelled(true);
            }
        }

    }

}
















