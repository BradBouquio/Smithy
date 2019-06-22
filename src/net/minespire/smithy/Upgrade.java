package net.minespire.smithy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

public class Upgrade implements Listener{

	private Player player;
	private boolean clickGui = true;
	private Inventory upgradeInventory;
	private String upgradeGuiName;
	
	
	public Upgrade() {
		upgradeGuiName = Smithy.plugin.getConfig().getString("GUI.GUIName");
	}
	
	//when player opens gui with command
	public void toolUpgrade(Player player) {
		this.player = player;
		ItemStack heldItem = player.getInventory().getItemInMainHand();
		if(Tool.checkForTool(heldItem)) {
			createGui(heldItem);
		}
	}
	
	//When a player right clicks with a smithy tool
	@EventHandler(priority = EventPriority.HIGH)
	public void toolUpgrade(PlayerInteractEvent click) {
		if(!Smithy.plugin.getConfig().getBoolean("General.RightClickGUI")) return;
		ItemStack itemStack;
		try {
			if(!click.getAction().equals(Action.RIGHT_CLICK_AIR) && !click.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
			itemStack = click.getItem();
			player = click.getPlayer();
			
		}
		catch(Exception e) {
			return;
		}

		if(Tool.checkForTool(itemStack)) {
			createGui(itemStack);
		}
	}
	
	//what to do when player clicks inside Upgrade Menu
	@EventHandler(priority = EventPriority.HIGH)
	public void cancelInventoryClick(InventoryClickEvent clickEvent) {
		if(clickEvent.getWhoClicked().getOpenInventory().getTitle().equals(upgradeGuiName)) {
			clickEvent.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void blockBroken(BlockBreakEvent breakEvent) {
		
		if(Tool.checkForTool(breakEvent.getPlayer().getInventory().getItemInMainHand())) {
			Tool.addToBlockTotal(breakEvent.getPlayer().getInventory().getItemInMainHand(), breakEvent.getPlayer());
		}
	}

	
    public void createGui(ItemStack itemInHand) {
    	SmithyGUI gui = new SmithyGUI(54, upgradeGuiName);
    	gui.makeGuiItem(itemInHand.getItemMeta().getDisplayName(), itemInHand, "Multiple lines|is awesome!", itemInHand.getType(), 0, 22);
    	gui.makeGuiItem("Enchants", "Multiple lines|is awesome!", Material.EXPERIENCE_BOTTLE, 0, 29);
    	gui.makeGuiItem("Tools Points", "Multiple lines|is awesome!", Material.ENCHANTED_BOOK, 1, 33);
    	gui.openSmithyGUI(player);
    }
	
}