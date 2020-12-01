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
    int indexInMenu;

    static {
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.BackButton.ItemName"), "Back");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.PreviousPageButton.ItemName"), "PreviousPageButton");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.NextPageButton.ItemName"), "NextPageButton");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.CloseButton.ItemName"), "Close");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Tool.ItemName"), "Tool");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Enchant.ItemName"), "Enchant");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Repair.ItemName"), "Repair");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Particles.ItemName"), "Particles");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.MainMenu.Conjure.ItemName"), "Conjure");
        smithyMenuItemNames.put("Enchant Upgrade","Enchant Upgrade");
        smithyMenuItemNames.put(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Accept.Name"),"Confirm Enchant Unlock Button");
    }

    MenuItem(ItemStack item, GuiManager manager, Player player) {
        this.player = player;
        this.guiManager = manager;
        this.itemStack = item;
    }

//    void press(int index) {
//        indexInMenu = index;
//        smithyMenuItemNames.keySet().forEach(buttonName -> {
//            if (this.itemStack.getItemMeta().getDisplayName().equals(buttonName)) {
//                this.menu = new Menu(this.player, this.guiManager);
//                handleByButtonName(smithyMenuItemNames.get(buttonName));
//                return;
//            }
//            if (Gui.smithyGUIButtonTypes.contains(this.itemStack.getType().toString())){
//                this.menu = new Menu(this.player, this.guiManager);
//                handleByButtonType(this.itemStack.getType().toString());
//                return;
//            }
//        });
//    }

    void press(int index) {
        indexInMenu = index;
        if (smithyMenuItemNames.containsKey(this.itemStack.getItemMeta().getDisplayName())) {
            this.menu = new Menu(this.player, this.guiManager);
            handleByButtonName(smithyMenuItemNames.get(this.itemStack.getItemMeta().getDisplayName()));
            return;
        }
        else if (Gui.smithyGUIButtonTypes.contains(this.itemStack.getType().toString())){
            this.menu = new Menu(this.player, this.guiManager);
            handleByButtonType(this.itemStack.getType().toString());
            return;
        }
    }

//    void press(int index) {
//        indexInMenu = index;
//        Predicate<String> findNameMatch = name -> this.itemStack.getItemMeta().getDisplayName().equals(name);
//        Predicate<String> findTypeMatch = name -> Gui.smithyGUIButtonTypes.contains(this.itemStack.getType().toString());
//
//        smithyMenuItemNames.keySet().stream().filter(findNameMatch).findFirst().forEach(buttonName -> {
//
//
//            if (this.itemStack.getItemMeta().getDisplayName().equals(buttonName)) {
//                this.menu = new Menu(this.player, this.guiManager);
//                handleByButtonName(smithyMenuItemNames.get(buttonName));
//                return;
//            }
//            if (Gui.smithyGUIButtonTypes.contains(this.itemStack.getType().toString())){
//                this.menu = new Menu(this.player, this.guiManager);
//                handleByButtonType(this.itemStack.getType().toString());
//                return;
//            }
//        });
//    }

    private void handleByButtonType(String buttonType) {
//        if (buttonType.equals(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.LockedItem.Item")))
//            this.menu.attemptEnchantUnlock(this.indexInMenu);
        if (buttonType.equals(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.LockedItem.Item"))){
            this.menu.generateEnchantLevelUpgrade(this.indexInMenu).prompt();

        }
    }

    private void handleByButtonName(String menuItemName) {
        if (menuItemName.equals("Back")) this.menu.openPreviousMenu();
        if (menuItemName.equals("Close")) this.menu.closeGUI();
        if (menuItemName.equals("PreviousPageButton")) this.menu.showPreviousPage();
        if (menuItemName.equals("NextPageButton")) this.menu.showNextPage();
        if (menuItemName.equals("Tool")) this.menu.openToolDetailsGUI();
        if (menuItemName.equals("Enchant")) {
            EnchantGui.enchantPageList.remove(this.player.getDisplayName());
            this.menu.openEnchantListGUI(null);
        }
        if (menuItemName.equals("Repair")) this.menu.openRepairUpgradeGUI();
        if (menuItemName.equals("Particles")) this.menu.openParticlesUpgradeGUI();
        if (menuItemName.equals("Conjure")) this.menu.openConjureUpgradeGUI();
        if (menuItemName.equals("Confirm Enchant Unlock Button")) this.menu.enchantLevelUpgradeConfirmed();
    }
}
