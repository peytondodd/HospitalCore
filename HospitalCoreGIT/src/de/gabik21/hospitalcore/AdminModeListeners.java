package de.gabik21.hospitalcore;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.gabik21.hospitalcore.gui.ReportGui;
import de.gabik21.hospitalcore.types.PlayerData;

public class AdminModeListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAdminDrop(PlayerDropItemEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (!pd.isInAdminmode())
	    return;

	if (e.getItemDrop().getItemStack().hasItemMeta()
		&& e.getItemDrop().getItemStack().getItemMeta().hasDisplayName())
	    e.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAdminPickUp(PlayerPickupItemEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isInAdminmode())
	    e.setCancelled(true);

    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;
	if (!(e.getDamager() instanceof Player))
	    return;

	Player p = (Player) e.getDamager();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isInAdminmode() && pd.isDisguised())
	    e.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAdminInteract(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.isInAdminmode())
	    return;

	if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK))
	    return;

	if (p.getItemInHand().getType() == Material.BOOK) {
	    new ReportGui(54, "§cReports", p);
	    e.setCancelled(true);
	}

	if (p.getItemInHand().getType() == Material.ARROW) {
	    p.performCommand("vanish");
	    e.setCancelled(true);
	}

    }

    @EventHandler
    public void blockbreak(BlockPlaceEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!pd.isInAdminmode())
	    return;

	if (p.getItemInHand().getType() == Material.CARPET && p.getItemInHand().hasItemMeta()
		&& p.getItemInHand().getItemMeta().hasDisplayName())
	    e.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAdminInteractEntity(PlayerInteractEntityEvent e) {

	if (!(e.getRightClicked() instanceof Player))
	    return;

	Player p = e.getPlayer();
	Player t = (Player) e.getRightClicked();

	PlayerData pd = HospitalCore.getData(p);

	if (!pd.isInAdminmode())
	    return;

	if (p.getItemInHand().getType() == Material.BLAZE_ROD)
	    p.performCommand("freeze " + t.getName());

	if (p.getItemInHand().getType() == Material.CARPET)
	    p.openInventory(t.getInventory());

    }

}
