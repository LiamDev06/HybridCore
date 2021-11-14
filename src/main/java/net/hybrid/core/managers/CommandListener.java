package net.hybrid.core.managers;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

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

        if (event.getMessage().toLowerCase().startsWith("/help")) {
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
            return;
        }

        if (event.getMessage().toLowerCase().startsWith("/clear")) {
            event.setCancelled(true);
            if (!hybridPlayer.getRankManager().isAdmin()) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                return;
            }

            String[] args = event.getMessage().split(" ");
            if (args.length == 1) {
                player.getInventory().clear();
                player.getItemInHand().setType(Material.AIR);

                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getInventory().setBoots(new ItemStack(Material.AIR));

                hybridPlayer.sendMessage("&aYou cleared your inventory!");
                hybridPlayer.sendMessage("&a&lTIP: &aUse &6/clear <player> &ato clear an inventory of someone else");
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 11, 2);
                return;
            }

            Player target;
            try {
                target = Bukkit.getPlayer(args[1]);

                if (target.getUniqueId() == player.getUniqueId()) {
                    player.getInventory().clear();
                    player.getItemInHand().setType(Material.AIR);

                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    player.getInventory().setBoots(new ItemStack(Material.AIR));

                    hybridPlayer.sendMessage("&aYou cleared your inventory!");
                    hybridPlayer.sendMessage("&a&lTIP: &aUse &6/clear <player> &ato clear an inventory of someone else");
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 11, 2);
                    return;
                }

                target.getInventory().clear();
                target.getItemInHand().setType(Material.AIR);

                target.getInventory().setHelmet(new ItemStack(Material.AIR));
                target.getInventory().setChestplate(new ItemStack(Material.AIR));
                target.getInventory().setLeggings(new ItemStack(Material.AIR));
                target.getInventory().setBoots(new ItemStack(Material.AIR));

                hybridPlayer.sendMessage("&aYou cleared the inventory of &6" + target.getName() + "&a!");
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 11, 2);

                target.sendMessage(CC.translate(
                        hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName() + " &acleared your inventory!"
                ));
                target.playSound(player.getLocation(), Sound.NOTE_BASS, 10, 2);
            } catch (Exception exception) {
                hybridPlayer.sendMessage("&c&lNOT ONLINE! &cThis player is currently not playing!");
            }
        }

        if (event.getMessage().toLowerCase().split(" ")[0].equalsIgnoreCase("/me")) {
            if (!hybridPlayer.getRankManager().isAdmin()) {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
                event.setCancelled(true);
            }
        }
    }
}
















