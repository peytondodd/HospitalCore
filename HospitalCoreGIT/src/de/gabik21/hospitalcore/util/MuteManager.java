package de.gabik21.hospitalcore.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.gabik21.hospitalcore.MySQL;

public class MuteManager {

    public static void mute(String uuid, String playername, String reason, long seconds, String muter) {

	long end = 0L;

	if (seconds == -1)
	    end = -1;
	else
	    end = System.currentTimeMillis() + (seconds * 1000);

	try {

	    PreparedStatement ps = MySQL.getConnection()
		    .prepareStatement("INSERT INTO mutes (Name, UUID, End, Reason, Muter) VALUES(?,?,?,?,?)");
	    ps.setString(1, playername);
	    ps.setString(2, uuid);
	    ps.setString(3, String.valueOf(end));
	    ps.setString(4, reason);
	    ps.setString(5, muter);

	    ps.executeUpdate();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static void unmute(String uuid) {

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM mutes WHERE UUID = ?");
	    ps.setString(1, uuid);

	    ps.executeUpdate();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static boolean isMuted(String uuid) {

	if (getEnd(uuid) == null)
	    return false;

	if (System.currentTimeMillis() > getEnd(uuid) && getEnd(uuid) > -1) {

	    unmute(uuid);
	    return false;
	}

	return true;

    }

    public static String getReason(String uuid) {

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mutes WHERE UUID = ?");
	    ps.setString(1, uuid);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next())
		return rs.getString("Reason");

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return null;
    }

    public static String getMuter(String uuid) {

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mutes WHERE UUID = ?");
	    ps.setString(1, uuid);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next())
		return rs.getString("Muter");

	} catch (SQLException e) {

	    e.printStackTrace();

	}
	return null;

    }

    public static Long getEnd(String uuid) {

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mutes WHERE UUID = ?");
	    ps.setString(1, uuid);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next())
		return Long.valueOf(rs.getLong("End"));

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return null;

    }

    public static List<String> getMutedPlayers(CommandSender sender) {

	List<String> temp = new ArrayList<String>();

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mutes");
	    ResultSet rs = ps.executeQuery();

	    while (rs.next())
		temp.add(rs.getString("Name"));

	} catch (SQLException e) {

	    e.printStackTrace();

	}
	Iterator<String> litr = temp.iterator();

	while (litr.hasNext()) {
	    String s = litr.next();
	    @SuppressWarnings("deprecation")
	    OfflinePlayer p = Bukkit.getOfflinePlayer(s);
	    if (p != null && !isMuted(p.getUniqueId().toString())) {
		litr.remove();
	    } else {
		sender.sendMessage("§e" + s);
	    }
	}
	sender.sendMessage("§e" + temp.size() + " players muted in total");
	return temp;

    }

    public static String getRemainingTime(String uuid) {

	if (getEnd(uuid) == -1)
	    return "§4Permanent";

	long time = getEnd(uuid) - System.currentTimeMillis();

	long seconds = (time / 1000) % 60;
	long minutes = ((time / (1000 * 60)) % 60);
	long hours = ((time / (1000 * 60 * 60)) % 24);
	long days = ((time / (1000 * 60 * 60 * 24)));

	String returnstring = "§c";

	if (days > 0)
	    returnstring = returnstring + days + " day(s) ";

	if (hours > 0)
	    returnstring = returnstring + hours + " hour(s) ";

	if (minutes > 0)
	    returnstring = returnstring + minutes + " minute(s) ";

	if (seconds > 0)
	    returnstring = returnstring + seconds + " second(s)";

	return returnstring;

    }

}
