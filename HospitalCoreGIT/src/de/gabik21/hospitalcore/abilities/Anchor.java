package de.gabik21.hospitalcore.abilities;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;

public class Anchor extends Ability {

    @Override
    public void onPlayerDamagedByPlayer(final EntityDamageByEntityEvent e) {

	if (((LivingEntity) e.getEntity())
		.getNoDamageTicks() > ((LivingEntity) e.getDamager()).getMaximumNoDamageTicks() / 2D || e.isCancelled())
	    return;

	new BukkitRunnable() {

	    public void run() {
		e.getEntity().setVelocity(new Vector(0, 0, 0));
	    }
	}.runTaskLater(HospitalCore.inst(), 1);

	((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.ANVIL_LAND, 0.1F, 0.1F);
	((Player) e.getDamager()).playSound(e.getEntity().getLocation(), Sound.ANVIL_LAND, 0.1F, 0.1F);
    }

    @Override
    public void onPlayerAttack(final EntityDamageByEntityEvent e) {

	if (((LivingEntity) e.getEntity())
		.getNoDamageTicks() > ((LivingEntity) e.getDamager()).getMaximumNoDamageTicks() / 2D || e.isCancelled())
	    return;

	new BukkitRunnable() {

	    public void run() {
		e.getEntity().setVelocity(new Vector(0, 0, 0));
	    }
	}.runTaskLater(HospitalCore.inst(), 1);

	((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.ANVIL_LAND, 0.1F, 0.1F);
	((Player) e.getDamager()).playSound(e.getEntity().getLocation(), Sound.ANVIL_LAND, 0.1F, 0.1F);
    }

}
