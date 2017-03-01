package de.gabik21.hospitalcore.abilities.red;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.api.GabikAPI;
import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Phantom extends Ability {

    final static long PHANTOM_COOLDOWN = 60000;
    private List<String> phantomflying = new ArrayList<String>();

    @Override
    public void activate(final Player p) {

	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig().getKitItem(Kit.PHANTOM) != p.getItemInHand().getType() || pd.isOnCooldown(Kit.PHANTOM))
	    return;

	for (Entity nearby : p.getNearbyEntities(100, 200, 100)) {
	    if (!(nearby instanceof Player))
		continue;
	    ((Player) nearby)
		    .sendMessage("§lA Phantom approaches...\nNote: they are not fly hacking, it's part of the kit.");
	}

	pd.useKit(Kit.PHANTOM);

	final ItemStack[] armor = p.getInventory().getArmorContents();

	ItemStack boots = GabikAPI.createLeatherBoots(Color.WHITE);
	ItemStack leggins = GabikAPI.createLeatherLeggins(Color.WHITE);
	ItemStack chest = GabikAPI.createLeatherChestplate(Color.WHITE);
	ItemStack helmet = GabikAPI.createLeatherHelmet(Color.WHITE);
	boots.setDurability((short) -1);
	leggins.setDurability((short) -1);
	chest.setDurability((short) -1);
	helmet.setDurability((short) -1);

	ItemStack[] phantomcontent = new ItemStack[] { boots, leggins, chest, helmet };

	p.getWorld().playSound(p.getLocation(), Sound.WITHER_DEATH, 10F, 10F);

	p.getInventory().setArmorContents(phantomcontent);
	p.setAllowFlight(true);
	p.updateInventory();

	phantomflying.add(p.getName());

	new BukkitRunnable() {

	    int i = 6;

	    public void run() {

		if (!p.isOnline()) {

		    phantomflying.remove(p.getName());
		    cancel();
		    return;
		}

		if (i <= 4 && i > 0)
		    p.sendMessage("§c" + i + " seconds of flight remaining");

		if (i == 0) {

		    p.getInventory().setArmorContents(armor);
		    p.setAllowFlight(false);
		    p.getWorld().playSound(p.getLocation(), Sound.WITHER_SPAWN, 0.1F, 5);
		    phantomflying.remove(p.getName());
		    Bukkit.getPluginManager().callEvent(new PlayerToggleFlightEvent(p, false));

		    cancel();

		}
		i--;

	    }
	}.runTaskTimer(HospitalCore.inst(), 0, 20);

    }

    @Override
    public void onInvClick(InventoryClickEvent e) {

	Player p = (Player) e.getWhoClicked();

	if (phantomflying.contains(p.getName()) && e.getInventory() != null && e.getSlot() > 35)
	    e.setCancelled(true);

    }

    @Override
    public long getCooldown() {
	return PHANTOM_COOLDOWN;
    }
}
