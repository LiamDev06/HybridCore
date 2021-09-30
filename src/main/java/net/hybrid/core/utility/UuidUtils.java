package net.hybrid.core.utility;

import org.bukkit.Bukkit;

import java.util.UUID;

public class UuidUtils {

    public static String getNameFromUUID(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public static UUID getUUIDFromName(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

}
