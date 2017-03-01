package de.gabik21.hospitalcore.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Hedgehog extends Ability {

    static final long HEDGEHOG_COOLDOWN = 40000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.HEDGEHOG) != p.getItemInHand().getType() || pd.isOnCooldown(Kit.HEDGEHOG))
	    return;

	pd.useKit(Kit.HEDGEHOG);

	Location loc = p.getEyeLocation();
	final Map<Arrow, Vector> arrows = new HashMap<Arrow, Vector>();

	for (int yaw = -180; yaw <= 180; yaw += 10)
	    for (int pitch = -75; pitch <= 10; pitch += 10) {

		Arrow a = p.launchProjectile(Arrow.class);

		a.setKnockbackStrength(4);
		a.setMetadata("hedge", new FixedMetadataValue(HospitalCore.inst(), true));

		loc.setYaw(yaw);
		loc.setPitch(pitch);

		Vector handle = loc.getDirection().multiply(0.5);
		arrows.put(a, handle);
		a.setVelocity(handle);

	    }

	new BukkitRunnable() {
	    int running = 0;
	    Map<Arrow, Location> arrowsloc = new HashMap<Arrow, Location>();

	    public void run() {

		if (running >= 8 * 20) {

		    for (Arrow a : arrows.keySet()) {
			a.remove();
		    }
		    cancel();
		    return;
		}

		if (running == 10) {

		    for (Arrow a : arrows.keySet()) {
			arrowsloc.put(a, a.getLocation());
		    }

		}

		for (Arrow a : arrows.keySet()) {

		    if (a == null || arrows.get(a) == null)
			continue;
		    if (a.isOnGround()) {
			a.remove();
			continue;
		    }
		    if (!a.isValid() || a.hasMetadata("neo") || a.isDead())
			continue;

		    if (running >= 10) {
			a.teleport(arrowsloc.get(a).add(0, 0.03, 0));

		    } else
			a.setVelocity(arrows.get(a));

		}

		running++;

	    }
	}.runTaskTimer(HospitalCore.inst(), 1, 1);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onProjectileHit(final EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Arrow) || !e.getDamager().hasMetadata("hedge")
		|| !(e.getEntity() instanceof Player))
	    return;

	if (((Arrow) e.getDamager()).getShooter() == e.getEntity()) {
	    e.setCancelled(true);
	    return;
	}

	e.setDamage(3D);

    }

    @Override
    public long getCooldown() {
	return HEDGEHOG_COOLDOWN;
    }
}
