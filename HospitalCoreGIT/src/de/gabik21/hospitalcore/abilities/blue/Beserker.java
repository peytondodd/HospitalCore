package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gabik21.hospitalcore.types.Ability;

public class Beserker extends Ability {

    @Override
    public void onKill(PlayerDeathEvent e) {

	Player p = e.getEntity().getKiller();
	p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7 * 20, 0));
	p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1));

    }

}
