package de.gabik21.hospitalcore.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.events.TeamLeaveEvent;

public class Team {

    private Player leader;
    private List<Player> inteam = new ArrayList<Player>();
    private int currentArena = -1;
    private int currentTeamtagArena = -1;
    private long lastswitch = 0;
    private boolean queued = false;

    public Team(Player p) {

	this.leader = p;
	this.inteam.add(p);
	HospitalCore.getData(p).setTeam(this);

    }

    public void remove() {

	for (Player p : getPlayers()) {

	    PlayerData pd = HospitalCore.getData(p);
	    p.sendMessage(
		    HospitalCore.inst().getConfig().getString("prefix") + "§cYour current team has been removed.");
	    pd.setTeam(null);

	}

    }

    public boolean isQueued() {

	return this.queued;

    }

    public void setQueued(boolean queued) {

	this.queued = queued;

    }

    public int size() {

	return this.inteam.size();

    }

    public void setCurrentArena(int id) {

	this.currentArena = id;

    }

    public int getCurrentArena() {

	return this.currentArena;

    }

    public void setCurrentTeamtagArena(int id) {

	this.currentTeamtagArena = id;

    }

    public int getCurrentTeamtagArena() {

	return this.currentTeamtagArena;

    }

    public List<Player> getPlayers() {

	return this.inteam;

    }

    public Player getLeader() {

	return this.leader;

    }

    public void setLeader(Player leader) {

	this.leader = leader;

    }

    public void sendMessage(String message) {

	for (Player team : inteam)
	    team.sendMessage(message);

    }

    public void addPlayer(Player p) {

	this.inteam.add(p);
	HospitalCore.getData(p).setTeam(this);
	sendMessage("§6§l[§cTeam§6§l] §b§lPlayer " + p.getName() + " entered the team!");

    }

    public void removePlayer(Player p) {

	TeamLeaveEvent e = new TeamLeaveEvent(p, this);
	Bukkit.getPluginManager().callEvent(e);

	this.inteam.remove(p);
	HospitalCore.getData(p).setTeam(null);

	if (e.getMessage() != null)
	    for (Player team : inteam)
		team.sendMessage(e.getMessage());

	if (getPlayers().size() == 0) {

	    remove();
	    return;
	}

	if (getLeader() == p)
	    setLeader(getPlayers().get((int) Math.random() * (getPlayers().size() - 1)));

    }

    public long getLastSwitch() {

	return lastswitch;

    }

    public void switched() {

	this.lastswitch = System.currentTimeMillis();

    }
}
