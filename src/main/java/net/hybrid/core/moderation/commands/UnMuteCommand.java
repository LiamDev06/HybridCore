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

public class UnMuteCommand extends ServerCommand {

    public UnMuteCommand() {
        super("unmute");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player to unmute and add a reason, valid usage: /unmute <player> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a reason for the unmute, valid usage: /unmute <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("§cYou cannot unmute yourself!");
            return;
        }

        if (!hybridTarget.isMuted()) {
            hybridPlayer.sendMessage("§c§lNOT MUTED! §cThis player is not muted and therefore cannot be unmuted!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("§c§lOUTRANKING! §cThis player either outranks you or you have the same rank meaning you cannot unmute them!");
            return;
        }

        StringBuilder reason = new StringBuilder();
        int value = 0;

        for (String s : args) {
            if (value > 0) {
                reason.append(s).append(" ");
            }

            value++;
        }

        String finalReason = reason.toString().trim();

        Document playerDocument = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());
        playerDocument.replace("muted", false);
        playerDocument.replace("muteId", "");
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        Document unbanDocument = new Document();
        unbanDocument.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        unbanDocument.append("punishmentType", "unmute");
        unbanDocument.append("issuerUuid", player.getUniqueId().toString());
        unbanDocument.append("unbannedUuid", offlinePlayer.getUniqueId().toString());
        unbanDocument.append("reason", finalReason);
        unbanDocument.append("issued", System.currentTimeMillis());
        CorePlugin.getInstance().getMongo().saveDocument("punishments", unbanDocument);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UnMuteIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("UnMuteIssued");

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
        out.write(msgBytes.toByteArray()); // Unbanned UUID
        out.write(msgBytes.toByteArray()); // Reason
        out.write(msgBytes.toByteArray()); // Colored Name

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("§aYou successfully unmuted " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "§a with the reason: §6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player to unmute and add a reason, valid usage: /unmute <player> <reason>");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a reason for the unmute, valid usage: /unmute <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());

        if (!hybridTarget.hasJoinedServerBefore()) {
            sender.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        if (!hybridTarget.isMuted()) {
            sender.sendMessage("§c§lNOT MUTED! §cThis player is not muted and therefore cannot be unmuted!");
            return;
        }

        StringBuilder reason = new StringBuilder();
        int value = 0;

        for (String s : args) {
            if (value > 0) {
                reason.append(s).append(" ");
            }

            value++;
        }

        String finalReason = reason.toString().trim();

        Document playerDocument = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());
        playerDocument.replace("muted", false);
        playerDocument.replace("muteId", "");
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        Document unbanDocument = new Document();
        unbanDocument.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        unbanDocument.append("punishmentType", "unmute");
        unbanDocument.append("issuerUuid", "CONSOLE");
        unbanDocument.append("unbannedUuid", offlinePlayer.getUniqueId().toString());
        unbanDocument.append("reason", finalReason);
        unbanDocument.append("issued", System.currentTimeMillis());
        CorePlugin.getInstance().getMongo().saveDocument("punishments", unbanDocument);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UnMuteIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("UnMuteIssued");

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
        out.write(msgBytes.toByteArray()); // Unbanned UUID
        out.write(msgBytes.toByteArray()); // Reason
        out.write(msgBytes.toByteArray()); // Colored Name

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            sender.sendMessage("§c§lERROR! §cThis cannot be executed due to a Bukkit/Spigot error.");
            return;
        }
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        sender.sendMessage("§aYou successfully unmuted " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "§a with the reason: §6" + reason);
    }
}
