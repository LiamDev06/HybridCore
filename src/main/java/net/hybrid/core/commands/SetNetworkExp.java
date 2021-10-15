package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetNetworkExp extends PlayerCommand {

    public SetNetworkExp() {
        super("setnetworkexp");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /setnetworkexp <player> <exp>");
            return;
        }

        if (args.length > 1) {
            double exp;
            HybridPlayer target = new HybridPlayer(UuidUtils.getUUIDFromName(args[0]));
            if (!target.hasJoinedServerBefore()) {
                hybridPlayer.sendMessage("&cThis player has never played on Hybrid before!");
                return;
            }

            try {
                exp = Double.parseDouble(args[1]);
            } catch (Exception exception) {
                hybridPlayer.sendMessage("&cInvalid exp amount!");
                return;
            }

            NetworkExpChangeEvent networkExpChangeEvent = new NetworkExpChangeEvent(
                    target.getNetworkLevelingManager().getExp(), exp, target.getUniqueId(), target
            );
            target.getNetworkLevelingManager().setExp(exp);
            Bukkit.getServer().getPluginManager().callEvent(networkExpChangeEvent);

            hybridPlayer.sendMessage("&a&lEXP UPDATED! &aYou set " +
                    target.getRankManager().getRank().getPrefixSpace() + target.getName() + "&a's exp in their current level to &b" + exp + "&a.");
        } else {
            hybridPlayer.sendMessage("&cMissing arguments! Use /setnetworkexp <player> <exp>");
        }
    }
}














