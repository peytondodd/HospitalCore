package de.gabik21.hospitalcore.abilities.purple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
// import org.bukkit.event.block.BlockDamageEvent;
// import org.bukkit.event.entity.PlayerDeathEvent;
// import org.bukkit.event.player.PlayerInteractEntityEvent;
// import org.bukkit.inventory.ItemStack;
// import org.bukkit.potion.PotionEffect;
// import org.bukkit.potion.PotionEffectType;
// import org.bukkit.scheduler.BukkitRunnable;
//
// import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Gladiator extends Ability implements Listener {

    static final long GLADIATOR_COOLDOWN = 40000;
    static final Map<String, Location> GLADIATORLOCS = new HashMap<String, Location>();

    @EventHandler
    public void onBlockDamage(final BlockDamageEvent e) {

	Player p = e.getPlayer();
	final PlayerData pd = HospitalCore.getData(p);

	if (GLADIATORLOCS.containsKey(p.getName()) && e.getBlock() != null) {
	    pd.addFakeBlock(e.getBlock().getLocation(), Material.BEDROCK);

	    e.setCancelled(true);

	    new BukkitRunnable() {
		public void run() {
		    pd.removeFakeBlock(e.getBlock().getLocation(), e.getBlock().getType());
		}
	    }.runTaskLater(HospitalCore.inst(), 20);
	}

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {

	Player p = e.getEntity().getKiller();
	PlayerData pd = HospitalCore.getData(p);
	Player t = (Player) e.getEntity();

	if (GLADIATORLOCS.containsKey(t.getName())) {

	    if (p != null && GLADIATORLOCS.containsKey(p.getName())) {
		final List<ItemStack> drops = new ArrayList<ItemStack>(e.getDrops());
		e.getDrops().clear();

		if (p.isOnline()) {

		    p.teleport(GLADIATORLOCS.get(p.getName()).add(0, 1, 0));
		    for (ItemStack drop : drops)
			p.getWorld().dropItemNaturally(GLADIATORLOCS.get(t.getName()), drop);
		    GLADIATORLOCS.remove(p.getName());
		}

		pd.setIn1v1(false);

	    }
	    GLADIATORLOCS.remove(t.getName());

	}

    }

    @Override
    public void onKill(PlayerDeathEvent e) {
	HospitalCore.getData(e.getEntity().getKiller()).useKit(Kit.GLADIATOR);
    }

    @Override
    public void onInteractEntity(PlayerInteractEntityEvent e) {

	if (!(e.getRightClicked() instanceof Player))
	    return;

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);
	Player t = (Player) e.getRightClicked();
	PlayerData td = HospitalCore.getData(t);

	Location loc = p.getLocation().clone().add(0, 35, 0);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.GLADIATOR) || pd.isIn1v1())
	    return;

	if (pd.isOnCooldown(Kit.GLADIATOR))
	    return;

	if (!createArena(loc, Material.STAINED_GLASS)) {
	    p.sendMessage("§cNo space above!");
	    return;
	}

	td.setIn1v1(true);
	pd.setIn1v1(true);

	GLADIATORLOCS.put(p.getName(), p.getLocation());
	GLADIATORLOCS.put(t.getName(), t.getLocation());

	startFight(loc, p, t);

    }

    private void startFight(final Location loc, final Player p, final Player t) {

	PotionEffect pot = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 150, 4);

	Location ploc = loc.clone().add(-7, 4, -7);
	Location tloc = loc.clone().add(7, 4, 7);
	ploc.setYaw(-45);
	tloc.setYaw(133);
	p.teleport(ploc);
	p.addPotionEffect(pot);
	t.teleport(tloc);
	t.addPotionEffect(pot);

	new BukkitRunnable() {
	    int running = 0;

	    public void run() {

		if (!GLADIATORLOCS.containsKey(p.getName()) && !GLADIATORLOCS.containsKey(t.getName())) {
		    createArena(loc, Material.AIR);
		    cancel();
		    return;
		}

		if (!GLADIATORLOCS.containsKey(t.getName())) {

		    p.teleport(GLADIATORLOCS.get(p.getName()));
		    GLADIATORLOCS.remove(p.getName());
		    createArena(loc, Material.AIR);
		    cancel();
		    return;
		}

		if (!GLADIATORLOCS.containsKey(p.getName())) {

		    t.teleport(GLADIATORLOCS.get(t.getName()));
		    GLADIATORLOCS.remove(t.getName());
		    createArena(loc, Material.AIR);
		    cancel();
		    return;
		}

		if (!p.isOnline() && !t.isOnline()) {

		    GLADIATORLOCS.remove(p.getName());
		    GLADIATORLOCS.remove(t.getName());
		    createArena(loc, Material.AIR);
		    cancel();
		    return;

		}

		if (!t.isOnline()) {

		    createArena(loc, Material.AIR);
		    p.teleport(GLADIATORLOCS.get(p.getName()));
		    GLADIATORLOCS.remove(p.getName());
		    cancel();
		    return;

		}

		if (!p.isOnline()) {

		    t.teleport(GLADIATORLOCS.get(t.getName()));
		    GLADIATORLOCS.remove(t.getName());
		    createArena(loc, Material.AIR);
		    cancel();
		    return;
		}

		if (running >= 120) {

		    t.teleport(GLADIATORLOCS.get(t.getName()));
		    p.teleport(GLADIATORLOCS.get(p.getName()));
		    GLADIATORLOCS.remove(t.getName());
		    GLADIATORLOCS.remove(p.getName());
		    PlayerData pd = HospitalCore.getData(p);
		    PlayerData td = HospitalCore.getData(t);
		    pd.setIn1v1(false);
		    td.setIn1v1(false);
		    createArena(loc, Material.AIR);
		    cancel();

		}

		running++;

	    }
	}.runTaskTimer(HospitalCore.inst(), 0, 20);

    }

    private boolean createArena(Location loc, Material m) {

	if (m != Material.AIR) {

	    for (int x = -10; x <= 10; x++)
		for (int z = -10; z <= 10; z++)
		    for (int y = 0; y <= 8; y++) {

			Location loc2 = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
			if (y != 8 && y != 0) {
			    if (z == -10 || z == 10 || x == 10 || x == -10)
				if (loc2.getBlock().getType() != Material.AIR)
				    return false;
				else if (loc2.getBlock().getType() != Material.AIR)
				    return false;
			} else if (loc2.getBlock().getType() != Material.AIR)
			    return false;
		    }
	}

	for (int x = -10; x <= 10; x++)
	    for (int z = -10; z <= 10; z++)
		for (int y = 0; y <= 8; y++) {

		    Location loc2 = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
		    if (y != 8 && y != 0) {
			if (z == -10 || z == 10 || x == 10 || x == -10)
			    loc2.getBlock().setType(m);
			else
			    loc2.getBlock().setType(Material.AIR);
		    } else
			loc2.getBlock().setType(m);

		}

	return true;

    }

    @Override
    public long getCooldown() {
	return GLADIATOR_COOLDOWN;
    }

}
