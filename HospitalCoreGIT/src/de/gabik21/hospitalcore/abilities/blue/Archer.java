package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Archer extends Ability {

    @SuppressWarnings("deprecation")
    @Override
    public void onProjectileHit(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Arrow))
	    return;

	Arrow a = (Arrow) e.getDamager();
	Player p = (Player) a.getShooter();

	if (e.getEntity() instanceof Player) {

	    Player t = (Player) e.getEntity();
	    PlayerData td = HospitalCore.getData(t);

	    if (td.getKitConfig().contains(Kit.NEO))
		e.setCancelled(true);
	    else
		p.getInventory().addItem(new ItemStack(Material.ARROW));

	}

    }

}
