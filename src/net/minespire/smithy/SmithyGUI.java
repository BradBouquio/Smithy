package net.minespire.smithy;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SmithyGUI {
	Inventory GUI;
	private ItemStack guiItem;
	private int pageNumber;
	public static Map<String, Stack<SmithyGUI>> menuStackMap = new HashMap<>();
	private boolean isStackedMenu = false;
	public static final int MAX_GUI_SLOTS = 54;

	public SmithyGUI(){};

	public SmithyGUI(int slots, String name) {
		GUI = Bukkit.createInventory(null, slots, name);
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.CloseButton.ItemName"),"",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.CloseButton.Item")),
				Smithy.plugin.getConfig().getInt("GUI.CloseButton.InventorySlot"));
	}

	public SmithyGUI(int slots, String name, boolean isStackedMenu) {
		this(slots,name);
		this.isStackedMenu = isStackedMenu;
		if(isStackedMenu) {
			makeGuiItem(Smithy.plugin.getConfig().getString("GUI.BackButton.ItemName"),"",
					Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.BackButton.Item")),
					Smithy.plugin.getConfig().getInt("GUI.BackButton.InventorySlot"));
		}
	}

	public void makePreviousPageButton(){
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.ItemName"),"",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.Item")),
				Smithy.plugin.getConfig().getInt("GUI.PreviousPageButton.InventorySlot"));
	}

	public void makeNextPageButton(){
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.NextPageButton.ItemName"),"",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.NextPageButton.Item")),
				Smithy.plugin.getConfig().getInt("GUI.NextPageButton.InventorySlot"));
	}
	
	public void makeGuiItem(String name, String lore, Material material, int slot) {
		guiItem = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = guiItem.getItemMeta();
		
		
		String[] lorePieces = lore.split("\\|");
		for(String t : lorePieces) {
			itemLore.add(t);
		}
		
		meta.setDisplayName(name);
		meta.setLore(itemLore);
		guiItem.setItemMeta(meta);
		GUI.setItem(slot, guiItem);
		
	}
	
	public void makeGuiItem(String name, ItemStack item, String lore, Material material, int itemNumber, int slot) {
		guiItem = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = guiItem.getItemMeta();
		
		
		
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
		guiItem.setItemMeta(meta);
		GUI.setItem(slot, guiItem);
		
	}

	public void open(Player player) {
		player.openInventory(GUI);
	}

	public void openAddToStack(Player player) {
		addToMenuMap(player);
		player.openInventory(GUI);
	}

	public void addToMenuMap(Player player) {
		Stack menuStack;
		if(menuStackMap.containsKey(player.getDisplayName())){
			menuStack = menuStackMap.get(player.getDisplayName());
			menuStack.push(this);
			//menuStackMap.replace(player.getDisplayName(), menuStack);
		}
		else {
			menuStack = new Stack<SmithyGUI>();
			menuStack.push(this);
			menuStackMap.put(player.getDisplayName(), menuStack);
		}
	}

	public String parsePlaceholders(String string, Tool tool) {
		if(string!=null){
			string = string.replace("{BlocksBroken}", String.valueOf(tool.getBlocksBroken()));
			string = string.replace("{Currency}", String.format("%." + Smithy.plugin.getConfig().getString("Currency.DecimalPlaces") + "f", tool.getEnergy()));
			string = string.replace("{ItemName}", String.valueOf(tool.getDisplayName()));
		}

		return string;
	}
}


