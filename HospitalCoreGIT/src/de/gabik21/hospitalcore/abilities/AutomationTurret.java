package de.gabik21.hospitalcore.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.Hologram;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;

public class AutomationTurret extends Ability implements Listener {

    private static final long AUTOMATION_TURRET = 90_000;
    private static final Set<Turret> TURRETS = new HashSet<>();

    @Override
    public void onDeath(PlayerDeathEvent e) {
	for (Turret turret : TURRETS)
	    if (turret.getOwner().equals(e.getEntity().getName()))
		turret.remove();
    }

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.AUTOMATIONTURRET))
	    return;

	for (Turret turret : TURRETS)
	    if (turret.getOwner().equals(p.getName()) && turret.getMinecart().getPassenger() == p) {
		turret.setBullets(turret.getBullets() - 1);
		Arrow arrow = p.launchProjectile(Arrow.class);
		MiscDisguise dis = new MiscDisguise(DisguiseType.DROPPED_ITEM);
		DroppedItemWatcher watcher = (DroppedItemWatcher) dis.getWatcher();
		watcher.setItemStack(new ItemStack(Material.ENDER_PEARL));
		DisguiseAPI.disguiseEntity(arrow, dis);
		return;
	    }

	if (pd.isOnCooldown(Kit.AUTOMATIONTURRET))
	    return;

	pd.useKit(Kit.AUTOMATIONTURRET);

	Turret turret = new Turret(p.getName(), p.getWorld().spawn(p.getLocation().add(0, 1.7, 0), Minecart.class),
		p.getLocation().getBlock().getState());
	p.getLocation().getBlock().setType(Material.FENCE);
	TURRETS.add(turret);
	turret.init();

    }

    class Turret {

	String owner;
	BlockState previousBlock;
	Minecart minecart;
	Location initialMinecartLoc;
	Hologram hologram;
	int health = 20, bullets = 120, missiles = 15, task;
	boolean update;

	Turret(String owner, Minecart minecart, BlockState state) {
	    this.owner = owner;
	    this.minecart = minecart;
	    this.previousBlock = state;
	    this.initialMinecartLoc = minecart.getLocation();
	}

	public String getOwner() {
	    return owner;
	}

	public void setHealth(int health) {
	    this.health = health;
	    this.update = true;
	}

	public int getHealth() {
	    return health;
	}

	public Minecart getMinecart() {
	    return minecart;
	}

	public int getBullets() {
	    return bullets;
	}

	public void setBullets(int bullets) {
	    this.bullets = bullets;
	    this.update = true;
	}

	public int getMissiles() {
	    return missiles;
	}

	public void setMissiles(int missiles) {
	    this.missiles = missiles;
	    this.update = true;
	}

	public Location getInitialMinecartLoc() {
	    return initialMinecartLoc;
	}

	public void remove() {
	    Bukkit.getScheduler().cancelTask(task);
	    previousBlock.update(true, false);
	    hologram.destroy();
	    minecart.remove();
	    TURRETS.remove(this);
	}

	public void init() {

	    Hologram h = new Hologram(new ArrayList<>());
	    h.show(minecart.getLocation().add(0, 1.1, 0));
	    h.addLine("§cHealth: " + health);
	    h.addLine("§9Bullets: " + bullets);
	    h.addLine("§3Missiles: " + missiles);
	    hologram = h;

	    BukkitTask t = new BukkitRunnable() {
		@Override
		public void run() {
		    if (update) {
			List<String> lines = Arrays.asList("§cHealth: " + health, "§9Bullets: " + bullets,
				"§3Missiles: " + missiles);
			h.change(lines);
			update = false;
		    }
		    if (health <= 0 || bullets <= 0)
			remove();

		}
	    }.runTaskTimer(HospitalCore.inst(), 1, 1);
	    task = t.getTaskId();

	}
    }

    @EventHandler
    public void onTurretMove(VehicleMoveEvent e) {
	for (Turret turret : TURRETS)
	    if (e.getVehicle().equals(turret.getMinecart())) {
		Location to = e.getTo();
		Location from = e.getFrom();
		if (from.getZ() != to.getZ() || from.getX() != to.getX()) {
		    e.getVehicle().teleport(turret.getInitialMinecartLoc());
		    e.getVehicle().setFallDistance(0);
		}
		break;
	    }
    }

    @EventHandler
    public void onTurretEnter(VehicleEnterEvent e) {
	for (Turret turret : TURRETS)
	    if (turret.getMinecart().equals(e.getVehicle()))
		if (e.getEntered() instanceof Player
			&& !((Player) e.getEntered()).getName().equals(turret.getOwner())) {
		    e.setCancelled(true);
		    break;
		}
    }

    @EventHandler
    public void onTurretDamage(VehicleDamageEvent e) {

	if (!(e.getVehicle() instanceof Minecart))
	    return;

	Minecart cart = (Minecart) e.getVehicle();
	for (Turret turret : TURRETS) {
	    if (turret.getMinecart().equals(cart)) {
		e.setCancelled(true);
		turret.setHealth(turret.getHealth() - 1);
		break;
	    }
	}
    }

    @Override
    public long getCooldown() {
	return AUTOMATION_TURRET;
    }

}
