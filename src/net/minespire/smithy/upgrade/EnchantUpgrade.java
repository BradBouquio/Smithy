package net.minespire.smithy.upgrade;

import net.minespire.smithy.Tool;
import org.bukkit.enchantments.Enchantment;

public class EnchantUpgrade implements Purchasable {
    Enchantment enchant;
    int enchantLevel;
    double energyCost;

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

    @Override
    public void apply(Tool tool) {
        tool.getItemStack().addUnsafeEnchantment(enchant,enchantLevel);
    }
}
