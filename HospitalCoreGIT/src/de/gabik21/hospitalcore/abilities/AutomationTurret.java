package de.gabik21.hospitalcore.abilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.util.Hologram;

public class AutomationTurret extends Ability implements Listener {

    private static final long AUTOMATION_TURRET = 90_000;
    // private static final Set<Turret> TURRETS = new HashSet<>();

    @Override
    public void activate(Player p) {

	// PlayerData pd = HospitalCore.getData(p);

	// if (p.getItemInHand().getType() !=
	// pd.getKitConfig().getKitItem(Kit.AUTOMATIONTURRET)
	// || pd.isOnCooldown(Kit.AUTOMATIONTURRET))
	// return;
	//
	// for (Turret turret : TURRETS)
	// if (turret.getOwner().equals(p.getName()))
	// return;
	//
	// pd.useKit(Kit.AUTOMATIONTURRET);

    }

    class Turret {

	String owner;
	Minecart minecart;
	int health = 20, bullets = 120, missiles = 15, task;
	boolean update;

	Turret(String owner, Minecart minecart) {

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

	public void remove() {

	    Bukkit.getScheduler().cancelTask(task);

	}

	public void init() {

	    final Hologram h = new Hologram(Arrays.asList("§cHealth: " + health));
	    h.show(minecart.getLocation().add(0, 1, 0));
	    h.addLine("§cBullets: " + bullets);
	    h.addLine("§3Missiles: " + missiles);

	    BukkitTask t = new BukkitRunnable() {
		@Override
		public void run() {
		    if (update) {
			List<String> lines = Arrays.asList("§cHealth: " + health, "§cBullets: ",
				"§3Missiles" + missiles);
			h.change(lines);
			update = false;
		    }

		}
	    }.runTaskTimer(HospitalCore.inst(), 1, 1);
	    task = t.getTaskId();

	}
    }

    @Override
    public long getCooldown() {
	return AUTOMATION_TURRET;
    }

}
