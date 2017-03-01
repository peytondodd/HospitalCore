package de.gabik21.hospitalcore.util;

import java.util.ArrayList;
import java.util.List;

public enum BanUnit {

    SECONDS(
	    "Sekunde(n)",
	    1L,
	    "s"),
    MINUTES(
	    "Minuten(n)",
	    60L,
	    "m"),
    HOUR(
	    "Stunde(n)",
	    3600L,
	    "h"),
    DAY(
	    "Tag(e)",
	    86400L,
	    "d"),
    WEEK(
	    "Woche(n)",
	    604800L,
	    "w");

    private String name;
    private long toSecond;
    private String shortcut;

    private BanUnit(String name, long toSecond, String shortcut) {
	this.name = name;
	this.toSecond = toSecond;
	this.shortcut = shortcut;
    }

    public long getToSecond() {
	return this.toSecond;
    }

    public String getName() {
	return this.name;
    }

    public String getShortcut() {
	return this.shortcut;
    }

    public static List<String> getUnitsAsString() {
	List<String> units = new ArrayList<String>();
	BanUnit[] arrayOfBanUnit;
	int j = (arrayOfBanUnit = values()).length;
	for (int i = 0; i < j; i++) {
	    BanUnit unit = arrayOfBanUnit[i];
	    units.add(unit.getShortcut().toLowerCase());
	}
	return units;
    }

    public static BanUnit getUnit(String unit) {
	BanUnit[] arrayOfBanUnit;
	int j = (arrayOfBanUnit = values()).length;
	for (int i = 0; i < j; i++) {
	    BanUnit units = arrayOfBanUnit[i];
	    if (units.getShortcut().toLowerCase().equals(unit.toLowerCase())) {
		return units;
	    }
	}
	return null;
    }

}
