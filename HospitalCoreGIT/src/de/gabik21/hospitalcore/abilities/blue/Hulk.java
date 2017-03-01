package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Hulk extends Ability {

    final static long HULK_COOLDOWN = 6000;

    @Override
    public void onInteractEntity(PlayerInteractEntityEvent e) {

	if (!(e.getRightClicked() instanceof Player))
	    return;

	Player t = (Player) e.getRightClicked();
	Player p = e.getPlayer();

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() == Material.AIR && p.isEmpty() && t.getPassenger() != p && !p.isDead()
		&& !t.isDead()) {

	    if (pd.isOnCooldown(Kit.HULK))
		return;

	    pd.useKit(Kit.HULK);

	    PlayerData td = HospitalCore.getData(t);
	    if (!td.isIngame())
		return;
	    p.setPassenger(t);
	}

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {

	Player p = e.getPlayer();

	if (e.getAction() == Action.LEFT_CLICK_AIR && !p.isEmpty()) {

	    Player t = (Player) p.getPassenger();

	    p.eject();

	    Vector handle = p.getEyeLocation().getDirection();
	    handle.multiply(0.8F);
	    handle.setY(0.4D);
	    t.setVelocity(handle);

	}

    }

    @Override
    public long getCooldown() {
	return HULK_COOLDOWN;
    }

}
