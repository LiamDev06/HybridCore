package net.hybrid.core.utility.menu;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuManager {

    private static final HashMap<Player, MenuUtility> menuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin) {

        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            System.out.println(rl.getListener().getClass().getSimpleName());
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }

    }

    public static void setup(Server server, Plugin plugin) {

        System.out.println("MENU MANAGER HAS BEEN SETUP");

        registerMenuListener(server, plugin);
        isSetup = true;

    }

    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            menuClass.getConstructor(MenuUtility.class).newInstance(getPlayerMenuUtility(player)).open();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MenuManagerException();
        }
    }

    public static MenuUtility getPlayerMenuUtility(Player p) throws MenuManagerException, MenuManagerNotSetupException {

        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        MenuUtility menuUtility;
        if (!(menuUtilityMap.containsKey(p))) {
            menuUtility = new MenuUtility(p);
            menuUtilityMap.put(p, menuUtility);

            return menuUtility;
        } else {
            return menuUtilityMap.get(p);
        }
    }
}
