package de.gabik21.hospitalcore.types;

public enum KitLevel {

    BLUE(
	    "§9",
	    50),
    PURPLE(
	    "§5",
	    35),
    RED(
	    "§c",
	    12),
    DARKRED(
	    "§4",
	    7.25),
    GOLD(
	    "§6",
	    5.25),
    PLATIN(
	    "§4§l[LOL] §4",
	    0.1);

    private final String prefix;
    private final double percent;

    private KitLevel(String prefix, double percent) {
	this.prefix = prefix;
	this.percent = percent;
    }

    public String getPrefix() {
	return prefix;
    }

    public double getPercent() {
	return percent;
    }

}
