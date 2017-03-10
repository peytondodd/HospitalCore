package de.gabik21.hospitalcore.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.gabik21.hospitalcore.events.PlayerChangeWalkSpeedEvent;
import de.gabik21.hospitalcore.util.Inventories;
import de.gabik21.hospitalcore.util.SpawnPoints;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerData {

    public static final Map<UUID, LoadedData> preloaded = new ConcurrentHashMap<UUID, LoadedData>();
    public static final Map<UUID, PlayerData> map = new HashMap<UUID, PlayerData>();
    static final Random rand = new Random();

    private Player player, lastDamaged, reply;
    private boolean adminmode = false, frozen = false, ingame = false, in1v1 = false, disguised = false,
	    challenge = false, fakekits = false, hg = false, build = false, spawnAtChallenge = false;
    private int currentArena = -1, chests;
    private AbstractGUI currentGui, previousGui;
    private PerKitCooldown perKitCooldown = new PerKitCooldown();
    private long lastReport, money, lasthit;
    private String nick, lastmessage, prefix;
    private Set<String> duels = new HashSet<String>();
    private Team team, teaminvitation;
    private Set<ProtectedRegion> currentregions = new HashSet<ProtectedRegion>();
    private KitConfiguration kitConfig;
    private KitConfiguration[] kitConfigs = new KitConfiguration[] { new KitConfiguration(), new KitConfiguration(),
	    new KitConfiguration(), new KitConfiguration(), new KitConfiguration() };
    private Map<Kit, AtomicInteger> ownedkits;
    private Map<Location, Material> fakeBlocks = new ConcurrentHashMap<Location, Material>();
    private Stats stats;
    private Location lastLocationOnGround;
    private Block lastBrokenBlock;

    public PlayerData(Player p) {

	this.player = p;
	LoadedData data = preloaded.get(p.getUniqueId());
	money = data.getMoney();
	chests = data.getChests();
	ownedkits = data.getOwnedkits();
	stats = data.getStats();
	kitConfigs = data.getKitconfigs();
	preloaded.remove(p.getUniqueId());
	map.put(p.getUniqueId(), this);
	lastLocationOnGround = player.getLocation();
	lastBrokenBlock = player.getWorld().getBlockAt(0, 0, 0);
	prefix = PermissionsEx.getUser(player).getPrefix();

    }

    public void setWalkSpeed(float speed) {

	PlayerChangeWalkSpeedEvent e = new PlayerChangeWalkSpeedEvent(player, player.getWalkSpeed(), speed);
	Bukkit.getPluginManager().callEvent(e);
	player.setWalkSpeed(speed);

    }

    public void delete() {
	map.remove(player);
    }

    public long getLasthit() {
	return lasthit;
    }

    public void hit() {
	this.lasthit = System.currentTimeMillis();
    }

    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public boolean hasMoney(long amount) {
	return (getMoney() - amount >= 0);
    }

    public void addMoney(long amount) {
	this.money += amount;
    }

    public long getMoney() {
	return this.money;
    }

    public void setLastDamaged(Player lastdamaged) {
	this.lastDamaged = lastdamaged;
    }

    public Player getLastDamaged() {
	return this.lastDamaged;
    }

    public boolean isOnCooldown(Kit kit) {
	return perKitCooldown.isOnCooldown(player, kit);
    }

    public PerKitCooldown getPerKitCooldown() {
	return perKitCooldown;
    }

    public void useKit(Kit kit) {
	this.perKitCooldown.useKit(kit);
    }

    public void addKit(Kit k) {
	if (ownedkits.containsKey(k))
	    ownedkits.get(k).incrementAndGet();
	else
	    ownedkits.put(k, new AtomicInteger(1));
    }

    public void removeKit(Kit k) {
	if (ownedkits.get(k).get() <= 1)
	    ownedkits.remove(k);
	else
	    ownedkits.get(k).decrementAndGet();
    }

    public Map<Kit, AtomicInteger> getOwnedKits() {

	if (isFakekits()) {
	    Map<Kit, AtomicInteger> temp = new HashMap<Kit, AtomicInteger>();
	    for (Kit k : Kit.values())
		temp.put(k, new AtomicInteger(1));
	    return temp;
	}

	return this.ownedkits;
    }

    public void setKit(KitConfiguration kitConfig) {
	this.perKitCooldown.reset();
	this.kitConfig = kitConfig;
    }

    public KitConfiguration getKitConfig() {
	return this.kitConfig;
    }

    public void updateRegions(Set<ProtectedRegion> newregions) {
	this.currentregions = newregions;
    }

    public Set<ProtectedRegion> getApplicableRegions() {
	return this.currentregions;
    }

    public void setTeam(Team team) {
	this.team = team;
    }

    public Team getTeam() {
	return this.team;
    }

    public Set<String> getDuels() {
	return this.duels;
    }

    public boolean isInChallenge() {
	return this.challenge;
    }

    public void setInChallenge(boolean mode) {
	this.challenge = mode;
    }

    public String getLastMessage() {
	return this.lastmessage;
    }

    public void setLastMessage(String s) {
	this.lastmessage = s;
    }

    public String getNick() {

	if (this.nick != null)
	    return this.nick;
	return player.getName();

    }

    public void setNick(String nick) {
	this.nick = nick;
    }

    public void duel(Player p) {
	duels.add(p.getName());
    }

    public void removeDuel(String s) {
	duels.remove(s);
    }

    public void report() {
	this.lastReport = System.currentTimeMillis();
    }

    public long getLastReport() {
	return this.lastReport;
    }

    public int getCurrentArena() {
	return this.currentArena;
    }

    public AbstractGUI getPreviousGui() {
	return previousGui;
    }

    public void setPreviousGui(AbstractGUI previousGui) {
	this.previousGui = previousGui;
    }

    public void setCurrentArena(int i) {
	this.currentArena = i;
    }

    public AbstractGUI getCurrentGui() {
	return this.currentGui;
    }

    public void setCurrentGui(AbstractGUI currentGui) {
	this.currentGui = currentGui;
    }

    public void setIn1v1(boolean mode) {
	this.in1v1 = mode;
    }

    public boolean isIn1v1() {
	return this.in1v1;
    }

    public void setDisguised(Boolean mode) {
	this.disguised = mode;
    }

    public Boolean isDisguised() {
	return this.disguised;
    }

    public void setIngame(boolean mode) {
	this.ingame = mode;
    }

    public boolean isIngame() {
	return this.ingame;
    }

    public void setAdminmode(Boolean mode) {
	this.adminmode = mode;
    }

    public Boolean isInAdminmode() {
	return this.adminmode;
    }

    public void setFrozen(Boolean mode) {
	this.frozen = mode;
    }

    public Boolean isFrozen() {
	return this.frozen;
    }

    public Player getPlayer() {
	return this.player;
    }

    public Team getTeaminvitation() {
	return teaminvitation;
    }

    public void setTeaminvitation(Team teaminvitation) {
	this.teaminvitation = teaminvitation;
    }

    public void sendHoverableMessage(String msg, String hovermsg, String command) {

	TextComponent tc = new TextComponent();
	tc.setText(msg);
	if (command != null)
	    tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/" + command));

	tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
		new ComponentBuilder(hovermsg).create()));

	getPlayer().spigot().sendMessage(tc);

    }

    public static TextComponent createComponent(String msg, String hovermsg, String command) {

	TextComponent tc = new TextComponent();
	tc.setText(msg);
	if (command != null)
	    tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/" + command));

	tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
		new ComponentBuilder(hovermsg).create()));

	return tc;

    }

    public void spawnOnJoin() {

	SpawnPoints.SPAWN.teleport(player);
	setWalkSpeed(0.2F);
	if (!player.isDead())
	    player.setHealth(20D);

	for (PotionEffect effect : player.getActivePotionEffects())
	    player.removePotionEffect(effect.getType());

	player.getInventory().setArmorContents(null);
	player.setLevel(0);
	player.setExp(0);

	Inventories.SPAWN.apply(player);
	player.setGameMode(GameMode.SURVIVAL);

    }

    public synchronized void spawn() {

	player.closeInventory();
	SpawnPoints.SPAWN.teleport(player);

	if (isInAdminmode())
	    player.performCommand("admin");
	else if (canBuild())
	    player.performCommand("build");

	this.setIngame(false);
	this.setIn1v1(false);
	this.setInChallenge(false);
	this.setKit(null);
	this.setCurrentArena(-1);
	this.setHg(false);
	this.setLastDamaged(null);
	player.setGameMode(GameMode.SURVIVAL);

	for (PlayerData data : map.values())
	    if (data.getLastDamaged() != null && data.getLastDamaged().equals(player))
		data.setLastDamaged(null);

	setWalkSpeed(0.2F);

	if (!player.isDead())
	    player.setHealth(20D);

	for (PotionEffect effect : player.getActivePotionEffects())
	    player.removePotionEffect(effect.getType());

	player.getInventory().setArmorContents(null);
	player.setLevel(0);
	player.setExp(0);

	Inventories.SPAWN.apply(player);

	if (isSpawnAtChallenge()) {

	    setIngame(true);
	    setInChallenge(true);
	    player.getInventory().clear();

	    Inventories.RC64.apply(player);
	}

    }

    public boolean isFakekits() {
	return fakekits;
    }

    public void setFakekits(boolean fakekits) {
	this.fakekits = fakekits;
    }

    public boolean isHg() {
	return hg;
    }

    public void setHg(boolean hg) {
	this.hg = hg;
    }

    public Player getReply() {
	return reply;
    }

    public void setReply(Player reply) {
	this.reply = reply;
    }

    public boolean canBuild() {
	return build;
    }

    public void setBuild(boolean build) {
	this.build = build;
    }

    public boolean isSpawnAtChallenge() {
	return spawnAtChallenge;
    }

    public void setSpawnAtChallenge(boolean spawnAtChallenge) {
	this.spawnAtChallenge = spawnAtChallenge;
    }

    public Map<Location, Material> getFakeBlocks() {
	return fakeBlocks;
    }

    public void addFakeBlock(Location loc, Material mat) {
	fakeBlocks.put(loc, mat);
    }

    @SuppressWarnings("deprecation")
    public void removeFakeBlock(Location loc, Material material) {
	fakeBlocks.remove(loc);
	player.sendBlockChange(loc, material, (byte) 0);
    }

    public Stats getStats() {
	return stats;
    }

    public int getChests() {
	return chests;
    }

    public void setChests(int chests) {
	this.chests = chests;
    }

    public KitConfiguration[] getKitConfigs() {
	return kitConfigs;
    }

    public void setLastBrokenBlock(Block lastBrokenBlock) {
	this.lastBrokenBlock = lastBrokenBlock;
    }

    public Block getLastBrokenBlock() {
	return lastBrokenBlock;
    }

    public Location getLastLocationOnGround() {
	Location loc = lastLocationOnGround.clone();
	loc.setYaw(player.getLocation().getYaw());
	loc.setPitch(player.getLocation().getPitch());
	return loc;
    }

    public void setLastLocationOnGround(Location lastLocationOnGround) {
	this.lastLocationOnGround = lastLocationOnGround;
    }

}
