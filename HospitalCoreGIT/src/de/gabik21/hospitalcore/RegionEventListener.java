package de.gabik21.hospitalcore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.gabik21.hospitalcore.events.MovementWay;
import de.gabik21.hospitalcore.events.RegionEnterEvent;
import de.gabik21.hospitalcore.events.RegionLeaveEvent;

public class RegionEventListener implements Listener {

    private HospitalCore plugin = null;
    private Map<Player, Set<ProtectedRegion>> playerRegions;

    public RegionEventListener(HospitalCore plugin) {
	this.plugin = plugin;
	this.playerRegions = new HashMap<Player, Set<ProtectedRegion>>();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
	Set<ProtectedRegion> regions = (Set<ProtectedRegion>) this.playerRegions.remove(e.getPlayer());
	if (regions != null) {
	    for (ProtectedRegion region : regions) {
		RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e);

		this.plugin.getServer().getPluginManager().callEvent(leaveEvent);
	    }
	}
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
	Set<ProtectedRegion> regions = (Set<ProtectedRegion>) this.playerRegions.remove(e.getPlayer());
	if (regions != null) {
	    for (ProtectedRegion region : regions) {
		RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e);

		this.plugin.getServer().getPluginManager().callEvent(leaveEvent);
	    }
	}
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	updateRegions(p, MovementWay.MOVE, e.getTo(), e);

    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
	updateRegions(e.getPlayer(), MovementWay.TELEPORT, e.getTo(), e);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
	updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getPlayer().getLocation(), e);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
	updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getRespawnLocation(), e);
    }

    private synchronized boolean updateRegions(Player player, MovementWay movement, Location to, PlayerEvent event) {
	Set<ProtectedRegion> regions = playerRegions.get(player) == null ? new HashSet<ProtectedRegion>()
		: new HashSet<ProtectedRegion>(playerRegions.get(player));
	Set<ProtectedRegion> oldRegions = new HashSet<ProtectedRegion>(regions);

	RegionManager rm = WorldGuardPlugin.inst().getRegionManager(to.getWorld());

	if (rm == null)
	    return false;

	ApplicableRegionSet appRegions = rm.getApplicableRegions(to);
	for (ProtectedRegion region : appRegions) {
	    if (!regions.contains(region)) {
		RegionEnterEvent e = new RegionEnterEvent(region, player, movement, event);

		this.plugin.getServer().getPluginManager().callEvent(e);
		if (e.isCancelled()) {

		    regions.clear();
		    regions.addAll(oldRegions);

		    if (movement == MovementWay.MOVE) {

			Vector maxpoint = new Vector(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(),
				region.getMaximumPoint().getZ());
			Vector minpoint = new Vector(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(),
				region.getMinimumPoint().getZ());
			Vector midpoint = minpoint.getMidpoint(maxpoint);

			Vector push = to.toVector().subtract(midpoint).normalize().setY(0.5);

			player.setVelocity(push);
		    }

		    return false;
		}

		regions.add(region);
	    }
	}
	Collection<ProtectedRegion> app = new ArrayList<ProtectedRegion>();
	Iterator<ProtectedRegion> litr = appRegions.iterator();

	while (litr.hasNext())
	    app.add(litr.next());

	Iterator<ProtectedRegion> itr = regions.iterator();
	while (itr.hasNext()) {
	    ProtectedRegion region = itr.next();
	    if (!app.contains(region)) {
		if (rm.getRegion(region.getId()) != region) {
		    itr.remove();
		} else {
		    RegionLeaveEvent e = new RegionLeaveEvent(region, player, movement, event);
		    this.plugin.getServer().getPluginManager().callEvent(e);
		    if (e.isCancelled()) {
			regions.clear();
			regions.addAll(oldRegions);

			if (movement == MovementWay.MOVE) {

			    Vector maxpoint = new Vector(region.getMaximumPoint().getX(),
				    region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
			    Vector minpoint = new Vector(region.getMinimumPoint().getX(),
				    region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
			    Vector midpoint = minpoint.getMidpoint(maxpoint);

			    Vector push = midpoint.subtract(to.toVector()).normalize().setY(0.5);

			    player.setVelocity(push);

			}

			return false;
		    }

		    itr.remove();
		}
	    }
	}
	this.playerRegions.put(player, regions);
	HospitalCore.getData(player).updateRegions(regions);
	return false;
    }

}
