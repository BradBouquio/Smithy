package net.minespire.smithy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

public class CommandHandler implements CommandExecutor {

	private Player player;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		player = sender instanceof Player ? (Player)sender : null;
		
		try {
			switch(args[0].toLowerCase()) {
				case "set": 
					if (player != null) {
						setAsSmithyTool(player.getInventory().getItemInMainHand());
						sender.sendMessage("Reached here");
						break;
					}
					else sender.sendMessage("You must be a player to use that command!");
				case "give": 
					if (args[2] != null) {
						boolean playerOnline = false;
						boolean validTool = false;
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.getName().equalsIgnoreCase(args[1])) {
								playerOnline = true;
								this.player = p;
								break;
							}
						}
						if (!playerOnline) {
							sender.sendMessage("That player is not online");
							break;
						}
						for (String tool : Tool.tools) {
							if(tool.equalsIgnoreCase(args[2])) {
								validTool = true;
								break;
							}
						}
						if (!validTool) {
							sender.sendMessage("That is not a valid tool!");
							break;
						}
						Tool newTool = new Tool(args[2]);
						newTool.giveTool(this.player);
						break;
						
					
					}
					else sender.sendMessage("Usage: /smithy give [player] [tool]");
				case "upgrade": 
					if (player != null) {
						Upgrade upgrade = new Upgrade();
						upgrade.toolUpgrade(player);
						break;
					}
					else sender.sendMessage("You must be a player to use that command!");
				case "reload": 
					if (player != null) {
						Smithy.plugin.reloadConfig();
						Tool.getToolNames();
						Smithy.plugin.getCommand("smithy").setTabCompleter(new CommandCompleter());
						Smithy.plugin.getCommand("smithy").setExecutor(new CommandHandler());
						sender.sendMessage("Smithy config reloaded!");
					}
					else sender.sendMessage("You must be a player to use that command!");
					break;
				default: return false;
			}
			return true;
		} catch ( IndexOutOfBoundsException e ) {
		    return false;
		}
			
	}
	
	public void setAsSmithyTool(ItemStack item) {
		
		//
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		lore.add("Smithy Level 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void giveTool(String tool) {
		if(tool.contains("Pickaxe")) {
			ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
			NamespacedKey key = new NamespacedKey(Smithy.plugin, "Smithy");
			ItemMeta itemMeta = itemStack.getItemMeta();
			

			
			CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();

			//if(tagContainer.hasCustomTag(key, ItemTagType.STRING)) {
			//    String foundValue = tagContainer.getCustomTag(key, ItemTagType.STRING);
			//}
			
			itemMeta.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, "TRUE");
			itemStack.setItemMeta(itemMeta);
			player.getInventory().addItem(itemStack);
			player.sendMessage("Gave");
		} else return;
	}
}
