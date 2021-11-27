package net.hybrid.core.commands.admin;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.entity.Player;

public class CoreVersionCommand extends PlayerCommand {

    public CoreVersionCommand() {
        super("coreversion");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        hybridPlayer.sendMessage("&fFetching version...");
        hybridPlayer.sendMessage("&fCurrent &aHybridCore &fversion: &cv" + CorePlugin.getInstance().getDescription().getVersion());
        SoundManager.playSound(player, "NOTE_PLING");

    }
}












