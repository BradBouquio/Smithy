package net.minespire.smithy.util;

import net.minespire.smithy.Smithy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Convert {

    private static Map<String, String> enchantNameMap = new HashMap<>();

    static{
        enchantNameMap.put("ARROW_DAMAGE", "Power"); enchantNameMap.put("ARROW_FIRE", "Flame");
        enchantNameMap.put("ARROW_INFINITE", "Infinity"); enchantNameMap.put("ARROW_KNOCKBACK", "Punch");
        enchantNameMap.put("BINDING_CURSE", "Curse of Binding"); enchantNameMap.put("CHANNELING", "Channeling");
        enchantNameMap.put("DAMAGE_ALL", "Sharpness"); enchantNameMap.put("DAMAGE_ARTHROPODS", "Bane of Arthropods");
        enchantNameMap.put("DAMAGE_UNDEAD", "Smite"); enchantNameMap.put("DEPTH_STRIDER", "Depth Strider");
        enchantNameMap.put("DIG_SPEED", "Efficiency"); enchantNameMap.put("DURABILITY", "Unbreaking");
        enchantNameMap.put("FIRE_ASPECT", "Fire Aspect"); enchantNameMap.put("FROST_WALKER", "Frost Walker");
        enchantNameMap.put("IMPALING", "Impaling"); enchantNameMap.put("KNOCKBACK", "Knockback");
        enchantNameMap.put("LOOT_BONUS_BLOCKS", "Fortune"); enchantNameMap.put("LOOT_BONUS_MOBS", "Looting");
        enchantNameMap.put("LOYALTY", "Loyalty"); enchantNameMap.put("LUCK", "Luck of the Sea");
        enchantNameMap.put("LURE", "Lure"); enchantNameMap.put("MENDING", "Mending");
        enchantNameMap.put("MULTISHOT", "Multishot"); enchantNameMap.put("OXYGEN", "Respiration");
        enchantNameMap.put("PIERCING", "Piercing"); enchantNameMap.put("PROTECTION_ENVIRONMENTAL", "Protection");
        enchantNameMap.put("PROTECTION_EXPLOSIONS", "Blast Protection"); enchantNameMap.put("PROTECTION_FALL", "Feather Falling");
        enchantNameMap.put("PROTECTION_FIRE", "Fire Protection"); enchantNameMap.put("PROTECTION_PROJECTILE", "Projectile Protection");
        enchantNameMap.put("QUICK_CHARGE", "Quick Charge"); enchantNameMap.put("RIPTIDE", "Riptide");
        enchantNameMap.put("SILK_TOUCH", "Silk Touch"); enchantNameMap.put("SOUL_SPEED", "Soul Speed");
        enchantNameMap.put("SWEEPING_EDGE", "Sweeping Edge"); enchantNameMap.put("THORNS", "Thorns");
        enchantNameMap.put("VANISHING_CURSE", "Curse of Vanishing"); enchantNameMap.put("WATER_WORKER", "Aqua Affinity");
    }

    public static String enchantToVanillaName(String bukkitName){
        return enchantNameMap.get(bukkitName);
    }

    public static String toRomanNumeral(int num) {
        LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
        roman_numerals.put("M", 1000);
        roman_numerals.put("CM", 900);
        roman_numerals.put("D", 500);
        roman_numerals.put("CD", 400);
        roman_numerals.put("C", 100);
        roman_numerals.put("XC", 90);
        roman_numerals.put("L", 50);
        roman_numerals.put("XL", 40);
        roman_numerals.put("X", 10);
        roman_numerals.put("IX", 9);
        roman_numerals.put("V", 5);
        roman_numerals.put("IV", 4);
        roman_numerals.put("I", 1);
        String res = "";
        for(Map.Entry<String, Integer> entry : roman_numerals.entrySet()){
            int matches = num/entry.getValue();
            res += repeat(entry.getKey(), matches);
            num = num % entry.getValue();
        }
        return res;
    }

    public static String placeholders(String text, String enchant, Integer enchantLevel){
        if (text != null) {
            text = text.replace("{Enchant}", Convert.enchantToVanillaName(enchant));
            if(enchantLevel==null) return text;
            if(Smithy.plugin.getConfig().getBoolean("GUI.MenuItems.EnchantMenu.EnchantLevels.RomanNumerals")){
                text = text.replace("{EnchantLevel}", Convert.toRomanNumeral(enchantLevel));
            } else text = text.replace("{EnchantLevel}", enchantLevel.toString());
        }
        return text;
    }

    private static String repeat(String s, int n) {
        if(s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

}
