package me.wang.whatthehell;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class tools {
    public static ItemStack createEnchantedBook(Enchantment enchantment, int level) {
        // 创建一个附魔书的物品堆
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        // 获取附魔书的元数据
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        // 向附魔书添加附魔
        meta.addStoredEnchant(enchantment, level, true);
        // 设置附魔书的元数据
        enchantedBook.setItemMeta(meta);
        return enchantedBook;
    }
}
