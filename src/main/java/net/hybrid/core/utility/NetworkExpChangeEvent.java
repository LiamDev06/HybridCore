package net.hybrid.core.utility;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class NetworkExpChangeEvent extends Event implements Cancellable {

    private final double currentExp;
    private final double expSetAmount;
    private final UUID updatedUUID;
    private final HybridPlayer updatedHybridPlayer;

    private boolean isCancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public NetworkExpChangeEvent(double currentExp, double expSetAmount, UUID updatedPlayer, HybridPlayer updatedHybridPlayer) {
        this.currentExp = currentExp;
        this.expSetAmount = expSetAmount;
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

    public double getCurrentExp() {
        return currentExp;
    }

    public double getExpSetAmount() {
        return expSetAmount;
    }

    public UUID getUpdatedPlayer() {
        return updatedUUID;
    }

    public HybridPlayer getUpdatedHybridPlayer() {
        return updatedHybridPlayer;
    }

}
