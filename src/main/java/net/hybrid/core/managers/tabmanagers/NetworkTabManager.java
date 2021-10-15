package net.hybrid.core.managers.tabmanagers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.ServerVersion;
import net.hybrid.core.utility.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NetworkTabManager {

    public static final Scoreboard SCOREBOARD = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void init() {
        int count = 0;

        for (PlayerRank rank : PlayerRank.values()) {
            createTeam(letters[count] + "-" + rank.getShortName().toLowerCase(), rank.getPrefixSpace());
            count++;
        }
    }

    private static void createTeam(String name, String prefix) {
        Scoreboard scoreboard = SCOREBOARD;
        if (scoreboard.getTeam(name) != null) {
            scoreboard.getTeam(name).unregister();
        }

        scoreboard.registerNewTeam(name);
        scoreboard.getTeam(name).setPrefix(CC.translate(prefix));
    }

    public static void setTabRank(Player player, PlayerRank rank) {
        int count = 0;

        for (PlayerRank playerRank : PlayerRank.values()) {
            if (playerRank == rank) break;
            count++;
        }

        Team team = SCOREBOARD.getTeam(letters[count] + "-" + rank.getShortName().toLowerCase());

        for (Team targetTeam : SCOREBOARD.getTeams()) {
            targetTeam.removeEntry(player.getName());
        }

        team.addEntry(player.getName());
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.setScoreboard(SCOREBOARD);
        }

        if (CorePlugin.VERSION == ServerVersion.v1_17_R1) {
            player.setPlayerListName(rank.getPrefixSpace() + player.getName());
        }
    }

    private static final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};


}








