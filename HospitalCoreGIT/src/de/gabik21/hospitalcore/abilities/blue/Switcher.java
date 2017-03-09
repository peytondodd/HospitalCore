package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.PlayerData;

public class Switcher extends Ability {

    @Override
    public void onProjectileHit(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Snowball))
	    return;

	@SuppressWarnings("deprecation")
	Player p = (Player) ((Snowball) e.getDamager()).getShooter();
	
	Player t = (Player) e.getEntity();
	PlayerData td = HospitalCore.getData(t);

	if (!td.isIngame())
	    return;

	Location ploc = p.getLocation();
	p.teleport(t, TeleportCause.COMMAND);
	t.teleport(ploc, TeleportCause.COMMAND);

	p.getInventory().addItem(new ItemStack(Material.SNOW_BALL));

    }

}
