package net.minespire.smithy.gui;

import net.minespire.smithy.Smithy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuItem {
    public static Map<String, String> smithyMenuItemNames = new HashMap<>(20);

    ItemStack itemStack;

    GuiManager guiManager;

    Player player;

    Menu menu;

    static {
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.BackButton.ItemName"), "Back");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.ItemName"), "PreviousPageButton");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.NextPageButton.ItemName"), "NextPageButton");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.CloseButton.ItemName"), "Close");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.ItemName"), "Tool");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.ItemName"), "Enchants");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.ItemName"), "Repair");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.ItemName"), "Particles");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.ItemName"), "Conjure");
    }

    MenuItem(ItemStack item, GuiManager manager, Player player) {
        this.player = player;
        this.guiManager = manager;
        this.itemStack = item;
    }

    void pressButton() {
        smithyMenuItemNames.keySet().forEach(buttonName -> {
            if (this.itemStack.getItemMeta().getDisplayName().equals(buttonName)) {
                this.menu = new Menu(this.player, this.guiManager);
                handleByButtonName(smithyMenuItemNames.get(buttonName));
                return;
            }
        });
    }

    private void handleByButtonName(String menuItemName) {
        if (menuItemName.equals("Back")) this.menu.openPreviousMenu();
        if (menuItemName.equals("Close")) this.menu.closeGUI();
        if (menuItemName.equals("PreviousPageButton")) this.menu.showPreviousPage();
        if (menuItemName.equals("NextPageButton")) this.menu.showNextPage();
        if (menuItemName.equals("Tool")) this.menu.openToolDetailsGUI();
        if (menuItemName.equals("Enchants")) {
            EnchantGui.enchantPageList.remove(this.player.getDisplayName());
            this.menu.openEnchantListGUI(null);
        }
        if (menuItemName.equals("Repair")) this.menu.openRepairUpgradeGUI();
        if (menuItemName.equals("Particles")) this.menu.openParticlesUpgradeGUI();
        if (menuItemName.equals("Conjure")) this.menu.openConjureUpgradeGUI();
        if (menuItemName.equals("Unlock Enchant")) this.menu.openEnchantPurchaseGUI(menuItemName);
    }
}
