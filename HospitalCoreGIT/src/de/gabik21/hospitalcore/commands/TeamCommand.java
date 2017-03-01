package de.gabik21.hospitalcore.commands;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.types.Team;

public class TeamCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isIngame()) {

	    p.sendMessage("§6§l[§cTeam§6§l] §b§lYou can't do that ingame!");
	    return true;
	}

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("deny")) {

		if (pd.getTeaminvitation() != null)
		    pd.getTeaminvitation().getLeader().sendMessage("§6§l[§cTeam§6§l] §2" + "");
		pd.setTeaminvitation(null);
		p.sendMessage("§6§l[§cTeam§6§l] §2" + "§aSucessfully denied your current team invitation!");
		return true;

	    }

	    if (args[0].equalsIgnoreCase("leave")) {

		if (pd.getTeam() != null) {
		    pd.getTeam().removePlayer(p);
		    p.sendMessage("§6§l[§cTeam§6§l] §b" + "Left the team.");
		}

		else
		    p.sendMessage("§6§l[§cTeam§6§l] §b" + "§cYou aren't in a team.");

		return true;
	    }

	    if (args[0].equalsIgnoreCase("accept")) {

		if (pd.getTeaminvitation() != null) {
		    if (pd.getTeaminvitation().isQueued() || pd.getTeaminvitation().getCurrentArena() != -1
			    || pd.getTeaminvitation().getCurrentTeamtagArena() != -1) {
			p.sendMessage("§cYou can't join the team right now.");
			return true;
		    }

		    p.sendMessage("§6§l[§cTeam§6§l] §b" + "§aEntered the team. Type /t [message] for team chat");
		    pd.getTeaminvitation().addPlayer(p);
		    pd.setTeaminvitation(null);

		} else
		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§cYou don't have a team invitation");

		return true;
	    }

	    if (args[0].equalsIgnoreCase("create")) {

		if (pd.getTeam() == null) {
		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§aCreating a team...");
		    pd.setTeam(new Team(p));
		}

		return true;
	    }

	}

	if (args.length == 2) {

	    if (args[0].equalsIgnoreCase("invite")) {

		if (pd.getTeam() == null) {

		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§aCreating a team...");
		    pd.setTeam(new Team(p));
		} else if (pd.getTeam().getLeader() != p) {

		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§cYou can only perform this action as the team leader");
		    return true;

		}

		if (pd.isIngame()) {

		    p.sendMessage("§6§l[§cTeam§6§l] §b§lYou can't do that ingame!");
		    return true;
		}

		if (pd.getTeam().isQueued()) {

		    p.sendMessage("§6§l[§cTeam§6§l] §b§lYou can't do that in queue!");
		    return true;
		}

		Player t = Bukkit.getPlayer(args[1]);

		if (t == null) {

		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§cThis player doesn't exist or isn't online!");
		    return true;
		}

		if (p == t) {

		    p.sendMessage("§6§l[§cTeam§6§l] §cInviting yourself? Seems legit.");
		    return true;
		}

		PlayerData td = HospitalCore.getData(t);

		if (td.getTeaminvitation() != null) {

		    p.sendMessage("§6§l[§cTeam§6§l] §2" + "§cThis player already has an invitation.");
		    return true;
		}

		if (td.getTeam() == pd.getTeam()) {

		    p.sendMessage("§6§l[§cTeam§6§l] §2§cThis player already is in your team!");
		    return true;

		}

		td.setTeaminvitation(pd.getTeam());

		p.sendMessage("§6§l[§cTeam§6§l] §2" + "§aThe invitation has been sent.");

		TextComponent tc1 = PlayerData.createComponent("§a[Accept] ", "§aYou accept by clicking here!",
			"team accept");

		TextComponent tc2 = PlayerData.createComponent("§c[Deny]", "§cYou deny by clicking here", "team deny");
		t.spigot().sendMessage(
			new TextComponent("§6§l[§cTeam§6§l] §2" + "§e" + p.getName() + " invited you into a team! "),
			tc1, tc2);

		return true;
	    }

	}

	return false;
    }

}
