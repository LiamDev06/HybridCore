package net.hybrid.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ServerManager {

    private final UUID uuid;
    public ServerManager(UUID uuid) {
        this.uuid = uuid;
    }

    public ServerType getServerType() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            if (player.getWorld().getName().equalsIgnoreCase("lobby")) {
                return ServerType.HUB;
            }
        }

        return ServerType.NONE;
    }

}
