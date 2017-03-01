package de.gabik21.hospitalcore.types;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public abstract class Ability {

    public void activate(Player p) {
    }

    public void click(Player p) {
    }

    public void onDamage(EntityDamageEvent e) {
    }

    public void onPlayerAttack(EntityDamageByEntityEvent e) {
    }

    public void onPlayerDamagedByPlayer(EntityDamageByEntityEvent e) {
    }

    public void onMove(PlayerMoveEvent e) {
    }

    public void onFish(PlayerFishEvent e) {
    }

    public void onToggleSneak(PlayerToggleSneakEvent e) {
    }

    public void onInteract(PlayerInteractEvent e) {
    }

    public void onInteractEntity(PlayerInteractEntityEvent e) {
    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    }

    public void onProjectileHit(EntityDamageByEntityEvent e) {
    }

    public void onKill(PlayerDeathEvent e) {
    }

    public void onProjectileLaunch(ProjectileLaunchEvent e) {
    }

    public void onInvClick(InventoryClickEvent e) {
    }

    public void onItemSwitch(PlayerItemHeldEvent e) {
    }

    public void onDrop(PlayerDropItemEvent e) {
    }
    
    public void onDeath(PlayerDeathEvent e) {
    }

    public long getCooldown() {
	return 0;
    }

}
