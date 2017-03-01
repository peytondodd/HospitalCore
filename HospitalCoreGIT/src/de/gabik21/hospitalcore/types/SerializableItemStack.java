package de.gabik21.hospitalcore.types;

import java.io.Serializable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SerializableItemStack extends ItemStack implements Serializable {
    private String name;
    private Material mat;
    private static final long serialVersionUID = -2715792697458777918L;

    public SerializableItemStack(ItemStack item, String name) {
	super(item);
	this.mat = item.getType();
	this.name = name;
	getItemMeta().setDisplayName(name);
    }

    public ItemStack get() {
	ItemStack stack = new ItemStack(this.mat);
	ItemMeta meta = stack.getItemMeta();
	meta.setDisplayName(this.name);
	stack.setItemMeta(meta);

	return stack;
    }

    public void setName(String name) {
	ItemMeta meta = getItemMeta();
	meta.setDisplayName(name);
	setItemMeta(meta);
	this.name = name;
    }
}
