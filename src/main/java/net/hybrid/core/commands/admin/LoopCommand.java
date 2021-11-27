package net.hybrid.core.commands.admin;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class LoopCommand extends PlayerCommand {

    public LoopCommand() {
        super("loop");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0 || args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid usage: /loop <times> <command>...");
            return;
        }

        int times;
        try {
            times = Integer.parseInt(args[0]);
        } catch (Exception exception) {
            hybridPlayer.sendMessage("&cInvalid 'times' amount entered!");
            return;
        }

        if (times <= 1) {
            hybridPlayer.sendMessage("&cThe loop only works with a number greater than 1!");
            return;
        }

        StringBuilder command = new StringBuilder();
        int count = 0;
        for (String s : args) {
            if (count >= 1) {
                command.append(s).append(" ");
            }

            count++;
        }

        for (int i = 0; i < times; i++) {
            player.performCommand(command.toString().trim());
        }

        hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The command '&2" + command.toString().trim() + "&6' was successfully executed &3" + times + " &6times!");
    }
}














