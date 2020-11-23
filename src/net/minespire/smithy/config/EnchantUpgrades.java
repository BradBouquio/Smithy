package net.minespire.smithy.config;

import net.minespire.smithy.Smithy;
import net.minespire.smithy.Tool;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class EnchantUpgrades {
    public static Map<String, Map<String,List<Integer>>> validToolEnchants;
    private static FileConfiguration pluginConfig = Smithy.plugin.getConfig();

    static {
        validToolEnchants = new HashMap<>();
        List<String> enchantsWithLevelsList;
        Map<String,List<Integer>> enchantsWithLevelsMap;
        for (String tool: pluginConfig.getConfigurationSection("Tools").getKeys(false)) {
            enchantsWithLevelsList = new ArrayList<>(Arrays.asList(pluginConfig.getString("Tools." + tool + ".DefaultEnchants").split(",")));
            enchantsWithLevelsList.addAll(Arrays.asList(pluginConfig.getString("Tools." + tool + ".UnlockableEnchants").split(",")));
            enchantsWithLevelsMap = new LinkedHashMap<>();

            for(String enchantWithLevels : enchantsWithLevelsList)
            {
                String[] enchantWithLevelsArr = enchantWithLevels.split(":");
                String enchantName = enchantWithLevelsArr[0];
                List<Integer> enchantLevels = new ArrayList<>();
                //if (enchantWithLevelsArr.length > 0) enchantLevels = enchantWithLevels.substring(enchantName.length()+1);
                if (enchantWithLevels.length() > enchantName.length()) { //enchantLevels = enchantWithLevels.substring(enchantName.length()+1);
                    enchantLevels = Arrays.asList(enchantWithLevelsArr).stream()
                            .skip(1)
                            .map(lvl -> Integer.valueOf(lvl))
                            .collect(Collectors.toList());
                }
                enchantsWithLevelsMap.put(enchantName,enchantLevels);

            }
            validToolEnchants.put(tool, enchantsWithLevelsMap);
            //UpgradeManager.upgradeGuiMenuNames.put(Smithy.plugin.getConfig().getString("Tools." + tool + ".UnlockableEnchants"), "Unlock Enchant");

        }


    }

    public static Map<String,List<Integer>> getAvailableEnchants(Tool tool){
        for (String toolName:validToolEnchants.keySet()) {
            if(toolName.equals(tool.getConfigDefinedToolName())) return validToolEnchants.get(toolName);
        }
        return new HashMap<>();
    }
}
