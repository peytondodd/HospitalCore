package de.gabik21.hospitalcore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.CustomTimingsHandler;

import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.Hologram;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.JsonObject;

public class BugfixListeners implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent e) {

	if (e.getPlayer().getItemInHand().getType() == Material.ARROW
		&& !HospitalCore.getData(e.getPlayer()).canBuild())
	    e.setCancelled(true);
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent e) {
	final Player p = e.getPlayer();
	new BukkitRunnable() {
	    public void run() {
		HospitalCore.inst().resetPosition(p);
		for (Hologram hologram : Hologram.getHolograms()) {
		    hologram.show(hologram.getLocation(), p);
		}
	    }
	}.runTaskLater(HospitalCore.inst(), 2);
    }

    @EventHandler
    public void onRespawnHolograms(PlayerRespawnEvent e) {
	final Player p = e.getPlayer();
	new BukkitRunnable() {
	    public void run() {
		for (Hologram hologram : Hologram.getHolograms()) {
		    hologram.show(hologram.getLocation(), p);
		}
	    }
	}.runTaskLater(HospitalCore.inst(), 2);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

	if (e.getMessage().startsWith("bukkit:"))
	    e.setCancelled(true);
	if (e.getMessage().equalsIgnoreCase("me"))
	    e.setCancelled(true);
	if (e.getMessage().equalsIgnoreCase("reload") || (e.getMessage().equalsIgnoreCase("rl")))
	    e.setCancelled(true);

	for (PlayerData pdall : PlayerData.map.values()) {
	    if (pdall.getNick() != pdall.getPlayer().getName() && e.getMessage().contains(pdall.getNick())) {
		e.setMessage(e.getMessage().replace(pdall.getNick(), pdall.getPlayer().getName()));
	    }
	}

    }

    @EventHandler
    public void onSmelt(BlockFadeEvent e) {
	e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
	if ((event.getMessage().startsWith("/timings paste")) && (event.getPlayer().hasPermission("timings.patch"))) {
	    handle(event.getPlayer());
	    event.setCancelled(true);
	}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {

	if (e.isCancelled()) {
	    Block b = e.getBlock();
	    Player p = e.getPlayer();
	    PlayerData pd = HospitalCore.getData(p);

	    if (b.getType().isSolid())
		pd.setLastBrokenBlock(b);
	}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {

	if (e.isCancelled() || !e.canBuild()) {

	    Block b = e.getBlockPlaced();
	    Player p = e.getPlayer();
	    PlayerData pd = HospitalCore.getData(p);

	    AxisAlignedBB playerbox = ((CraftPlayer) p).getHandle().boundingBox;

	    if (playerbox.b(AxisAlignedBB.a(b.getLocation().getX(), b.getLocation().getY() + 1, b.getLocation().getZ(),
		    b.getLocation().getX() + 1, b.getLocation().getY() + 2, b.getLocation().getZ() + 1))) {
		p.teleport(pd.getLastLocationOnGround());
		p.sendMessage("§cBlock glitching is not allowed");
	    }

	}

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);
	Block b = pd.getLastBrokenBlock();

	AxisAlignedBB playerbox = ((CraftPlayer) p).getHandle().boundingBox;
	if (playerbox.b(AxisAlignedBB.a(b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ(),
		b.getLocation().getX() + 1, b.getLocation().getY() + 1, b.getLocation().getZ() + 1))) {
	    p.teleport(pd.getLastLocationOnGround());
	    p.sendMessage("§cBlock glitching is not allowed");
	    return;
	}

	if (p.isOnGround())
	    pd.setLastLocationOnGround(e.getFrom());

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onVel(PlayerVelocityEvent e) {

	EntityDamageEvent cause = e.getPlayer().getLastDamageCause();
	Player p = e.getPlayer();

	if (cause != null && cause instanceof EntityDamageByEntityEvent
		&& cause.getCause() == DamageCause.ENTITY_ATTACK) {

	    if (p.isOnGround()) {

		if (e.getVelocity().getY() < 0.3) {
		    Vector a = e.getVelocity().multiply(3.2D).add(new Vector(0, -0.459, 0));
		    if ((Math.abs(a.getX()) + Math.abs(a.getZ())) > 1.7)
			return;
		    if (a.getY() < 0.34)
			a.setY(0.36);

		    p.setVelocity(a);

		}
	    }
	}

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
	if (e.getEntity() instanceof Arrow)
	    e.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent e) {

	final Player p = e.getPlayer();

	new BukkitRunnable() {
	    public void run() {
		HospitalCore.inst().resetPosition(p);
	    }
	}.runTaskLater(HospitalCore.inst(), 8);

	p.setNoDamageTicks(6);
	p.setFallDistance(-2);

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void DamageNerf(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	if (!(e.getDamager() instanceof Player))
	    return;

	Player p = (Player) e.getDamager();

	if (((LivingEntity) e.getEntity())
		.getNoDamageTicks() > ((LivingEntity) e.getDamager()).getMaximumNoDamageTicks() / 2D
		&& p.getLastDamageCause() instanceof EntityDamageByEntityEvent
		&& p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {

	    e.setDamage(0D);
	    return;
	}

	if ((p.getFallDistance() > 0.0F) && (!p.isOnGround()) && (!p.hasPotionEffect(PotionEffectType.BLINDNESS))) {
	    e.setDamage(e.getDamage() / 1.5 + 2D);
	    if (p.getItemInHand().getType() == Material.AIR)
		e.setDamage(e.getDamage() - 2);
	}

	if (p.getItemInHand().getType().name().contains("SWORD"))
	    e.setDamage(e.getDamage() - 2);

    }

    private void handle(CommandSender sender) {
	if (!HospitalCore.inst().getServer().getPluginManager().useTimings()) {
	    sender.sendMessage("Please enable timings by setting \"settings.plugin-profiling\" to true in bukkit.yml");
	    return;
	}
	if (!HospitalCore.inst().getServer().getPluginManager().useTimings()) {
	    sender.sendMessage("Please enable timings by typing /timings on");
	    return;
	}
	long sampleTime = System.nanoTime() - TimingsCommand.timingStart;
	int index = 0;
	File timingFolder = new File("timings");
	timingFolder.mkdirs();
	File timings = new File(timingFolder, "timings.txt");
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	while (timings.exists()) {
	    timings = new File(timingFolder, "timings" + ++index + ".txt");
	}
	PrintStream fileTimings = new PrintStream(bout);

	CustomTimingsHandler.printTimings(fileTimings);
	fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1.0E9D + "s)");

	fileTimings.println("<spigotConfig>");
	fileTimings.println(Bukkit.spigot().getConfig().saveToString());
	fileTimings.println("</spigotConfig>");

	new PasteThread(sender, bout).start();
    }

    private static class PasteThread extends Thread {
	private final CommandSender sender;
	private final ByteArrayOutputStream bout;

	PasteThread(CommandSender sender, ByteArrayOutputStream bout) {
	    this.sender = sender;
	    this.bout = bout;
	}

	public synchronized void start() {
	    if ((sender instanceof RemoteConsoleCommandSender)) {
		run();
	    } else {
		super.start();
	    }
	}

	public void run() {
	    try {
		HttpURLConnection con = (HttpURLConnection) new URL("https://timings.spigotmc.org/paste")
			.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setInstanceFollowRedirects(false);

		OutputStream out = con.getOutputStream();
		Throwable localThrowable3 = null;
		try {
		    out.write(bout.toByteArray());
		} catch (Throwable localThrowable1) {
		    localThrowable3 = localThrowable1;
		    try {
			throw localThrowable1;
		    } catch (Throwable e) {
			e.printStackTrace();
		    }
		} finally {
		    if (out != null)
			if (localThrowable3 != null)
			    try {
				out.close();
			    } catch (Throwable localThrowable2) {
				localThrowable3.addSuppressed(localThrowable2);
			    }
			else
			    out.close();
		}
		JsonObject location = (JsonObject) new Gson().fromJson(new InputStreamReader(con.getInputStream()),
			JsonObject.class);
		con.getInputStream().close();

		String pasteID = location.get("key").getAsString();
		sender.sendMessage(ChatColor.GREEN
			+ "Timings results can be viewed at https://www.spigotmc.org/go/timings?url=" + pasteID);
	    } catch (IOException ex) {
		sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
		Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
	    }
	}

    }
}
