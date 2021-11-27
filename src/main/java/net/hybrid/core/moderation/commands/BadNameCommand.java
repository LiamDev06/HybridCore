package net.hybrid.core.moderation.commands;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.data.Mongo;
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

public class BadNameCommand extends ServerCommand {

    public BadNameCommand() {
        super("badname");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player, valid usage: /badname <player>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("§cYou cannot 'bad-name' yourself!");
            return;
        }

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("§c§lOUTRANKING! §cThis player either outranks you or you have the same rank meaning you cannot 'bad-name' them!");
            return;
        }

        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document badNameList = mongo.loadDocument("serverData", "serverDataType", "badNameList");
        int addValue = badNameList.getInteger("addValue");

        if (badNameList.containsValue(offlinePlayer.getName().toLowerCase())) {
            hybridPlayer.sendMessage("§c§lALREADY BAD-NAMED! §cThe username of this player is already on the 'bad-name' list!");
            return;
        }

        badNameList.append("name-" + (addValue + 1), offlinePlayer.getName().toLowerCase());
        badNameList.replace("addValue", addValue + 1);
        mongo.saveDocument("serverData", badNameList, "serverDataType", "badNameList");

        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "badName");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("badName", offlinePlayer.getName());
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BadNameIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("BadNameIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getName());
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Bad Name UUID
        out.write(msgBytes.toByteArray()); // Bad Name (actual name)
        out.write(msgBytes.toByteArray()); // Color Name

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("§aYou 'bad-named' " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "§a and the name '§6" + offlinePlayer.getName() + "§a'!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§lMISSING ARGUMENTS! §cSpecify a player, valid usage: /badname <player>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());

        if (!hybridTarget.hasJoinedServerBefore()) {
            sender.sendMessage("§c§lNOT FOUND! §cThis player has never played on Hybrid before!");
            return;
        }

        Mongo mongo = CorePlugin.getInstance().getMongo();
        Document badNameList = mongo.loadDocument("serverData", "serverDataType", "badNameList");
        int addValue = badNameList.getInteger("addValue");

        if (badNameList.containsValue(offlinePlayer.getName().toLowerCase())) {
            sender.sendMessage("§c§lALREADY BAD-NAMED! §cThe username of this player is already on the 'bad-name' list!");
            return;
        }

        badNameList.append("name-" + (addValue + 1), offlinePlayer.getName().toLowerCase());
        badNameList.replace("addValue", addValue + 1);
        mongo.saveDocument("serverData", badNameList, "serverDataType", "badNameList");

        Document document = new Document();
        document.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        document.append("punishmentType", "badName");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", "CONSOLE");
        document.append("againstUuid", offlinePlayer.getUniqueId().toString());
        document.append("badName", offlinePlayer.getName());
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BadNameIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("BadNameIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF("CONSOLE");
            msgOut.writeUTF(offlinePlayer.getUniqueId().toString());
            msgOut.writeUTF(offlinePlayer.getName());
            msgOut.writeUTF(hybridTarget.getColoredName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray()); // Issuer UUID
        out.write(msgBytes.toByteArray()); // Bad Name UUID
        out.write(msgBytes.toByteArray()); // Bad Name (actual name)
        out.write(msgBytes.toByteArray()); // Color Name

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            sender.sendMessage("§c§lERROR! §cThis cannot be executed due to a Bukkit/Spigot error.");
            return;
        }

        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        sender.sendMessage("§aYou 'bad-named' " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "§a and the name '§6" + offlinePlayer.getName() + "§a'!");
    }

}











