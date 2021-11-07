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

public class BanCommand extends PlayerCommand {

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
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cSpecify a player and a reason, valid usage: /ban <player> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to enter a reason, valid usage: /ban <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        StringBuilder reason = new StringBuilder();

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNOT FOUND! &cThis player has never played on Hybrid before!");
            return;
        }

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("&cYou cannot ban yourself!");
            return;
        }

        if (hybridTarget.isBanned()) {
            hybridPlayer.sendMessage("&c&lALREADY BANNED! &cThis player has already been banned from the server!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("&c&lOUTRANKING! &cThis player either outranks you or you have the same rank meaning you cannot ban them!");
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

        hybridPlayer.sendMessage("&aYou successfully permanently banned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + " &afrom entering this server, with reason: &6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }
}










