package net.hybrid.core.rank;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerCommand;
import net.hybrid.core.utility.UuidUtils;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand extends ServerCommand {

    public RankCommand(){
        super("rank");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!player.getUniqueId().toString().equals("de17e166-a71f-4ab5-ba83-7dc38a8e6723")
                && !player.getUniqueId().toString().equals("96fb68a4-601b-41cb-99c0-c8a575abb98e")
                && !player.getUniqueId().toString().equals("6dc247f1-1492-4d93-95dc-08fc40ebb94a")) {

            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing required arguments! Use /rank <rank> <player>");
            return;
        }

        if (args[0].equalsIgnoreCase("check")) {
            if (args.length > 1) {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(UuidUtils.getUUIDFromName(args[1]));
                HybridPlayer targetPlayer = new HybridPlayer(offlineTarget.getUniqueId());

                if (!targetPlayer.hasJoinedServerBefore()) {
                    hybridPlayer.sendMessage("&cThis player does not seem to exist in our database. Are you sure you typed their name correctly?");
                    return;
                }

                hybridPlayer.sendMessage("&a" + targetPlayer.getName() + "'s rank: " +
                        targetPlayer.getRankManager().getRank().getDisplayName());

            } else {
                hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "specify_player"));
            }
            return;
        }

        int amount = PlayerRank.values().length;
        int times = 0;

        for (PlayerRank rank : PlayerRank.values()) {
            if (rank.name().equalsIgnoreCase(args[0])) {
                if (args.length > 1) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UuidUtils.getUUIDFromName(args[1]));
                    HybridPlayer targetPlayer = new HybridPlayer(offlinePlayer.getUniqueId());

                    if (!targetPlayer.hasJoinedServerBefore()) {
                        hybridPlayer.sendMessage("&cThis player does not seem to exist in our database. Are you sure you typed their name correctly?");
                        return;
                    }

                    PlayerRank setRank = PlayerRank.valueOf(args[0].toUpperCase());
                    if (targetPlayer.getRankManager().getRank() == setRank) {
                        hybridPlayer.sendMessage("&c" + targetPlayer.getName() + " is already " + setRank.getDisplayName());
                        return;
                    }

                    targetPlayer.getRankManager().setRank(setRank);

                    hybridPlayer.sendMessage("&aYou set " + targetPlayer.getName() + "'s rank to " + setRank.name().replace("_", " ").toUpperCase());

                    try {
                        targetPlayer.sendBungeeMessage("&aYou are now " + setRank.name().replace("_", " ").toUpperCase());
                    } catch (Exception ignored) {}

                } else {
                    hybridPlayer.sendMessage("&cYou need to specify a player first! Use /rank <rank> <player>");
                }
                return;
            }

            times++;
            if (times == amount) {
                hybridPlayer.sendMessage("&cNo rank was found! The available ranks are:");
                hybridPlayer.sendMessage("&c" + getAvailableRanks());
            }
        }

    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

    }

    private String getAvailableRanks() {
        StringBuilder builder = new StringBuilder();

        for (PlayerRank rank : PlayerRank.values()) {
            builder.append(rank.name().toUpperCase()).append(", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

}














