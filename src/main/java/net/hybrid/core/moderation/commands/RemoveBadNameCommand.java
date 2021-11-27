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
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoveBadNameCommand extends ServerCommand {

    public RemoveBadNameCommand() {
        super("removebadname");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a name, valid usage: /removebadname <name> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a reason for the removal, valid usage: /removebadname <name> <reason>");
            return;
        }

        StringBuilder reason = new StringBuilder();
        int count = 0;
        for (String s : args) {
            if (count > 0) {
                reason.append(s).append(" ");
            }

            count++;
        }

        String finalReason = reason.toString().trim();
        Document badNameDoc = CorePlugin.getInstance().getMongo().loadDocument("serverData", "serverDataType", "badNameList");
        String targetName = args[0].toLowerCase();

        if (!badNameDoc.containsValue(targetName)) {
            hybridPlayer.sendMessage("§c§lDOES NOT EXISTS! §cThe bad name '§6" + targetName + "§c' has never been added to the bad name list!");
            return;
        }

        for (String key : badNameDoc.keySet()) {
            if (badNameDoc.get(key).equals(targetName)) {
                badNameDoc.remove(key);
                break;
            }
        }

        badNameDoc.replace("addValue", badNameDoc.getInteger("addValue") - 1);

        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "badnameRemoved");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("reason", finalReason);
        document.append("name", targetName);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BadNameRemoved");
        out.writeUTF("ONLINE");
        out.writeUTF("BadNameRemoved");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(targetName);
            msgOut.writeUTF(finalReason);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Bad Name (actual name)
        out.write(msgBytes.toByteArray()); // Reason for removal

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("§aYou removed the 'bad-name' §6" + targetName + "§a from the 'bad-name' list!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);

        CorePlugin.getInstance().getMongo().saveDocument("serverData", badNameDoc, "serverDataType", "badNameList");
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a name, valid usage: /removebadname <name> <reason>");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a reason for the removal, valid usage: /removebadname <name> <reason>");
            return;
        }

        StringBuilder reason = new StringBuilder();
        int count = 0;
        for (String s : args) {
            if (count > 0) {
                reason.append(s).append(" ");
            }

            count++;
        }

        String finalReason = reason.toString().trim();
        Document badNameDoc = CorePlugin.getInstance().getMongo().loadDocument("serverData", "serverDataType", "badNameList");
        String targetName = args[0].toLowerCase();

        if (!badNameDoc.containsValue(targetName)) {
            sender.sendMessage("§c§lDOES NOT EXISTS! §cThe bad name '§6" + targetName + "§c' has never been added to the bad name list!");
            return;
        }

        for (String key : badNameDoc.keySet()) {
            if (badNameDoc.get(key).equals(targetName)) {
                badNameDoc.remove(key);
                break;
            }
        }

        badNameDoc.replace("addValue", badNameDoc.getInteger("addValue") - 1);

        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "badnameRemoved");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", "CONSOLE");
        document.append("reason", finalReason);
        document.append("name", targetName);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BadNameRemoved");
        out.writeUTF("ONLINE");
        out.writeUTF("BadNameRemoved");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF("CONSOLE");
            msgOut.writeUTF(targetName);
            msgOut.writeUTF(finalReason);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Bad Name (actual name)
        out.write(msgBytes.toByteArray()); // Reason for removal

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            sender.sendMessage("§c§lERROR! §cThis cannot be executed due to a Bukkit/Spigot error.");
            return;
        }
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());
        sender.sendMessage("§aYou removed the 'bad-name' §6" + targetName + "§a from the 'bad-name' list!");

        CorePlugin.getInstance().getMongo().saveDocument("serverData", badNameDoc, "serverDataType", "badNameList");
    }
}












