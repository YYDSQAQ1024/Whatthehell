package me.wang.whatthehell;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static me.wang.whatthehell.WTH.enchantments;
import static me.wang.whatthehell.enchantments.CustomEnchantment.addEnchment;

public class command implements CommandExecutor {
    public ItemStack createCustomBlockItem() {
        ItemStack item = new ItemStack(Material.SHULKER_SHELL); // Use a base material, like NOTE_BLOCK
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Custom Block");
        meta.setCustomModelData(1); // Set custom model data
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("wth")){
            if (strings[0].equalsIgnoreCase("get")){
                if (strings[1].equalsIgnoreCase("101")){
                    try {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.getPersistentDataContainer().set(enchantments.get(101).getKey(),PersistentDataType.INTEGER,0);
                        itemStack.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(itemStack);
                    } catch (Exception e) {

                    }
                }
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta = addEnchment(itemMeta,enchantments.get(Integer.valueOf(strings[1])),Integer.valueOf(strings[2]));
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
            }
            if (strings[0].equalsIgnoreCase("c")){
                ItemStack item = new ItemStack(Material.getMaterial(strings[1].toLowerCase()));
                ItemMeta meta = item.getItemMeta();
                meta.setCustomModelData(1);
                //meta.setDisplayName(ChatColor.RESET + "破片手雷");
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                player.sendMessage("ok");
            }
        }
        return false;
    }
}
