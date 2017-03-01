package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import de.gabik21.hospitalcore.types.Ability;

public class Monkey extends Ability {

    @Override
    public void onToggleSneak(PlayerToggleSneakEvent e) {

	Player p = e.getPlayer();

	if (e.isSneaking() && p.getVelocity().getY() <= 0 && checkforWall(p)) {
	    Vector vec = p.getLocation().getDirection().multiply(2.1);
	    p.setVelocity(vec);

	    p.setFallDistance(((float) -(Math.abs(vec.getY()) * 5)) + p.getFallDistance());
	}

    }

    @Override
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();

	if (p.isSneaking() && p.getVelocity().getY() <= 0 && checkforWall(p)) {
	    Vector vec = p.getLocation().getDirection().multiply(2.1);
	    p.setVelocity(vec);

	    p.setFallDistance(((float) -(Math.abs(vec.getY()) * 5)) + p.getFallDistance());
	}

    }

    private boolean checkforWall(Player item) {

	Block itemb = item.getLocation().getBlock();

	if (itemb.getRelative(BlockFace.NORTH).getType().isSolid()
		|| itemb.getRelative(BlockFace.NORTH).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.SOUTH).getType().isSolid()
		|| itemb.getRelative(BlockFace.SOUTH).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.WEST).getType().isSolid()
		|| itemb.getRelative(BlockFace.WEST).getType() == Material.GLASS)
	    return true;

	if (itemb.getRelative(BlockFace.EAST).getType().isSolid()
		|| itemb.getRelative(BlockFace.EAST).getType() == Material.GLASS)
	    return true;

	return false;
    }

}
