package net.hybrid.core.moderation.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.moderation.PlayerReportManager;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import net.hybrid.core.utility.UuidUtils;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReportCommand extends PlayerCommand {

    public ReportCommand() {
        super("report", "userreport", "playerreport", "reportplayer");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /report <player> <reason> to report someone!");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer target = new HybridPlayer(targetOffline.getUniqueId());
        if (!target.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&cThis player has never played on Hybrid before. Are you sure you typed their name right?");
            return;
        }

        if (targetOffline.getUniqueId() == player.getUniqueId()) {
            hybridPlayer.sendMessage("&cYou cannot report yourself!");
            return;
        }

        if (target.getRankManager().isSeniorModerator()) {
            hybridPlayer.sendMessage("&cThis player cannot be reported!");
            return;
        }

        if (args.length > 1) {
            StringBuilder builder = new StringBuilder();
            int count = 0;

            for (String s : args) {
                if (count >= 1) {
                    builder.append(s).append(" ");
                }

                count++;
            }

            hybridPlayer.sendMessage("&aYou have reported " +
                    target.getRankManager().getRank().getPrefixSpace() + target.getColoredName() + "&a. Thanks fella for making the server a better " +
                    "place! :)");
            SoundManager.playSound(player, "LEVEL_UP");
            PlayerReportManager.createNewReport(hybridPlayer.getUniqueId(), target.getUniqueId(), builder.toString().trim(), System.currentTimeMillis());

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerReportIssued");
            out.writeUTF("ONLINE");
            out.writeUTF("PlayerReportIssued");

            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream msgOut = new DataOutputStream(msgBytes);

            try {
                msgOut.writeUTF(player.getUniqueId().toString());
                msgOut.writeUTF(target.getUniqueId().toString());
                msgOut.writeUTF(builder.toString().trim());
                msgOut.writeUTF(target.getColoredName());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.write(msgBytes.toByteArray()); // Reporter
            out.write(msgBytes.toByteArray()); // Report Against (Rule Breaker)
            out.write(msgBytes.toByteArray()); // Reason
            out.write(msgBytes.toByteArray()); // Against Colored Name

            player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        } else {
            hybridPlayer.sendMessage("&cYou need to enter a reason! Use /report <player> <reason>");
        }
    }
}














