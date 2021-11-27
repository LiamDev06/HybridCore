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

public class BanCommand extends ServerCommand {

    public BanCommand() {
        super("ban");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player and a reason, valid usage: /ban <player> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to enter a reason, valid usage: /ban <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("§cYou cannot ban yourself!");
            return;
        }

        if (hybridTarget.isBanned()) {
            hybridPlayer.sendMessage("§c§lALREADY BANNED! §cThis player has already been banned from the server!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("§c§lOUTRANKING! §cThis player either outranks you or you have the same rank meaning you cannot ban them!");
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
        String punishmentId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

        if (finalReason.equalsIgnoreCase("cheating") || finalReason.equalsIgnoreCase("hacking")) {
            finalReason = "Cheating through the use of modifications that results in an unfair advantage";

        } else if (finalReason.equalsIgnoreCase("evading")) {
            finalReason = "Alt-account punishment evading";

        } else if (finalReason.equalsIgnoreCase("exploit")) {
            finalReason = "Abusing glitches, bugs and/or exploits intentionally";

        } else if (finalReason.equalsIgnoreCase("sabotage")) {
            finalReason = "Intentionally sabotaging the game for other players";

        } else if (finalReason.equalsIgnoreCase("teaming")) {
            finalReason = "Game sabotaging in the form of cross-teaming";
        }

        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "ban");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("duration", "permanent");
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        Document playerDocument = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());
        playerDocument.replace("banned", true);
        playerDocument.replace("banId", punishmentId);
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PermanentBanIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("PermanentBanIssued");

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

        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("§aYou successfully permanently banned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " §afrom entering this server, with reason: §6" + finalReason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player and a reason, valid usage: /ban <player> <reason>");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cYou need to enter a reason, valid usage: /ban <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (!hybridTarget.hasJoinedServerBefore()) {
            sender.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        if (hybridTarget.isBanned()) {
            sender.sendMessage("§c§lALREADY BANNED! §cThis player has already been banned from the server!");
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
        String punishmentId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

        if (finalReason.equalsIgnoreCase("cheating") || finalReason.equalsIgnoreCase("hacking")) {
            finalReason = "Cheating through the use of modifications that results in an unfair advantage";

        } else if (finalReason.equalsIgnoreCase("evading")) {
            finalReason = "Alt-account punishment evading";

        } else if (finalReason.equalsIgnoreCase("exploit")) {
            finalReason = "Abusing glitches, bugs and/or exploits intentionally";

        } else if (finalReason.equalsIgnoreCase("sabotage")) {
            finalReason = "Intentionally sabotaging the game for other players";

        } else if (finalReason.equalsIgnoreCase("teaming")) {
            finalReason = "Game sabotaging in the form of cross-teaming";
        }

        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "ban");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", "CONSOLE");
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("duration", "permanent");
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        Document playerDocument = CorePlugin.getInstance().getMongo().loadDocument("playerData", offlinePlayer.getUniqueId());
        playerDocument.replace("banned", true);
        playerDocument.replace("banId", punishmentId);
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PermanentBanIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("PermanentBanIssued");

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

        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            sender.sendMessage("§c§lERROR! §cThis cannot be executed due to a Bukkit/Spigot error.");
            return;
        }
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        sender.sendMessage("§aYou successfully permanently banned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " §afrom entering this server, with reason: §6" + finalReason);
    }
}










