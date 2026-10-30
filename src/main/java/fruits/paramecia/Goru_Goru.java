package fruits.paramecia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import users.OPUser;
import cast.FruitCaster;

import java.util.*;
import java.util.UUID;

public class Goru_Goru extends Paramecia {
    private static final int ABSORB_GOLD_COOLDOWN = 0;
    private static final int GOLD_CREATION_COOLDOWN = 0;
    private static final int CONTROL_GOLD_COOLDOWN = 0;
    private static final int GOLD_RISE_COOLDOWN = 0;

    private int radiusFloor = 3;
    private double storaged = 0;
    private final double maxStoraged = 1024.0;
    private boolean opened = false;
    private boolean glide = false;
    private boolean flightEnabled = false;
    private final Particle.DustOptions goldDust = new Particle.DustOptions(Color.YELLOW, 1.0F);

    public static int getFruitID() {
        return 1016;
    }

    public Goru_Goru(int id) {
        super(id, "goru_goru", "Goru Goru no Mi", "goru_goru");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Absorb Gold", ABSORB_GOLD_COOLDOWN, () -> {
            OPUser user = this.getUser();
            if (user != null && user.getPlayer() != null) {
                Player p = user.getPlayer();
                absorb(p);
            }
        }));
        basicSet.addAbility(new CooldownAbility("Gold Creation", GOLD_CREATION_COOLDOWN, this::goldCreation));
        basicSet.addAbility(new CooldownAbility("Control Gold", CONTROL_GOLD_COOLDOWN, this::controlGold));
        basicSet.addAbility(new CooldownAbility("Gold Rise", GOLD_RISE_COOLDOWN, this::goldRise));
        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {}

    @Override
    public void onRemoveFruit() {}

    public void absorb(Player player) {
        OPUser user = this.getUser();
        if (user == null) return;
        if (storaged >= maxStoraged) return;

        if (player.isSneaking()) {
            absorbObject(player);
        } else {
            int x1 = player.getLocation().getBlockX() - radiusFloor;
            int y1 = player.getLocation().getBlockY() - radiusFloor;
            int z1 = player.getLocation().getBlockZ() - radiusFloor;
            int x2 = player.getLocation().getBlockX() + radiusFloor;
            int y2 = player.getLocation().getBlockY() + radiusFloor;
            int z2 = player.getLocation().getBlockZ() + radiusFloor;

            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        Location block = new Location(player.getWorld(), x, y, z);
                        if (isGold(block.getBlock()) && storaged < maxStoraged) {
                            absorbBlock(block);
                        }
                    }
                }
            }
        }
    }

    private void absorbObject(Player player) {
        ItemStack hand = player.getInventory().getItemInOffHand();
        if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasCustomModelData()) {
            hand = player.getInventory().getItemInMainHand();
        }

        if (hand == null) return;

        switch (hand.getType()) {
            case GOLDEN_APPLE:
                storaged += 15 * hand.getAmount();
                hand.setAmount(0);
                break;
            case GOLDEN_CARROT:
                storaged += 10 * hand.getAmount();
                hand.setAmount(0);
                break;
            case GOLD_INGOT:
                storaged += hand.getAmount();
                hand.setAmount(0);
                break;
            case GOLD_NUGGET:
                storaged += hand.getAmount() / 9.0;
                hand.setAmount(0);
                break;
            case GOLD_BLOCK:
                storaged += 9 * hand.getAmount();
                hand.setAmount(0);
                break;
            case RAW_GOLD_BLOCK:
                storaged += 18 * hand.getAmount();
                hand.setAmount(0);
                break;
            case RAW_GOLD:
                storaged += 2 * hand.getAmount();
                hand.setAmount(0);
                break;
            case GOLDEN_SWORD:
                storaged += 2;
                hand.setAmount(0);
                break;
            case GOLDEN_PICKAXE:
                storaged += 3;
                hand.setAmount(0);
                break;
            case GOLDEN_AXE:
                storaged += 3;
                hand.setAmount(0);
                break;
            case GOLDEN_SHOVEL:
                storaged += 1;
                hand.setAmount(0);
                break;
            case GOLDEN_HOE:
                storaged += 1;
                hand.setAmount(0);
                break;
            case GOLDEN_HELMET:
                storaged += 5;
                hand.setAmount(0);
                break;
            case GOLDEN_CHESTPLATE:
                storaged += 8;
                hand.setAmount(0);
                break;
            case GOLDEN_LEGGINGS:
                storaged += 7;
                hand.setAmount(0);
                break;
            case GOLDEN_BOOTS:
                storaged += 4;
                hand.setAmount(0);
                break;
            case GOLDEN_HORSE_ARMOR:
                storaged += 15;
                hand.setAmount(0);
                break;
        }
    }

    public void goldCreation() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (!player.isSneaking()) {
            goldCreationMenu(player);
        } else {
            if (storaged > 0) {
                if (storaged >= 30) {
                    storaged -= 30;
                    player.getInventory().addItem(createReinforcedGoldAxe());
                    player.sendMessage("§ePurchased Reinforced Gold Axe for 30 gold");
                } else if (storaged >= 20) {
                    storaged -= 20;
                    player.getInventory().addItem(createReinforcedGoldSword());
                    player.sendMessage("§ePurchased Reinforced Gold Sword for 20 gold");
                } else if (storaged > 3) {
                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_AXE));
                    storaged -= 3;
                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
                    storaged -= 2;
                }
            }
        }
    }

    private ItemStack createReinforcedGoldSword() {
        ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§eReinforced Gold Sword");
        meta.setUnbreakable(true);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
        AttributeModifier speedMod = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedMod);
        sword.setItemMeta(meta);
        return sword;
    }

    private ItemStack createReinforcedGoldAxe() {
        ItemStack axe = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.setDisplayName("§eReinforced Gold Axe");
        meta.setUnbreakable(true);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
        AttributeModifier speedMod = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 1.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedMod);
        axe.setItemMeta(meta);
        return axe;
    }

    public void goldCreationMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "Gold Creation (" + String.format("%.1f", storaged) + ")");
        
        ItemStack goldBlock = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemStack goldIngot = new ItemStack(Material.GOLD_INGOT, 1);
        ItemStack goldNugget = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemStack goldSword = new ItemStack(Material.GOLDEN_SWORD, 1);
        ItemStack goldPickaxe = new ItemStack(Material.GOLDEN_PICKAXE, 1);
        ItemStack goldAxe = new ItemStack(Material.GOLDEN_AXE, 1);
        ItemStack goldShovel = new ItemStack(Material.GOLDEN_SHOVEL, 1);
        ItemStack goldHelmet = new ItemStack(Material.GOLDEN_HELMET, 1);
        ItemStack goldChestplate = new ItemStack(Material.GOLDEN_CHESTPLATE, 1);
        ItemStack goldLeggings = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
        ItemStack goldBoots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        ItemStack goldHorseArmor = new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1);
        ItemStack goldenCarrot = new ItemStack(Material.GOLDEN_CARROT, 1);
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemStack lightWeightedPressurePlate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 16);
        
        ItemMeta goldBlockMeta = goldBlock.getItemMeta();
        goldBlockMeta.setDisplayName("Gold Block - Cost: 9");
        goldBlock.setItemMeta(goldBlockMeta);
        
        ItemMeta goldIngotMeta = goldIngot.getItemMeta();
        goldIngotMeta.setDisplayName("Gold Ingot - Cost: 1");
        goldIngot.setItemMeta(goldIngotMeta);
        
        ItemMeta goldNuggetMeta = goldNugget.getItemMeta();
        goldNuggetMeta.setDisplayName("Gold Nugget - Cost: 1/9");
        goldNugget.setItemMeta(goldNuggetMeta);
        
        ItemMeta goldSwordMeta = goldSword.getItemMeta();
        goldSwordMeta.setDisplayName("Golden Sword - Cost: 2");
        goldSword.setItemMeta(goldSwordMeta);
        
        ItemMeta goldPickaxeMeta = goldPickaxe.getItemMeta();
        goldPickaxeMeta.setDisplayName("Golden Pickaxe - Cost: 3");
        goldPickaxe.setItemMeta(goldPickaxeMeta);
        
        ItemMeta goldAxeMeta = goldAxe.getItemMeta();
        goldAxeMeta.setDisplayName("Golden Axe - Cost: 3");
        goldAxe.setItemMeta(goldAxeMeta);
        
        ItemMeta goldShovelMeta = goldShovel.getItemMeta();
        goldShovelMeta.setDisplayName("Golden Shovel - Cost: 1");
        goldShovel.setItemMeta(goldShovelMeta);
        
        ItemMeta goldHelmetMeta = goldHelmet.getItemMeta();
        goldHelmetMeta.setDisplayName("Golden Helmet - Cost: 5");
        goldHelmet.setItemMeta(goldHelmetMeta);
        
        ItemMeta goldChestplateMeta = goldChestplate.getItemMeta();
        goldChestplateMeta.setDisplayName("Golden Chestplate - Cost: 8");
        goldChestplate.setItemMeta(goldChestplateMeta);
        
        ItemMeta goldLeggingsMeta = goldLeggings.getItemMeta();
        goldLeggingsMeta.setDisplayName("Golden Leggings - Cost: 7");
        goldLeggings.setItemMeta(goldLeggingsMeta);
        
        ItemMeta goldBootsMeta = goldBoots.getItemMeta();
        goldBootsMeta.setDisplayName("Golden Boots - Cost: 4");
        goldBoots.setItemMeta(goldBootsMeta);

        inv.setItem(10, goldBlock);
        inv.setItem(11, goldIngot);
        inv.setItem(12, goldNugget);
        inv.setItem(13, goldSword);
        inv.setItem(14, goldPickaxe);
        inv.setItem(15, goldAxe);
        inv.setItem(16, goldShovel);
        inv.setItem(17, goldHelmet);
        inv.setItem(18, goldChestplate);
        inv.setItem(19, goldLeggings);
        inv.setItem(20, goldBoots);
        inv.setItem(21, goldHorseArmor);
        inv.setItem(22, goldenCarrot);
        inv.setItem(23, goldenApple);
        inv.setItem(24, lightWeightedPressurePlate);

        opened = true;
        player.openInventory(inv);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (opened && event.getView().getTitle().contains("Gold Creation")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null) return;
            event.setCancelled(true);

            double cost = 0;
            boolean canBuy = false;
            
            switch (clicked.getType()) {
                case GOLD_NUGGET:
                    cost = clicked.getAmount() / 9.0;
                    canBuy = storaged >= cost;
                    break;
                case GOLD_INGOT:
                    cost = clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case GOLD_BLOCK:
                    cost = 9 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case RAW_GOLD_BLOCK:
                    cost = 18 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case LIGHT_WEIGHTED_PRESSURE_PLATE:
                    cost = 2 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_PICKAXE:
                    cost = 3;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_AXE:
                    cost = 3;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_SHOVEL:
                    cost = 1;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_SWORD:
                    cost = 2;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_HELMET:
                    cost = 5;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_CHESTPLATE:
                    cost = 8;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_LEGGINGS:
                    cost = 7;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_BOOTS:
                    cost = 4;
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_HORSE_ARMOR:
                    cost = 15 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_CARROT:
                    cost = 10 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
                case GOLDEN_APPLE:
                    cost = 15 * clicked.getAmount();
                    canBuy = storaged >= cost;
                    break;
            }

            if (canBuy) {
                storaged -= cost;
                player.getInventory().addItem(clicked);
                player.sendMessage("§ePurchased " + clicked.getItemMeta().getDisplayName() + " for " + cost + " gold");
            } else {
                player.sendMessage("§cYou don't have enough gold to buy this item");
            }
            player.closeInventory();
            opened = false;
        }
    }

    public void onInventoryDrag(InventoryDragEvent event) {
        if (opened && event.getView().getTitle().contains("Gold Creation")) {
            event.setCancelled(true);
        }
    }

    private void absorbBlock(Location block) {
        Block b = block.getBlock();
        if (storaged >= maxStoraged) return;

        switch (b.getType()) {
            case GOLD_BLOCK:
                storaged += 9;
                b.setType(Material.AIR);
                break;
            case GOLD_ORE:
                storaged += 2;
                b.setType(Material.STONE);
                break;
            case NETHER_GOLD_ORE:
                storaged += 2;
                b.setType(Material.NETHERRACK);
                break;
            case DEEPSLATE_GOLD_ORE:
                storaged += 2;
                b.setType(Material.DEEPSLATE);
                break;
            case RAW_GOLD_BLOCK:
                storaged += 18;
                b.setType(Material.AIR);
                break;
            default:
                if (b.getType().toString().contains("GOLD")) {
                    storaged += 1;
                    b.setType(Material.AIR);
                }
                break;
        }
    }

    public void controlGold() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();
        currentPL.setY(currentPL.getY() + 1);
        currentPL.add(direction.multiply(1.5));
        if (player.isSneaking()) {
            absorbBlock(currentPL);
        } else {
            if (!isGold(currentPL.getBlock()) && storaged > 0 &&
                    currentPL.getBlock().getType() != Material.BEDROCK) {
                currentPL.getBlock().setType(Material.GOLD_BLOCK);
                storaged--;
            }
        }
    }

    public void goldRise() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (!player.isSneaking()) {
            if (storaged > 9) {
                Location currentPL = player.getLocation().clone().add(0, 2, 0);
                Entity entity = player.getWorld().spawnFallingBlock(currentPL, Material.GOLD_BLOCK.createBlockData());
                entity.setGravity(false);
                entity.setVelocity(player.getLocation().getDirection());
                OPHLib.repealEntity(entity, player);
                storaged -= 9;
            }
        } else {
            Location currentPL = player.getLocation().clone().add(0, 2, 0);
            Vector direction = player.getEyeLocation().getDirection();
            new BukkitRunnable() {
                final Location current = currentPL.clone();
                int i = 0;
                @Override
                public void run() {
                    if (isGold(current.add(direction).getBlock()) || i > 15) {
                        if (current.getBlock().getType() != Material.AIR) {
                            Entity floatingBlock = current.getWorld().spawnFallingBlock(current, current.getBlock().getBlockData());
                            floatingBlock.setGravity(false);
                            current.getBlock().setType(Material.AIR);
                            OPHLib.catchEntity(floatingBlock, player);
                        }
                        cancel();
                    }
                    i++;
                }
            }.runTaskTimer(OPhabs.getInstance(), 5, 3);
        }
    }

    private boolean isGold(Block block) {
        return block.getType().toString().contains("GOLD");
    }

    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        super.onPlayerMove(event);
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        // Check for THIS fruit's caster in hand
        boolean hasCaster = false;
        FruitCaster caster = (FruitCaster) user.getActiveCasters().get(this.getID());
        
        if (caster != null) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            
            if (caster.isThisItem(mainHand) || caster.isThisItem(offHand)) {
                hasCaster = true;
            }
        }

        // Flight permission: caster in hand + storaged > 4
        boolean canFly = hasCaster && storaged > 4.0;

        if (canFly && !flightEnabled) {
            flightEnabled = true;
            player.setAllowFlight(true);
        } else if (!canFly && flightEnabled) {
            flightEnabled = false;
            player.setAllowFlight(false);
            player.setFlying(false);
            glide = false;
        }

        // Gold particles/disc ONLY when actually flying/gliding (not on ground)
        if (flightEnabled && player.isFlying() && !player.isOnGround()) {
            if (!glide) {
                player.setGliding(true);
                glide = true;
            }
            // Gold particles around player (disc effect)
            spawnGoldParticles(player);
        } else {
            glide = false;
        }
    }

    private void spawnGoldParticles(Player player) {
        Location loc = player.getLocation();
        double radius = 1.1;
        double density = 0.3;
        for (double x = -radius; x <= radius; x += density) {
            for (double z = -radius; z <= radius; z += density) {
                double distanceSquared = x * x + z * z;
                if (distanceSquared <= radius * radius) {
                    Location particleLoc = new Location(player.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z);
                    player.getWorld().spawnParticle(Particle.DUST, particleLoc, 0, 0, 0, 0, 1, goldDust);
                }
            }
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        if (event.getEntity() instanceof Player) {
            OPUser user = this.getUser();
            if (user != null && event.getEntity().equals(user.getPlayer())) {
                double finalDamage = event.getDamage();
                if (storaged > 0) {
                    // Halve the damage first, then reduce by stored gold
                    double halfDamage = finalDamage / 2.0;
                    double damageReduction = Math.min(halfDamage, storaged);
                    event.setDamage(finalDamage - damageReduction);
                    storaged -= damageReduction;
                    
                    if (storaged <= 0.0) {
                        storaged = 0.0;
                        event.setDamage(finalDamage);
                    }
                    OPUser user2 = this.getUser();
                    if (user2 != null) {
                        Player player = user2.getPlayer();
                        if (player != null) {
                            player.spawnParticle(Particle.DUST, event.getEntity().getLocation(), 100, 0, 1, 0, goldDust);
                        }
                    }
                }
            }
        }
    }
}
