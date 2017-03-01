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

public class BlueBeam extends Ability {

    final static long BLUE_BEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.BLUEBEAM) != p.getItemInHand().getType() || pd.isOnCooldown(Kit.BLUEBEAM))
	    return;

	pd.useKit(Kit.BLUEBEAM);

	Beam bluebeam = new Beam(p, Material.LAPIS_BLOCK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6 * 20, 10));
		hit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6 * 20, 200));
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }
	};

	bluebeam.eject();

    }

    @Override
    public long getCooldown() {
	return BLUE_BEAM_COOLDOWN;
    }
}
