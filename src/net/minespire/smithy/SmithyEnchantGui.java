package net.minespire.smithy;

import org.bukkit.entity.Player;

import java.util.*;

public class SmithyEnchantGui extends SmithyGUI{
    public static Map<String, Stack<SmithyGUI>> enchantStackMap = new HashMap<>();
    List<String> enchantsToSkip = new ArrayList<>();
    String lastLevelListedInGUI = "";

    public SmithyEnchantGui(int slots, String name, boolean isStackedMenu){
        super(slots, name, isStackedMenu);
    }

    public void addEnchantsToSkip(List<String> enchantsToSkip){
        this.enchantsToSkip.addAll(enchantsToSkip);
        super.makeNextPageButton();
    }

    public List<String> getEnchantsToSkip(){
        return enchantsToSkip;
    }

    public String getLastLevelListedInGUI(){
        return lastLevelListedInGUI;
    }

    public void setLastLevelListedInGUI(String lastLevel){
        this.lastLevelListedInGUI = lastLevel;
    }

    public void openAddToStack(Player player) {
        addToEnchantMap(player);
        player.openInventory(GUI);
    }

    public void addToEnchantMap(Player player) {
        Stack menuStack;
        if(enchantStackMap.containsKey(player.getDisplayName())){
            menuStack = enchantStackMap.get(player.getDisplayName());
            menuStack.push(this);
        }
        else {
            menuStack = new Stack<SmithyEnchantGui>();
            menuStack.push(this);
            enchantStackMap.put(player.getDisplayName(), menuStack);
        }
    }
}
