package net.hybrid.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerNetworkExpChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Double setAmount;
    private final Double oldAmount;
    private final Boolean causedLevelUp;
    private final UUID uuid;

    public PlayerNetworkExpChangeEvent(UUID uuid, double setAmount, double oldAmount, boolean causedLevelUp) {
        this.uuid = uuid;
        this.setAmount = setAmount;
        this.oldAmount = oldAmount;
        this.causedLevelUp = causedLevelUp;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Boolean getCausedLevelUp() {
        return causedLevelUp;
    }

    public Double getOldAmount() {
        return oldAmount;
    }

    public Double getSetAmount() {
        return setAmount;
    }

    public UUID getUuid() {
        return uuid;
    }

}
