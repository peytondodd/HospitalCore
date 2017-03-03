package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import de.gabik21.hospitalcore.types.Ability;

public class FisherMan extends Ability {

    @Override
    public void onFish(PlayerFishEvent e) {

	Player p = e.getPlayer();

	if (!(e.getCaught() instanceof Player))
	    return;

	Player t = (Player) e.getCaught();

	p.getItemInHand().setDurability((short) 0);

	t.teleport(p);

    }
}
