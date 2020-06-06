package net.minespire.smithy.upgrade;

import net.minespire.smithy.Smithy;
import net.minespire.smithy.Tool;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public class EnchantUpgrade implements Applicable {
    Enchantment enchant;
    int enchantLevel;
    double energyCost;
    public static Map<String,Map<String,String>> validToolEnchants;

    static {
        validToolEnchants = new HashMap<>();
        List<String> enchantsWithLevelsList;
        Map<String,String> enchantsWithLevelsMap;
        for (String tool:Smithy.plugin.getConfig().getConfigurationSection("Tools").getKeys(false)) {
            enchantsWithLevelsList = new ArrayList<>(Arrays.asList(Smithy.plugin.getConfig().getString("Tools." + tool + ".DefaultEnchants").split(",")));
            enchantsWithLevelsList.addAll(Arrays.asList(Smithy.plugin.getConfig().getString("Tools." + tool + ".UnlockableEnchants").split(",")));
            enchantsWithLevelsMap = new HashMap<>();
            for(String enchantWithLevels : enchantsWithLevelsList)
            {
                String[] enchantWithLevelsArr = enchantWithLevels.split(":");
                String enchantName = enchantWithLevelsArr[0];
                String enchantLevels = "";
                //if (enchantWithLevelsArr.length > 0) enchantLevels = enchantWithLevels.substring(enchantName.length()+1);
                if (enchantWithLevels.length() > enchantName.length()) enchantLevels = enchantWithLevels.substring(enchantName.length()+1);
                enchantsWithLevelsMap.put(enchantName,enchantLevels);

            }
            validToolEnchants.put(tool, enchantsWithLevelsMap);
            //UpgradeManager.upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("Tools." + tool + ".UnlockableEnchants"), "Unlock Enchant");
        }
    }
    EnchantUpgrade setEnergyCost(double cost){
        energyCost = cost;
        return this;
    }

    EnchantUpgrade setEnchant(Enchantment enchant){
        this.enchant = enchant;
        return this;
    }

    EnchantUpgrade setEnchantLevel(int level){
        enchantLevel = level;
        return this;
    }

    public static Map<String,String> getAvailableEnchants(Tool tool){
        for (String toolName:validToolEnchants.keySet()) {
            if(toolName.equals(tool.getConfigDefinedToolName())) return validToolEnchants.get(toolName);
        }
        return new HashMap<String,String>();
    }

    @Override
    public void apply(Tool tool) {
        tool.getItemStack().addUnsafeEnchantment(enchant,enchantLevel);
    }
}
