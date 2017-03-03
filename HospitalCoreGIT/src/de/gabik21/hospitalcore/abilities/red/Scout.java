package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionType;

import de.gabik21.api.GabikAPI;
import de.gabik21.hospitalcore.types.Ability;

public class Scout extends Ability {

    @Override
    public void onKill(PlayerDeathEvent e) {
	e.getDrops().add(GabikAPI.createPotion(PotionType.SPEED, true, 2));
    }
}
