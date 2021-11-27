package net.hybrid.core.managers.tabmanagers;

import net.hybrid.core.CorePlugin;
import net.hybrid.core.utility.TabInterface;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TabListManager_1_8_R3 implements TabInterface {

    private static final List<Object> headers = new ArrayList<>();
    private static final List<Object> footers = new ArrayList<>();

    private static int headerCount;
    private static int footerCount;

    @Override
    public void init() {
        headerCount = 0;
        footerCount = 0;
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (headerCount == headers.size()) {
                    headerCount = 0;
                }

                if (footerCount == footers.size()) {
                    footerCount = 0;
                }

                addHeader("&6&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&d&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&c&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&a&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&b&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&e&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");
                addHeader("&9&lHybrid Server\n&eYou are playing on &a&lhybridplays.com\n ");

                addFooter("\n &b&lDiscord: &fhybridplays.com/discord\n &b&lTwitter: &f@PlayHybridMC");

                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    Field b = packet.getClass().getDeclaredField("b");

                    a.setAccessible(true);
                    b.setAccessible(true);

                    a.set(packet, headers.get(headerCount));
                    b.set(packet, footers.get(footerCount));

                    if (Bukkit.getOnlinePlayers().size() == 0) return;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }

                    headerCount++;
                    footerCount++;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(CorePlugin.getInstance(), 0, 25);
    }

    private static void addHeader(String text) {
        headers.add(new ChatComponentText(text.replace("&", "§")));
    }

    private static void addFooter(String text) {
        footers.add(new ChatComponentText(text.replace("&", "§")));
    }
}











