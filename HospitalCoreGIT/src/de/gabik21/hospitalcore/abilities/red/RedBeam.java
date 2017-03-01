package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Beam;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class RedBeam extends Ability {

    final static long REDBEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.REDBEAM) != p.getItemInHand().getType() || pd.isOnCooldown(Kit.REDBEAM))
	    return;

	pd.useKit(Kit.REDBEAM);

	Beam redbeam = new Beam(p, Material.REDSTONE_BLOCK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.setFireTicks(10 * 20);
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }
	};

	redbeam.eject();

    }

    @Override
    public long getCooldown() {
	return REDBEAM_COOLDOWN;
    }

}
