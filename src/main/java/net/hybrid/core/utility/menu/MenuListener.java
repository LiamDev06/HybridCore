package net.hybrid.core.utility.menu;


import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            if (e.getCurrentItem() == null) {
                return;
            }
            Menu menu = (Menu) holder;

            if (menu.cancelAllClicks()) {
                e.setCancelled(true);
            }

            try {
                menu.handleMenu(e);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                System.out.println(ChatColor.RED + "NO SE HA CONFIGURADO EL ADMINISTRADOR DE MENÚS. Usa MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }
    }
}