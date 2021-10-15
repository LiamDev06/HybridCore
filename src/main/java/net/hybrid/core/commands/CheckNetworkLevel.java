package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.UuidUtils;
import org.bukkit.entity.Player;

public class CheckNetworkLevel extends PlayerCommand {

    public CheckNetworkLevel() {
        super("checknetworklevel");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /checknetworklevel <player>");
            return;
        }

        HybridPlayer target = new HybridPlayer(UuidUtils.getUUIDFromName(args[0]));
        hybridPlayer.sendMessage(target.getRankManager().getRank().getPrefixSpace() +
                target.getName() + "&a's current level is &b" + target.getNetworkLevelingManager().getLevel() + "&a.");

    }
}










