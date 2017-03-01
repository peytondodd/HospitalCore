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

public class WhiteBeam extends Ability {

    final static long WHITE_BEAM_COOLDOWN = 40000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.WHITEBEAM) != p.getItemInHand().getType()
		|| pd.isOnCooldown(Kit.WHITEBEAM))
	    return;

	pd.useKit(Kit.WHITEBEAM);

	Beam whitebeam = new Beam(p, Material.QUARTZ_BLOCK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8 * 20, 2));
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }
	};

	whitebeam.eject();

    }

    @Override
    public long getCooldown() {
	return WHITE_BEAM_COOLDOWN;
    }

}
