package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gabik21.hospitalcore.types.Ability;

public class Camel extends Ability {

    @Override
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	Block down = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

	if (down.getType() != Material.SAND && down.getType() != Material.AIR) {

	    p.removePotionEffect(PotionEffectType.SPEED);
	    return;
	}

	if (down.getType() != Material.SAND)
	    return;

	p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

    }
}
