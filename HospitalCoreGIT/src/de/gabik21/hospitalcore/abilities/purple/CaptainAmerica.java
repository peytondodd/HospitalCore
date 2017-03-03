package de.gabik21.hospitalcore.abilities.purple;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;
import net.minecraft.util.com.google.common.util.concurrent.AtomicDouble;

public class CaptainAmerica extends Ability {

    private static final long CAPTAIN_AMERICA_COOLDOWN = 20000;
    private Map<String, AtomicDouble> damage = new HashMap<String, AtomicDouble>();

    @Override
    public void onDamage(EntityDamageEvent e) {

	Player p = (Player) e.getEntity();
	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.CAPTAINAMERICA)
		|| pd.isOnCooldown(Kit.CAPTAINAMERICA))
	    return;

	Location loc = p.getEyeLocation();

	p.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0.5F, false, false);
	if (!damage.containsKey(p.getName()))
	    damage.put(p.getName(), new AtomicDouble(e.getDamage()));
	else if (damage.get(p.getName()).getAndAdd(e.getDamage()) >= 18D) {
	    pd.useKit(Kit.CAPTAINAMERICA);
	    p.sendMessage("§cYour shield is now on cooldown");
	    damage.remove(p.getName());
	}
	e.setDamage(0D);

    }

    @Override
    public long getCooldown() {
	return CAPTAIN_AMERICA_COOLDOWN;
    }

}
