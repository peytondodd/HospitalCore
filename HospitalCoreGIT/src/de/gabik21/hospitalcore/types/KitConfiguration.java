package de.gabik21.hospitalcore.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.gabik21.api.GabikAPI;

public class KitConfiguration implements Serializable {

    private static final long serialVersionUID = 6539844117052650239L;

    private static final double MAX_POINTS = 25;

    private double points;
    private List<Kit> abilities;
    private Map<Kit, SerializableItemStack> items;

    public KitConfiguration() {
	this(0, new ArrayList<Kit>(), new HashMap<Kit, SerializableItemStack>());
    }

    public KitConfiguration(int points, List<Kit> abilities, Map<Kit, SerializableItemStack> items) {
	this.points = points;
	this.abilities = abilities;
	this.items = items;
    }

    public static double getMaxPoints() {
	return MAX_POINTS;
    }

    public double getPoints() {
	return points;
    }

    public boolean canEffortAbility(Kit k) {
	if (points + k.getPoints() <= MAX_POINTS)
	    return true;
	return false;
    }

    public void addAbility(Kit k) {
	abilities.add(k);
	points += k.getPoints();
    }

    public void removeAbility(Kit k) {
	if (abilities.remove(k)) {
	    points -= k.getPoints();
	    items.remove(k);
	}
    }

    public List<Kit> getAbilities() {
	return abilities;
    }

    public Map<Kit, SerializableItemStack> getKitItems() {
	return items;
    }

    public boolean contains(Kit k) {
	return abilities.contains(k);
    }

    public String getNames() {

	StringBuilder names = new StringBuilder();
	List<Kit> abilities = getAbilities();
	if (abilities.size() == 0) {
	    return "Nothing";
	}
	for (int i = 0; i < abilities.size(); i++) {
	    if (i > 0) {
		names.append(", ");
	    }
	    names.append(abilities.get(i).getName());
	}
	return names.toString();
    }

    public List<ItemStack> getItems() {

	List<ItemStack> items = new ArrayList<ItemStack>();
	for (SerializableItemStack stack : this.items.values()) {
	    items.add(stack.get());
	}
	for (Kit kit : getAbilities())
	    for (ItemStack stack : kit.getItems())
		items.add(stack);

	return items;

    }

    public Material getKitItem(Kit kit) {
	return items.get(kit).get().getType();
    }

    public void kitItem(Kit kit, ItemStack item) {
	items.remove(kit);
	items.put(kit,
		new SerializableItemStack(
			GabikAPI.createItem(item.getType(), kit.getLevel().getPrefix() + kit.getName()),
			kit.getLevel().getPrefix() + kit.getName()));
    }

}
