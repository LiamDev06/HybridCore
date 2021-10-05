package net.hybrid.core.managers;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

        if (event.getMessage().toLowerCase().startsWith("/tp")
                && !event.getMessage().toLowerCase().startsWith("/tphere")
                && !event.getMessage().toLowerCase().startsWith("/tpall")) {
            event.setCancelled(true);
            if (!hybridPlayer.getRankManager().isAdmin()) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                return;
            }

            String[] args = event.getMessage().split(" ");
            if (args.length == 1) {
                hybridPlayer.sendMessage("&cMissing arguments! Use /tp <player> and specify someone!");
                return;
            }

            HybridPlayer hybridTarget;
            Player target;

            try {
                target = Bukkit.getPlayer(args[1]);
                if (player.getUniqueId() == target.getUniqueId()) {
                    hybridPlayer.sendMessage("&cYou cannot teleport to yourself!");
                    return;
                }

                hybridTarget = new HybridPlayer(target.getUniqueId());
                player.teleport(target.getLocation());

                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);
                target.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);

                player.sendMessage(CC.translate(
                        "&aYou teleported to " +
                                hybridTarget.getRankManager().getRank().getPrefixSpace() +
                                target.getName() + "&a's location."
                ));

                target.sendMessage(CC.translate(
                        hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                                player.getName() + " &ateleported to your location."
                ));
            } catch (Exception e) {
                hybridPlayer.sendMessage("&cThis player is not online!");
            }
        }
    }
}
















