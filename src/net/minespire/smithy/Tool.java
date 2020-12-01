package net.minespire.smithy;

import net.md_5.bungee.api.ChatColor;
import net.minespire.smithy.config.EnchantUpgrades;
import net.minespire.smithy.util.Convert;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Tool {
	private String displayName;
	public static NamespacedKey toolID = new NamespacedKey(Smithy.plugin, "ToolID");
	public static final String[] tools = new String[100];
	public static Map<String, Tool> toolSet = new HashMap<>(100);
	private long blocksBroken;
	private double energy;
	private String uniqueToolID;
	private boolean inToolMap = false; 
	private ItemStack item;
	private ItemMeta itemMeta;
	private Map<Enchantment,Integer> enchantmentList;
	private String configDefinedToolName;
	public static double currencyGainRate = Smithy.plugin.getConfig().getDouble("Currency.GainRate");

	public Tool(String toolName) {
		this.displayName = Smithy.plugin.getConfig().getString("Tools." + toolName + ".DisplayName");
		configDefinedToolName = toolName;
		energy = Smithy.plugin.getConfig().getDouble("Currency.StartingBalance");
		item = new ItemStack(Material.getMaterial(Smithy.plugin.getConfig().getString("Tools." + toolName + ".Item").toUpperCase()));
		itemMeta = item.getItemMeta();
		enchantmentList = item.getEnchantments();
		uniqueToolID = getToolID();
		CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();

		tagContainer.setCustomTag(toolID, ItemTagType.STRING, uniqueToolID);
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		item.setItemMeta(itemMeta);
		saveToolToMap(this);
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
		    tool = getTool(possibleID);
		    
			if(tool == null) {
				tool = Smithy.plugin.getDatabase().getTool(possibleID);
				if(tool != null) {
					tool.setItem(item);
					tool.setDisplayName(itemMeta.getDisplayName());
					saveToolToMap(tool);
				}
				
			}
		}
		
		return tool;
		
	}

	public void setConfigDefinedToolName(String name) {
		configDefinedToolName = name;
	}

	public void addToBlockTotal(long numToAdd) {
		blocksBroken += numToAdd;
	}
	
	public long getTotalBlocksBroken(ItemStack item) {
		return blocksBroken;
	}
	
	public void giveTool(Player playerName) {

		playerName.getInventory().addItem(applyDefaultEnchants(item));

		playerName.sendMessage("You have been given a " + ChatColor.translateAlternateColorCodes('&', displayName) + ChatColor.WHITE +"!");
	}

	private ItemStack applyDefaultEnchants(ItemStack item){
		Map<String, List<Integer>> enchantsWithLevelsMap = EnchantUpgrades.getAvailableEnchants(this);
		for(String enchant :  enchantsWithLevelsMap.keySet()){
			Integer firstLevelOfEnchant = enchantsWithLevelsMap.get(enchant).get(0);
			if(firstLevelOfEnchant != 0) item.addUnsafeEnchantment(EnchantmentWrapper.getByKey(NamespacedKey.minecraft(Convert.enchantToVanillaName(enchant).toLowerCase().replace(" ", "_"))), firstLevelOfEnchant);
		}
		return item;
	}
	
	public static void getToolNames() {
		int x = 0;
		for(String toolName : Smithy.plugin.getConfig().getConfigurationSection("Tools").getKeys(false)) {
			tools[x++] = toolName;
		}
	}

	public static Tool getTool(String id) {
		return toolSet.get(id);
	}

	public static boolean toolExists(String id) {
		return toolSet.containsKey(id);
	}

	public static String getToolID() {
		return UUID.randomUUID().toString();
	}

	public static void saveToolToMap(Tool tool) {
		toolSet.put(tool.getID(), tool);
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
		Smithy.plugin.getDatabase().saveTool(this);
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

	public ItemStack getItemStack(){
		return item;
	}

	public String getConfigDefinedToolName(){
		return configDefinedToolName;
	}

	public Map<Enchantment,Integer> getEnchantmentList(){
		return enchantmentList;
	}
}
