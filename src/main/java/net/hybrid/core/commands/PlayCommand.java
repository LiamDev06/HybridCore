package net.hybrid.core.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.managers.GameManager;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayCommand extends PlayerCommand {

    public PlayCommand() {
        super("play");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(CC.translate("&c&lMISSING ARGUMENTS! &cValid Usage: /play <game>"));
            return;
        }

        if (args[0].equalsIgnoreCase("lobby") || args[0].equalsIgnoreCase("hub")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SendToLobbyIssued");
            out.writeUTF("ONLINE");
            out.writeUTF("SendToLobbyIssued");

            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream msgOut = new DataOutputStream(msgBytes);

            try {
                msgOut.writeUTF(player.getUniqueId().toString());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.write(msgBytes.toByteArray());
            player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
            return;
        }

        if (args[0].equalsIgnoreCase("survival")) {
            GameManager.playSurvival(player);
            return;
        }

        if (args[0].equalsIgnoreCase("ffa") || args[0].equalsIgnoreCase("freeforall")) {
            GameManager.playFreeForAll(player);
            return;
        }

        if (args[0].equalsIgnoreCase("dev")) {
            if (hybridPlayer.getRankManager().isAdmin() || player.getName().equalsIgnoreCase("Joel_H_Miner") || player.getName().equalsIgnoreCase("HyperOP")) {
                GameManager.devServer(hybridPlayer, player);
            } else {
                hybridPlayer.sendMessage("&cYou do not have access to play this!");
            }
            return;
        }

        player.sendMessage(CC.translate("&c&lINVALID GAME! &cNo game could be found with that name!"));
    }
}
















