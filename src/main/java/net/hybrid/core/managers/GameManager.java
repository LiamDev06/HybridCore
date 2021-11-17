package net.hybrid.core.managers;

import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.entity.Player;

public class GameManager {

    public static boolean isCorrectVersion(Player player) {
        return true;
    }

    public static void playSurvival(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage("&c&lMAINTENANCE! &cThis server is currently in maintenance!");
            return;
        }

        if (isCorrectVersion(player)) {
            hybridPlayer.sendMessage("&aSending you to survival...");
            SoundManager.playSound(player, "ENDERMAN_TELEPORT");

            hybridPlayer.sendToServer("mini_survival_1");
        } else {
            hybridPlayer.sendMessage("&cYou need to be on &61.17 &cto play this!");
        }
    }

    public static void playFreeForAll(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        hybridPlayer.sendMessage("&aSending you to freeforall...");
        SoundManager.playSound(player, "ENDERMAN_TELEPORT");

        hybridPlayer.sendToServer("mini_ffa_1");
    }

    public static void devServer(HybridPlayer hybridPlayer, Player player) {
        hybridPlayer.sendMessage("&aSending you to the dev server...");
        SoundManager.playSound(player, "ENDERMAN_TELEPORT");

        hybridPlayer.sendToServer("dev");
    }

}
