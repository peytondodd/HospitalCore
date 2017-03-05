package de.gabik21.hospitalcore;

import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class AbilityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {

	Player p = e.getEntity();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isIngame() && pd.getKitConfig() != null) {

	    Iterator<ItemStack> litr = e.getDrops().iterator();
	    while (litr.hasNext()) {
		ItemStack stack = litr.next();

		for (ItemStack d : pd.getKitConfig().getItems())
		    if (stack.getType().equals(d.getType())) {
			litr.remove();
			break;
		    }

	    }

	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onDeath(e);

	}

	if (p.getKiller() != null) {

	    PlayerData kd = HospitalCore.getData(p.getKiller());

	    if (kd != null && kd.isIngame() && kd.getKitConfig() != null)
		for (Kit kit : kd.getKitConfig().getAbilities())
		    kit.getAbility().onKill(e);

	}
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemSwitch(PlayerItemHeldEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onItemSwitch(e);
	ItemStack itemInHand = p.getInventory().getContents()[e.getNewSlot()];
	if (pd.getKitConfig() != null && itemInHand != null && itemInHand.hasItemMeta()
		&& itemInHand.getItemMeta().hasDisplayName()) {
	    for (Kit kit : pd.getKitConfig().getAbilities()) {
		if (pd.getKitConfig().getKitItem(kit) != null
			&& pd.getKitConfig().getKitItem(kit) == itemInHand.getType()) {
		    ItemMeta meta = itemInHand.getItemMeta();
		    meta.setDisplayName(
			    kit.getLevel().getPrefix() + kit.getName() + pd.getPerKitCooldown().getSeconds(kit));
		    itemInHand.setItemMeta(meta);
		    break;
		}
	    }
	}

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.getKitConfig() == null)
	    return;

	for (Kit k : pd.getKitConfig().getAbilities())
	    k.getAbility().onDrop(e);

	for (ItemStack d : pd.getKitConfig().getItems())
	    if (e.getItemDrop().getItemStack().getType() == d.getType()) {
		e.setCancelled(true);
		p.sendMessage("§cYou can not drop your kit items");
		break;
	    }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isIngame() && pd.getKitConfig() != null) {

	    for (Kit kit : pd.getKitConfig().getAbilities()) {
		kit.getAbility().onInteract(e);

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		    kit.getAbility().activate(e.getPlayer());

		if (e.getAction() != Action.PHYSICAL)
		    kit.getAbility().click(e.getPlayer());
	    }

	}

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractEntity(PlayerInteractEntityEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onInteractEntity(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onMove(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFish(PlayerFishEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onFish(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onToggleSneak(PlayerToggleSneakEvent e) {

	PlayerData pd = HospitalCore.getData(e.getPlayer());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onToggleSneak(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeash(PlayerLeashEntityEvent e) {
	e.setCancelled(true);
	e.getPlayer().updateInventory();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	PlayerData pd = HospitalCore.getData((Player) e.getEntity());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onDamage(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
	    return;

	PlayerData pd = HospitalCore.getData((Player) e.getDamager());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onPlayerAttack(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamagedByPlayer(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
	    return;

	PlayerData pd = HospitalCore.getData((Player) e.getEntity());

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onPlayerDamagedByPlayer(e);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInvClick(InventoryClickEvent e) {

	Player p = (Player) e.getWhoClicked();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onInvClick(e);

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Projectile))
	    return;

	Projectile p = (Projectile) e.getDamager();

	if (!(p.getShooter() instanceof Player))
	    return;

	Player player = (Player) p.getShooter();

	if (!player.isOnline())
	    return;

	PlayerData pd = HospitalCore.getData(player);

	if (pd.isIngame() && pd.getKitConfig() != null)
	    for (Kit kit : pd.getKitConfig().getAbilities())
		kit.getAbility().onProjectileHit(e);

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileLaunch(ProjectileLaunchEvent e) {

	final Projectile projec = e.getEntity();

	new BukkitRunnable() {

	    public void run() {

		if (projec.isDead()) {
		    cancel();
		    return;
		}

		for (Entity ent : projec.getNearbyEntities(1.9, 2.5, 1.9)) {

		    if (!(ent instanceof Player) || ent == projec.getShooter())
			continue;

		    Player p = (Player) ent;
		    PlayerData pd = HospitalCore.getData(p);

		    if ((pd.getKitConfig() != null && !pd.getKitConfig().contains(Kit.NEO))
			    || pd.getKitConfig() == null)
			continue;

		    projec.setMetadata("neo", new FixedMetadataValue(HospitalCore.inst(), true));
		    projec.getLocation().setDirection(projec.getLocation().getDirection().multiply(-1));
		    projec.setVelocity(projec.getVelocity().multiply(-1));

		}

		if (projec.isOnGround())
		    cancel();

	    }
	}.runTaskTimer(HospitalCore.inst(), 0, 1);

    }
}
