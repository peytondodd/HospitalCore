package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.gabik21.hospitalcore.types.Ability;

public class Noob2 extends Ability {

    @Override
    public void onDrop(PlayerDropItemEvent e) {
	if (e.getItemDrop().getItemStack().getType() == Material.MUSHROOM_SOUP)
	    e.setCancelled(true);
    }
}
