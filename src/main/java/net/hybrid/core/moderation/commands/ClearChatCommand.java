package net.hybrid.core.moderation.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hybrid.core.CorePlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerCommand;
import net.hybrid.core.utility.enums.PlayerRank;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClearChatCommand extends ServerCommand {

    public ClearChatCommand() {
        super("clearchat");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isSeniorModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid usage: /clearchat <reason>. Note that the reason will only be dispalyed to staff.");
            return;
        }

        StringBuilder reason = new StringBuilder();

        for (String s : args) {
            reason.append(s).append(" ");
        }

        String finalReason = reason.toString().trim();

        for (Player target : Bukkit.getOnlinePlayers()) {
            HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());

            if (!hybridTarget.getRankManager().isStaff()) {
                for (int i = 0; i<100; i++) {
                    target.sendMessage(" ");
                }
            }

            target.sendMessage(CC.translate("&7&m--------------------------------"));
            target.sendMessage(CC.translate("         &6&lCHAT CLEARED"));
            target.sendMessage("   ");
            target.sendMessage(CC.translate("&7The chat was cleared by staff!"));
            target.sendMessage(CC.translate("&7&m--------------------------------"));

            if (hybridTarget.getRankManager().isStaff()) {
                target.sendMessage(CC.translate("&aYou are staff, so you bypassed the chat clearing!"));
            }
        }

        String punishmentId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        Document document = new Document();
        document.append("punishmentId", punishmentId);
        document.append("punishmentType", "chatClear");
        document.append("issued", System.currentTimeMillis());
        document.append("issuerUuid", player.getUniqueId().toString());
        document.append("serverIssuedOn", "");
        document.append("reason", finalReason);
        CorePlugin.getInstance().getMongo().saveDocument("punishments", document);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ClearChatIssued");
        out.writeUTF("ONLINE");
        out.writeUTF("ClearChatIssued");

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);

        try {
            msgOut.writeUTF(player.getUniqueId().toString());
            msgOut.writeUTF(finalReason);
            msgOut.writeUTF(punishmentId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        out.write(msgBytes.toByteArray());
        player.sendPluginMessage(CorePlugin.getInstance(), "BungeeCord", out.toByteArray());

        hybridPlayer.sendMessage("&aYou successfully cleared the chat!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 11, 2);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

    }

}












