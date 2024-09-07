package me.wang.whatthehell.enchantments;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.wang.whatthehell.WTH.enchantments;

public class CustomEnchantment {

    /*
    转换附魔等级
     */
    public static String translationLevel(int level){
        switch (level){
            case 1 :
                return "I";
            case 2 :
                return "II";
            case 3 :
                return "III";
            case 4 :
                return "IV";
            case 5 :
                return "V";
        }
        return "level."+level;
    }

    /*
    添加WTH附魔
     */
    public static ItemMeta addEnchment(ItemMeta meta, Enchantment enchantment, int level){
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(enchantment.getKey())){
            int l = pdc.get(enchantment.getKey(),PersistentDataType.INTEGER);
            if (enchantment.getMaxLevel() == l){
                removeEnchment(meta,enchantment,l);
                level = enchantment.getMaxLevel();
            }else if (l > level){
                return meta;
            }else if (l < level){
                removeEnchment(meta,enchantment,l);
            }else if (l == level){
                level = level + 1;
            }
        }
        pdc.set(enchantment.getKey(), PersistentDataType.INTEGER,level);
        pdc.set(enchantments.get(101).getKey(),PersistentDataType.INTEGER,0);
        List<String> list;
        if (meta.getLore() == null){
            list = new ArrayList<>();
        }else {
            list = meta.getLore();
        }
        list.add(ChatColor.GRAY+enchantment.getName()+" "+translationLevel(level));
        meta.setLore(list);
        return meta;
    }

    /*
    移除WTH附魔
     */
    public static ItemMeta removeEnchment(ItemMeta meta, Enchantment enchantment, int level){
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(enchantment.getKey());
        List<String> list = meta.getLore();
        list.remove(ChatColor.GRAY+enchantment.getName()+" "+translationLevel(level));
        meta.setLore(list);
        return meta;
    }
}
