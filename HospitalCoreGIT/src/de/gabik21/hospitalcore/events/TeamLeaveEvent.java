package de.gabik21.hospitalcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import de.gabik21.hospitalcore.types.Team;

public class TeamLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Team team;
    private String message;

    public TeamLeaveEvent(Player who, Team team) {

	super(who);
	this.team = team;

    }

    public Team getTeam() {

	return this.team;
    }

    public String getMessage() {

	return this.message;

    }

    public void setMessage(String message) {

	this.message = message;

    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    public HandlerList getHandlers() {
	return handlers;
    }

}
