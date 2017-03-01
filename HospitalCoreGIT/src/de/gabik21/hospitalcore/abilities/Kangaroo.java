package de.gabik21.hospitalcore.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;

public class Kangaroo extends Ability {

    private List<String> inair = new ArrayList<String>();

    @Override
    public void onDamage(EntityDamageEvent e) {

	if (e.getCause() != DamageCause.FALL)
	    return;

	if (e.getDamage() > 4)
	    e.setDamage(4D);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();

	if (!inair.contains(p.getName()) || !p.isOnGround())
	    return;
	inair.remove(p.getName());

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {

	Player p = e.getPlayer();

	if (e.getAction() == Action.PHYSICAL
		|| p.getItemInHand().getType() != HospitalCore.getData(p).getKitConfig().getKitItem(Kit.KANGAROO))
	    return;

	e.setCancelled(true);

	if (inair.contains(p.getName()))
	    return;

	Vector handle = p.getEyeLocation().getDirection();

	if (!p.isSneaking()) {

	    handle.multiply(0);
	    handle.setY(1F);
	    p.setVelocity(handle);

	} else {

	    handle.multiply(1.5F);
	    handle.setY(0.5F);
	    p.setVelocity(handle);

	}

	inair.add(p.getName());

    }
}
