package net.hybrid.core.managers;

import net.hybrid.core.events.PlayerNetworkLevelUpEvent;
import net.hybrid.core.utility.CC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NetworkLevelListener implements Listener {

    @EventHandler
    public void onLevelUp(PlayerNetworkLevelUpEvent event) {
        Player player = Bukkit.getPlayer(event.getUuid());

        if (player.isOnline()) {
            player.sendMessage(CC.translate("&7&m-----------------------------------"));
            player.sendMessage(" ");
            player.sendMessage(CC.translate("    &9&kLLL &6&lLEVEL UP &9&kLLL"));
            player.sendMessage(CC.translate("&aYou just leveled up from level &b&l" + event.getOldLevel() + " &a&l➟ &b&l" + event.getLevelTo() + "&a!"));
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&7Claim your reward at the &bRewards &7NPC in the main lobby by performing &d/lobby"));
            player.sendMessage(CC.translate("&7&m-----------------------------------"));

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 14, 1);
        }
    }
}













