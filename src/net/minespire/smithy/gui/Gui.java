package net.minespire.smithy.gui;

import net.md_5.bungee.api.ChatColor;
import net.minespire.smithy.Smithy;
import net.minespire.smithy.Tool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Gui {
	Inventory GUI;
	private ItemStack guiItem;
	private int pageNumber;
	public static Map<String, Stack<Gui>> menuStackMap = new HashMap<>();
	private boolean isStackedMenu = false;
	public static final int MAX_GUI_SLOTS = 54;
	protected Player player;

	public static Map<String, String> smithyGUIMenuNames = new HashMap<>(20);
	public static List<String> smithyGUIButtonTypes = new ArrayList<>(20);

	static {
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.GUIName"), "MainMenu");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.ToolMenu.MenuName"), "Tool");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.MenuName"), "Enchants");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.RepairMenu.MenuName"), "Repair");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.ParticlesMenu.MenuName"), "Particles");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.ConjureMenu.MenuName"), "Conjure");
		smithyGUIMenuNames.put("Confirm Upgrade Purchase", "ConfirmEnchant");
		smithyGUIMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Title"),"Confirm Enchant Unlock");

		smithyGUIButtonTypes.add(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.LockedItem.Item"));
		smithyGUIButtonTypes.add(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.LockedItem.Item"));


	}

	public Gui(int slots, String name) {
		this.GUI = Bukkit.createInventory(null, slots, name);
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.CloseButton.ItemName"), "",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.CloseButton.Item")), Smithy.plugin
						.getConfig().getInt("GUI.CloseButton.InventorySlot"));
	}

	public Gui(int slots, String name, boolean isStackedMenu) {
		this(slots, name);
		this.isStackedMenu = isStackedMenu;
		if (isStackedMenu)
			makeGuiItem(Smithy.plugin.getConfig().getString("GUI.BackButton.ItemName"), "",
					Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.BackButton.Item")), Smithy.plugin
							.getConfig().getInt("GUI.BackButton.InventorySlot"));
	}

	public void makePreviousPageButton() {
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.ItemName"), "",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.Item")), Smithy.plugin
						.getConfig().getInt("GUI.PreviousPageButton.InventorySlot"));
	}

	public void makeNextPageButton() {
		makeGuiItem(Smithy.plugin.getConfig().getString("GUI.NextPageButton.ItemName"), "",
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.NextPageButton.Item")), Smithy.plugin
						.getConfig().getInt("GUI.NextPageButton.InventorySlot"));
	}

	public void makeGuiItem(String name, String lore, Material material, int slot) {
		this.guiItem = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = this.guiItem.getItemMeta();
		String[] lorePieces = lore.split("\\|");
		for (int x = 0; x < lorePieces.length; x++)
			itemLore.add(x, ChatColor.translateAlternateColorCodes('&', lorePieces[x]));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		meta.setLore(itemLore);
		this.guiItem.setItemMeta(meta);
		this.GUI.setItem(slot, this.guiItem);
	}

	public void makeGuiItem(String name, ItemStack item, String lore, Material material, int itemNumber, int slot) {
		this.guiItem = new ItemStack(material);
		List<String> itemLore = new ArrayList<>();
		ItemMeta meta = this.guiItem.getItemMeta();
		Tool tool = Tool.checkForTool(item);
		name = parsePlaceholders(name, tool);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		lore = parsePlaceholders(lore, tool);
		String[] lorePieces = lore.split("\\|");
		for (int x = 0; x < lorePieces.length; x++)
			itemLore.add(x, ChatColor.translateAlternateColorCodes('&', lorePieces[x]));
		meta.setLore(itemLore);
		this.guiItem.setItemMeta(meta);
		this.GUI.setItem(slot, this.guiItem);
	}

	public void open(Player player) {
		player.openInventory(this.GUI);
	}

	public void openAndAddToGuiList(Player player) {
		addToMenuMap(player);
		player.openInventory(this.GUI);
	}


	public void addToMenuMap(Player player) {
		if (menuStackMap.containsKey(player.getDisplayName())) {
			Stack<Gui> menuStack = menuStackMap.get(player.getDisplayName());
			menuStack.push(this);
		} else {
			Stack<Gui> menuStack = new Stack();
			menuStack.push(this);
			menuStackMap.put(player.getDisplayName(), menuStack);
		}
		Bukkit.broadcastMessage(menuStackMap.get(player.getDisplayName()).size() + "");
	}

	public String parsePlaceholders(String string, Tool tool) {
		if (string != null) {
			string = string.replace("{BlocksBroken}", String.valueOf(tool.getBlocksBroken()));
			string = string.replace("{Currency}", String.format("%." + Smithy.plugin.getConfig().getString("Currency.DecimalPlaces") + "f", new Object[] { Double.valueOf(tool.getEnergy()) }));
			string = string.replace("{ItemName}", String.valueOf(tool.getDisplayName()));
		}
		return string;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}