package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.UuidUtils;
import org.bukkit.entity.Player;

public class SetNetworkLevel extends PlayerCommand {

    public SetNetworkLevel() {
        super("setnetworklevel");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /setnetworklevel <player> <level>");
            return;
        }

        if (args.length > 1) {
            int level;
            HybridPlayer target = new HybridPlayer(UuidUtils.getUUIDFromName(args[0]));
            if (!target.hasJoinedServerBefore()) {
                hybridPlayer.sendMessage("&cThis player has never played on Hybrid before!");
                return;
            }

            try {
                level = Integer.parseInt(args[1]);
            } catch (Exception exception) {
                hybridPlayer.sendMessage("&cInvalid level!");
                return;
            }

            target.getNetworkLevelingManager().setLevel(level);

            hybridPlayer.sendMessage("&a&lLEVEL UPDATED! &aYou set " +
                    target.getRankManager().getRank().getPrefixSpace() + target.getName() + "&a's level to &b" + level + "&a.");
        } else {
            hybridPlayer.sendMessage("&cMissing arguments! Use /setnetworklevel <player> <level>");
        }
    }
}












