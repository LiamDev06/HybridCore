package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class FeedCommand extends PlayerCommand {

    public FeedCommand() {
        super("feed");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            player.setFoodLevel(20);
            hybridPlayer.sendMessage("&eYou are not hungry anymore!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 2);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            hybridPlayer.sendMessage("&cThis player is not online!");
            return;
        }

        HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());
        target.setFoodLevel(20);

        hybridPlayer.sendMessage("&eYou fed " + hybridTarget.getRankManager().getRank().getPrefixSpace() + target.getName() + "&e!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 2);
    }

}








