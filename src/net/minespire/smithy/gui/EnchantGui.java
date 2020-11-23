package net.minespire.smithy.gui;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantGui extends Gui {
    boolean finishedAddingEnchants = true;
    boolean finishedAddingLevels = true;
    public static Map<String, List<EnchantGui>> enchantPageList = new HashMap<>();
    public static Map<String, Integer> currentlyViewedPage = new HashMap<>();

    List<String> enchantsToSkip = new ArrayList<>();
    Integer lastLevelListedInGUI = Integer.valueOf(0);

    public EnchantGui(int slots, String name, boolean isStackedMenu) {
        super(slots, name, isStackedMenu);
    }

    public void addEnchantsToSkip(List<String> enchantsToSkip) {
        this.enchantsToSkip = enchantsToSkip;
        if (!isFinishedAddingEnchants() || !isFinishedAddingLevels())
            makeNextPageButton();
    }

    public List<String> getEnchantsToSkip() {
        return this.enchantsToSkip;
    }

    public Integer getLastLevelListedInGUI() {
        return this.lastLevelListedInGUI;
    }

    public void setLastLevelListedInGUI(Integer lastLevel) {
        this.lastLevelListedInGUI = lastLevel;
    }

    public void openAndAddToList(Player player) {
        addToEnchantPageList(player);
//        Integer currentPage = Integer.valueOf(0);
//        if (currentlyViewedPage.containsKey(player.getDisplayName()))
//            currentPage = currentlyViewedPage.get(player.getDisplayName());
//        currentlyViewedPage.put(player.getDisplayName(), currentPage = Integer.valueOf(currentPage.intValue() + 1));
        player.openInventory(this.GUI);
    }

    public void addToEnchantPageList(Player player) {
        if (enchantPageList.containsKey(player.getDisplayName())) {
            List<EnchantGui> pageList = enchantPageList.get(player.getDisplayName());
            if (pageList.contains(this)) return;
            pageList.add(this);
        } else {
            List<EnchantGui> pageList = new ArrayList<>();
            pageList.add(this);
            enchantPageList.put(player.getDisplayName(), pageList);
        }
    }

    public boolean isFinishedAddingEnchants() {
        return this.finishedAddingEnchants;
    }

    public void setFinishedAddingEnchants(boolean isFinished) {
        this.finishedAddingEnchants = isFinished;
    }

    public boolean isFinishedAddingLevels() {
        return this.finishedAddingLevels;
    }

    public void setFinishedAddingLevels(boolean isFinished) {
        this.finishedAddingLevels = isFinished;
    }
}