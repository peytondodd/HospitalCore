package de.gabik21.hospitalcore.types;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class PerKitCooldown {

    private static final long GLOBAL_USAGE_COOLDOWN = 3000;
    private static final DecimalFormat COOLDOWN_FORMAT = new DecimalFormat("#0.0");

    private long globalcooldown;
    private Map<Kit, Long> kitCooldowns = new HashMap<Kit, Long>();

    public boolean isOnCooldown(Player p, Kit kit) {

	if (kitCooldowns.containsKey(kit)
		&& (System.currentTimeMillis() - kitCooldowns.get(kit)) < kit.getCooldown(p)) {
	    sendKitCooldown(p, kit);
	    return true;
	}
	if ((System.currentTimeMillis() - globalcooldown) < GLOBAL_USAGE_COOLDOWN) {
	    sendGlobalCooldown(p);
	    return true;
	}

	return false;
    }

    public void useKit(Kit kit) {
	globalcooldown = System.currentTimeMillis();
	kitCooldowns.remove(kit);
	kitCooldowns.put(kit, System.currentTimeMillis());
    }

    public void reset() {
	kitCooldowns.clear();
	globalcooldown = 0;
    }

    private void sendKitCooldown(Player p, Kit kit) {
	long l = kit.getCooldown(p) - (System.currentTimeMillis() - kitCooldowns.get(kit));
	double display = ((double) l) / 1000;
	p.sendMessage("§cYour ability " + kit.getName() + " is still on cooldown : " + COOLDOWN_FORMAT.format(display));
    }

    private void sendGlobalCooldown(Player p) {
	long l = GLOBAL_USAGE_COOLDOWN - (System.currentTimeMillis() - globalcooldown);
	double display = ((double) l) / 1000;
	p.sendMessage("§cYou're still on global cooldown: " + COOLDOWN_FORMAT.format(display));
    }

}
