package de.gabik21.hospitalcore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

public class Hologram {
    private static final List<Hologram> all = new ArrayList<Hologram>();
    private static final double distance = 0.23;
    private Map<Integer, EntityWitherSkull> skull = new HashMap<Integer, EntityWitherSkull>();
    private Map<Integer, EntityHorse> horse = new HashMap<Integer, EntityHorse>();
    private List<String> lines = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    private boolean showing = false;
    private Location location;

    public Hologram(List<String> lines) {
	all.add(this);
	this.lines = lines;
    }

    public Hologram(String... lines) {
	all.add(this);
	this.lines.addAll(Arrays.asList(lines));
    }

    public void addLine(String line) {
	this.lines.add(line);
	reset();
    }

    public void change(List<String> lines) {
	this.lines = lines;
	reset();
    }

    public void show(Location loc, Player player) {
	Location first = loc.clone().add(0, (this.lines.size() / 2) * distance, 0);
	for (int i = 0; i < this.lines.size(); i++) {
	    ids.addAll(showLine(first.clone(), this.lines.get(i), player, i));
	    first.subtract(0, distance, 0);
	}
	showing = true;
	this.location = loc;
    }

    public void show(Location loc, Player player, long ticks) {
	show(loc, player);
	new BukkitRunnable() {
	    public void run() {
		destroy();
	    }
	}.runTaskLater(HospitalCore.inst(), ticks);
    }

    public void show(Location loc) {
	if (showing == true) {
	    try {
		throw new Exception("Is already showing!");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	Location first = loc.clone().add(0, (this.lines.size() / 2) * distance, 0);
	for (int i = 0; i < this.lines.size(); i++) {
	    ids.addAll(showLine(first.clone(), this.lines.get(i), null, i));
	    first.subtract(0, distance, 0);
	}
	showing = true;
	this.location = loc;
    }


    public List<String> getLines() {
	return lines;
    }

    @SuppressWarnings("deprecation")
    public void reset() {

	if (showing == false) {
	    try {
		throw new Exception("Isn't showing!");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	int[] ints = new int[this.ids.size()];
	for (int j = 0; j < ints.length; j++) {
	    ints[j] = ids.get(j);
	}
	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
	for (Player player : Bukkit.getOnlinePlayers()) {
	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	ids.clear();
	showing = false;
	show(this.location);

    }

    @SuppressWarnings("deprecation")
    public void destroy() {
	if (showing == false) {
	    try {
		throw new Exception("Isn't showing!");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	int[] ints = new int[this.ids.size()];
	for (int j = 0; j < ints.length; j++) {
	    ints[j] = ids.get(j);
	}
	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
	for (Player player : Bukkit.getOnlinePlayers()) {
	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	showing = false;
	this.location = null;
	all.remove(this);
    }

    public static List<Hologram> getHolograms() {
	return all;
    }

    @SuppressWarnings("deprecation")
    private List<Integer> showLine(Location loc, String text, Player player, int i) {

	if (!showing) {

	    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
	    EntityWitherSkull skull = new EntityWitherSkull(world);
	    skull.setLocation(loc.getX(), loc.getY() + 54.56D, loc.getZ(), 0, 0);
	    PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull, 66);
	    this.skull.put(i, skull);

	    EntityHorse horse = new EntityHorse(world);
	    horse.setLocation(loc.getX(), loc.getY() + 54.56, loc.getZ(), 0, 0);
	    horse.setAge(-1700000);
	    horse.setCustomName(text);
	    horse.setCustomNameVisible(true);
	    PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
	    this.horse.put(i, horse);

	    if (player == null) {
		for (Player all : Bukkit.getOnlinePlayers()) {
		    EntityPlayer nmsPlayer = ((CraftPlayer) all).getHandle();
		    nmsPlayer.playerConnection.sendPacket(packedt);
		    nmsPlayer.playerConnection.sendPacket(skull_packet);

		    PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
		    nmsPlayer.playerConnection.sendPacket(pa);
		}
	    } else {

		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		nmsPlayer.playerConnection.sendPacket(packedt);
		nmsPlayer.playerConnection.sendPacket(skull_packet);
		PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
		nmsPlayer.playerConnection.sendPacket(pa);

	    }
	    return Arrays.asList(skull.getId(), horse.getId());
	} else {

	    skull.get(i).setLocation(loc.getX(), loc.getY() + 54.56D, loc.getZ(), 0, 0);
	    PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull.get(i), 66);

	    horse.get(i).setLocation(loc.getX(), loc.getY() + 54.56, loc.getZ(), 0, 0);
	    horse.get(i).setCustomName(text);
	    horse.get(i).setCustomNameVisible(true);
	    PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse.get(i));

	    if (player == null) {
		for (Player all : Bukkit.getOnlinePlayers()) {
		    EntityPlayer nmsPlayer = ((CraftPlayer) all).getHandle();
		    nmsPlayer.playerConnection.sendPacket(packedt);
		    nmsPlayer.playerConnection.sendPacket(skull_packet);

		    PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse.get(i), skull.get(i));
		    nmsPlayer.playerConnection.sendPacket(pa);
		}
	    } else {

		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		nmsPlayer.playerConnection.sendPacket(packedt);
		nmsPlayer.playerConnection.sendPacket(skull_packet);
		PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse.get(i), skull.get(i));
		nmsPlayer.playerConnection.sendPacket(pa);

	    }

	}

	return new ArrayList<Integer>();
    }

    public Location getLocation() {
	return location;
    }

}
