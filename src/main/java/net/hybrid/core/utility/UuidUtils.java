package net.hybrid.core.utility;

import org.bukkit.Bukkit;

import java.util.UUID;

public class UuidUtils {

    /**
     * Methods needs to be re-worked and changed to implement the Mojang API instead
     */

    public static String getNameFromUUID(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public static UUID getUUIDFromName(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

}
