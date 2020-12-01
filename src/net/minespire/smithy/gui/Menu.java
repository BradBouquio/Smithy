package net.minespire.smithy.gui;

import net.minespire.smithy.Smithy;
import net.minespire.smithy.config.EnchantUpgrades;
import net.minespire.smithy.upgrade.EnchantUpgrade;
import net.minespire.smithy.util.Convert;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.*;

public class Menu {
    Player player;

    GuiManager guiManager;
    private List<MenuItem> menuItemList = new ArrayList<>();
    private int currentPage;
    static Map<String, EnchantGui> enchantGUIMap = new HashMap<>();

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
        enchantGUIMap.put(player.getDisplayName(), EnchantGui.enchantPageList.get(player.getDisplayName()).get(currentPage-1));
    }

    void showNextPage() {
        this.currentPage = EnchantGui.currentlyViewedPage.get(this.player.getDisplayName());
        if ((EnchantGui.enchantPageList.get(this.player.getDisplayName())).size() > this.currentPage+1) {
            ((EnchantGui.enchantPageList.get(this.player.getDisplayName())).get(this.currentPage+1)).open(this.player);
            enchantGUIMap.put(player.getDisplayName(), EnchantGui.enchantPageList.get(player.getDisplayName()).get(currentPage+1));
        } else {
            openEnchantListGUI((EnchantGui.enchantPageList.get(this.player.getDisplayName())).get(this.currentPage));
        }
        EnchantGui.currentlyViewedPage.put(this.player.getDisplayName(), Integer.valueOf(this.currentPage + 1));
    }

    void openConjureUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.ConjureMenu.MenuName"), true);
        upgradeGui.openAndAddToGuiList(this.player);
    }

    void openParticlesUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.ParticlesMenu.MenuName"), true);
        upgradeGui.openAndAddToGuiList(this.player);
    }

    void openPreviousMenu() {
        if(Gui.menuStackMap.get(this.player.getDisplayName()).size()>1) {
            Gui.menuStackMap.get(this.player.getDisplayName()).pop();
            Gui.menuStackMap.get(this.player.getDisplayName()).peek().open(this.player);
        }
        else
            ((Gui.menuStackMap.get(this.player.getDisplayName())).peek()).open(this.player);
    }

    void closeGUI() {
        this.player.getOpenInventory().close();
    }

    void openToolDetailsGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.ToolMenu.MenuName"), true);
        upgradeGui.openAndAddToGuiList(this.player);
    }

    void openEnchantListGUI(EnchantGui previousEnchantGUI) {
        EnchantGui enchantGUI = new EnchantGui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.MenuName"), true);
        enchantGUI.setPlayer(player);
        enchantGUIMap.put(player.getDisplayName(), enchantGUI);

        guiManager.getTool().setItem(player.getInventory().getItemInMainHand());
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

            Enchantment minecraftEnchant = Enchantment.getByKey(NamespacedKey.minecraft(Convert.enchantToVanillaName(enchant).replace(" ", "_").toLowerCase()));
            // Check if enchant in config starts with level 0 and if it does not exist on tool.
            if(enchantWithLevelsMap.get(enchant).get(0)==0 &&
                    !this.guiManager.getTool().getItemStack().containsEnchantment(minecraftEnchant)) {
                enchantGUI.makeGuiItem(Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.LockedItem.Name"),
                        enchant, null), "",
                        Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.LockedItem.Item")), enchantNameSlot);
                enchantNameSlot += 9;
                enchantsToSkip.add(enchant);
                continue;
            }

            if (previousEnchantGUI == null || previousEnchantGUI.isFinishedAddingLevels()) {
                enchantGUI.makeGuiItem(Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.UnlockedItem.Name"),
                        enchant, null), "",
                        Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.Enchantments.UnlockedItem.Item")), enchantNameSlot);
                enchantNameSlot += 9;
            }
            for (Integer enchantLevel : enchantWithLevelsMap.get(enchant)) {
                if(enchantLevel == 0) continue;
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


                if(this.guiManager.getTool().getItemStack().getEnchantmentLevel(minecraftEnchant)<enchantLevel){
                    Map.Entry<String, Integer> indexToEnchantMap;
                    indexToEnchantMap = new AbstractMap.SimpleEntry(enchant,enchantLevel);
                    enchantGUI.getEnchantFromIndex.put(nextSlot, indexToEnchantMap); //Associate the index of the inventory with the enchant/enchantlevel for future retrieval
                    enchantGUI.makeGuiItem(Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.LockedItem.Name"),
                            enchant, enchantLevel), Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.LockedItem.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.LockedItem.Item")), nextSlot++);

                    enchantGUI.setLastLevelListedInGUI(enchantLevel);
                    enchantGUI.setFinishedAddingLevels(true);
                    enchantsToSkip.add(enchant);
                    break;
                } else {
                    enchantGUI.makeGuiItem(Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.UnlockedItem.Name"),
                            enchant, enchantLevel), Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.UnlockedItem.Lore"), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.EnchantLevels.UnlockedItem.Item")), nextSlot++);
                }


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

    void openRepairUpgradeGUI() {
        Gui upgradeGui = new Gui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.RepairMenu.MenuName"), true);
        upgradeGui.setPlayer(player);
        upgradeGui.openAndAddToGuiList(this.player);
    }

    public List<MenuItem> getMenuItemList(){
        return menuItemList;
    }

    public boolean addToMenuItemList(MenuItem mi){
        return menuItemList.add(mi);
    }

    public void enchantLevelUpgradeConfirmed() {
        ((ConfirmGui)Gui.menuStackMap.get(player.getDisplayName()).peek()).getUpgrade().apply();
        EnchantGui.enchantPageList.remove(this.player.getDisplayName());
        openEnchantListGUI(null);
    }

    public void attemptEnchantUnlock(int indexInMenu) {
        //Bukkit.broadcastMessage(enchantGUIMap.get(player.getDisplayName()).getEnchantFromIndex.get(indexInMenu).get(0).toString());
    }

    public EnchantUpgrade generateEnchantLevelUpgrade(int indexInMenu) {
        Map.Entry<String,Integer> enchantEntry = enchantGUIMap.get(player.getDisplayName())
                .getEnchantFromIndex.get(indexInMenu);
        String enchantName = enchantEntry.getKey();
        Integer enchantLevel = enchantEntry.getValue();
        EnchantUpgrade upgrade = new EnchantUpgrade(player.getInventory().getItemInMainHand());

        //An EnchantGui is only added to the menuStackMap if we exit the EnchantGui through a
        //prompt (by attempting an upgrade). Otherwise the EnchantGui has its own forward and
        //backward navigation via its list of enchant pages. If we find that we've added the
        //same EnchantGui by moving forward and backward multiple times, we should then remove
        //that redundant EnchantGui.
        enchantGUIMap.get(player.getDisplayName()).addToMenuMap(player);
        int playersMenuStackSize = Gui.menuStackMap.get(player.getDisplayName()).size();
        Gui gui1 = Gui.menuStackMap.get(player.getDisplayName()).get(playersMenuStackSize-1);
        Gui gui2 = Gui.menuStackMap.get(player.getDisplayName()).get(playersMenuStackSize-2);
        if(gui1.equals(gui2))
            Gui.menuStackMap.get(player.getDisplayName()).pop();

        return upgrade.setEnchant(enchantName).setPlayer(player).setLevel(enchantLevel);
    }

}
