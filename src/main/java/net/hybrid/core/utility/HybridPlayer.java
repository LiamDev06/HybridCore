package net.hybrid.core.utility;

import net.hybrid.core.managers.RankManager;

import java.util.UUID;

public class HybridPlayer {

    private final UUID uuid;
    private final RankManager rankManager;

    public HybridPlayer(UUID uuid) {
        this.uuid = uuid;
        this.rankManager = new RankManager(uuid);
    }

    public RankManager getRankManager() {
        return rankManager;
    }

}




