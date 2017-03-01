package de.gabik21.hospitalcore.abilities;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class SentryGun extends Ability {

    private static final long SENTRYGUN_COOLDOWN = 40000;

    @Override
    public void activate(final Player p) {

	PlayerData pd = HospitalCore.getData(p);
	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.SENTRYGUN)
		|| pd.isOnCooldown(Kit.SENTRYGUN))
	    return;

	pd.useKit(Kit.SENTRYGUN);

	new BukkitRunnable() {
	    int i = 0;

	    public void run() {
		if (i >= 65 || !p.isOnline() || p.isDead()) {
		    cancel();
		    return;
		}
		i++;
		p.launchProjectile(Arrow.class).teleport(p.getEyeLocation().add(0, 0.25, 0));
	    }
	}.runTaskTimer(HospitalCore.inst(), 0, 2);

    }

    @Override
    public long getCooldown() {
	return SENTRYGUN_COOLDOWN;
    }

}
