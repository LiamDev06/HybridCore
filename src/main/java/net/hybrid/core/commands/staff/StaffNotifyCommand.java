package net.hybrid.core.commands.staff;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StaffNotifyCommand extends PlayerCommand {

    public StaffNotifyCommand() {
        super("staffnotify", "staffnotifications", "staffnotification");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("NotifyModeChanged");
        out.writeUTF("ONLINE");
        out.writeUTF("NotifyModeChanged");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (hybridPlayer.getMetadataManager().hasStaffNotify()) {
            hybridPlayer.sendMessage("&eYou turned &c&lOFF &estaff notification mode.");
            hybridPlayer.getMetadataManager().setStaffNotify(false);

            try {
                msgOut.writeUTF("off");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            hybridPlayer.sendMessage("&eYou turned &a&lON &estaff notification mode.");
            hybridPlayer.getMetadataManager().setStaffNotify(true);

            try {
                msgOut.writeUTF("on");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        out.write(msgBytes.toByteArray()); // Performed UUID
        out.write(msgBytes.toByteArray()); // On / Off, regarding if they turned it on or off
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }
}










