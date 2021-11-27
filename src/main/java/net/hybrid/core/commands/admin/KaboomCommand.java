package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class KaboomCommand extends PlayerCommand {

    public static ArrayList<UUID> kaboomNoFallDamage;

    public KaboomCommand() {
        super("kaboom");

        kaboomNoFallDamage = new ArrayList<>();
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
                hybridPlayer.sendMessage("&c&lNOT ENOUGH PLAYERS! &cYou are the only one in this world, there are not enough players to kaboom!");
                return;
            }

            for (Player target : player.getWorld().getPlayers()) {
                if (target.getUniqueId() != player.getUniqueId()) {
                    kaboomNoFallDamage.add(target.getUniqueId());
                    target.sendMessage(CC.translate("&2&lKABOOM!"));

                    target.setVelocity(new Vector(0, 100, 0));
                    target.getWorld().strikeLightningEffect(target.getLocation());
                }
            }

            if (player.getWorld().getPlayers().size() == 2) {
                hybridPlayer.sendMessage("&eYou just &e&lKABOOMED &b" + "1" + "&e player!");
            } else {
                hybridPlayer.sendMessage("&eYou just &e&lKABOOMED &b" + (player.getWorld().getPlayers().size() - 1) + "&e players!");
            }
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            kaboomNoFallDamage.add(target.getUniqueId());

            target.setVelocity(new Vector(0, 100, 0));
            target.getWorld().strikeLightningEffect(target.getLocation());
            target.sendMessage(CC.translate(
                    hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName() + " &ejust &e&lKABOOMED &eyou! The power of the admins are too strong to be destroyed *evil laugh*"
            ));

            hybridPlayer.sendMessage("&eYou just &e&lKABOOMED &6" + target.getName() + "&e! What a fool they are, mohaha!");
        } else {
            hybridPlayer.sendMessage("&c&lNOT ONLINE! &cThis player is not online!");
        }
    }
}








