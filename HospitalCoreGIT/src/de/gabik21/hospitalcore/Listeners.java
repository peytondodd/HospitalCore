package de.gabik21.hospitalcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.types.Report;
import de.gabik21.hospitalcore.util.BanManager;
import de.gabik21.hospitalcore.util.Hologram;
import de.gabik21.hospitalcore.util.MuteManager;
import de.gabik21.hospitalcore.util.SavingUnit;
import de.gabik21.hospitalcore.util.SpawnPoints;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Listeners implements Listener {

    private HospitalCore main;
    private static Lock lock = new ReentrantLock();
    public static final AtomicBoolean GLOBAL_CHATMUTE = new AtomicBoolean(false);

    public Listeners(HospitalCore main) {

	this.main = main;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {

	UUID uuid = e.getUniqueId();

	try {
	    if (BanManager.isBanned(uuid.toString())) {
		e.disallow(Result.KICK_BANNED,
			"§c-------------\n§cYou are banned from the server. \nReason: "
				+ BanManager.getReason(uuid.toString()) + "\nRemaining time: "
				+ BanManager.getRemainingTime(uuid.toString())
				+ "\n§cFalsely banned? Join ts.pvphospital.eu\n§c-------------");
		return;
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    e.disallow(Result.KICK_OTHER,
		    "§cError on loading ban data. Try to relog\n§cIf the problem persists join ts.pvphospital.eu");
	}

	PlayerData.preloaded.put(uuid, SavingUnit.loadData(e.getUniqueId(), true));

    }

    @EventHandler
    public void onPlayerTabComplete(PlayerChatTabCompleteEvent e) {

	Collection<String> collection = new ArrayList<String>(e.getTabCompletions());
	e.getTabCompletions().clear();

	for (String comp : collection) {
	    PlayerData pcp = HospitalCore.getData(Bukkit.getPlayer(comp));
	    e.getTabCompletions().add(pcp.getNick());
	}

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isFrozen() && (e.getFrom().getBlockX() != e.getTo().getBlockX()
		|| e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
	    p.teleport(e.getFrom());

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (MuteManager.isMuted(p.getUniqueId().toString())) {

	    p.sendMessage("§cYou currently muted for " + MuteManager.getReason(p.getUniqueId().toString()));
	    p.sendMessage("§cReamining Time: " + MuteManager.getRemainingTime(p.getUniqueId().toString()));

	    e.setCancelled(true);
	    return;
	}

	if (GLOBAL_CHATMUTE.get() && !p.hasPermission("Donator")) {
	    p.sendMessage("§cGlobal chat is muted!");
	    e.setCancelled(true);
	    return;
	}

	if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null) {

	    System.out.println("Could not find PermissionsEx");
	    return;
	}

	e.getRecipients().clear();
	for (PlayerData pdall : PlayerData.map.values()) {
	    if (!pdall.getPlayer().getWorld().getName().equals("replay"))
		e.getRecipients().add(pdall.getPlayer());
	}

	if (p.hasPermission("Owner") || p.hasPermission("Moderator"))
	    e.setMessage(e.getMessage().replace("&", "§"));

	if (e.getMessage().startsWith("!sc")) {

	    if (p.hasPermission("Owner") || p.hasPermission("Moderator") || p.hasPermission("Supporter")) {

		String s = "§c§l[§2Staffchat§c§l] §6" + p.getName() + "§c§l> §r"
			+ e.getMessage().replace("!sc", "").replace("&", "§");

		informStaff(s);
		e.setCancelled(true);
		return;
	    }
	}

	String s = PermissionsEx.getUser(pd.getNick()).getPrefix();
	setNick(p, s);

	if (pd.getLastMessage() != null && pd.getLastMessage().equalsIgnoreCase(e.getMessage())) {
	    e.setCancelled(true);
	    p.sendMessage(main.getConfig().getString("prefix") + "§cPlease don't repeat yourself");

	}

	pd.setLastMessage(e.getMessage());
	e.setMessage(e.getMessage().replaceAll(pd.getNick(), s + pd.getNick()));

	if (!pd.getNick().equals(p.getName())) {
	    e.setFormat(s + pd.getNick() + "§7§l> §r" + e.getMessage());
	} else {
	    e.setFormat(s + "%s§7§l> §r%s");
	}

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {

	final Player p = e.getPlayer();
	final PlayerData data = HospitalCore.getData(p);

	final UUID uuid = p.getUniqueId();

	e.setQuitMessage(null);
	data.setFakekits(false);
	SavingUnit.saveData(data);

	if (data.getTeam() != null)
	    data.getTeam().removePlayer(p);
	if (data.isFrozen())
	    new BukkitRunnable() {
		public void run() {
		    if (!BanManager.isBanned(uuid.toString()))
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				"tempban " + p.getName() + " 10 d [Console] Logging while frozen");

		}
	    }.runTaskAsynchronously(main);

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWeatherChange(WeatherChangeEvent e) {
	e.setCancelled(true);
    }

    @EventHandler
    public void onLiquid(PlayerBucketEmptyEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.canBuild() && !pd.isHg())
	    e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFoodChange(FoodLevelChangeEvent e) {

	Player p = (Player) e.getEntity();
	PlayerData pd = HospitalCore.getData(p);
	if (pd.isHg())
	    return;

	if (p.getFoodLevel() < 20)
	    p.setFoodLevel(20);

	e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHit(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	if (!(e.getDamager() instanceof Player))
	    return;

	Player p = (Player) e.getDamager();
	Player t = (Player) e.getEntity();
	PlayerData pd = HospitalCore.getData(p);

	pd.setLastDamaged(t);
	pd.hit();

	if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.STONE_SWORD
		&& p.getItemInHand().getDurability() > (short) 40)
	    p.getItemInHand().setDurability((short) 0);

    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {

	List<String> motdlist = main.getConfig().getStringList("motd");
	int rand = (int) ((Math.random() * 3));

	switch (rand) {

	case 0:
	    e.setMotd(motdlist.get(1).replace("/newline", "\n"));
	    break;
	case 1:
	    e.setMotd(motdlist.get(2).replace("/newline", "\n"));
	    break;
	case 2:
	    e.setMotd(motdlist.get(3).replace("/newline", "\n"));
	    break;
	case 3:
	    e.setMotd(motdlist.get(4).replace("/newline", "\n"));
	    break;
	default:
	    e.setMotd("Failed to load MOTD!");
	    break;
	}

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.canBuild() && !pd.isHg())
	    e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.canBuild() && !pd.isHg())
	    e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isInAdminmode() || pd.canBuild() || (pd.isIngame() && (!pd.isInChallenge() || !pd.isIn1v1()))
		|| pd.isHg())
	    return;

	if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
	    e.setCancelled(true);

    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
	e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSoup(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	Damageable d = p;

	PlayerData pd = HospitalCore.getData(p);

	if (pd.isFrozen())
	    e.setCancelled(true);

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
	    if (p.getItemInHand().getType() == Material.MUSHROOM_SOUP) {

		if (p.getFoodLevel() < 20) {

		    if (p.getFoodLevel() <= 13) {

			p.setFoodLevel(p.getFoodLevel() + 7);
			p.setSaturation(p.getSaturation() + 10);

			p.getItemInHand().setType(Material.BOWL);

		    } else if (p.getFoodLevel() > 20 - 7) {

			p.setFoodLevel(20);
			p.setSaturation(p.getSaturation() + 10);
			p.getItemInHand().setType(Material.BOWL);
		    }

		} else if ((d.getHealth() < 20) && (d.getHealth() > 0)) {
		    if (d.getHealth() <= 13) {

			p.getItemInHand().setType(Material.BOWL);
			p.setHealth(d.getHealth() + 7);

		    } else if ((d.getHealth() < 20) && (d.getHealth() > 20 - 7)) {

			p.setHealth(20D);
			p.getItemInHand().setType(Material.BOWL);

		    }
		}
	    }

	if (pd.isHg())
	    return;

	if (e.getAction() == Action.RIGHT_CLICK_BLOCK)

	    if (e.getClickedBlock().getState() instanceof Sign) {
		Sign s = (Sign) e.getClickedBlock().getState();
		if (s.getLine(1).equalsIgnoreCase("[SOUP]")) {

		    Inventory soupinv = Bukkit.createInventory(null, 54, "Refill");
		    for (ItemStack i1 : soupinv.getContents())
			if (soupinv.firstEmpty() != -1)
			    if (i1 == null)
				soupinv.setItem(soupinv.firstEmpty(), new ItemStack(Material.MUSHROOM_SOUP));

		    p.openInventory(soupinv);

		}

	    }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPvP(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
	    return;

	Player t = (Player) e.getEntity();
	Player p = (Player) e.getDamager();

	PlayerData pd = HospitalCore.getData(p);
	PlayerData td = HospitalCore.getData(t);

	if (td.isFrozen()) {
	    p.sendMessage(main.getConfig().getString("prefix") + "§cThis Player is frozen!");
	    e.setCancelled(true);
	}

	if (pd.isFrozen()) {
	    p.sendMessage(main.getConfig().getString("prefix") + "§cYou can't hit players while frozen!");
	    e.setCancelled(true);
	}

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	Player p = (Player) e.getEntity();

	PlayerData pd = HospitalCore.getData(p);

	if (pd.isFrozen())
	    e.setCancelled(true);

	if (!pd.isIngame() && !pd.isHg())
	    e.setCancelled(true);

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {

	final Player p = e.getPlayer();
	p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

	PlayerData pd = new PlayerData(p);
	e.setJoinMessage(null);

	pd.spawnOnJoin();

	new BukkitRunnable() {
	    public void run() {
		for (final Player pl : Bukkit.getOnlinePlayers().clone()) {

		    if (!pl.isOnline())
			continue;
		    PlayerData pdall = HospitalCore.getData(pl);

		    if (!pdall.getNick().equals(pl.getName())) {
			if (!p.hasPermission("Supporter") && !p.hasPermission("Moderator")
				&& !p.hasPermission("YouTuber") && !p.hasPermission("Owner")) {
			    final PlayerDisguise dis = new PlayerDisguise(pdall.getNick());
			    new BukkitRunnable() {
				public void run() {
				    DisguiseAPI.disguiseToPlayers(pl, dis, Arrays.asList(p));
				}
			    }.runTask(main);

			}
		    }

		    setNick(pl, PermissionsEx.getUser(HospitalCore.getData(pl).getNick()).getPrefix());
		}
	    }
	}.runTaskAsynchronously(main);

	for (Player pl : Bukkit.getOnlinePlayers()) {
	    PlayerData pdall = HospitalCore.getData(pl);
	    if (pdall.isDisguised())
		main.disguise(pl);
	}

	for (Hologram hologram : Hologram.getHolograms()) {
	    hologram.show(hologram.getLocation(), p);
	}

	makeScoreboard(p);

    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {

	if (e.getEntity().hasMetadata("nodespawn")) {
	    e.setCancelled(true);
	    return;
	}

	if (e.getEntity().getWorld().equals(Bukkit.getWorlds().get(0))
		|| e.getEntity().getWorld().getName().equals("replay")) {
	    Entity et = e.getEntity();
	    et.getWorld().playEffect(et.getLocation(), Effect.SMOKE, 4);
	    et.getWorld().playSound(et.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
	} else if (e.getEntity().getTicksLived() < 1000) {
	    e.setCancelled(true);
	} else {
	    Entity et = e.getEntity();
	    et.getWorld().playEffect(et.getLocation(), Effect.SMOKE, 4);
	    et.getWorld().playSound(et.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);

	}

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {

	e.setDeathMessage(null);

	final Player p = e.getEntity();
	final PlayerData pd = HospitalCore.getData(e.getEntity());

	p.eject();

	if (pd.isInChallenge())
	    e.getDrops().clear();

	p.getWorld().spigot().strikeLightningEffect(p.getLocation(), true);

	new BukkitRunnable() {
	    public void run() {

		if (p.isOnline() && p.isDead())
		    pd.getPlayer().spigot().respawn();

	    }
	}.runTaskLater(main, 15L);

    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());
	if (!pd.isIngame() && !pd.isHg())
	    e.setCancelled(true);

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (p.isInsideVehicle())
	    p.getVehicle().eject();

	if (pd.isSpawnAtChallenge())
	    e.setRespawnLocation(SpawnPoints.CHALLENGE.getLocation());
	else
	    e.setRespawnLocation(SpawnPoints.SPAWN.getLocation());
	pd.spawn();

    }

    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent e) {
	e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.isIngame() && !pd.isInAdminmode() && !pd.isHg())
	    e.setCancelled(true);

	if (e.getItemDrop().getItemStack().getType() == Material.BOWL && e.getItemDrop().getItemStack().getAmount() == 1
		&& (pd.isIn1v1() || pd.isInChallenge()))
	    e.getItemDrop().setPickupDelay(20 * 20);

    }

    @SuppressWarnings("deprecation")
    public static void setNick(Player player, String nick) {

	lock.lock();
	try {

	    for (Player online : Bukkit.getOnlinePlayers()) {

		if (!online.isOnline())
		    continue;

		PlayerData pd = HospitalCore.getData(player);

		Scoreboard scoreboard = online.getScoreboard();

		Team team = scoreboard.getTeam(player.getName());
		if (team == null)
		    team = scoreboard.registerNewTeam(player.getName());

		team.setPrefix(nick);
		team.addEntry(pd.getNick());
		team.setSuffix("");
		online.setScoreboard(scoreboard);

	    }
	} finally {
	    lock.unlock();
	}
    }

    @SuppressWarnings("deprecation")
    private void makeScoreboard(final Player p) {

	new BukkitRunnable() {
	    public void run() {

		if (!p.isOnline())
		    return;

		PlayerData pd = HospitalCore.getData(p);

		Scoreboard sb = p.getScoreboard();
		Objective obj = sb.registerNewObjective("sidebar", "bbb");

		obj.setDisplayName("§4PvPHospital");
		makeTeam(sb, "player", "Player", "", "");
		obj.getScore("Player").setScore(7);

		makeTeam(sb, "playername", "§r§c", "  ", pd.getNick());
		obj.getScore("§r§c").setScore(6);

		makeTeam(sb, "rank", "Rank", "", "");
		obj.getScore("Rank").setScore(4);

		Team playerrank = sb.registerNewTeam("playerrank");

		if (PermissionsEx.getUser(p).getGroups().length > 0)
		    playerrank.setSuffix(
			    PermissionsEx.getUser(p).getPrefix() + PermissionsEx.getUser(p).getGroups()[0].getName());
		else
		    playerrank.setSuffix("default");
		playerrank.setPrefix("  ");
		playerrank.addEntry("§r");
		obj.getScore("§r").setScore(3);

		makeTeam(sb, "money", "Money", "", "");
		obj.getScore("Money").setScore(1);

		makeTeam(sb, "playermoney", "§c", "  ", String.valueOf(pd.getMoney()));
		obj.getScore("§c").setScore(0);

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		makeTeam(sb, "place", "", "", "");
		makeTeam(sb, "place2", "§9", "", "");
		obj.getScore("§9").setScore(2);
		obj.getScore("").setScore(5);

		// Admin mode scoreboard
		if (!p.hasPermission("Owner") && !p.hasPermission("Moderator") && !p.hasPermission("Supporter"))
		    return;

		Objective admin = sb.registerNewObjective("admin", "dummy");
		admin.setDisplayName("§4§lStaffmode");

		Team visible = sb.registerNewTeam("visible");
		visible.addEntry("Visible");
		admin.getScore("Visible").setScore(7);

		makeTeam(sb, "playervisible", "  ", "§c", String.valueOf(pd.isDisguised()));
		admin.getScore("  ").setScore(6);

		Team reports = sb.registerNewTeam("reports");
		reports.addEntry("Reports");
		admin.getScore("Reports").setScore(4);

		makeTeam(sb, "playerreports", "§c ", " ", String.valueOf(Report.list.size()));
		admin.getScore("§c ").setScore(3);

		makeTeam(sb, "build", "Build", "", "");
		admin.getScore("Build").setScore(1);

		makeTeam(sb, "playerbuild", "§c  ", "", String.valueOf(pd.canBuild()));
		admin.getScore("§c  ").setScore(0);

		makeTeam(sb, "aplace", "", "", "");
		makeTeam(sb, "aplace2", "§9", "", "");
		admin.getScore("§9").setScore(2);
		admin.getScore("").setScore(5);
	    }
	}.runTaskAsynchronously(main);

    }

    private Team makeTeam(Scoreboard sb, String name, String entry, String prefix, String suffix) {

	Team team = sb.registerNewTeam(name);
	team.addEntry(entry);
	team.setPrefix(prefix);
	team.setSuffix(suffix);

	return team;

    }

    @SuppressWarnings("deprecation")
    private void informStaff(String s) {

	for (Player p : Bukkit.getOnlinePlayers())
	    if (p.hasPermission("Owner") || p.hasPermission("Moderator") || p.hasPermission("Supporter"))
		p.sendMessage(s);

	Bukkit.getConsoleSender().sendMessage(s);
    }

}
