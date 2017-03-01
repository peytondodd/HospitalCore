package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.types.Ability;

public class Noob extends Ability {

    @Override
    public void onDrop(PlayerDropItemEvent e) {
	if (e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD
		&& !e.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.STONE_SWORD), 2))
	    e.setCancelled(true);
    }

}
