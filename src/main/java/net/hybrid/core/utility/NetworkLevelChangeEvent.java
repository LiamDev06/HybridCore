package net.hybrid.core.utility;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class NetworkLevelChangeEvent extends Event implements Cancellable {

    private final int newLevel;
    private final int oldLevel;
    private final UUID updatedUUID;
    private final HybridPlayer updatedHybridPlayer;

    private boolean isCancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public NetworkLevelChangeEvent(int newLevel, int oldLevel, UUID updatedPlayer, HybridPlayer updatedHybridPlayer) {
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
        this.updatedUUID = updatedPlayer;
        this.updatedHybridPlayer = updatedHybridPlayer;
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

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public UUID getUpdatedPlayer() {
        return updatedUUID;
    }

    public HybridPlayer getUpdatedHybridPlayer() {
        return updatedHybridPlayer;
    }
}









