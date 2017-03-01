package de.gabik21.hospitalcore.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.gui.MuteGUI;
import de.gabik21.hospitalcore.util.BanUnit;
import de.gabik21.hospitalcore.util.MuteManager;

public class MuteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {

	if (!sender.hasPermission("Owner") && !sender.hasPermission("Moderator")
		&& !sender.hasPermission("Supporter")) {

	    sender.sendMessage("§cInsufficient permissions.");
	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("mute")) {

	    if (args.length < 2)
		return false;

	    final String toban = args[0];

	    String reasonbuilder = "";
	    for (int i = 1; i < args.length; i++)
		reasonbuilder = reasonbuilder + args[i] + " ";

	    final String reason = reasonbuilder;

	    new BukkitRunnable() {

		@SuppressWarnings("deprecation")
		public void run() {

		    UUID uuid = Bukkit.getOfflinePlayer(toban).getUniqueId();

		    if (MuteManager.isMuted(uuid.toString()))
			MuteManager.unmute(uuid.toString());

		    MuteManager.mute(uuid.toString(), toban, reason, -1, sender.getName());

		    new BukkitRunnable() {

			public void run() {

			    Bukkit.broadcastMessage("§c" + sender.getName() + " muted player " + toban + " for "
				    + reason + " for a period of forever.");

			}
		    }.runTask(HospitalCore.inst());

		}
	    }.runTaskAsynchronously(HospitalCore.inst());

	    return true;
	}

	if (cmd.getName().equalsIgnoreCase("tempmute")) {

	    if (args.length == 1 && sender instanceof Player) {

		new MuteGUI((Player) sender, args[0]);
		return true;
	    }

	    if (args.length < 4)
		return false;

	    final String toban = args[0];

	    String reasonbuilder = "";
	    for (int i = 3; i < args.length; i++)
		reasonbuilder = reasonbuilder + args[i] + " ";

	    String unitString = args[2];
	    List<String> unitList = BanUnit.getUnitsAsString();
	    if (!unitList.contains(unitString.toLowerCase()))
		return false;

	    final String reason = reasonbuilder;
	    final BanUnit unit = BanUnit.getUnit(unitString);
	    int timeint;
	    try {
		timeint = Integer.valueOf(args[1]);
	    } catch (NumberFormatException e) {

		sender.sendMessage("§cValue is too high for an Integer. INTEGER_MAX_VALUE = " + Integer.MAX_VALUE);
		return true;
	    }

	    final long time = timeint * unit.getToSecond();

	    new BukkitRunnable() {

		public void run() {

		    @SuppressWarnings("deprecation")
		    UUID uuid = Bukkit.getOfflinePlayer(toban).getUniqueId();

		    if (MuteManager.isMuted(uuid.toString()))
			MuteManager.unmute(uuid.toString());

		    MuteManager.mute(uuid.toString(), toban, reason, time + 1, sender.getName());
		    final String remaining = MuteManager.getRemainingTime(uuid.toString());

		    @SuppressWarnings("deprecation")
		    Player[] players = Bukkit.getOnlinePlayers().clone();
		    for (Player all : players)
			if (all.isOnline())
			    all.sendMessage("§c" + sender.getName() + " muted player " + toban + " for " + reason
				    + " for a period of " + remaining);

		}
	    }.runTaskAsynchronously(HospitalCore.inst());

	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("unmute")) {

	    if (args.length != 1)
		return false;

	    final String name = args[0];

	    new BukkitRunnable() {

		public void run() {

		    @SuppressWarnings("deprecation")
		    UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

		    if (!MuteManager.isMuted(uuid.toString())) {
			sender.sendMessage("§aIsn't banned.");
			return;
		    }

		    MuteManager.unmute(uuid.toString());

		    new BukkitRunnable() {

			public void run() {

			    Bukkit.broadcastMessage("§a" + name + " has been unmuted by " + sender.getName());

			}
		    }.runTask(HospitalCore.inst());

		}
	    }.runTaskAsynchronously(HospitalCore.inst());
	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("mutecheck")) {

	    if (args.length != 1)
		return false;

	    final String name = args[0];

	    new BukkitRunnable() {

		public void run() {

		    if (name.equalsIgnoreCase("list")) {

			MuteManager.getMutedPlayers(sender);
			return;

		    }

		    @SuppressWarnings("deprecation")
		    UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

		    if (!MuteManager.isMuted(uuid.toString()))
			sender.sendMessage("§aThe player isn't muted");
		    else {

			sender.sendMessage("§c-------------");
			sender.sendMessage("§cName: " + name);
			sender.sendMessage("§cReason: " + MuteManager.getReason(uuid.toString()));
			sender.sendMessage("§cTime left: " + MuteManager.getRemainingTime(uuid.toString()));
			sender.sendMessage("§cMuted by: " + MuteManager.getMuter(uuid.toString()));
			sender.sendMessage("§c-------------");

		    }

		}
	    }.runTaskAsynchronously(HospitalCore.inst());
	}

	return false;
    }
}
