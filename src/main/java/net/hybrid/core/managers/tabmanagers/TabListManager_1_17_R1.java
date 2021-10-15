package net.hybrid.core.managers.tabmanagers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.TabInterface;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListManager_1_17_R1 implements TabInterface {

    @Override
    public void init() {
        new BukkitRunnable() {
            String header = "§6§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
            final String footer = "\n §b§lDiscord: §fdsc.gg/hybridserver\n §b§lTwitter: §f@PlayHybridMC";
            int count = 0;

            @Override
            public void run() {
                if (count == 0) {
                    header = "§6§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 1) {
                    header = "§d§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 2) {
                    header = "§c§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 3) {
                    header = "§a§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 4) {
                    header = "§b§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 5) {
                    header = "§e§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                }

                if (count == 6) {
                    header = "§9§lHybrid Server\n§eYou are playing on §a§lhybridplays.com\n ";
                    count = 0;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) player).setPlayerListHeaderFooter(
                            header, footer
                    );
                }
                count++;
            }
        }.runTaskTimer(CorePlugin.getInstance(), 0L, 25L);
    }
}







