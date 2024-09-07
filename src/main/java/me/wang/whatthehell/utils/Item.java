package me.wang.whatthehell.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item {

    /*
    获取武器伤害
     */
    public static double getWeaponDamage(ItemStack item) {
        double baseDamage = 0.0;

        switch (item.getType()) {
            case WOODEN_SWORD:
                baseDamage = 4.0;
                break;
            case STONE_SWORD:
                baseDamage = 5.0;
                break;
            case IRON_SWORD:
                baseDamage = 6.0;
                break;
            case GOLDEN_SWORD:
                baseDamage = 4.0;
                break;
            case DIAMOND_SWORD:
                baseDamage = 7.0;
                break;
            case NETHERITE_SWORD:
                baseDamage = 8.0;
                break;
            case WOODEN_AXE:
                baseDamage = 7.0;
                break;
            case STONE_AXE:
                baseDamage = 9.0;
                break;
            case IRON_AXE:
                baseDamage = 9.0;
                break;
            case GOLDEN_AXE:
                baseDamage = 7.0;
                break;
            case DIAMOND_AXE:
                baseDamage = 9.0;
                break;
            case NETHERITE_AXE:
                baseDamage = 10.0;
                break;
            default:
                break;
        }

        // 计算附魔的额外伤害
        for (int level:item.getEnchantments().values()){
            baseDamage += 1.0 + (level - 1) * 0.5;
        }

        return baseDamage;
    }

    /*
    武器列表
     */
    public static List<Material> weaponMaterials = new ArrayList<>(Arrays.asList(
            Material.WOODEN_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    ));


}
