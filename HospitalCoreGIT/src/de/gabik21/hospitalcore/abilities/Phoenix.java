package de.gabik21.hospitalcore.abilities;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.Inventories;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public class Phoenix extends Ability {

    private Set<String> phoenixed = new HashSet<String>();

    @Override
    public void onDamage(EntityDamageEvent e) {

	Damageable d = (Damageable) e.getEntity();
	Player p = (Player) e.getEntity();
	PlayerData pd = HospitalCore.getData(p);

	if (!e.isCancelled() && d.getHealth() - e.getDamage() <= 0
		&& !phoenixed.remove(((Player) e.getEntity()).getName())) {

	    phoenixed.add(((Player) e.getEntity()).getName());
	    d.setHealth(20D);
	    Inventories.RC32.apply(((Player) e.getEntity()));

	    e.setCancelled(true);
	    for (Entity ent : d.getNearbyEntities(50, 50, 50)) {
		if (ent instanceof Player) {
		    Player nearby = (Player) ent;
		    nearby.sendMessage("§4§lBe a witness in reincarnation of the mighty phoenix you're united with");

		}
	    }

	    new BukkitRunnable() {

		double height = 0;
		double radius = 10;

		double px = p.getLocation().getX();
		double py = p.getLocation().getY();
		double pz = p.getLocation().getZ();

		@SuppressWarnings("deprecation")
		@Override
		public void run() {

		    if (!pd.isIngame()) {
			cancel();
			return;
		    }

		    if (radius > 0) {

			radius = radius - 0.1;
			height = height + 0.1;

			double y = height;
			double x = radius * Math.sin(y);
			double z = radius * Math.cos(y);

			Location loc = new Location(p.getWorld(), px + x, py + y, pz + z);
			Location loc2 = new Location(p.getWorld(), px - z, py + y, pz + x);
			Location loc3 = new Location(p.getWorld(), px - x, py + y, pz - z);
			Location loc4 = new Location(p.getWorld(), px + z, py + y, pz - x);

			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("fireworksSpark",
				(float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles("fireworksSpark",
				(float) (loc2.getX()), (float) (loc2.getY()), (float) (loc2.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles("fireworksSpark",
				(float) (loc3.getX()), (float) (loc3.getY()), (float) (loc3.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles("fireworksSpark",
				(float) (loc4.getX()), (float) (loc4.getY()), (float) (loc4.getZ()), 0, 0, 0, 0, 1);
			
			for (Player online : Bukkit.getOnlinePlayers()) {
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet2);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet3);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet4);
			}

			p.setVelocity(new Vector(0, 0.14, 0));

		    } else {

			cancel();

			Location loc = p.getLocation();
			Firework fw = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta meta = fw.getFireworkMeta();
			meta.addEffect(FireworkEffect.builder().flicker(true).trail(true).withColor(Color.WHITE)
				.withFade(Color.BLUE).build());
			meta.setPower(1);
			fw.setFireworkMeta(meta);
			
			new BukkitRunnable() {
			    @Override
			    public void run() {
				fw.detonate();
			    }
			}.runTaskLater(HospitalCore.inst(), 1);

			for (int i = 0; i < 5; i++) {

			    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("fireworksSpark",
				    (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), i, i, i, i, 20);

			    for (Player online : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
			    }

			}

		    }

		}
	    }.runTaskTimer(HospitalCore.inst(), 0, 1);

	}

    }

    @Override
    public void onKill(PlayerDeathEvent e) {
	phoenixed.remove(e.getEntity().getName());
    }

}
