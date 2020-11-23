package net.minespire.smithy.gui;

import net.minespire.smithy.Smithy;
import net.minespire.smithy.config.EnchantUpgrades;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu {
    Player player;

    GuiManager guiManager;

    private int currentPage;

    Menu(Player player, GuiManager manager) {
        this.player = player;
        this.guiManager = manager;
    }

    void showPreviousPage() {
        int currentPage = (EnchantGui.currentlyViewedPage.get(this.player.getDisplayName())).intValue();
        if (currentPage > 0) {
            ((EnchantGui.enchantPageList.get(this.player.getDisplayName())).get(currentPage - 1)).open(this.player);
            EnchantGui.currentlyViewedPage.put(this.player.getDisplayName(), Integer.valueOf(currentPage - 1));
        }
    }

    void showNextPage() {
        this.currentPage = EnchantGui.currentlyViewedPage.get(this.player.getDisplayName());
        if ((EnchantGui.enchantPageList.get(this.player.getDisplayName())).size() > this.currentPage+1) {
            ((EnchantGui.enchantPageList.get(this.player.getDisplayName())).get(this.currentPage+1)).open(this.player);
        } else {
            openEnchantListGUI((EnchantGui.enchantPageList.get(this.player.getDisplayName())).get(this.currentPage));
        }
        EnchantGui.currentlyViewedPage.put(this.player.getDisplayName(), Integer.valueOf(this.currentPage + 1));
    }

    void openConjureUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.Conjure.MenuName"), true);
        upgradeGui.openAddToList(this.player);
    }

    void openParticlesUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.Particles.MenuName"), true);
        upgradeGui.openAddToList(this.player);
    }

    void openPreviousMenu() {
        if(Gui.menuStackMap.get(this.player.getDisplayName()).size()>1)
            ((Gui.menuStackMap.get(this.player.getDisplayName())).pop()).open(this.player);
        else
            ((Gui.menuStackMap.get(this.player.getDisplayName())).peek()).open(this.player);
    }

    void closeGUI() {
        this.player.getOpenInventory().close();
    }

    void openToolDetailsGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.Tool.MenuName"), true);
        upgradeGui.openAddToList(this.player);
    }

    void openEnchantListGUI(EnchantGui previousEnchantGUI) {
        EnchantGui enchantGUI = new EnchantGui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.Enchants.MenuName"), true);

        if(previousEnchantGUI != null) enchantGUI.makePreviousPageButton();
        else EnchantGui.currentlyViewedPage.put(player.getDisplayName(), 0);

        List<String> enchantsToSkip = (previousEnchantGUI == null) ? new ArrayList<>() : previousEnchantGUI.getEnchantsToSkip();
        int enchantNameSlot = 0;
        Map<String, List<Integer>> enchantWithLevelsMap = EnchantUpgrades.getAvailableEnchants(this.guiManager.getTool());

        for (String enchant : enchantWithLevelsMap.keySet()) {
            if (enchantNameSlot > 36) {
                enchantGUI.setFinishedAddingEnchants(false);
                break;
            }
            int nextSlot = enchantNameSlot + 1;

            if (enchantsToSkip.contains(enchant)) continue;
            if (previousEnchantGUI == null || previousEnchantGUI.isFinishedAddingLevels()) {
                enchantGUI.makeGuiItem(enchant, "", Material.ENCHANTED_BOOK, enchantNameSlot);
                enchantNameSlot += 9;
            }
            for (Integer enchantLevel : enchantWithLevelsMap.get(enchant)) {
                if (nextSlot > 44) {
                    enchantGUI.setFinishedAddingLevels(false);
                    break;
                }
                if (previousEnchantGUI != null &&
                        !previousEnchantGUI.isFinishedAddingLevels()) {

                    if(enchantLevel == enchantWithLevelsMap.get(enchant).get(enchantWithLevelsMap.get(enchant).size()-1)) {
                        enchantNameSlot += 9;
                        previousEnchantGUI.setFinishedAddingLevels(true);
                    }
                    if(enchantLevel <= previousEnchantGUI.getLastLevelListedInGUI()) continue;
                }
                if (nextSlot % 9 == 0) {
                    nextSlot++;
                    enchantNameSlot += 9;
                }

                enchantGUI.makeGuiItem(enchant + ": Level " + enchantLevel, "", Material.BLACK_STAINED_GLASS_PANE, nextSlot++);
                enchantGUI.setLastLevelListedInGUI(enchantLevel);
                if (enchantLevel.equals((enchantWithLevelsMap.get(enchant)).get((enchantWithLevelsMap.get(enchant)).size() - 1))){
                    enchantGUI.setFinishedAddingLevels(true);
                    enchantsToSkip.add(enchant);
                }
            }
        }
        enchantGUI.addEnchantsToSkip(enchantsToSkip);
        enchantGUI.openAndAddToList(this.player);
    }

    void openEnchantPurchaseGUI(String menuName) {}

    void openRepairUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.Repair.MenuName"), true);
        upgradeGui.openAddToList(this.player);
    }
}
