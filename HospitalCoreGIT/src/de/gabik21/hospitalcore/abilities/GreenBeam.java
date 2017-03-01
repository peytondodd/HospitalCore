package de.gabik21.hospitalcore.abilities;

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

public class GreenBeam extends Ability {

    final static long GREEN_BEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.GREENBEAM) != p.getItemInHand().getType()
		|| pd.isOnCooldown(Kit.GREENBEAM))
	    return;

	pd.useKit(Kit.GREENBEAM);

	Beam greenbeam = new Beam(p, Material.EMERALD_BLOCK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1));
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }

	};

	greenbeam.eject();

    }

    @Override
    public long getCooldown() {
	return GREEN_BEAM_COOLDOWN;
    }
}
