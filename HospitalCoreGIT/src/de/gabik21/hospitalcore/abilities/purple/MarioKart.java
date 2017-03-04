package de.gabik21.hospitalcore.abilities.purple;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class MarioKart extends Ability implements Listener {

    final static long MARIOKART_COOLDOWN = 60_000;
    final static Set<String> incart = new HashSet<String>();

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.MARIOKART) || pd.isOnCooldown(Kit.MARIOKART)
		|| pd.isIn1v1())
	    return;

	pd.useKit(Kit.MARIOKART);

	incart.add(p.getName());
	Minecart m = (Minecart) p.getWorld().spawn(p.getEyeLocation(), Minecart.class);
	m.setPassenger(p);
	m.setMaxSpeed(m.getMaxSpeed() + 0.5D);
	new BukkitRunnable() {
	    public void run() {
		incart.remove(p.getName());
		m.eject();
		m.remove();
	    }
	}.runTaskLater(HospitalCore.inst(), 20 * 30);

    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent e) {

	if (!(e.getVehicle() instanceof Minecart))
	    return;
	Minecart m = (Minecart) e.getVehicle();
	if (!(m.getPassenger() instanceof Player))
	    return;

	Player p = (Player) m.getPassenger();

	if (!incart.contains(p.getName()))
	    return;

	Location loc = p.getLocation();
	loc.setPitch(0);

	Vector v = loc.getDirection().multiply(15);
	v.divide(m.getDerailedVelocityMod());

	if (checkforWall(m) && (m.getVelocity().getY() <= 0))
	    v.setY(2.4);
	else if (!m.isOnGround())
	    v.setY(-0.5);

	m.setVelocity(v);

    }

    private boolean checkforWall(Entity item) {

	Block itemb = item.getLocation().getBlock();

	if (itemb.getRelative(BlockFace.NORTH).getType().isSolid()
		|| itemb.getRelative(BlockFace.NORTH).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.SOUTH).getType().isSolid()
		|| itemb.getRelative(BlockFace.SOUTH).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.WEST).getType().isSolid()
		|| itemb.getRelative(BlockFace.WEST).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.EAST).getType().isSolid()
		|| itemb.getRelative(BlockFace.EAST).getType() == Material.GLASS)
	    return true;

	return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
	incart.remove(e.getEntity().getName());
    }

    @EventHandler
    public void onVehicleLeave(VehicleExitEvent e) {

	if (!(e.getExited() instanceof Player))
	    return;

	Player p = (Player) e.getExited();

	if (incart.contains(p.getName()))
	    e.setCancelled(true);

    }

    @Override
    public long getCooldown() {
	return MARIOKART_COOLDOWN;
    }

}
