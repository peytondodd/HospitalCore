package de.gabik21.hospitalcore.abilities.purple;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Zen extends Ability {

    private static final long ZEN_COOLDOWN = 35000;

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.ZEN) || pd.isOnCooldown(Kit.ZEN))
	    return;

	Player nearest = getNearest(p, 100D);

	if (nearest == null) {
	    p.sendMessage("§cCould not find a nearby player");
	    return;
	}
	pd.useKit(Kit.ZEN);
	p.teleport(nearest);

    }

    public Player getNearest(Player p, double range) {

	double distance = Double.POSITIVE_INFINITY;

	Player target = null;
	for (Entity e : p.getNearbyEntities(range, range, range)) {

	    if (!(e instanceof Player))
		continue;

	    Player current = (Player) e;
	    PlayerData cd = HospitalCore.getData(current);

	    if (!cd.isIngame())
		continue;

	    double distanceto = p.getLocation().distance(e.getLocation());

	    if (distance < distanceto)
		continue;

	    distance = distanceto;
	    target = (Player) e;
	}
	return target;
    }

    @Override
    public long getCooldown() {
	return ZEN_COOLDOWN;
    }

}
