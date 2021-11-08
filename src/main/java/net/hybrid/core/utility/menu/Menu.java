package net.hybrid.core.utility.menu;

import com.sun.istack.internal.NotNull;
import net.hybrid.core.utility.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    protected MenuUtility menuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte)7)
            .setDisplayName("")
            .build();
    public Menu(MenuUtility menuUtility) {
        this.menuUtility = menuUtility;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    public abstract void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        menuUtility.getOwner().openInventory(inventory);
        menuUtility.pushMenu(this);
    }

    public void back() throws MenuManagerException, MenuManagerNotSetupException {
        MenuManager.openMenu(menuUtility.lastMenu().getClass(), menuUtility.getOwner());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    public <T> T PMUCaster(MenuUtility abstractPlayerMenuUtility, Class<T> t) {
        return t.cast(abstractPlayerMenuUtility);
    }

}
