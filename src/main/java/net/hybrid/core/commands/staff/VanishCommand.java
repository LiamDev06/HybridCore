package net.hybrid.core.commands.staff;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.ServerType;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishCommand extends PlayerCommand {

    public VanishCommand() {
        super("vanish");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.TWITCH_STREAMER)) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (hybridPlayer.getServerManager().getServerType() != ServerType.HUB) {
            hybridPlayer.sendMessage("&cThis command can only be performed in lobbies!");
            return;
        }

        // VANISH
        if (hybridPlayer.getDisguiseManager().isVanished()) {
            hybridPlayer.sendMessage("&aYou reappeared!");
            player.setPlayerListName(hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName());
            hybridPlayer.getDisguiseManager().setVanished(false);

            for (Player target : Bukkit.getOnlinePlayers()) {
                target.showPlayer(player);
            }

        } else {
            //UN VANISH

            hybridPlayer.sendMessage("&aYou vanished! Unvanish by typing &6/vanish &aagain.");
            hybridPlayer.getDisguiseManager().setVanished(true);
            player.setPlayerListName(hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName() + CC.translate(" &5[V]"));

            for (Player target : Bukkit.getOnlinePlayers()) {
                HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());
                if (!hybridTarget.getRankManager().isStaff()) {
                    target.hidePlayer(player);
                }
            }
        }
    }
}