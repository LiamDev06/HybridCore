package net.hybrid.core.managers;

import net.hybrid.core.commands.admin.KaboomCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerManager implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                && KaboomCommand.kaboomNoFallDamage.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
            event.setDamage(0);
            KaboomCommand.kaboomNoFallDamage.remove(event.getEntity().getUniqueId());
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        KaboomCommand.kaboomNoFallDamage.remove(event.getPlayer().getUniqueId());
    }

}
