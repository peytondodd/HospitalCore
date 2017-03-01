package de.gabik21.hospitalcore.commands;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.MuteManager;

public class MsgCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command cmd, String l, String[] args) {

	final Player player = Bukkit.getPlayerExact(args[0]);
	final PlayerData pd = HospitalCore.getData(player);

	if (player == null) {
	    sender.sendMessage("§cThere's no player by that name online.");
	} else {
	    final StringBuilder message = new StringBuilder();
	    for (int i = 1; i < args.length; i++) {
		if (i > 1)
		    message.append(" ");
		message.append(args[i]);
	    }

	    final AtomicReference<String> result = new AtomicReference<String>(
		    "§6[§e" + sender.getName() + " §6-> §eYou§6] " + message);

	    if (sender instanceof Player) {
		pd.setReply((Player) sender);
		HospitalCore.getData((Player) sender).setReply(player);
		result.set("§6[§e" + HospitalCore.getData((Player) sender).getNick() + " §6-> §eYou§6] " + message);
	    }

	    new BukkitRunnable() {

		public void run() {

		    if (sender instanceof Player) {
			if (MuteManager.isMuted(((Player) sender).getUniqueId().toString())) {

			    sender.sendMessage("§cYou currently muted for "
				    + MuteManager.getReason(((Player) sender).getUniqueId().toString()));
			    sender.sendMessage("§cReamining Time: "
				    + MuteManager.getRemainingTime(((Player) sender).getUniqueId().toString()));

			    return;
			}
		    }

		    sender.sendMessage("§6[§eYou §6-> §e" + pd.getNick() + "§6] " + message);
		    player.sendMessage(result.get());
		}
	    }.runTaskAsynchronously(HospitalCore.inst());

	}

	return true;
    }
}
