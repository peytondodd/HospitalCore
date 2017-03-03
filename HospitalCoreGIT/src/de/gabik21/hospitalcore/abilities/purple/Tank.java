package de.gabik21.hospitalcore.abilities.purple;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;

public class Tank extends Ability {

    @Override
    public void onKill(PlayerDeathEvent e) {

	Player t = e.getEntity();
	final World w = t.getWorld();
	final Location loc = t.getLocation();
	final List<ItemStack> drops = new ArrayList<ItemStack>(e.getDrops());

	e.getDrops().clear();
	w.createExplosion(t.getLocation().getX(), t.getLocation().getY(), loc.getZ(), 5.5F, false, false);

	new BukkitRunnable() {

	    public void run() {

		for (ItemStack stack : drops)
		    w.dropItemNaturally(loc, stack);

	    }
	}.runTaskLater(HospitalCore.inst(), 30);

    }

    @Override
    public void onDamage(EntityDamageEvent e) {

	if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION)
	    e.setCancelled(true);

    }
}
