package net.hybrid.core.commands;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.ServerType;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UnnickCommand extends PlayerCommand {

    public UnnickCommand() {
        super("unnick");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (!player.getName().equalsIgnoreCase("LiamHBest")) {
            return;
        }

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.TWITCH_STREAMER)) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (hybridPlayer.getServerManager().getServerType() != ServerType.HUB) {
            hybridPlayer.sendMessage("&cThis command can only be performed in lobbies!");
            return;
        }

        if (hybridPlayer.getDisguiseManager().isNicked()) {
            hybridPlayer.getDisguiseManager().unnick();

            new BukkitRunnable() {
                @Override
                public void run() {
                    hybridPlayer.getDisguiseManager().unnick();
                }
            }.runTaskLater(CorePlugin.getInstance(), 2);

            hybridPlayer.sendMessage("&aYou are not nicked anymore!");
        } else {
            hybridPlayer.sendMessage("&cYou are not currently nicked!");
        }

    }

}











