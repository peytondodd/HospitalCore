package de.gabik21.hospitalcore.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Beam;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class NetherBeam extends Ability {

    final static long NETHER_BEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.NETHERBEAM)
		|| pd.isOnCooldown(Kit.NETHERBEAM))
	    return;

	pd.useKit(Kit.NETHERBEAM);

	Beam netherbeam = new Beam(p, Material.NETHERRACK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(8D);
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }

	};

	netherbeam.eject();

    }

    @Override
    public long getCooldown() {
	return NETHER_BEAM_COOLDOWN;
    }

}
