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

public class KickCommand extends ServerCommand {

    public KickCommand() {
        super("kick");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isStaff()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player and a reason, valid usage: /kick <player> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to enter a reason, valid usage: /kick <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("§cYou cannot kick yourself!");
            return;
        }

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("§cThis player is not online!");
            return;
        }

        if (!hybridTarget.isOnline()) {
            hybridPlayer.sendMessage("§cThis player is not online!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("§c§lOUTRANKING! §cThis player either outranks you or you have the same rank meaning you cannot kick them!");
            return;
        }

        int value = 0;
        for (String s : args) {
            if (value > 0) {
                reason.append(s).append(" ");
            }

            value++;
        }

        String finalReason = reason.toString().trim();
        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "kick");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("KickIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Kicked UUID
        out.write(msgBytes.toByteArray()); // Reason UUID
        out.write(msgBytes.toByteArray()); // Color Name

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("§aYou kicked " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " §afrom the server, with reason: §6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player and a reason, valid usage: /kick <player> <reason>");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to enter a reason, valid usage: /kick <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (!hybridTarget.hasJoinedServerBefore()) {
            sender.sendMessage("§cThis player is not online!");
            return;
        }

        if (!hybridTarget.isOnline()) {
            sender.sendMessage("§cThis player is not online!");
            return;
        }

        int value = 0;
        for (String s : args) {
            if (value > 0) {
                reason.append(s).append(" ");
            }

            value++;
        }

        String finalReason = reason.toString().trim();
        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "kick");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", "CONSOLE");
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("KickIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF("CONSOLE");
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Kicked UUID
        out.write(msgBytes.toByteArray()); // Reason UUID
        out.write(msgBytes.toByteArray()); // Color Name

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            sender.sendMessage("§c§lERROR! §cThis cannot be executed due to a Bukkit/Spigot error.");
            return;
        }
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        sender.sendMessage("§aYou kicked " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " §afrom the server, with reason: §6" + reason);
    }
}









