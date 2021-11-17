package net.hybrid.core.moderation.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TempBanCommand extends PlayerCommand {

    public TempBanCommand() {
        super("tempban");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cSpecify a player, ban duration and a reason. Valid Usage: /tempban <player> <duration> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to add a reason and a ban duration. Valid Usage: /tempban <player> <duration> <reason>");
            return;
        }

        if (args.length == 2) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to add a reason for the ban. Valid Usage: /tempban <player> <duration> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("&cYou cannot ban yourself!");
            return;
        }

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNOT FOUND! &cThis player has never played on Hybrid before!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("&c&lOUTRANKING! &cThis player either outranks you or you have the same rank meaning you cannot ban them!");
            return;
        }

        if (hybridTarget.isBanned()) {
            hybridPlayer.sendMessage("&c&lALREADY BANNED! &cThis player already has an active ban on their account!");
            return;
        }

        String durationArg = args[1].toLowerCase();
        if (!durationArg.endsWith("m") && !durationArg.endsWith("h") && !durationArg.endsWith("d")) {
            hybridPlayer.sendMessage("&c&lINVALID DURATION! &cThe duration suffix you entered is not valid. The allowed ones are: &6m (minutes)&c, &6h (hours)&c, &6d (days)&c!");
            return;
        }

        long timeFormat;
        try {
            timeFormat = Long.parseLong(durationArg.replace("m", "").replace("h", "").replace("d", ""));
        } catch (Exception exception) {
            hybridPlayer.sendMessage("&c&lINVALID DURATION! &cThe duration you entered is not valid. Please try again!");
            return;
        }

        char durationChar = durationArg.charAt(durationArg.length() - 1);
        long timeFormatInMillis = 0;
        String durationNormal = String.valueOf(durationChar);

        if (durationChar == 'd') {
            timeFormatInMillis = System.currentTimeMillis() + (timeFormat * 864_00_000);
            durationNormal = "day(s)";
        } else if (durationChar == 'h') {
            timeFormatInMillis = System.currentTimeMillis() + (timeFormat * 3_600_000);
            durationNormal = "hour(s)";
        } else if (durationChar == 'm') {
            timeFormatInMillis = System.currentTimeMillis() + (timeFormat * 60_000);
            durationNormal = "minute(s)";
        }

        if (timeFormatInMillis == 0L)  {
            hybridPlayer.sendMessage("&c&lERROR! &cSomething went wrong with setting the ban duration. Please try again!");
            return;
        }

        int value = 0;
        for (String s : args) {
            if (value > 1) {
                reason.append(s).append(" ");
            }

            value++;
        }

        String finalReason = reason.toString().trim();
        String punishmentId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "tempban");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("expires", timeFormatInMillis);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        Document playerDocument = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());
        playerDocument.replace("banned", true);
        playerDocument.replace("banId", punishmentId);
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("TempBanIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("TempBanIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(timeFormat + " " + durationNormal);
            msgOut.writeUTF(hybridTarget.getColoredName());
            msgOut.writeUTF(punishmentId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Against UUID
        out.write(msgBytes.toByteArray()); // Reason
        out.write(msgBytes.toByteArray()); // Expires (normal)
        out.write(msgBytes.toByteArray()); // Colored Name
        out.write(msgBytes.toByteArray()); // Punishment ID

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("&aYou successfully temporarily banned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " &afor &b" + timeFormat + " " + durationNormal + "&a, with the reason: &6" + finalReason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

}
