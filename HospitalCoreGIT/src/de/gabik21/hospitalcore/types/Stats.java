package de.gabik21.hospitalcore.types;

public class Stats {

    private int kills, deaths, wins, killstreak;

    public Stats(int kills, int deaths, int wins) {
	this.kills = kills;
	this.deaths = deaths;
	this.wins = wins;
    }

    public int getKills() {
	return kills;
    }

    public int getDeaths() {
	return deaths;
    }

    public int getWins() {
	return wins;
    }

    public int getKillstreak() {
	return killstreak;
    }

    public void addKill() {
	this.kills++;
	this.killstreak++;
    }

    public void addDeath() {
	this.deaths++;
    }

    public void addWin() {
	this.wins++;
    }

    public void resetKillStreak() {
	this.killstreak = 0;
    }

}
