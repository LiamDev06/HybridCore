package net.hybrid.core.utility;

import net.hybrid.core.CorePlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {

    public static void playSound(Player player, String sound) {
        if (CorePlugin.VERSION == ServerVersion.v1_8_R3) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 10, 1);
        }
    }

    public static void playSound(Player player, String sound, float volume, float pitch){
        if (CorePlugin.VERSION == ServerVersion.v1_8_R3) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void playClickSound(Player player){
        if (CorePlugin.VERSION == ServerVersion.v1_8_R3) {
            player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
        }
    }

    public static void playErrorSound(Player player){
        if (CorePlugin.VERSION == ServerVersion.v1_8_R3) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
        }
    }

}
