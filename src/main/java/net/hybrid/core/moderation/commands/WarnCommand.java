package net.hybrid.core.moderation.commands;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerCommand;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WarnCommand extends ServerCommand {

    public WarnCommand() {
        super("warn");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cSpecify a player, reason and choose if they should be kicked on warn. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to add a reason and choose if they should be kicked on warn. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (args.length == 2) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to add a reason for the warning. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
            hybridPlayer.sendMessage("&c&lINVALID VALUE! &cThe '&6kickOnWarn&c' value can only be true or false!");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("&cYou cannot warn yourself!");
            return;
        }

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNOT FOUND! &cThis player has never played on Hybrid before!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("&c&lOUTRANKING! &cThis player either outranks you or you have the same rank meaning you cannot warn them!");
            return;
        }

        Document playerDoc = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());

        if (playerDoc.getBoolean("waitingOnSeeWarning")) {
            hybridPlayer.sendMessage("&c&lALREADY HAS WARNING! &cThis player already has a warning against them that they have not seen yet!");
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
        String punishmentId = RandomStringUtils.randomAlphanumeric(10);

        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "warning");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("kickedOnWarn", Boolean.valueOf(args[1]));
        document.append("playerHasSeen", false);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        playerDoc.replace("waitingOnSeeWarning", true);
        playerDoc.replace("warningWaitingId", punishmentId);
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDoc, offlinePlayer.getUniqueId());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("WarningIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("WarningIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(args[1]);
            msgOut.writeUTF(punishmentId);
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Warned UUID
        out.write(msgBytes.toByteArray()); // Reason UUID
        out.write(msgBytes.toByteArray()); // Kick On Warn (true / false)
        out.write(msgBytes.toByteArray()); // Punishment ID
        out.write(msgBytes.toByteArray()); // Color Name

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("&aYou warned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "&a, with reason: &6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player, reason and choose if they should be kicked on warn. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to add a reason and choose if they should be kicked on warn. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (args.length == 2) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to add a reason for the warning. Valid Usage: /warn <player> <kickOnWarn (true/false)> <reason>");
            return;
        }

        if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
            sender.sendMessage("§c§lINVALID VALUE! §cThe '§6kickOnWarn§c' value can only be true or false!");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (!hybridTarget.hasJoinedServerBefore()) {
            sender.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        Document playerDoc = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());

        if (playerDoc.getBoolean("waitingOnSeeWarning")) {
            sender.sendMessage("§c§lALREADY HAS WARNING! §cThis player already has a warning against them that they have not seen yet!");
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
        String punishmentId = RandomStringUtils.randomAlphanumeric(10);

        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "warning");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", "CONSOLE");
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("kickedOnWarn", Boolean.valueOf(args[1]));
        document.append("playerHasSeen", false);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        playerDoc.replace("waitingOnSeeWarning", true);
        playerDoc.replace("warningWaitingId", punishmentId);
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDoc, offlinePlayer.getUniqueId());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("WarningIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("WarningIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF("CONSOLE");
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(args[1]);
            msgOut.writeUTF(punishmentId);
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Warned UUID
        out.write(msgBytes.toByteArray()); // Reason UUID
        out.write(msgBytes.toByteArray()); // Kick On Warn (true / false)
        out.write(msgBytes.toByteArray()); // Punishment ID
        out.write(msgBytes.toByteArray()); // Color Name

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        sender.sendMessage("§aYou warned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "§a, with reason: §6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

}











