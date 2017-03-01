package de.gabik21.hospitalcore.abilities.blue;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Gambler extends Ability {

    final static long GAMBLER_COOLDOWN = 55000;

    @Override
    public void onInteract(PlayerInteractEvent e) {

	Block b = e.getClickedBlock();
	if (b == null || b.getType() != Material.STONE_BUTTON)
	    return;

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isOnCooldown(Kit.GAMBLER))
	    return;
	pd.useKit(Kit.GAMBLER);

	int random = randomint(1, 6);

	switch (random) {
	case 1:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 0));
	    p.sendMessage("§9You win: §6speed");
	    break;
	case 2:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 0));
	    p.sendMessage("§9You win: §6slowness");
	    break;
	case 3:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15 * 20, 0));
	    p.sendMessage("§9You win: §6strength");
	    break;
	case 4:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30 * 20, 0));
	    p.sendMessage("§9You win: §6weakness");
	    break;
	case 5:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 0));
	    p.sendMessage("§9You win: §6regeneration");
	    break;
	case 6:
	    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30 * 20, 0));
	    p.sendMessage("§9You win: §6poison");
	    break;
	default:
	    p.sendMessage("Something went wrong. Report this to an admin.");
	    System.out.println("Gambler went wrong");
	}

    }

    public int randomint(int min, int max) {

	return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public long getCooldown() {
	return GAMBLER_COOLDOWN;
    }

}
