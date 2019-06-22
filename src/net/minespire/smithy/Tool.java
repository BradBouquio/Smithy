package net.minespire.smithy;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import net.md_5.bungee.api.ChatColor;



public class Tool {
	private Material material;
	private String displayName;
	private static NamespacedKey toolKey = new NamespacedKey(Smithy.plugin, "Smithy");
	private static NamespacedKey blocksBrokenKey = new NamespacedKey(Smithy.plugin, "Blocks");
	public static final String[] tools = new String[100];

	public Tool(String toolName) {
		this.material = Material.getMaterial(Smithy.plugin.getConfig().getString("Tools." + toolName + ".Item").toUpperCase());
		this.displayName = Smithy.plugin.getConfig().getString("Tools." + toolName + ".DisplayName");	
	}
	
	
	public static boolean checkForTool(ItemStack item) {
		ItemMeta itemMeta;
		try{
			itemMeta = item.getItemMeta();
		}
		catch(Exception e) {
			return false;
		}

		
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		if(tagContainer.hasCustomTag(toolKey, ItemTagType.STRING)) {
		    String foundValue = tagContainer.getCustomTag(toolKey, ItemTagType.STRING);
		    if(foundValue.equals("TRUE")) return true;
		}
		return false;
	}
	
	public static void addToBlockTotal(ItemStack item, Player player) {
		ItemMeta itemMeta = item.getItemMeta();
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		int previousBlockTotal = itemMeta.getCustomTagContainer().getCustomTag(blocksBrokenKey, ItemTagType.INTEGER);
		player.sendMessage("" + itemMeta.getCustomTagContainer().getCustomTag(blocksBrokenKey, ItemTagType.INTEGER));
		itemMeta.getCustomTagContainer().removeCustomTag(blocksBrokenKey);
		previousBlockTotal++;
		itemMeta.getCustomTagContainer().setCustomTag(blocksBrokenKey, ItemTagType.INTEGER, previousBlockTotal);
		player.sendMessage("" + itemMeta.getCustomTagContainer().getCustomTag(blocksBrokenKey, ItemTagType.INTEGER));
		item.setItemMeta(itemMeta);
	}
	
	public static int getTotalBlocksBroken(ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		return itemMeta.getCustomTagContainer().getCustomTag(blocksBrokenKey, ItemTagType.INTEGER);
	}
	
	public void giveTool(Player playerName) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		itemMeta.getCustomTagContainer().setCustomTag(toolKey, ItemTagType.STRING, "TRUE");
		itemMeta.getCustomTagContainer().setCustomTag(blocksBrokenKey, ItemTagType.INTEGER, 0);
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		itemStack.setItemMeta(itemMeta);
		playerName.getInventory().addItem(itemStack);
		playerName.sendMessage("You have been given a " + ChatColor.translateAlternateColorCodes('&', displayName) + ChatColor.WHITE +"!");
	}
	
	public static void getToolNames() {
		int x = 0;
		for(String toolName : Smithy.plugin.getConfig().getConfigurationSection("Tools").getKeys(false)) {
			tools[x++] = toolName;
		}
	}
	
}
