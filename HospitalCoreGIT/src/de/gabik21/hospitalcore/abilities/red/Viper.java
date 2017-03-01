package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gabik21.hospitalcore.types.Ability;

public class Viper extends Ability {

    @Override
    public void onPlayerAttack(EntityDamageByEntityEvent e) {

	Player t = (Player) e.getEntity();

	int rand = (int) (Math.random() * 100);

	if (rand < 80)
	    return;

	t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));

    }

}
