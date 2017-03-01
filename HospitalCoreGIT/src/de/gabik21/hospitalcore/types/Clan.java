package de.gabik21.hospitalcore.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

public class Clan {

    private String name;
    private List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();

    public String getName() {

	return this.name;

    }

    public List<OfflinePlayer> getPlayers() {

	return this.players;

    }

    // TODO Finish clan system
}
