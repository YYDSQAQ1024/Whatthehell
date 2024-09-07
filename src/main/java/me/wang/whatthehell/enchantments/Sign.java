package me.wang.whatthehell.enchantments;

import me.wang.whatthehell.WTH;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Sign extends Enchantment {
    @Override
    public String getName() {
        return "WTH";
    }


    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WTH.getPlugin(me.wang.whatthehell.WTH.class), "WTH_Sign");
    }

    @Override
    public String getTranslationKey() {
        return "WTH";
    }
}
