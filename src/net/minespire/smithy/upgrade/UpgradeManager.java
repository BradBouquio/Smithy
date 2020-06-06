package net.minespire.smithy.upgrade;

import net.minespire.smithy.SmithyEnchantGui;
import net.minespire.smithy.Smithy;
import net.minespire.smithy.SmithyGUI;
import net.minespire.smithy.Tool;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeManager implements Listener{

	private Player player;
	private boolean clickGui = true;
	private InventoryView openInvView;
	public static Map<String,String> upgradeGuiMenuNames;
	private Tool tool;

	static {
		upgradeGuiMenuNames = new HashMap<String, String>(50);
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.GUIName"), "MainMenu");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.BackButton.ItemName"), "Back");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.ItemName"), "PreviousPageButton");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.NextPageButton.ItemName"), "NextPageButton");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.CloseButton.ItemName"), "Close");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.ItemName"), "Tool");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.ItemName"), "Enchants");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.ItemName"), "Repair");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.ItemName"), "Particles");
		upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.ItemName"), "Conjure");
	}

	public UpgradeManager() {
	}
	
	//when player opens gui with command
	public void toolUpgrade(Player player) {
		this.player = player;
		ItemStack heldItem = player.getInventory().getItemInMainHand();
		if(Tool.checkForTool(heldItem) != null) {
			createMainGui(heldItem);
		}
	}
	
	//When a player right clicks with a smithy tool
	@EventHandler(priority = EventPriority.HIGH)
	public void toolUpgrade(PlayerInteractEvent click) {
		if(!Smithy.plugin.getConfig().getBoolean("General.RightClickGUI")) return;
		ItemStack itemStack;
		try {
			if(!click.getAction().equals(Action.RIGHT_CLICK_AIR) && !click.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
			if(click.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
			itemStack = click.getPlayer().getInventory().getItemInMainHand();
			String itemMaterialName = itemStack.getType().name();
			if((itemMaterialName.endsWith("SHOVEL") || itemMaterialName.endsWith("HOE")) && click.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				String clickedBlockName = click.getClickedBlock().getBlockData().getMaterial().name();
				if(clickedBlockName.equals("GRASS_BLOCK") || clickedBlockName.equals("DIRT") || clickedBlockName.equals("COARSE_DIRT")
				|| clickedBlockName.equals("GRASS_PATH") || clickedBlockName.equals("FARMLAND")) return;
			}
			player = click.getPlayer();
		}
		catch(Exception e) {
			return;
		}
		
		if((tool = Tool.checkForTool(itemStack)) != null) {
			createMainGui(itemStack);
		}
	}

	//what to do when player clicks inside Upgrade Menu
	@EventHandler(priority = EventPriority.HIGH)
	public void handleUpgradeInventoryClick(InventoryClickEvent clickEvent) {
		this.player = (Player) clickEvent.getWhoClicked();
		openInvView = this.player.getOpenInventory();
		if(!upgradeGuiMenuNames.containsKey(openInvView.getTitle())) return;
		ItemStack clickedItem = clickEvent.getCurrentItem();
		clickEvent.setCancelled(true);
		if(clickedItem==null) return;

		//compare every possible menu name with the clicked item name to find which menu to open
		upgradeGuiMenuNames.keySet().forEach(menuName -> {
			if (clickedItem.getItemMeta().getDisplayName().equals(menuName)) {
				handleMenuItemClick(upgradeGuiMenuNames.get(menuName));
				return;
			}
		});
	}

	private void handleMenuItemClick(String menuItemName) {
		if(menuItemName.equals("Back")) openPreviousMenu();
		if(menuItemName.equals("Close")) closeGUI();
		if(menuItemName.equals("PreviousPageButton")) showPreviousPage();
		if(menuItemName.equals("NextPageButton")) showNextPage();
		if(menuItemName.equals("Tool")) openToolDetailsGUI();
		if(menuItemName.equals("Enchants")) openEnchantListGUI(null);
		if(menuItemName.equals("Repair")) openRepairUpgradeGUI();
		if(menuItemName.equals("Particles")) openParticlesUpgradeGUI();
		if(menuItemName.equals("Conjure")) openConjureUpgradeGUI();
		if(menuItemName.equals("Unlock Enchant")) openEnchantPurchaseGUI(menuItemName);
	}

	private void showPreviousPage() {
		SmithyEnchantGui.enchantStackMap.get(player.getDisplayName()).pop();
		SmithyEnchantGui.enchantStackMap.get(player.getDisplayName()).peek().open(player);
	}

	private void showNextPage() {
		openEnchantListGUI(((SmithyEnchantGui) SmithyEnchantGui.enchantStackMap.get(player.getDisplayName()).peek()).getEnchantsToSkip());
	}

	private void openConjureUpgradeGUI() {
		SmithyGUI upgradeGui = new SmithyGUI(SmithyGUI.MAX_GUI_SLOTS, Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.ItemName"), true);
		upgradeGui.openAddToStack(this.player);
	}

	private void openParticlesUpgradeGUI() {
		SmithyGUI upgradeGui = new SmithyGUI(SmithyGUI.MAX_GUI_SLOTS, Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.ItemName"), true);
		upgradeGui.openAddToStack(this.player);
	}

	private void openPreviousMenu() {
		SmithyGUI.menuStackMap.get(player.getDisplayName()).pop();
		SmithyGUI.menuStackMap.get(player.getDisplayName()).peek().open(player);
		SmithyEnchantGui.enchantStackMap.remove(player); //not ideal
	}

	private void closeGUI() {
		SmithyGUI.menuStackMap.remove(player.getDisplayName());
		openInvView.close();
	}

	private void openToolDetailsGUI() {
		SmithyGUI upgradeGui = new SmithyGUI(SmithyGUI.MAX_GUI_SLOTS, Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.ItemName"), true);
		upgradeGui.openAddToStack(this.player);
	}

	private void openEnchantListGUI(List<String> enchantsToSkip) {

		SmithyEnchantGui enchantGUI = new SmithyEnchantGui(SmithyGUI.MAX_GUI_SLOTS, Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.ItemName"), true);
		int enchantNameSlot = 0;
		int followingSlot;
		Map<String,String> enchants;
		if(enchantsToSkip==null) {
			enchantGUI.addToMenuMap(this.player);
			enchantsToSkip = new ArrayList<>();
		} else enchantGUI.makePreviousPageButton();
		enchants = EnchantUpgrade.getAvailableEnchants(tool);
		for (String enchant:enchants.keySet()) {
			followingSlot = enchantNameSlot + 1;
			if(enchantNameSlot>44) break;
			if(!enchantsToSkip.contains(enchant)) {
				enchantGUI.makeGuiItem(enchant,"",Material.ENCHANTED_BOOK, enchantNameSlot);
				enchantNameSlot+=9;
				enchantsToSkip.add(enchant);
			}
			for(String enchantLevel:enchants.get(enchant).split(":")){
				if (enchantLevel.equals(enchantGUI.getLastLevelListedInGUI())) continue;
				if(followingSlot%9==0) {
					followingSlot++;
					enchantNameSlot+=9;
				}
				enchantGUI.makeGuiItem(enchant +": Level " + enchantLevel,"",Material.BLACK_STAINED_GLASS_PANE, followingSlot++);
				enchantGUI.setLastLevelListedInGUI(enchantLevel);
				if (followingSlot > 44) break;
			}

		}
		enchantGUI.addEnchantsToSkip(enchantsToSkip);
		enchantGUI.openAddToStack(this.player);
	}

	private void openEnchantPurchaseGUI(String menuName){
	}

	private void openRepairUpgradeGUI() {
		SmithyGUI upgradeGui = new SmithyGUI(SmithyGUI.MAX_GUI_SLOTS, Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.ItemName"), true);
		upgradeGui.openAddToStack(this.player);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void blockBroken(BlockBreakEvent breakEvent) {
		
		Tool tool = Tool.checkForTool(breakEvent.getPlayer().getInventory().getItemInMainHand());
		if(tool != null) {
			tool.addToBlockTotal(1);
			tool.addEnergy(Tool.currencyGainRate);
		}
		
	}
	
    public void createMainGui(ItemStack itemInHand) {
    	SmithyGUI gui = new SmithyGUI(54, Smithy.plugin.getConfig().getString("GUI.GUIName"));
    	gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.ItemName"), itemInHand, Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.Lore"), itemInHand.getType(), 0, Smithy.plugin.getConfig().getInt("GUI.MenuItems.Tool.InventorySlot"));
    	gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.ItemName"), Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.Item")), Smithy.plugin.getConfig().getInt("GUI.MenuItems.Enchants.InventorySlot"));
    	gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.ItemName"), Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.Item")), Smithy.plugin.getConfig().getInt("GUI.MenuItems.Repair.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.ItemName"), Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.Item")), Smithy.plugin.getConfig().getInt("GUI.MenuItems.Particles.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.ItemName"), Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.Item")), Smithy.plugin.getConfig().getInt("GUI.MenuItems.Conjure.InventorySlot"));
    	gui.openAddToStack(player);
    }
	
}
