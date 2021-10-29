package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TpAllCommand extends PlayerCommand {

    public TpAllCommand() {
        super("tpall");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            if (player.getWorld().getPlayers().size() == 1) {
                hybridPlayer.sendMessage("&cThere are no other players except you in this world!");
                return;
            }

            hybridPlayer.sendMessage("&aTeleporting all players to your location.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);

            for (Player target : player.getWorld().getPlayers()) {
                if (target.getUniqueId() != player.getUniqueId()) {
                    target.teleport(player.getLocation());
                    target.sendMessage(CC.translate(
                            hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                                    player.getName() + " &ateleported you to their location."
                    ));
                    target.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);
                }
            }
            return;
        }

        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (!player.getWorld().getName().equalsIgnoreCase(target.getWorld().getName())) {
                hybridPlayer.sendMessage("&cBoth of you must be in the same world/server for this to work!");
                return;
            }

            target.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);
            target.sendMessage(CC.translate(
                    hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                            player.getName() + " &ateleported &eEVERYONE &ato your location! Yay!"
            ));

            for (Player teleporting : player.getWorld().getPlayers()) {
                if (teleporting.getUniqueId() != target.getUniqueId()) {
                    teleporting.teleport(target.getLocation());
                    teleporting.getPlayer().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);

                    teleporting.sendMessage(CC.translate(
                            hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                                    player.getName() + " &ateleported you to &e" + target.getName() + "'s &alocation."
                    ));
                }
            }
        } catch (Exception e) {
            hybridPlayer.sendMessage("&cThis player is not online!");
        }
    }
}














