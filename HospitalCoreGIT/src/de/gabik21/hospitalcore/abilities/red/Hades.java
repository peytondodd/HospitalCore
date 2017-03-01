package de.gabik21.hospitalcore.abilities.red;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Hades extends Ability {

    private static final long HADES_COOLDOWN = 45000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.HADES) || pd.isOnCooldown(Kit.HADES))
	    return;

	final Set<Wolf> minions = new HashSet<Wolf>();
	pd.useKit(Kit.HADES);

	for (int i = 0; i <= 10; i++) {
	    Wolf w = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
	    w.setTamed(true);
	    w.setOwner(p);
	    w.setHealth(1D);
	    w.setMaxHealth(1D);
	    w.setSitting(false);
	    w.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

	    w.setCustomName(pd.getNick() + "'s wolf");
	    w.setCustomNameVisible(true);

	    minions.add(w);
	}

	new BukkitRunnable() {
	    public void run() {

		for (Wolf w : minions)
		    if (w.isValid())
			w.remove();
	    }
	}.runTaskLater(HospitalCore.inst(), 10 * 20);

    }

    @Override
    public long getCooldown() {
	return HADES_COOLDOWN;
    }

}
