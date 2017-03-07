package de.gabik21.hospitalcore.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.gabik21.hospitalcore.HospitalCore;

public class Chest implements Listener {

    static ArrayList<String> opening = new ArrayList<String>();
    static HashMap<String, Inventory> inv = new HashMap<String, Inventory>();

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {

	final Player p = (Player) e.getPlayer();

	new BukkitRunnable() {

	    public void run() {
		if (inv.containsKey(p.getName()) && !p.getOpenInventory().equals(inv.get(p.getName())))
		    p.openInventory(inv.get(p.getName()));
	    }
	}.runTaskLater(HospitalCore.inst(), 1);

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

	Player p = (Player) e.getWhoClicked();

	if (opening.contains(p.getName()))
	    e.setCancelled(true);

    }

    public Chest() {
    }

    public Chest(final Player p) {

	new BukkitRunnable() {
	    public void run() {
		if (p.isOnline()) {
		    opening.add(p.getName());
		    Inventory invr = Bukkit.createInventory(null, 27, "§c§lChest");
		    for (int i = 0; i < 27; i++)
			invr.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
		    inv.put(p.getName(), invr);
		    invr.setItem(4, new ItemStack(Material.STICK));
		    invr.setItem(22, new ItemStack(Material.STICK));
		    start(p);
		}

	    }
	}.runTaskLater(HospitalCore.inst(), 5);

    }

    private void start(final Player p) {

	PlayerData pd = HospitalCore.getData(p);
	List<Pair<Kit, Double>> notowned = new ArrayList<Pair<Kit, Double>>();
	for (Kit k : Kit.values())
	    notowned.add(new Pair<Kit, Double>(k, k.getLevel().getPercent()));
	p.openInventory(inv.get(p.getName()));

	AtomicInteger taskid = new AtomicInteger(-1);
	BukkitTask task = new BukkitRunnable() {
	    List<ItemStack> allitems = new ArrayList<ItemStack>();
	    Kit chosen;
	    double speed = 400;
	    double recentspeed;
	    boolean previousLoopDone = true;

	    public void run() {

		if (previousLoopDone) {
		    previousLoopDone = false;
		    Bukkit.getScheduler().scheduleSyncDelayedTask(HospitalCore.inst(), new Runnable() {
			double decelleration = ThreadLocalRandom.current().nextDouble(2, 10);

			public void run() {

			    if (!p.isOnline()) {
				opening.remove(p.getName());
				inv.remove(p.getName());
				Bukkit.getScheduler().cancelTask(taskid.get());
				return;
			    }

			    EnumeratedDistribution<Kit> dist = new EnumeratedDistribution<Kit>(notowned);
			    Kit k = dist.sample();

			    ItemStack item = k.getInvItem().clone();
			    ItemMeta meta = item.getItemMeta();
			    meta.setDisplayName(k.getLevel().getPrefix() + k.getName());
			    meta.setLore(k.getDescription());
			    item.setItemMeta(meta);

			    allitems.add(item);

			    Inventory inven = inv.get(p.getName());

			    p.playSound(p.getLocation(), Sound.CLICK, 5, 5);

			    for (int i = 0; i < allitems.size(); i++) {
				inven.setItem(17 - i, allitems.get(i));
			    }
			    if (allitems.size() >= 9)
				allitems.remove(0);

			    previousLoopDone = true;
			    if (speed > 20) {
				recentspeed = speed;
				speed = recentspeed - decelleration;
			    } else {
				Bukkit.getScheduler().cancelTask(taskid.get());
				ItemStack stack = inv.get(p.getName()).getItem(13);
				for (Kit kit : Kit.values()) {
				    if (stack.getItemMeta().getDisplayName()
					    .equals(kit.getLevel().getPrefix() + kit.getName())) {
					chosen = kit;
					break;
				    }
				}

				pd.addKit(chosen);

				new BukkitRunnable() {
				    public void run() {

					opening.remove(p.getName());
					inv.remove(p.getName());

					if (p.isOnline()) {
					    p.closeInventory();
					    p.playSound(p.getLocation(), Sound.LEVEL_UP, 5, 5);
					    p.sendMessage("§aYou won " + chosen.getName());
					}

				    }
				}.runTaskLater(HospitalCore.inst(), 2 * 20);
			    }
			}
		    }, (int) Math.floor(400 / speed));
		}

	    }
	}.runTaskTimer(HospitalCore.inst(), 20, 1);
	taskid.set(task.getTaskId());

    }
}
