package net.hybrid.core.managers.tabmanagers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ServerVersion;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

public class NetworkTabManager {

    public static HashMap<UUID, Scoreboard> scoreboards = new HashMap<>();

    public static void initTeams(Scoreboard scoreboard) {
        int count = 0;

        for (PlayerRank rank : PlayerRank.values()) {
            createTeam(letters[count] + "-" + rank.getShortName().toLowerCase(), rank.getPrefixSpace(), scoreboard);
            count++;
        }
    }

    private static void createTeam(String name, String prefix, Scoreboard board) {
        if (board.getTeam(name) != null) {
            board.getTeam(name).unregister();
        }

        board.registerNewTeam(name);
        board.getTeam(name).setPrefix(CC.translate(prefix));
    }

    public static void setTabRank(Player player, PlayerRank rank, Scoreboard board) {
        initTeams(board);

        int count = 0;

        for (PlayerRank playerRank : PlayerRank.values()) {
            if (playerRank == rank) break;
            count++;
        }

        Team team = board.getTeam(letters[count] + "-" + rank.getShortName().toLowerCase());

        for (Team targetTeam : board.getTeams()) {
            targetTeam.removeEntry(player.getName());
        }

        team.addEntry(player.getName());

        int targetCount = 0;
        boolean breaking = false;
        for (PlayerRank playerRank : PlayerRank.values()) {
            for (Player target : Bukkit.getOnlinePlayers()) {

                if (target.getUniqueId() != player.getUniqueId()) {
                    HybridPlayer hybridTarget = new HybridPlayer(target.getUniqueId());
                    if (hybridTarget.getRankManager().getRank() == playerRank) {
                        board.getTeam(letters[targetCount] + "-" + hybridTarget.getRankManager().getRank().getShortName().toLowerCase()).addEntry(target.getName());
                        breaking = true;
                        break;
                    }
                }

            }

            if (breaking) break;
            targetCount++;
        }

        player.setScoreboard(board);

        if (CorePlugin.VERSION == ServerVersion.v1_17_R1) {
            player.setPlayerListName(rank.getPrefixSpace() + player.getName());
        }
    }

    private static final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
}








