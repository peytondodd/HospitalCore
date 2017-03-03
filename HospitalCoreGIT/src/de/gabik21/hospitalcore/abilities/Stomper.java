package de.gabik21.hospitalcore.abilities;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Stomper extends Ability {

    final static double STOMPER_RANGE = 3.5;

    @Override
    public void onDamage(EntityDamageEvent e) {

	Player p = (Player) e.getEntity();

	if (e.getCause() != DamageCause.FALL || e.isCancelled())
	    return;

	e.setCancelled(true);

	World w = p.getWorld();

	w.playSound(p.getLocation(), Sound.ANVIL_LAND, 5.0F, 5.0F);
	w.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);

	List<Entity> ents = p.getNearbyEntities(STOMPER_RANGE, STOMPER_RANGE, STOMPER_RANGE);

	for (Entity et : ents) {

	    if (!(et instanceof Player))
		continue;

	    Player t = (Player) et;
	    PlayerData td = HospitalCore.getData(t);
	    w.playEffect(t.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);

	    if (td.getKitConfig().contains(Kit.ANTISTOMPER)) {
		p.damage(e.getDamage(), t);
		continue;
	    }

	    if (t.isSneaking())
		t.damage(1D, p);
	    else
		t.damage(e.getDamage(), p);

	}

	if (e.isCancelled()) {

	    if (e.getDamage() >= 4D)
		p.damage(4D);
	    else
		p.damage(e.getDamage());
	    p.setLastDamageCause(e);

	}

    }

}
