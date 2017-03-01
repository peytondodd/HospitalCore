package de.gabik21.hospitalcore.abilities.red;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Napalm extends Ability implements Listener {

    private static final long NAPALM_COOLDOWN = 60000;
    private static final Set<FireField> fields = new HashSet<Napalm.FireField>();

    @Override
    public void activate(Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.NAPALM) || pd.isOnCooldown(Kit.NAPALM))
	    return;

	pd.useKit(Kit.NAPALM);
	Fireball ball = p.launchProjectile(Fireball.class);
	ball.setVelocity(ball.getVelocity().multiply(1.5));
	ball.setIsIncendiary(false);
	ball.setMetadata("napalm", new FixedMetadataValue(HospitalCore.inst(), p.getName()));

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {

	if (e.getEntityType() != EntityType.FIREBALL)
	    return;

	if (!e.getEntity().hasMetadata("napalm"))
	    return;

	String owner = e.getEntity().getMetadata("napalm").get(0).asString();

	for (FireField f : fields) {
	    if (f.getOwner().equals(owner))
		return;
	}

	final FireField field = new FireField(e.getEntity().getLocation(), owner);
	fields.add(field);

	new BukkitRunnable() {
	    public void run() {

		field.clear();
		fields.remove(field);

	    }
	}.runTaskLater(HospitalCore.inst(), 15 * 20);

	e.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = false)
    public void onDamage(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	if (e.getCause() != DamageCause.FIRE && e.getCause() != DamageCause.FIRE_TICK)
	    return;

	if (e.getEntity().getLocation().getBlock().getType() != Material.FIRE)
	    return;

	for (FireField ff : fields) {

	    if (ff.contains(e.getEntity().getLocation().getBlock().getLocation())) {
		if (!ff.getOwner().equals(((Player) e.getEntity()).getName())) {
		    e.setDamage(e.getDamage() + 3D);
		}
		break;
	    }

	}

    }

    class FireField {

	String owner;
	Set<Location> blocks;

	public FireField(Location loc, String owner) {

	    this.owner = owner;
	    blocks = new HashSet<Location>(100);

	    World world = loc.getWorld();

	    int X = loc.getBlockX();
	    int Y = loc.getBlockY();
	    int Z = loc.getBlockZ();

	    for (int x = -5; x <= 5; x++) {
		for (int z = -5; z <= 5; z++) {
		    Block block = setOnFire(world, X + x, Y, Z + z, 0);
		    if (block != null)
			blocks.add(block.getLocation());
		}
	    }

	}

	public String getOwner() {
	    return owner;
	}

	public Set<Location> getBlocks() {
	    return blocks;
	}

	public boolean contains(Location loc) {
	    return blocks.contains(loc.getBlock().getLocation());
	}

	public void clear() {

	    for (Location loc : blocks)
		if (loc.getBlock().getType() == Material.FIRE)
		    loc.getBlock().setType(Material.AIR);
	    blocks.clear();

	}

	private Block setOnFire(World world, int x, int y, int z, int counter) {

	    if (counter > 4)
		return null;

	    if (!world.getBlockAt(x, y - 1, z).getType().isSolid()) {
		return setOnFire(world, x, y - 1, z, counter + 1);
	    }

	    if (world.getBlockAt(x, y, z).getType() != Material.AIR) {
		return setOnFire(world, x, y + 1, z, counter + 1);
	    }

	    world.getBlockAt(x, y, z).setType(Material.FIRE);
	    return world.getBlockAt(x, y, z);
	}

    }

    @Override
    public long getCooldown() {
	return NAPALM_COOLDOWN;
    }

}
