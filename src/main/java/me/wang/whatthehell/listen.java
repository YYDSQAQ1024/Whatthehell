package me.wang.whatthehell;

import me.wang.whatthehell.enchantments.Sign;
import me.wang.whatthehell.utils.Item;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static me.wang.whatthehell.WTH.enchantments;
import static me.wang.whatthehell.WTH.namespacedKeys;
import static me.wang.whatthehell.enchantments.CustomEnchantment.addEnchment;


public class listen implements Listener {
    Plugin plugin = me.wang.whatthehell.WTH.getPlugin(me.wang.whatthehell.WTH.class);

    HashMap<String,String> death = new HashMap<>();

    public static Random random = new Random();
    /*
    万物皆可附魔
     */
    @EventHandler
    public void anvil(PrepareAnvilEvent event){
        if (plugin.getConfig().getBoolean("AnvilForEverything")){
            if (event.getInventory().getRepairCost() < 4){
                event.getInventory().setRepairCostAmount(4);
                event.getInventory().setRepairCost(4);
            }
        }
    }




    @EventHandler
    public void result(PrepareInventoryResultEvent e){
        if (e.getInventory().getItem(0) == null || e.getInventory().getItem(1) == null){
            return;
        }
        if (e.getInventory().getItem(1).getItemMeta() == null){
            return;
        }
        ItemStack itemStack = e.getInventory().getItem(0).clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemStack book = e.getInventory().getItem(1);

        if (!book.getItemMeta().getPersistentDataContainer().has(new Sign().getKey())){
            return;
        }
        if (book.getItemMeta().getLore() == null){
            return;
        }

        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
        if (bookMeta == null) {
            return;
        }
        Map<Enchantment,Integer> map = bookMeta.getStoredEnchants();

        for (Enchantment enchantment:map.keySet()){
            int l = map.get(enchantment);
            if (itemStack.containsEnchantment(enchantment)){
                if (itemStack.getEnchantmentLevel(enchantment) == map.get(enchantment)){
                    l = map.get(enchantment)+1;
                }
            }
            itemMeta.addEnchant(enchantment,l,false);
        }
        for (NamespacedKey namespacedKey:bookMeta.getPersistentDataContainer().getKeys()){
            if (!namespacedKeys.containsKey(namespacedKey)){
                continue;
            }
            Enchantment enchantment = namespacedKeys.get(namespacedKey);
            if (enchantment.getName().equalsIgnoreCase("WTH")){
                continue;
            }
            int level = bookMeta.getPersistentDataContainer().get(namespacedKey,PersistentDataType.INTEGER);
            itemMeta = addEnchment(itemMeta,enchantment,level);
        }
        itemMeta.setEnchantmentGlintOverride(true);
        itemStack.setItemMeta(itemMeta);
        e.setResult(itemStack);
        e.getInventory().setItem(2,itemStack);
    }



    @EventHandler
    public void move(PlayerMoveEvent e){
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        for (ItemStack itemStack:inventory.getArmorContents()){
            if (itemStack == null){
                continue;
            }
            if (!itemStack.hasItemMeta()){
                continue;
            }
            /*
            火焰附加
             */
            if (itemStack.containsEnchantment(Enchantment.FIRE_ASPECT) && plugin.getConfig().getBoolean("FIRE_ASPECT")){
                player.setFireTicks(100+(itemStack.getEnchantmentLevel(Enchantment.FIRE_ASPECT)-1)*40);
            }
            /*
            锋利
            */
            if (itemStack.containsEnchantment(Enchantment.SHARPNESS) && plugin.getConfig().getBoolean("SHARPNESS")){
                player.damage(itemStack.getEnchantmentLevel(Enchantment.SHARPNESS));
            }
            /*
            引雷
             */
            if (itemStack.containsEnchantment(Enchantment.CHANNELING)){
                if (random.nextDouble() <= plugin.getConfig().getInt("CHANNELING")){
                    player.getWorld().strikeLightning(player.getLocation());
                }
            }
            /*
            水肺
             */
            if (itemStack.getItemMeta().getPersistentDataContainer().has(enchantments.get(102).getKey()) && plugin.getConfig().getBoolean("WaterBreathing")){
                if (player.isInWater()){
                    if (player.getRemainingAir() > 2){
                        player.setRemainingAir(2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e){
        Entity entity1 =  e.getEntity();

        if (e.getDamager() instanceof Player) {
            Player player1 = (Player) e.getDamager();
            PlayerInventory inventory1 = player1.getInventory();
            // 检查被攻击者是否是生物（排除玩家自身）
            if (e.getEntity() instanceof LivingEntity && e.getEntity() != player1) {
                if (inventory1.getItemInMainHand().getType() == Material.AIR){
                    return;
                }
                if (inventory1.getItemInMainHand() == null){
                    return;
                }
                if (!inventory1.getItemInMainHand().hasItemMeta()){
                    return;
                }
                /*
                精准采集
                 */
                if (inventory1.getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)){
                    if (random.nextDouble() <= plugin.getConfig().getInt("SILK_TOUCH")){
                        ItemStack spawnEgg = new ItemStack(Material.LEGACY_MONSTER_EGG);

                        SpawnEggMeta meta = (SpawnEggMeta) spawnEgg.getItemMeta();

                        meta.setSpawnedType(e.getEntityType());

                        spawnEgg.setItemMeta(meta);
                    }
                }
                if (inventory1.getItemInMainHand().getItemMeta().getPersistentDataContainer().has(enchantments.get(103).getKey()) && plugin.getConfig().getBoolean("Unarmed")){
                    double d = 0;
                    for (int i=9;i < 46;i++){
                        ItemStack itemStack = inventory1.getItem(i);
                        if (itemStack == null){
                            continue;
                        }
                        d = d + Item.getWeaponDamage(itemStack);
                        if (Item.weaponMaterials.contains(itemStack.getType())){
                            inventory1.setItem(i,new ItemStack(Material.AIR));
                        }
                    }
                    if (entity1 instanceof LivingEntity){
                        LivingEntity livingEntity = (LivingEntity) entity1;
                        livingEntity.damage(d);
                        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacy(ChatColor.AQUA+"你对目标实体造成了"+d+"点额外伤害！"));
                    }else {
                        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacy(ChatColor.AQUA+"Oh no!你浪费了"+d+"点额外伤害"));
                    }
                }
            }
            return;
        }

        if (!(entity1 instanceof Player)){
            return;
        }
        Player player = (Player) entity1;
        PlayerInventory inventory = player.getInventory();
        for (ItemStack itemStack:inventory.getArmorContents()) {
            if (itemStack == null){
                continue;
            }
            if (!itemStack.hasItemMeta()){
                continue;
            }
            /*
            击退
            说明：向后back_distance格，向上1格
             */
            if (itemStack.containsEnchantment(Enchantment.KNOCKBACK) && plugin.getConfig().getBoolean("KNOCKBACK.enable")){
                double back_distance = plugin.getConfig().getInt("KNOCKBACK.x")+((itemStack.getEnchantmentLevel(Enchantment.KNOCKBACK)-1) * 2.586);

                Location loc = player.getLocation();
                Vector direction = loc.getDirection().normalize();
                direction.setY(plugin.getConfig().getInt("KNOCKBACK.y")); // 保持水平击退
                Vector knockback = direction.multiply(-back_distance); // 设置击退距离为5格

                player.setVelocity(knockback);
            }

            if (e.getDamager() instanceof Arrow){
                /*
                无限
                */
                if (itemStack.containsEnchantment(Enchantment.INFINITY) && plugin.getConfig().getBoolean("INFINITY")){
                    player.setHealth(0);
                    String death_msg = player.getDisplayName()+"被无限的箭矢杀死了";
                    death.put(player.getName(),death_msg);
                }
                /*
                多重射击(伤害乘3)
                 */
                if (itemStack.containsEnchantment(Enchantment.MULTISHOT) && plugin.getConfig().getBoolean("MULTISHOT")){
                    double damage = e.getDamage();
                    player.damage(damage*3);
                }
                /*
                穿透(延时5秒死亡)
                */
                if (itemStack.containsEnchantment(Enchantment.PIERCING) && plugin.getConfig().getBoolean("PIERCING")){
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player.setHealth(0);
                        death.put(player.getName(),player.getDisplayName()+"因流血过多而亡");
                    }, 100);//延时
                }
            }
            /*
            横扫之刃
            说明：使以玩家为中心1×0.25×1立方体范围内的生物都会受到伤害
             */
            if (itemStack.containsEnchantment(Enchantment.SWEEPING_EDGE) && plugin.getConfig().getBoolean("SWEEPING_EDGE.enable")){
                for (Entity entity : player.getNearbyEntities(plugin.getConfig().getInt("SWEEPING_EDGE.v"), plugin.getConfig().getInt("SWEEPING_EDGE.v1"), plugin.getConfig().getInt("SWEEPING_EDGE.v2"))) {
                    if (!entity.isValid() || entity.isDead()) {
                        continue;
                    }
                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.damage(e.getDamage(),e.getDamager());
                }
            }

        }
    }


    /*
    修改死亡消息
     */
    @EventHandler
    public void ondeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        /*
        搭配附魔
         */
        if (death.containsKey(player.getName())){
            e.setDeathMessage(death.get(player.getName()));
        }
    }

    /*
    合成WTH附魔书
    ***目前仅供内部使用***
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] items = inventory.getMatrix();

        ItemStack diamond = null;
        ItemStack enchantedBook = null;

        for (ItemStack item : items) {
            if (item != null) {
                if (item.getType() == Material.DIAMOND) {
                    diamond = item;
                } else if (item.getType() == Material.ENCHANTED_BOOK) {
                    enchantedBook = item;
                }
            }
        }

        if (diamond != null && enchantedBook != null) {
            ItemStack result = new ItemStack(Material.ENCHANTED_BOOK);//enchantedBook.clone();
            ItemMeta itemMeta = result.getItemMeta();
            itemMeta = addEnchment(itemMeta,enchantments.get(103),1);
            result.setItemMeta(itemMeta);
            inventory.setResult(result);
        }
    }

    /*
    WTH附魔在附魔台有10%的概率出现
     */
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        if (random.nextDouble() <= plugin.getConfig().getDouble("EnchantTable")) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR) {
                event.setCancelled(true);
                ItemStack itemStack = event.getInventory().getItem(0).clone();
                event.getInventory().clear(0);

                Collection<Enchantment> c_enchantments = enchantments.values();
                List<Enchantment> pdc_enchantments = new ArrayList<>(c_enchantments);
                int c = random.nextInt(pdc_enchantments.size());
                if (c == 0){
                    c = 1;
                }
                Enchantment enchantment = pdc_enchantments.get(c);
                int level = 1 + random.nextInt(enchantment.getMaxLevel());

                ItemMeta meta;
                if (itemStack.getType() == Material.BOOK){
                    itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                }
                if (itemStack.getItemMeta() == null) {
                    ItemStack tempItem;
                    tempItem = new ItemStack(item.getType());
                    meta = tempItem.getItemMeta();
                }else {
                    meta = itemStack.getItemMeta();
                }
                addEnchment(meta,enchantment,level);
                itemStack.setItemMeta(meta);
                event.getInventory().setItem(0,itemStack);
                event.getEnchanter().setLevel(event.getEnchanter().getLevel() - event.getExpLevelCost());
                int lapisCost = event.whichButton() + 1;
                removeLapis(event.getEnchanter(), lapisCost);
            }
        }
    }


    private void removeLapis(Player player, int amount) {
        ItemStack item = player.getOpenInventory().getItem(1);
        if (item == null){
            return;
        }
        int itemAmount = item.getAmount();
        if (itemAmount > amount) {
            item.setAmount(itemAmount - amount);
        } else {
            player.getInventory().remove(item);
            amount -= itemAmount;
            if (amount <= 0) {
            }
        }

    }

    /*
    WTH附魔书在村庄的箱子中有10%的概率生成
     */
    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {

        String lootTable = event.getLootTable().getKey().getKey();
        if (lootTable.contains("village")) {


            if (random.nextDouble() <= plugin.getConfig().getDouble("Village")) {

                ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();


                Collection<Enchantment> c_enchantments = enchantments.values();
                List<Enchantment> pdc_enchantments = new ArrayList<>(c_enchantments);
                int c = random.nextInt(pdc_enchantments.size());
                if (c == 0){
                    c = 1;
                }
                Enchantment enchantment = pdc_enchantments.get(c);
                int level = 1 + random.nextInt(enchantment.getMaxLevel());


                addEnchment(meta,enchantment,level);
                enchantedBook.setItemMeta(meta);


                event.getLoot().add(enchantedBook);
            }
        }
    }


    /*
    弩可以发射方块
    说明：
    初始重力加速度：0.001
    增加重力加速度： 0.0001
    TNT会在落地后激活
     */
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player) {
            Player player = (Player) projectile.getShooter();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (!mainHandItem.hasItemMeta()){
                return;
            }
            if (!mainHandItem.getItemMeta().getPersistentDataContainer().has(enchantments.get(104).getKey())){
                return;
            }

            if (offHandItem != null) {
                if (!offHandItem.getType().isBlock() && offHandItem.getType() != Material.WATER_BUCKET && offHandItem.getType() != Material.LAVA_BUCKET && offHandItem.getType() != Material.FLINT_AND_STEEL){
                    return;
                }
                if (mainHandItem.getItemMeta().getPersistentDataContainer().get(enchantments.get(104).getKey(),PersistentDataType.INTEGER) != 2){
                    if (offHandItem.getType() == Material.WATER_BUCKET || offHandItem.getType() == Material.LAVA_BUCKET || offHandItem.getType() == Material.FLINT_AND_STEEL){
                        return;
                    }
                }
                if (!plugin.getConfig().getBoolean("ProjectileLaunch.enable")){
                    return;
                }

                event.setCancelled(true);

                ItemStack itemStack = offHandItem.clone();


                Material material = offHandItem.getType();
                if (offHandItem.getType() == Material.WATER_BUCKET){
                    material = Material.WATER;
                }
                if (offHandItem.getType() == Material.LAVA_BUCKET){
                    material = Material.LAVA;
                }
                if (offHandItem.getType() == Material.FLINT_AND_STEEL){
                    material = Material.FIRE;
                }
                FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(
                        player.getEyeLocation(),
                        material.createBlockData()
                );

                ItemStack itemStack1 = player.getInventory().getItemInMainHand();
                ItemMeta meta = itemStack1.getItemMeta();
                List<String> list = meta.getLore();

                if (list != null){

                    for (int i=0;i < list.size();i++){
                        if (list.get(i).startsWith(ChatColor.AQUA+"弹射物")){
                            list.remove(i);
                        }
                    }
                }


                String name = material == Material.FIRE ? "火" : material.name().toLowerCase();
                list.add(ChatColor.AQUA+"弹射物"+ChatColor.GRAY+"["+ChatColor.WHITE+name+ChatColor.GRAY+"]");
                meta.setLore(list);
                itemStack1.setItemMeta(meta);
                player.getInventory().setItemInMainHand(itemStack1);


                Vector velocity = player.getLocation().getDirection().multiply(1.5);
                fallingBlock.setVelocity(velocity);


                fallingBlock.setDropItem(false);


                new BukkitRunnable() {
                    private double gravity = plugin.getConfig().getDouble("ProjectileLaunch.InitialGravitationalAcceleration");

                    @Override
                    public void run() {

                        if (!fallingBlock.isValid() || fallingBlock.isOnGround()) {
                            this.cancel();
                            if (itemStack.getType() == Material.TNT && plugin.getConfig().getBoolean("ProjectileLaunch.TNTPrimed")){
                                Location location = fallingBlock.getLocation();
                                World world = fallingBlock.getWorld();
                                fallingBlock.remove();
                                world.setType(location,Material.AIR);
                                world.spawn(location, TNTPrimed.class);
                            }
                            return;
                        }


                        Vector velocity = fallingBlock.getVelocity();

                        fallingBlock.setVelocity(new Vector(velocity.getX(), velocity.getY() - gravity, velocity.getZ()));


                        gravity += plugin.getConfig().getDouble("ProjectileLaunch.IncreaseGravitationalAcceleration");
                    }
                }.runTaskTimer(plugin, 0L, 1L);


                if (material != Material.FIRE){
                    if (offHandItem.getAmount() > 1) {
                        offHandItem.setAmount(offHandItem.getAmount() - 1);
                    } else {
                        player.getInventory().setItemInOffHand(null);
                    }
                }
            }
        }
    }



}
