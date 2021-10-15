package net.hybrid.core.moderation;

import net.hybrid.core.moderation.PlayerReportManager;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import net.hybrid.core.utility.UuidUtils;
import net.hybrid.core.utility.bookgui.BookUtil;
import net.hybrid.core.utility.enums.ReportReason;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class ReportCommand extends PlayerCommand {

    public ReportCommand() {
        super("report", "userreport", "playerreport", "reportplayer");
    }

    public static ArrayList<UUID> inCustomReportReason = new ArrayList<>();

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /report <player> <reason> to report someone!");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(UuidUtils.getUUIDFromName(args[0]));
        HybridPlayer target = new HybridPlayer(targetOffline.getUniqueId());
        if (!target.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&cThis player has never played on Hybrid before. Are you sure you typed their name right?");
            return;
        }

        if (args.length > 1) {
            StringBuilder builder = new StringBuilder();
            int count = 1;
            // report liamhbest bad person he cheats so hard pls ban bannnnn

            for (String s : args) {
                if (count > 2) {
                    builder.append(s).append(" ");
                }

                count++;
            }

            hybridPlayer.sendMessage("&aYou have reported " +
                    target.getRankManager().getRank().getPrefixSpace() + target.getColoredName() + "&a. Thanks fella for making the server a better " +
                    "place! :)");
            SoundManager.playSound(player, "LEVEL_UP");
            PlayerReportManager.createNewReport(hybridPlayer.getUniqueId(), target.getUniqueId(), builder.toString(), System.currentTimeMillis());

        } else {
            hybridPlayer.sendMessage("&cYou need to enter a reason! Use /report <player> <reason>");
        }
    }
}














