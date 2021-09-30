package net.hybrid.core.commands.gmcommands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GmaCommand extends PlayerCommand {

    public GmaCommand(){
        super("gma");
    }

    private final GameMode GAMEMODE = GameMode.ADVENTURE;

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.ADMIN)) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            player.setGameMode(GAMEMODE);
            player.sendMessage(CC.translate("&aYou set your own gamemode to &e" + GAMEMODE.name() + "&a."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);
            return;
        }

        try {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer.getUniqueId() == player.getUniqueId()) {
                player.setGameMode(GAMEMODE);
                player.sendMessage(CC.translate("&aYou set your own gamemode to &e" + GAMEMODE.name() + "&a."));
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);
                return;
            }

            targetPlayer.setGameMode(GAMEMODE);
            targetPlayer.sendMessage(CC.translate("&6" + player.getName() + " &aset your gamemode to &e" + GAMEMODE.name() + "&a."));
            player.sendMessage(CC.translate("&aYou set &6" + targetPlayer.getName() + "'s &agamemode to &e" + GAMEMODE.name() + "&a."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);
            targetPlayer.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 3);

        } catch (NullPointerException exception) {
            hybridPlayer.sendMessage("&cThis player is not online!");
        }

    }
}
