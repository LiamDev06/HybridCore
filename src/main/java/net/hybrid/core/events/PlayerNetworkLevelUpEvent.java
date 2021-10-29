package net.hybrid.core.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerNetworkLevelUpEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    private int levelTo;
    private final UUID uuid;
    private final int oldLevel;

    public PlayerNetworkLevelUpEvent(UUID uuid, int levelTo, int oldLevel) {
        this.uuid = uuid;
        this.levelTo = levelTo;
        this.oldLevel = oldLevel;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public HandlerList getHandlerList() {
        return HANDLERS_LIST;
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
