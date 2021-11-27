package net.hybrid.core.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.DisguiseManager;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerType;
import net.hybrid.core.utility.enums.NickSkinType;
import net.hybrid.core.utility.nick.Nick;
import net.hybrid.core.utility.nick.NickUtils;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.bookgui.BookUtil;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NickCommand extends PlayerCommand {

    public NickCommand() {
        super("nick");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (!player.getName().equalsIgnoreCase("LiamHBest")) {
            return;
        }

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().hasRank(PlayerRank.TWITCH_STREAMER)) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (hybridPlayer.getServerManager().getServerType() != ServerType.HUB) {
            hybridPlayer.sendMessage("&cThis command can only be performed in lobbies!");
            return;
        }

        if (args.length == 0) {
            if (hybridPlayer.getDisguiseManager().isNicked()) {
                hybridPlayer.sendMessage("&cYou are already nicked! Unnick with &6/unnick &cfirst!");
                return;
            }

            DisguiseManager.nicksCache.remove(player.getUniqueId());
            DisguiseManager.nickedPlayersCache.remove(player.getUniqueId());
            BookUtil.openPlayer(player, NickUtils.getNickStartBook());
            return;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            player.chat("/unnick");
            return;
        }

        if (args[0].equalsIgnoreCase("random")) {
            if (hybridPlayer.getDisguiseManager().isNicked()) {
                hybridPlayer.sendMessage("&cYou are already nicked! Unnick with &6/unnick &cfirst!");
                return;
            }

            Nick nick = new Nick(
                    player.getUniqueId(),
                    NickUtils.generateRandomNickname(),
                    NickUtils.getRandomNickRank(),
                    NickSkinType.RANDOM,
                    NickUtils.getRandomNickFile()
            ).save();
            hybridPlayer.getDisguiseManager().nick(nick);

            hybridPlayer.sendMessage("&aYou are now nicked! Perform &6/unnick &ato get back to your usual self!");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 2);
            return;
        }

        if (hybridPlayer.getDisguiseManager().isNicked()) {
            hybridPlayer.sendMessage("&cYou are already nicked! Unnick with &6/unnick &cfirst!");
            return;
        }

        if (!NickUtils.passedFilter(args[0].trim())) {
            hybridPlayer.sendMessage("&cInvalid nickname!");
            return;
        }

        Nick nick = new Nick(
                player.getUniqueId(),
                args[0].trim(),
                NickUtils.getRandomNickRank(),
                NickSkinType.RANDOM,
                NickUtils.getRandomNickFile()
        ).save();
        hybridPlayer.getDisguiseManager().nick(nick);

        hybridPlayer.sendMessage("&aYou are now nicked! Perform &6/unnick &ato get back to your usual self!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 2);
    }

}















