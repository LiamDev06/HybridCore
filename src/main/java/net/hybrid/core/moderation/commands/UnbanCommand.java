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

public class UnbanCommand extends PlayerCommand {

    public UnbanCommand() {
        super("unban");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cSpecify a player to unban and add a reason, valid usage: /unban <player> <reason>");
            return;
        }

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cSpecify a reason for the unban, valid usage: /unban <player> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());

        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNOT FOUND! &cThis player has never played on Hybrid before!");
            return;
        }

        if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
            hybridPlayer.sendMessage("&cYou are literally typing this meaning you're not banned you stup-, or excuse me what?");
            return;
        }

        if (!hybridTarget.isBanned()) {
            hybridPlayer.sendMessage("&c&lNOT BANNED! &cThis player is not banned and therefore cannot be unbanned!");
            return;
        }

        if (hybridPlayer.getRankManager().getRank().getOrdering() <= hybridTarget.getRankManager().getRank().getOrdering()) {
            hybridPlayer.sendMessage("&c&lOUTRANKING! &cThis player either outranks you or you have the same rank meaning you cannot unban them!");
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
        playerDocument.replace("banned", false);
        playerDocument.replace("banId", "");
        CorePlugin.getInstance().getMongo().saveDocument("playerData", playerDocument, offlinePlayer.getUniqueId());

        Document unbanDocument = new Document();
        unbanDocument.append("punishmentId", RandomStringUtils.randomAlphanumeric(10));
        unbanDocument.append("punishmentType", "unban");
        unbanDocument.append("issuerUuid", player.getUniqueId().toString());
        unbanDocument.append("unbannedUuid", offlinePlayer.getUniqueId().toString());
        unbanDocument.append("reason", finalReason);
        unbanDocument.append("issued", System.currentTimeMillis());
        CorePlugin.getInstance().getMongo().saveDocument("punishments", unbanDocument);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UnbanIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("UnbanIssued");

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

        hybridPlayer.sendMessage("&aYou successfully unbanned " + hybridTarget.getRankManager().getRank().getPrefixSpace() + hybridTarget.getName() + "&a with the reason: &6" + reason);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }
}
