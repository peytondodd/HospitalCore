package de.gabik21.hospitalcore.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;

public enum SpawnPoints {

    SPAWN(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("spawn.x"),
		    HospitalCore.inst().getConfig().getDouble("spawn.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("spawn.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("spawn.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("spawn.pitch"))),
    ONEVSONE(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("1v1.x"),
		    HospitalCore.inst().getConfig().getDouble("1v1.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("1v1.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("1v1.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("1v1.pitch"))),
    CHALLENGE(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("challenge.x"),
		    HospitalCore.inst().getConfig().getDouble("challenge.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("challenge.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("challenge.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("challenge.pitch"))),
    EHG(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("ehg.x"),
		    HospitalCore.inst().getConfig().getDouble("ehg.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("ehg.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("ehg.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("ehg.pitch"))),
    MARKET(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("market.x"),
		    HospitalCore.inst().getConfig().getDouble("market.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("market.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("market.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("market.pitch"))),
    KITCREATION(
	    new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("kitcreation.x"),
		    HospitalCore.inst().getConfig().getDouble("kitcreation.y") + 1,
		    HospitalCore.inst().getConfig().getDouble("kitcreation.z"),
		    (float) HospitalCore.inst().getConfig().getDouble("kitcreation.yaw"),
		    (float) HospitalCore.inst().getConfig().getDouble("kitcreation.pitch")));

    private Location loc;

    private SpawnPoints(Location loc) {
	this.loc = loc;
    }

    public void teleport(Player p) {
	p.teleport(loc);
    }

    public Location getLocation() {
	return this.loc;
    }

    private void setLocation(Location loc) {
	this.loc = loc;
    }

    public static void refresh() {
	for (SpawnPoints point : SpawnPoints.values()) {
	    if (point == ONEVSONE) {
		point.setLocation(
			new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble("1v1.x"),
				HospitalCore.inst().getConfig().getDouble("1v1.y") + 1,
				HospitalCore.inst().getConfig().getDouble("1v1.z"),
				(float) HospitalCore.inst().getConfig().getDouble("1v1.yaw"),
				(float) HospitalCore.inst().getConfig().getDouble("1v1.pitch")));
	    } else {
		String name = point.name().toLowerCase();
		point.setLocation(
			new Location(Bukkit.getWorlds().get(0), HospitalCore.inst().getConfig().getDouble(name + ".x"),
				HospitalCore.inst().getConfig().getDouble(name + ".y") + 1,
				HospitalCore.inst().getConfig().getDouble(name + ".z"),
				(float) HospitalCore.inst().getConfig().getDouble(name + ".yaw"),
				(float) HospitalCore.inst().getConfig().getDouble(name + ".pitch")));
	    }
	}
    }

}
