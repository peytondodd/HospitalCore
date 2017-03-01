package de.gabik21.hospitalcore.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.util.GrapplerRod;

public class Grappler extends Ability {

    private Map<Player, GrapplerRod> hooks = new HashMap<Player, GrapplerRod>();

    @Override
    public void onInteract(PlayerInteractEvent e) {
	Player p = e.getPlayer();

	if (p.getItemInHand().getType() != HospitalCore.getData(p).getKitConfig().getKitItem(Kit.GRAPPLER))
	    return;

	if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

	    if (this.hooks.containsKey(p)) {
		this.hooks.get(p).remove();
		this.hooks.remove(p);
	    }

	    GrapplerRod rod = new GrapplerRod(p.getWorld(), ((CraftPlayer) p).getHandle());
	    rod.spawn(p.getEyeLocation());
	    this.hooks.put(p, rod);

	} else if (this.hooks.containsKey(p) && this.hooks.get(p).isHooked()) {

	    grapple(e.getPlayer());
	}
    }

    @Override
    public void onItemSwitch(PlayerItemHeldEvent e) {
	if (this.hooks.containsKey(e.getPlayer())) {
	    this.hooks.get(e.getPlayer()).remove();
	    this.hooks.remove(e.getPlayer());
	}
    }

    private void grapple(Player p) {

	double d = (this.hooks.get(p)).getBukkitEntity().getLocation().distance(p.getLocation());
	double t = d;
	double v_x = (0.8D + 0.07D * t)
		* (((GrapplerRod) this.hooks.get(p)).getBukkitEntity().getLocation().getX() - p.getLocation().getX())
		/ t;
	double v_y = (0.9D + 0.01D * t)
		* (((GrapplerRod) this.hooks.get(p)).getBukkitEntity().getLocation().getY() - p.getLocation().getY())
		/ t;
	double v_z = (0.8D + 0.07D * t)
		* (((GrapplerRod) this.hooks.get(p)).getBukkitEntity().getLocation().getZ() - p.getLocation().getZ())
		/ t;

	Vector v = p.getVelocity();
	v.setX(v_x);
	v.setY(v_y);
	v.setZ(v_z);
	p.setVelocity(v);
	p.setFallDistance(0);

	p.getWorld().playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);

    }

    @Override
    public void onDamage(EntityDamageEvent e) {

	Damageable d = (Damageable) e.getEntity();

	if (d.getHealth() - e.getDamage() <= 0 && !e.isCancelled() && this.hooks.containsKey(d)) {
	    this.hooks.get((Player) e.getEntity()).remove();
	    this.hooks.remove((Player) e.getEntity());
	}
    }
}
