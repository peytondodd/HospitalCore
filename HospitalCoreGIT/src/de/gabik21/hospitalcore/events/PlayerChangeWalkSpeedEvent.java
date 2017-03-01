package de.gabik21.hospitalcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChangeWalkSpeedEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();
    private float from;
    private float to;

    public PlayerChangeWalkSpeedEvent(Player who, float from, float to) {
	super(who);

	this.from = from;
	this.to = to;

    }

    public float getFrom() {
	return from;
    }

    public float getTo() {
	return to;
    }

    public static HandlerList getHandlerList() {

	return handlerList;

    }

    @Override
    public HandlerList getHandlers() {

	return handlerList;
    }

}
