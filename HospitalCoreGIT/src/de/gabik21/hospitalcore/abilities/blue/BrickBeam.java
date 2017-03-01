package de.gabik21.hospitalcore.abilities.blue;

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

public class BrickBeam extends Ability {

    final static long BRICK_BEAM_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.BRICKBEAM) != p.getItemInHand().getType()
		|| pd.isOnCooldown(Kit.BRICKBEAM))
	    return;
	pd.useKit(Kit.BRICKBEAM);

	Beam brickbeam = new Beam(p, Material.SMOOTH_BRICK) {

	    @Override
	    public void onHit(Player hit, Item item) {

		hit.damage(1D);
		hit.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 0));
		hit.setVelocity(item.getVelocity().setY(0.36).multiply(0.5));

	    }

	};

	brickbeam.eject();

    }

    @Override
    public long getCooldown() {
	return BRICK_BEAM_COOLDOWN;
    }

}
