package de.gabik21.hospitalcore.types;

import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;

public class Beam {

    static double BEAM_RANGE = 1.0;

    private Player owner;
    private Material mat;
    private Vector vec;
    private Item item;

    public Beam(Player owner, Material mat) {

	this.owner = owner;
	this.mat = mat;
	this.vec = owner.getEyeLocation().getDirection();
	this.item = owner.getWorld().dropItem(owner.getEyeLocation(), new ItemStack(mat));
	this.item.setPickupDelay(Integer.MAX_VALUE);

    }

    @SuppressWarnings("deprecation")
    public void eject() {

	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.item.getEntityId());
	for (Player p : Bukkit.getOnlinePlayers())
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

	new BukkitRunnable() {

	    boolean softcancel = false;
	    boolean neo = false;

	    public void run() {

		if (!owner.isOnline() || item.isDead() || item.isOnGround()) {

		    item.remove();
		    cancel();
		    return;
		}

		softcancel = checkforWall(item);

		for (Entity e : item.getNearbyEntities(BEAM_RANGE, BEAM_RANGE, BEAM_RANGE)) {

		    if (!(e instanceof Player))
			continue;

		    Player hit = (Player) e;
		    PlayerData hd = HospitalCore.getData(hit);

		    if ((hit == owner && !neo) || !hd.isIngame() || hd.isInAdminmode())
			continue;

		    if (hd.getKitConfig() != null && hd.getKitConfig().contains(Kit.NEO)) {

			vec = vec.multiply(-1);
			neo = true;
			continue;
		    }

		    onHit(hit, item);
		    softcancel = true;

		}

		if (softcancel) {
		    cancel();
		    item.remove();
		}

		item.setVelocity(vec);
		item.getWorld().playEffect(item.getLocation(), Effect.STEP_SOUND, mat);
		item.getWorld().playSound(item.getLocation(), Sound.STEP_STONE, 1, 1);

	    }

	    private boolean checkforWall(Item item) {

		Block itemb = item.getLocation().getBlock();

		if (itemb.getRelative(BlockFace.UP).getType().isSolid()
			|| itemb.getRelative(BlockFace.UP).getType() == Material.GLASS)
		    return true;

		if (itemb.getRelative(BlockFace.NORTH).getType().isSolid()
			|| itemb.getRelative(BlockFace.NORTH).getType() == Material.GLASS)
		    return true;

		if (itemb.getRelative(BlockFace.SOUTH).getType().isSolid()
			|| itemb.getRelative(BlockFace.SOUTH).getType() == Material.GLASS)
		    return true;

		if (itemb.getRelative(BlockFace.WEST).getType().isSolid()
			|| itemb.getRelative(BlockFace.WEST).getType() == Material.GLASS)
		    return true;

		if (itemb.getRelative(BlockFace.EAST).getType().isSolid()
			|| itemb.getRelative(BlockFace.EAST).getType() == Material.GLASS)
		    return true;

		return false;
	    }
	}.runTaskTimer(HospitalCore.inst(), 0, 1);

    }

    public void onHit(Player hit, Item item) {
    }

}
