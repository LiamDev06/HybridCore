package net.hybrid.core.commands.staff;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.UuidUtils;
import org.bukkit.entity.Player;

public class CheckNetworkExp extends PlayerCommand {

    public CheckNetworkExp() {
        super("checknetworkexp");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_helper"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /checknetworkexp <player>");
            return;
        }

        HybridPlayer target = new HybridPlayer(UuidUtils.getUUIDFromName(args[0]));
        hybridPlayer.sendMessage(target.getRankManager().getRank().getPrefixSpace() +
                target.getName() + "&a's exp in their current level is &b" + target.getNetworkLevelingManager().getExp() + "&a.");
    }
}











