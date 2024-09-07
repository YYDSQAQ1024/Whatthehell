package me.wang.whatthehell;

import me.wang.whatthehell.enchantments.ItemAttached;
import me.wang.whatthehell.enchantments.Sign;
import me.wang.whatthehell.enchantments.Unarmed;
import me.wang.whatthehell.enchantments.WaterBreathing;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class WTH extends JavaPlugin {
    public static Plugin plugin;
    public static Map<Integer, Enchantment> enchantments = new HashMap<>();
    public static Map<NamespacedKey, Enchantment> namespacedKeys = new HashMap<>();
    public static NamespacedKey submachine;
    public static NamespacedKey magazine;

    public void reset(Plugin p){
        plugin = p;
        submachine = new NamespacedKey(plugin,"bullet");
        magazine = new NamespacedKey(plugin,"magazine");
    }

    public void register(int id,Enchantment enchantment){
        enchantments.put(id,enchantment);
        namespacedKeys.put(enchantment.getKey(),enchantment);
    }
    @Override
    public void onEnable() {
        saveResource("config.yml",false);
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(new listen(),this);
        Bukkit.addRecipe(recipes.badluck(this));
        getCommand("wth").setExecutor(new command());



        /*
        注册自定义附魔
         */
        register(101, new Sign());
        register(102, new WaterBreathing());
        register(103,new Unarmed());
        register(104,new ItemAttached());

        plugin = me.wang.whatthehell.WTH.getPlugin(me.wang.whatthehell.WTH.class);
        reset(plugin);
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
        // Plugin shutdown logic
    }




}
