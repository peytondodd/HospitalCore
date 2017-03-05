package de.gabik21.hospitalcore.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.gabik21.api.GabikAPI;

public enum Inventories {

    RC32,
    RC64,
    SPAWN(
	    new ItemStack[] { GabikAPI.createItem(Material.ANVIL, "§1Kitcreation"), null,
		    GabikAPI.createItem(Material.ENCHANTMENT_TABLE, "§5Market"), null,
		    GabikAPI.createItem(Material.COMPASS, "§4Gamemodes"), null,
		    GabikAPI.createItem(Material.CHEST, "§2Chests"), null, }),
    ADMIN(
	    new ItemStack[] { GabikAPI.createItem(Material.BOOK, "§aReports"),
		    GabikAPI.createItem(Material.CARPET, "§aInv-Viewer"), null, null,
		    GabikAPI.createItem(Material.BLAZE_ROD, "§aFreezer"), null, null, null,
		    GabikAPI.createItem(Material.ARROW, "§aVisibility") }),
    MARKET(
	    new ItemStack[] { GabikAPI.createItem(Material.BLAZE_ROD, "§6Trade wand"), null, null, GabikAPI.createItem(Material.ENCHANTMENT_TABLE, "§4Market"), null,
		    GabikAPI.createItem(Material.BEACON, "§4Sell an ability"), null, null,
		    GabikAPI.createItem(Material.SLIME_BALL, "§4Back to spawn") }),
    KITCREATOR(
	    new ItemStack[] { null, null, null, null, null, null, null, null,
		    GabikAPI.createItem(Material.SLIME_BALL, "§4Back to spawn") });

    private ItemStack[] contents;

    private Inventories() {
    }

    private Inventories(ItemStack[] contents) {
	this.contents = contents;
    }

    public void setContents(ItemStack[] contents) {
	this.contents = contents;
    }

    public ItemStack[] getContents() {
	return contents;
    }

    public void apply(Player p) {
	p.getInventory().setContents(contents);
    }

}
