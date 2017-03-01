package de.gabik21.hospitalcore.abilities.red;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Monk extends Ability {

    final static long MONK_COOLDOWN = 15000;

    @Override
    public void onInteractEntity(PlayerInteractEntityEvent e) {

	if (!(e.getRightClicked() instanceof Player))
	    return;

	Player p = e.getPlayer();

	PlayerData pd = HospitalCore.getData(p);

	if (p.getItemInHand().getType() != pd.getKitConfig().getKitItem(Kit.MONK) || pd.isOnCooldown(Kit.MONK))
	    return;

	pd.useKit(Kit.MONK);

	Player t = (Player) e.getRightClicked();
	int random = randomint(9, 34);

	ItemStack scrambled = t.getInventory().getItem(random);
	ItemStack toScramble = t.getItemInHand();

	if (toScramble.getType() == Material.AIR)
	    return;

	t.getInventory().removeItem(toScramble);

	if (scrambled != null)
	    t.getInventory().removeItem(scrambled);

	t.getInventory().setItem(random, toScramble);

	if (scrambled != null) {
	    t.getInventory().setItemInHand(scrambled);
	    t.getInventory().addItem(scrambled);
	}

	p.sendMessage("§aMonked!");

    }

    public int randomint(int min, int max) {

	return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public long getCooldown() {
	return MONK_COOLDOWN;
    }

}
