package me.wang.whatthehell;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class recipes {
    public static ShapelessRecipe badluck(Plugin plugin){
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        //创建一个无序配方
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin, "wth_special_book"), enchantedBook);
        recipe.addIngredient(1,Material.DIAMOND);
        recipe.addIngredient(1,Material.ENCHANTED_BOOK);
        return recipe;
    }
}
