package de.gabik21.hospitalcore.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.gabik21.hospitalcore.types.LeftRightAbstractGUI;
import de.gabik21.hospitalcore.types.Report;

public class ReportGui extends LeftRightAbstractGUI {

    private int i = 0;

    public ReportGui(int slots, String title, Player player) {
	super(slots, title, player);

	for (final Report report : Report.list) {

	    final int current = i;

	    ItemStack skull = new ItemStack(Material.SKULL_ITEM);
	    skull.setDurability((short) 3);
	    SkullMeta sm = (SkullMeta) skull.getItemMeta();
	    sm.setOwner(report.getReported());
	    sm.setDisplayName("§c" + report.getReported());

	    List<String> lore = new ArrayList<String>();
	    lore.add("");
	    lore.add("§6Reported by: §c" + report.getReporter());
	    lore.add("§6Reason: " + report.getReason());
	    lore.add("");
	    lore.add("§a§lLeft click to check the player");
	    lore.add("§a§lRight click to remove the report");
	    sm.setLore(lore);

	    skull.setItemMeta(sm);

	    AbstractAction action = new AbstractAction() {

		public void click(Player player) {

		    if (report != null) {

			player.performCommand("check " + report.getReported());
		    }

		}
	    };

	    AbstractAction right = new AbstractAction() {

		public void click(Player player) {
		    report.remove();
		    setItem(new ItemStack(Material.AIR), current, new AbstractAction() {
			public void click(Player player) {
			}
		    });

		}
	    };
	    setItem(skull, i, action, right);
	    i++;

	}

	player.openInventory(getInventory());

    }

}
