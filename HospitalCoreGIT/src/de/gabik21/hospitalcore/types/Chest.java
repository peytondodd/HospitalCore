package de.gabik21.hospitalcore.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	final PlayerData pd = HospitalCore.getData(p);
	final List<Pair<Kit, Double>> notowned = new ArrayList<Pair<Kit, Double>>();
	for (Kit k : Kit.values())
	    notowned.add(new Pair<Kit, Double>(k, k.getLevel().getPercent()));
	p.openInventory(inv.get(p.getName()));

	new BukkitRunnable() {
	    int i = 0;
	    List<ItemStack> allitems = new ArrayList<ItemStack>();
	    Kit chosen;

	    public void run() {

		if (!p.isOnline()) {

		    opening.remove(p.getName());
		    inv.remove(p.getName());
		    cancel();
		    return;
		}

		EnumeratedDistribution<Kit> dist = new EnumeratedDistribution<Kit>(notowned);
		Kit k = dist.sample();

		ItemStack item = k.getInvItem().clone();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(k.getLevel().getPrefix() + k.getName());
		meta.setLore(k.getDescription());
		item.setItemMeta(meta);

		if (i == 27)
		    chosen = k;
		allitems.add(item);

		Inventory inven = inv.get(p.getName());

		p.playSound(p.getLocation(), Sound.CLICK, 5, 5);

		for (int i = 0; i < allitems.size(); i++) {
		    inven.setItem(17 - i, allitems.get(i));
		}
		if (allitems.size() >= 9)
		    allitems.remove(0);

		if (i > 30) {

		    pd.addKit(chosen);
		    p.playSound(p.getLocation(), Sound.LEVEL_UP, 5, 5);
		    p.sendMessage("§aYou won " + chosen.getName());

		    new BukkitRunnable() {

			public void run() {

			    opening.remove(p.getName());
			    inv.remove(p.getName());

			    if (p.isOnline())
				p.closeInventory();

			}
		    }.runTaskLater(HospitalCore.inst(), 3 * 20);

		    cancel();
		    return;
		}

		i++;

	    }
	}.runTaskTimer(HospitalCore.inst(), 20, 5);

    }
}
