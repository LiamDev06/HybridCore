package net.hybrid.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerNetworkLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private int levelTo;
    private final UUID uuid;
    private final int oldLevel;

    public PlayerNetworkLevelUpEvent(UUID uuid, int levelTo, int oldLevel) {
        this.uuid = uuid;
        this.levelTo = levelTo;
        this.oldLevel = oldLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getLevelTo() {
        return levelTo;
    }

    public void setLevelTo(int levelTo) {
        this.levelTo = levelTo;
    }

    public UUID getUuid() {
        return uuid;
    }

}
