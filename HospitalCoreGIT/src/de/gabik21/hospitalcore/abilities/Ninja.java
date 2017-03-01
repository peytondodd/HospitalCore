package de.gabik21.hospitalcore.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Ability;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.PlayerData;

public class Ninja extends Ability {

    private final static long NINJA_COOLDOWN = 8000;

    @Override
    public void onToggleSneak(PlayerToggleSneakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = HospitalCore.getData(p);

	if (!e.isSneaking() || (System.currentTimeMillis() - pd.getLasthit()) > 15000)
	    return;

	if (pd.isOnCooldown(Kit.NINJA))
	    return;

	PlayerData td = HospitalCore.getData(pd.getLastDamaged());

	if (pd.getLastDamaged() != null && pd.getLastDamaged().isOnline() && td.isIngame()
		&& (!td.isIn1v1() && !pd.isIn1v1() || pd.isIn1v1() && td.isIn1v1())) {

	    p.teleport(pd.getLastDamaged(), TeleportCause.COMMAND);
	    pd.useKit(Kit.NINJA);

	}

    }

    @Override
    public long getCooldown() {
	return NINJA_COOLDOWN;
    }

}
