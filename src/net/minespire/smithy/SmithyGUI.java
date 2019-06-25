package net.minespire.smithy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import net.md_5.bungee.api.ChatColor;

public class SmithyGUI {
	private Inventory GUI;
	private ItemStack[] guiItem;
	
	public SmithyGUI(int slots, String name) {
		guiItem = new ItemStack[slots];
		GUI = Bukkit.createInventory(null, slots, name);
	}
	
	
	public void makeGuiItem(String name, String lore, Material material, int itemNumber, int slot) {
		guiItem[itemNumber] = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = guiItem[itemNumber].getItemMeta();
		
		
		String[] lorePieces = lore.split("\\|");
		for(String t : lorePieces) {
			itemLore.add(t);
		}
		
		meta.setDisplayName(name);
		meta.setLore(itemLore);
		guiItem[itemNumber].setItemMeta(meta);
		GUI.setItem(slot, guiItem[itemNumber]);
		
	}
	
	public void makeGuiItem(String name, ItemStack item, String lore, Material material, int itemNumber, int slot) {
		guiItem[itemNumber] = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = guiItem[itemNumber].getItemMeta();
		
		
		
		//for(String t : lorePieces) {
		//	itemLore.add(t);
		//}
		Tool tool = Tool.checkForTool(item);
		name = parsePlaceholders(name, tool);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		lore = parsePlaceholders(lore, tool);
		String[] lorePieces = lore.split("\\|");
		
		for(int x = 0;x < lorePieces.length; x++) {
			itemLore.add(x, ChatColor.translateAlternateColorCodes('&', lorePieces[x]));
		}
		//itemLore.add(0, "Blocks Broken: " + tool.getBlocksBroken());
		//itemLore.add(1, "Energy: " + String.format("%." + Smithy.plugin.getConfig().getString("Currency.DecimalPlaces") + "f", tool.getEnergy()));
		meta.setLore(itemLore);
		guiItem[itemNumber].setItemMeta(meta);
		GUI.setItem(slot, guiItem[itemNumber]);
		
	}
	
	public void openSmithyGUI(Player player) {
		player.openInventory(GUI);
	}
	
	public String parsePlaceholders(String string, Tool tool) {
		string = string.replace("{BlocksBroken}", String.valueOf(tool.getBlocksBroken()));
		string = string.replace("{Currency}", String.format("%." + Smithy.plugin.getConfig().getString("Currency.DecimalPlaces") + "f", tool.getEnergy()));
		string = string.replace("{ItemName}", String.valueOf(tool.getDisplayName()));
		return string;
	}
}


