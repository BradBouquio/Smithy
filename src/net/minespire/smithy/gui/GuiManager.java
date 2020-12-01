package net.minespire.smithy.gui;

import net.minespire.smithy.Smithy;
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

public class GuiManager implements Listener {
	private Player player;
	private boolean clickGui = true;
	private InventoryView openInvView;
	private Tool tool;
	MenuItem menuItem;
	private GuiClick guiClick;

	public void toolUpgrade(Player player) {
		this.player = player;
		ItemStack heldItem = player.getInventory().getItemInMainHand();
		if (Tool.checkForTool(heldItem) != null)
			createMainGui(heldItem);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void toolUpgrade(PlayerInteractEvent click) {
		ItemStack itemStack;
		if (!Smithy.plugin.getConfig().getBoolean("General.RightClickGUI"))
			return;
		try {
			if (!click.getAction().equals(Action.RIGHT_CLICK_AIR) && !click.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				return;
			if (click.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))
				return;
			itemStack = click.getPlayer().getInventory().getItemInMainHand();
			String itemMaterialName = itemStack.getType().name();
			if ((itemMaterialName.endsWith("SHOVEL") || itemMaterialName.endsWith("HOE")) && click.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				String clickedBlockName = click.getClickedBlock().getBlockData().getMaterial().name();
				if (clickedBlockName.equals("GRASS_BLOCK") || clickedBlockName.equals("DIRT") || clickedBlockName.equals("COARSE_DIRT") || clickedBlockName
						.equals("GRASS_PATH") || clickedBlockName.equals("FARMLAND"))
					return;
			}
			this.player = click.getPlayer();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if ((this.tool = Tool.checkForTool(itemStack)) != null)
			createMainGui(itemStack);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void handleGuiInventoryClick(InventoryClickEvent clickEvent) {
		this.guiClick = new GuiClick(clickEvent, this);
		if (!this.guiClick.hasSmithyMenuName())
			return;
		if ((this.menuItem = this.guiClick.getMenuItem()) == null)
			return;
		this.menuItem.press(clickEvent.getSlot());
	}

	Tool getTool() {
		return this.tool;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void blockBroken(BlockBreakEvent breakEvent) {
		Tool tool = Tool.checkForTool(breakEvent.getPlayer().getInventory().getItemInMainHand());
		if (tool != null) {
			tool.addToBlockTotal(1L);
			tool.addEnergy(Double.valueOf(Tool.currencyGainRate));
		}
	}

	public void clearPlayerMenuStack(int indexToOpen) {
		if(Gui.menuStackMap.get(this.player.getDisplayName()) != null)
			Gui.menuStackMap.get(this.player.getDisplayName()).clear();
	}

	public void createMainGui(ItemStack itemInHand) {
		clearPlayerMenuStack(0);
		Gui gui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.GUIName"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Tool.ItemName"), itemInHand,
				Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Tool.Lore"), itemInHand.getType(),
				0, Smithy.plugin.getConfig().getInt("GUI.MenuItems.MainMenu.Tool.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Enchant.ItemName"),
				Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Enchant.Lore"),
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Enchant.Item")),
				Smithy.plugin.getConfig().getInt("GUI.MenuItems.MainMenu.Enchant.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Repair.ItemName"),
				Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Repair.Lore"),
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Repair.Item")),
				Smithy.plugin.getConfig().getInt("GUI.MenuItems.MainMenu.Repair.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Particles.ItemName"),
				Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Particles.Lore"),
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Particles.Item")),
				Smithy.plugin.getConfig().getInt("GUI.MenuItems.MainMenu.Particles.InventorySlot"));
		gui.makeGuiItem(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Conjure.ItemName"),
				Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Conjure.Lore"),
				Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Conjure.Item")),
				Smithy.plugin.getConfig().getInt("GUI.MenuItems.MainMenu.Conjure.InventorySlot"));
		gui.openAndAddToGuiList(this.player);
	}
}
