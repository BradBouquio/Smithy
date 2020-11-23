package net.minespire.smithy.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class GuiClick {
    Player player;

    InventoryView inventoryView;

    InventoryClickEvent clickEvent;

    GuiManager guiManager;

    GuiClick(InventoryClickEvent event, GuiManager manager) {
        this.guiManager = manager;
        this.clickEvent = event;
        this.player = (Player)this.clickEvent.getWhoClicked();
        this.inventoryView = this.player.getOpenInventory();
    }

    boolean hasSmithyMenuName() {
        for (String name : Gui.smithyGUIMenuNames.keySet()) {
            if (this.inventoryView.getTitle().equals(name))
                return true;
        }
        return false;
    }

    MenuItem getMenuItem() {
        ItemStack clickedItem = this.clickEvent.getCurrentItem();
        this.clickEvent.setCancelled(true);
        if (clickedItem == null)
            return null;
        return new MenuItem(clickedItem, this.guiManager, this.player);
    }

    InventoryView getInventoryView() {
        return this.inventoryView;
    }
}
