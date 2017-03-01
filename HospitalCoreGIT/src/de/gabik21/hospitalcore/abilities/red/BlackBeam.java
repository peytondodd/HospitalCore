package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Beam;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class BlackBeam extends Ability {

    final static long BLACK_BEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.BLACKBEAM) != p.getItemInHand().getType()
		|| pd.isOnCooldown(Kit.BLACKBEAM))
	    return;

	pd.useKit(Kit.BLACKBEAM);

	Beam blackbeam = new Beam(p, Material.OBSIDIAN) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 1));
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }
	};

	blackbeam.eject();

    }

    @Override
    public long getCooldown() {
	return BLACK_BEAM_COOLDOWN;
    }

}
