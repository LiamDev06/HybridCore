package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TpHereCommand extends PlayerCommand {

    public TpHereCommand() {
        super("tphere");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use &6/tphere <player> &cor &6/tphere <player> <player> <player>...&c!");
            return;
        }

        StringBuilder error = new StringBuilder();
        StringBuilder teleporting = new StringBuilder();
        boolean shouldSendError = false;

        if (args.length >= 2){
            error.append(CC.RED).append("The players ");
        } else {
            error.append(CC.RED).append("The player ");
        }

        teleporting.append(CC.GREEN).append("Teleporting ");

        int times = 0;
        for (String s : args) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(s);
            if (target.isOnline()) {
                times++;
                HybridPlayer targetUser = new HybridPlayer(target.getUniqueId());
                SoundManager.playSound(target, "ENDERMAN_TELEPORT");
                target.getPlayer().teleport(player.getLocation());
                targetUser.sendMessage(hybridPlayer.getRankManager().getRank().getPrefixSpace() +
                        player.getName() + " &ateleported you to their location.");

                teleporting.append(targetUser.getRankManager().getRank().getPrefixSpace()).append(target.getName()).append(", ");

            } else {
                shouldSendError = true;
                error.append(target.getName()).append(CC.GREEN).append(", ").append(CC.RED);
            }


        }

        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);
        teleporting.append(CC.GREEN).append("to your position.");
        error.append("could not be teleported. They are offline!");

        if (times >= 1){
            player.sendMessage(teleporting.toString());
        }

        if (shouldSendError) player.sendMessage(error.toString());
    }
}

















