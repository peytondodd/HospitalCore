package de.gabik21.hospitalcore.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.util.Inventories;

public class Phoenix extends Ability {

    private List<String> phoenixed = new ArrayList<String>();

    @Override
    public void onDamage(EntityDamageEvent e) {

	Damageable d = (Damageable) e.getEntity();

	if (!e.isCancelled() && d.getHealth() - e.getDamage() <= 0
		&& !phoenixed.remove(((Player) e.getEntity()).getName())) {

	    phoenixed.add(((Player) e.getEntity()).getName());
	    d.setHealth(20D);
	    Inventories.RC32.apply(((Player) e.getEntity()));

	    e.setCancelled(true);
	    for (Entity ent : d.getNearbyEntities(50, 50, 50)) {
		if (ent instanceof Player) {
		    Player nearby = (Player) ent;
		    nearby.sendMessage("§4§lBe a witness in reincarnation of the mighty phoenix you're united with");

		}
	    }

	}

    }

    @Override
    public void onKill(PlayerDeathEvent e) {
	phoenixed.remove(e.getEntity().getName());
    }

}
