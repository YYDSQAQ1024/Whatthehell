package me.wang.whatthehell.enchantments;

import me.wang.whatthehell.WTH;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class WaterBreathing extends Enchantment {

    public WaterBreathing(){
        super();
    }

    @Override
    public String getName() {
        return ChatColor.STRIKETHROUGH +"水肺";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
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
        return new NamespacedKey(WTH.getPlugin(me.wang.whatthehell.WTH.class), "WTH_WaterBreathing");
    }

    @Override
    public String getTranslationKey() {
        return "WaterBreathing";
    }
}
