package net.minespire.smithy.upgrade;

import net.minespire.smithy.Smithy;
import net.minespire.smithy.gui.ConfirmGui;
import net.minespire.smithy.util.Convert;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantUpgrade implements Purchasable {
    private Enchantment enchant;
    private int level;
    private double cost = 0;
    private Player player;
    private String name;
    private ItemStack item;

    public EnchantUpgrade(ItemStack item){
        this.item = item;
    }

    public EnchantUpgrade setCost(double cost){
        this.cost = cost;
        return this;
    }

    public EnchantUpgrade setEnchant(String enchant){
        name = enchant;
        this.enchant = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(Convert.enchantToVanillaName(enchant).toLowerCase().replace(" ", "_")));
        return this;
    }

    public EnchantUpgrade setLevel(int level){
        this.level = level;
        return this;
    }

    public EnchantUpgrade setPlayer(Player player) {
        this.player = player;
        return this;
    }

    @Override
    public void apply() {
        item.addUnsafeEnchantment(enchant, level);
    }

    public void prompt() {
        ConfirmGui confirmGui = new ConfirmGui(54, Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Title"), this);
        confirmGui.setPlayer(player);
        for(int i = 10; i < 31; i++) {
            if(i==13 || i==22) i+=6;
            confirmGui.makeGuiItem( Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Deny.Name"), name, level),
                    Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Deny.Lore"), name, level), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Deny.Item")), i);

            confirmGui.makeGuiItem( Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Accept.Name"), name, level),
                    Convert.placeholders(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Accept.Lore"), name, level), Material.getMaterial(Smithy.plugin.getConfig().getString("GUI.MenuItems.EnchantMenu.ConfirmationPrompt.Accept.Item")), i+4);
        }
        confirmGui.openAndAddToGuiList(this.player);
    }

}
