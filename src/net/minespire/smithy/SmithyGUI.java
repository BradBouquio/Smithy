package net.minespire.smithy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		
		
		String[] lorePieces = lore.split("\\|");
		for(String t : lorePieces) {
			itemLore.add(t);
		}
		
		meta.setDisplayName(name);
		itemLore.add(0, "Blocks Broken: " + Tool.getTotalBlocksBroken(item));
		meta.setLore(itemLore);
		guiItem[itemNumber].setItemMeta(meta);
		GUI.setItem(slot, guiItem[itemNumber]);
		
	}
	
	public void openSmithyGUI(Player player) {
		player.openInventory(GUI);
	}
}


