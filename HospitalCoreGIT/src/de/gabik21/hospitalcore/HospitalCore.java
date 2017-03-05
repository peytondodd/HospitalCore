package de.gabik21.hospitalcore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.gabik21.hospitalcore.abilities.AutomationTurret;
import de.gabik21.hospitalcore.abilities.purple.Gladiator;
import de.gabik21.hospitalcore.abilities.purple.MarioKart;
import de.gabik21.hospitalcore.abilities.red.Napalm;
import de.gabik21.hospitalcore.commands.AdminCommand;
import de.gabik21.hospitalcore.commands.AlertCommand;
import de.gabik21.hospitalcore.commands.BanCommand;
import de.gabik21.hospitalcore.commands.BuildCommand;
import de.gabik21.hospitalcore.commands.CheckCommand;
import de.gabik21.hospitalcore.commands.ChestCommand;
import de.gabik21.hospitalcore.commands.ClearChatCommand;
import de.gabik21.hospitalcore.commands.FakeKitsCommand;
import de.gabik21.hospitalcore.commands.FixCommand;
import de.gabik21.hospitalcore.commands.FreezeCommand;
import de.gabik21.hospitalcore.commands.GlobalMuteCommand;
import de.gabik21.hospitalcore.commands.HologramCommand;
import de.gabik21.hospitalcore.commands.HubCommand;
import de.gabik21.hospitalcore.commands.KickCommand;
import de.gabik21.hospitalcore.commands.MoneyCommand;
import de.gabik21.hospitalcore.commands.MsgCommand;
import de.gabik21.hospitalcore.commands.MuteCommand;
import de.gabik21.hospitalcore.commands.NickCommand;
import de.gabik21.hospitalcore.commands.PayCommand;
import de.gabik21.hospitalcore.commands.PingCommand;
import de.gabik21.hospitalcore.commands.ReplyCommand;
import de.gabik21.hospitalcore.commands.ReportCommand;
import de.gabik21.hospitalcore.commands.SeeKitCommand;
import de.gabik21.hospitalcore.commands.SetSpawnCommand;
import de.gabik21.hospitalcore.commands.SpawnCommand;
import de.gabik21.hospitalcore.commands.TCommand;
import de.gabik21.hospitalcore.commands.TeamCommand;
import de.gabik21.hospitalcore.commands.VCommand;
import de.gabik21.hospitalcore.types.Chest;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.LoadedData;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.types.Report;
import de.gabik21.hospitalcore.util.Hologram;
import de.gabik21.hospitalcore.util.Inventories;
import de.gabik21.hospitalcore.util.SavingUnit;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HospitalCore extends JavaPlugin {

    public static final String PREFIX = "§6§l[§cHospital§6§l] ";

    private static HospitalCore instance;

    public static HospitalCore inst() {
	return instance;
    }

    public void onEnable() {

	instance = this;

	new MySQLFile().readData();
	MySQL.connect();

	reloadCompatibilty();
	loadListeners();
	loadCommands();
	loadConfig();
	loadHolograms();

	List<String> all = new ArrayList<String>();
	for (Kit k : Kit.values())
	    all.add(k.getName());
	all.remove("None");

    }

    public void onDisable() {

	saveData();
	saveConfig();

	System.out.println("Removing all entities...");

	for (Entity ent : Bukkit.getWorlds().get(0).getEntities())
	    ent.remove();

	System.out.println("Done.");

    }

    @SuppressWarnings("deprecation")
    private void saveData() {

	for (Player pl : Bukkit.getOnlinePlayers()) {
	    PlayerData data = HospitalCore.getData(pl);
	    SavingUnit.saveDataSync(data);
	}

    }

    @SuppressWarnings("deprecation")
    private void reloadCompatibilty() {
	for (Player p : Bukkit.getOnlinePlayers()) {
	    LoadedData data = SavingUnit.loadData(p.getUniqueId(), true);
	    PlayerData.preloaded.put(p.getUniqueId(), data);
	    PlayerData pd = new PlayerData(p);
	    pd.spawn();
	}
    }

    private void loadConfig() {

	getConfig().options().copyDefaults(true);
	saveConfig();

	File file = new File(getDataFolder(), "kits.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	cfg.options().copyDefaults(true);

	try {
	    cfg.save(file);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	List<ItemStack> ehg = new ArrayList<ItemStack>();

	try {
	    String items = cfg.getString("Kits.ehg");
	    String[] indiItems = items.split(", ");
	    for (String s1 : indiItems) {
		String[] itemAmounts = s1.split("-");
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Integer.valueOf(itemAmounts[0]), Integer.valueOf(itemAmounts[1]));
		ehg.add(item);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	Inventories.RC32.setContents(ehg.toArray(new ItemStack[ehg.size()]));

	List<ItemStack> ehg64 = new ArrayList<ItemStack>();

	try {
	    String items = cfg.getString("Kits.ehg64");
	    String[] indiItems = items.split(", ");
	    for (String s1 : indiItems) {
		String[] itemAmounts = s1.split("-");
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Integer.valueOf(itemAmounts[0]), Integer.valueOf(itemAmounts[1]));
		ehg64.add(item);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

	Inventories.RC64.setContents(ehg64.toArray(new ItemStack[ehg64.size()]));
    }

    private void loadCommands() {

	getCommand("fix").setExecutor(new FixCommand(this));
	getCommand("admin").setExecutor(new AdminCommand(this));
	getCommand("freeze").setExecutor(new FreezeCommand(this));
	getCommand("spawn").setExecutor(new SpawnCommand(this));
	getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
	getCommand("hub").setExecutor(new HubCommand());
	getCommand("vanish").setExecutor(new VCommand(this));
	getCommand("check").setExecutor(new CheckCommand(this));
	getCommand("ping").setExecutor(new PingCommand(this));
	getCommand("report").setExecutor(new ReportCommand());
	getCommand("nick").setExecutor(new NickCommand(this));
	getCommand("team").setExecutor(new TeamCommand());
	getCommand("t").setExecutor(new TCommand());
	getCommand("hologram").setExecutor(new HologramCommand());
	getCommand("money").setExecutor(new MoneyCommand());
	getCommand("chest").setExecutor(new ChestCommand());

	BanCommand bancmd = new BanCommand();
	getCommand("tempban").setExecutor(bancmd);
	getCommand("bancheck").setExecutor(bancmd);
	getCommand("ban").setExecutor(bancmd);
	getCommand("unban").setExecutor(bancmd);
	getCommand("kick").setExecutor(new KickCommand());

	MuteCommand mutecmd = new MuteCommand();
	getCommand("tempmute").setExecutor(mutecmd);
	getCommand("mutecheck").setExecutor(mutecmd);
	getCommand("mute").setExecutor(mutecmd);
	getCommand("unmute").setExecutor(mutecmd);
	getCommand("globalmute").setExecutor(new GlobalMuteCommand());

	getCommand("bcconfig").setExecutor(new AlertCommand());
	getCommand("msg").setExecutor(new MsgCommand());
	getCommand("r").setExecutor(new ReplyCommand());
	getCommand("build").setExecutor(new BuildCommand());
	getCommand("pay").setExecutor(new PayCommand());
	getCommand("fakekits").setExecutor(new FakeKitsCommand());
	getCommand("ckit").setExecutor(new SeeKitCommand());
	getCommand("clearchat").setExecutor(new ClearChatCommand());

    }

    @SuppressWarnings("deprecation")
    private void loadListeners() {

	Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
	Bukkit.getPluginManager().registerEvents(new BugfixListeners(), this);
	Bukkit.getPluginManager().registerEvents(new AdminModeListeners(), this);
	Bukkit.getPluginManager().registerEvents(new RegionEventListener(this), this);
	Bukkit.getPluginManager().registerEvents(new AbilityListener(), this);
	Bukkit.getPluginManager().registerEvents(new Chest(), this);
	Bukkit.getPluginManager().registerEvents(new Gladiator(), this);
	Bukkit.getPluginManager().registerEvents(new MarioKart(), this);
	Bukkit.getPluginManager().registerEvents(new Napalm(), this);
	Bukkit.getPluginManager().registerEvents(new AutomationTurret(), this);

	ShapelessRecipe milkrecipe = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
	milkrecipe.addIngredient(2, Material.CACTUS);
	milkrecipe.addIngredient(Material.BOWL);
	Bukkit.getServer().addRecipe(milkrecipe);

	ShapelessRecipe cmilkrecipe = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
	cmilkrecipe.addIngredient(1, Material.INK_SACK, 3);
	cmilkrecipe.addIngredient(Material.BOWL);
	Bukkit.getServer().addRecipe(cmilkrecipe);

	new BukkitRunnable() {
	    public void run() {
		Player[] players = Bukkit.getOnlinePlayers().clone();

		for (Player p : players) {

		    if (!p.isOnline())
			continue;

		    Scoreboard sb = p.getScoreboard();
		    Objective obj = sb.getObjective("sidebar");
		    Objective admin = sb.getObjective("admin");
		    Team name = sb.getTeam("playername");
		    Team money = sb.getTeam("playermoney");
		    Team playerrank = sb.getTeam("playerrank");

		    PlayerData pd = HospitalCore.getData(p);
		    if (pd.isIngame())
			obj.setDisplaySlot(null);
		    else if (obj.getDisplaySlot() != DisplaySlot.SIDEBAR && !pd.isInAdminmode())
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		    if (pd.isInAdminmode() && admin.getDisplaySlot() != DisplaySlot.SIDEBAR)
			admin.setDisplaySlot(DisplaySlot.SIDEBAR);

		    if (PermissionsEx.getUser(p).getGroups().length > 0)
			playerrank.setSuffix(PermissionsEx.getUser(p).getPrefix()
				+ PermissionsEx.getUser(p).getGroups()[0].getName());
		    else
			playerrank.setSuffix("default");

		    name.setSuffix(pd.getNick());
		    money.setSuffix(String.valueOf(pd.getMoney()));

		    if (admin == null)
			continue;

		    Team playerreports = sb.getTeam("playerreports");
		    Team playervisible = sb.getTeam("playervisible");
		    Team playerbuild = sb.getTeam("playerbuild");

		    playerreports.setSuffix(String.valueOf(Report.list.size()));
		    playervisible.setSuffix(String.valueOf(!pd.isDisguised()));
		    playerbuild.setSuffix(String.valueOf(pd.canBuild()));

		}
	    }
	}.runTaskTimerAsynchronously(instance, 10 * 20, 10);

	new BukkitRunnable() {
	    public void run() {

		List<String> bc = getConfig().getStringList("bc");
		if (bc.size() <= 0)
		    return;
		int random = (int) (Math.random() * bc.size());
		Player[] players = Bukkit.getOnlinePlayers().clone();
		for (Player all : players) {
		    if (all.isOnline()) {
			all.sendMessage("§6§l[§bAlert§6§l] §r" + bc.get(random));
			SavingUnit.saveData(HospitalCore.getData(all));
		    }
		}

	    }
	}.runTaskTimerAsynchronously(instance, 10 * 20, 90 * 20);

	new BukkitRunnable() {
	    public void run() {

		for (PlayerData pd : PlayerData.map.values()) {

		    Map<Location, Material> blocks = pd.getFakeBlocks();
		    for (Location loc : blocks.keySet())
			if (loc != null && blocks.get(loc) != null)
			    pd.getPlayer().sendBlockChange(loc, blocks.get(loc), (byte) 0);

		}

	    }
	}.runTaskTimerAsynchronously(instance, 10 * 20, 1);
	new BukkitRunnable() {
	    public void run() {

		for (Player p : Bukkit.getOnlinePlayers()) {
		    if (getData(p) == null)
			p.kickPlayer("Uhm.. seems like the database is broken :/");
		}
	    }
	}.runTaskTimer(instance, 0, 1);
    }

    private void loadHolograms() {

	ConfigurationSection section = getConfig().getConfigurationSection("holograms");

	if (section == null)
	    return;

	for (String name : section.getKeys(false)) {

	    Location loc = new Location(Bukkit.getWorlds().get(0), getConfig().getDouble("holograms." + name + ".x"),
		    getConfig().getDouble("holograms." + name + ".y"),
		    getConfig().getDouble("holograms." + name + ".z"));
	    List<String> text = getConfig().getStringList("holograms." + name + ".message");

	    new Hologram(text).show(loc);

	}

    }

    public static PlayerData getData(Player p) {
	return PlayerData.map.get(p);
    }

    public static Report getReport(UUID uuid) {

	for (Report report : Report.list)
	    if (report.getId() == uuid)
		return report;

	return null;

    }

    @SuppressWarnings("deprecation")
    public void disguise(Player p) {

	for (Player pl : Bukkit.getOnlinePlayers()) {

	    if (!pl.canSee(p))
		continue;

	    if (p.hasPermission("Supporter") && !p.hasPermission("Moderator")) {

		if (pl.hasPermission("Supporter"))
		    continue;
		if (pl.hasPermission("Moderator"))
		    continue;
		if (pl.hasPermission("Owner"))
		    continue;
	    }

	    if (p.hasPermission("Moderator") && !p.hasPermission("Supporter")) {

		if (pl.hasPermission("Moderator"))
		    continue;
		if (pl.hasPermission("Owner"))
		    continue;
	    }

	    if (p.hasPermission("Owner")) {

		if (pl.hasPermission("Owner"))
		    continue;
	    }

	    pl.hidePlayer(p);
	}
	getData(p).setDisguised(true);

    }

    @SuppressWarnings("deprecation")
    public void undisguise(Player p) {

	for (Player pl : Bukkit.getOnlinePlayers())
	    pl.showPlayer(p);

	getData(p).setDisguised(false);

    }

    public void resetPosition(Player p, Player target) {

	if (target.canSee(p)) {
	    target.hidePlayer(p);
	    target.showPlayer(p);
	}

    }

    @SuppressWarnings("deprecation")
    public void resetPosition(Player p) {

	Set<String> undis = new HashSet<String>();

	for (Player pl : Bukkit.getOnlinePlayers()) {

	    if (pl.canSee(p))
		undis.add(pl.getName());
	    pl.hidePlayer(p);

	}

	for (Player pl : Bukkit.getOnlinePlayers()) {

	    if (undis.contains(pl.getName()))
		pl.showPlayer(p);

	}
    }
}
