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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;



public class Tool {
	private String displayName;
	public static NamespacedKey toolID = new NamespacedKey(Smithy.plugin, "ToolID");
	public static final String[] tools = new String[100];
	private long blocksBroken;
	private double energy;
	private String uniqueToolID;
	private boolean inToolMap = false; 
	private ItemStack item;
	private ItemMeta itemMeta;
	public static double currencyGainRate = Smithy.plugin.getConfig().getDouble("Currency.GainRate");

	public Tool(String toolName) {
		this.displayName = Smithy.plugin.getConfig().getString("Tools." + toolName + ".DisplayName");
		energy = Smithy.plugin.getConfig().getDouble("Currency.StartingBalance");
		item = new ItemStack(Material.getMaterial(Smithy.plugin.getConfig().getString("Tools." + toolName + ".Item").toUpperCase()));
		itemMeta = item.getItemMeta();
		uniqueToolID = Smithy.plugin.getToolID();
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		
		itemMeta.getCustomTagContainer().setCustomTag(toolID, ItemTagType.STRING, uniqueToolID);
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		item.setItemMeta(itemMeta);
		Smithy.plugin.saveToolToMap(this);
		this.saveToDatabase();
	}
	
	public Tool() {
		
	}
	
	public static Tool checkForTool(ItemStack item) {
		ItemMeta itemMeta;
		Tool tool = null;
		if(item.getType().equals(Material.AIR)) return tool;
		try{
			itemMeta = item.getItemMeta();
		}
		catch(Exception e) {
			return tool;
		}
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
		if(tagContainer.hasCustomTag(toolID, ItemTagType.STRING)) {
		    String possibleID = tagContainer.getCustomTag(toolID, ItemTagType.STRING);
		    tool = Smithy.plugin.getTool(possibleID);
		    
			if(tool == null) {
				tool = Smithy.plugin.getDatabase().getTool(possibleID);
				if(tool != null) {
					tool.setID(possibleID);
					tool.setItem(item);
					tool.setDisplayName(itemMeta.getDisplayName());
					Smithy.plugin.saveToolToMap(tool);
				}
				
			}
		}
		
		return tool;
		
	}
	
	public void addToBlockTotal(long numToAdd) {
		blocksBroken += numToAdd;
	}
	
	public long getTotalBlocksBroken(ItemStack item) {
		return blocksBroken;
	}
	
	public void giveTool(Player playerName) {

		playerName.getInventory().addItem(item);

		playerName.sendMessage("You have been given a " + ChatColor.translateAlternateColorCodes('&', displayName) + ChatColor.WHITE +"!");
	}
	
	public static void getToolNames() {
		int x = 0;
		for(String toolName : Smithy.plugin.getConfig().getConfigurationSection("Tools").getKeys(false)) {
			tools[x++] = toolName;
		}
	}
	
	public void setBlocksBroken(long blocksBroken) {
		this.blocksBroken = blocksBroken;
	}
	
	public long getBlocksBroken() {
		return blocksBroken;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void setEnergy(Double energy) {
		this.energy = energy;
	}
	
	public void addEnergy(Double energy) {
		this.energy+=energy;
	}
	
	public void saveToDatabase() {
		Smithy.plugin.getDatabase().saveTool(uniqueToolID, blocksBroken, energy);
	}

	public String getID() {
		return uniqueToolID;
	}
	
	public void setID(String id) {
		uniqueToolID = id;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String name) {
		this.displayName = name;
	}
}
