package de.gabik21.hospitalcore.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Assassin extends Ability {

    private static final long ASSASSIN_COOLDOWN = 30000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.ASSASSIN) != p.getItemInHand().getType() || pd.isOnCooldown(Kit.ASSASSIN))
	    return;

	PlayerData td = HospitalCore.getData(pd.getLastDamaged());

	if (pd.getLastDamaged() != null && pd.getLastDamaged().isOnline() && td.isIngame()
		&& (!td.isIn1v1() && !pd.isIn1v1() || pd.isIn1v1() && td.isIn1v1())) {

	    Location loc = pd.getLastDamaged().getLocation().add(0, 7, 0);
	    if (blockAbove(pd.getLastDamaged().getLocation()))
		return;
	    p.teleport(loc);
	    p.setFallDistance(-7);
	    pd.useKit(Kit.ASSASSIN);
	}

    }

    private boolean blockAbove(Location loc) {

	for (int i = 0; i < 8; i++) {
	    if (loc.clone().add(0, i, 0).getBlock().getType() != Material.AIR)
		return true;
	}
	return false;
    }

    @Override
    public long getCooldown() {
	return ASSASSIN_COOLDOWN;
    }

}
