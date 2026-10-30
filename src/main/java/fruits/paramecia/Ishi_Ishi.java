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
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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

import java.util.ArrayList;
import java.util.UUID;

public class Ishi_Ishi extends Paramecia {
    private static final int ABSORB_STONE_COOLDOWN = 0;
    private static final int STONE_CREATION_COOLDOWN = 0;
    private static final int CONTROL_STONE_COOLDOWN = 0;
    private static final int STONE_RISE_COOLDOWN = 0;

    private int radiusFloor = 3;
    private double storaged = 0;
    private final double maxStoraged = 1024.0;
    private boolean opened = false;
    private final Particle.DustOptions stoneDust = new Particle.DustOptions(Color.GRAY, 1.0F);

    public static int getFruitID() {
        return 1014;
    }

    public Ishi_Ishi(int id) {
        super(id, "ishi_ishi", "Ishi Ishi no Mi", "ishi_ishi");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Absorb Stone", ABSORB_STONE_COOLDOWN, () -> {
            OPUser user = this.getUser();
            if (user != null && user.getPlayer() != null) {
                absorb(user.getPlayer());
            }
        }));
        basicSet.addAbility(new CooldownAbility("Stone Creation", STONE_CREATION_COOLDOWN, this::stoneCreation));
        basicSet.addAbility(new CooldownAbility("Control Stone", CONTROL_STONE_COOLDOWN, this::controlStone));
        basicSet.addAbility(new CooldownAbility("Stone Rise", STONE_RISE_COOLDOWN, this::stoneRise));
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
            absorbBlock(player.getLocation().add(0, 1, 0).add(player.getEyeLocation().getDirection().multiply(1.5)));
        } else {
            int x1 = player.getLocation().getBlockX() - radiusFloor;
            int y1 = player.getLocation().getBlockY();
            int z1 = player.getLocation().getBlockZ() - radiusFloor;
            int x2 = player.getLocation().getBlockX() + radiusFloor;
            int y2 = player.getLocation().getBlockY() + radiusFloor;
            int z2 = player.getLocation().getBlockZ() + radiusFloor;

            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        Location block = new Location(player.getWorld(), x, y, z);
                        if (isStone(block.getBlock()) && storaged < maxStoraged) {
                            absorbBlock(block);
                        }
                    }
                }
            }
        }
    }

    private void absorbBlock(Location loc) {
        OPUser user = this.getUser();
        if (user == null) return;
        Block b = loc.getBlock();

        switch (b.getType()) {
            case STONE:
            case DEEPSLATE:
                storaged += 1;
                b.setType(Material.AIR);
                break;
            case COBBLESTONE:
                storaged += 1;
                b.setType(Material.AIR);
                break;
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.RAW_IRON));
                break;
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.RAW_GOLD));
                break;
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.DIAMOND));
                break;
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.REDSTONE));
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.REDSTONE));
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.REDSTONE));
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.REDSTONE));
                break;
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.LAPIS_LAZULI));
                break;
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.COAL));
                break;
            case COPPER_ORE:
            case DEEPSLATE_COPPER_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.RAW_COPPER));
                break;
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                storaged += 1;
                b.setType(Material.AIR);
                b.getWorld().dropItemNaturally(loc, new ItemStack(Material.EMERALD));
                break;
            case COBBLED_DEEPSLATE:
                storaged += 1;
                b.setType(Material.AIR);
                break;
            default:
                if (b.getType().name().contains("DEEPSLATE") || (b.getType().name().contains("STONE") && !b.getType().name().contains("REDSTONE"))) {
                    storaged += 1;
                    b.setType(Material.AIR);
                }
                break;
        }
    }

    public void stoneCreation() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (!player.isSneaking()) {
            stoneCreationMenu(player);
        } else {
            if (storaged > 0) {
                if (storaged >= 30) {
                    storaged -= 30;
                    player.getInventory().addItem(createReinforcedStoneAxe());
                    player.sendMessage("§7Purchased Reinforced Stone Axe for 30 stone");
                } else if (storaged >= 20) {
                    storaged -= 20;
                    player.getInventory().addItem(createReinforcedStoneSword());
                    player.sendMessage("§7Purchased Reinforced Stone Sword for 20 stone");
                } else if (storaged > 3) {
                    player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    storaged -= 2;
                } else {
                    player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    storaged -= 2;
                }
            }
        }
    }

    private ItemStack createReinforcedStoneSword() {
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§7Reinforced Stone Sword");
        meta.setUnbreakable(true);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
        AttributeModifier speedMod = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedMod);
        sword.setItemMeta(meta);
        return sword;
    }

    private ItemStack createReinforcedStoneAxe() {
        ItemStack axe = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.setDisplayName("§7Reinforced Stone Axe");
        meta.setUnbreakable(true);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
        AttributeModifier speedMod = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 1.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedMod);
        axe.setItemMeta(meta);
        return axe;
    }

    public void stoneCreationMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "Stone Creation (" + String.format("%.1f", storaged) + ")");
        ItemStack stone = new ItemStack(Material.STONE, 1);
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
        ItemStack cobblestoneWall = new ItemStack(Material.COBBLESTONE_WALL, 1);
        ItemStack cobblestoneSlab = new ItemStack(Material.COBBLESTONE_SLAB, 1);
        ItemStack cobblestoneStairs = new ItemStack(Material.COBBLESTONE_STAIRS, 1);
        ItemStack stonePickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        ItemStack stoneAxe = new ItemStack(Material.STONE_AXE, 1);
        ItemStack stoneShovel = new ItemStack(Material.STONE_SHOVEL, 1);
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD, 1);
        ItemStack stoneHoe = new ItemStack(Material.STONE_HOE, 1);
        ItemStack stoneStairs = new ItemStack(Material.STONE_STAIRS, 1);
        ItemStack stoneSlab = new ItemStack(Material.STONE_SLAB, 1);
        ItemStack stoneBrick = new ItemStack(Material.STONE_BRICKS, 1);
        ItemStack stoneBrickStairs = new ItemStack(Material.STONE_BRICK_STAIRS, 1);
        ItemStack stoneBrickSlab = new ItemStack(Material.STONE_BRICK_SLAB, 1);
        ItemStack stoneBrickWall = new ItemStack(Material.STONE_BRICK_WALL, 1);
        ItemStack stoneButton = new ItemStack(Material.STONE_BUTTON, 1);
        ItemStack stonePressurePlate = new ItemStack(Material.STONE_PRESSURE_PLATE, 1);
        ItemStack furnace = new ItemStack(Material.FURNACE, 1);

        ItemMeta stoneMeta = stone.getItemMeta();
        stoneMeta.setDisplayName("Stone - Cost: 1");
        stone.setItemMeta(stoneMeta);

        ItemMeta cobblestoneMeta = cobblestone.getItemMeta();
        cobblestoneMeta.setDisplayName("Cobblestone - Cost: 1");
        cobblestone.setItemMeta(cobblestoneMeta);

        ItemMeta cobblestoneWallMeta = cobblestoneWall.getItemMeta();
        cobblestoneWallMeta.setDisplayName("Cobblestone Wall - Cost: 1");
        cobblestoneWall.setItemMeta(cobblestoneWallMeta);

        ItemMeta cobblestoneSlabMeta = cobblestoneSlab.getItemMeta();
        cobblestoneSlabMeta.setDisplayName("Cobblestone Slab - Cost: 1");
        cobblestoneSlab.setItemMeta(cobblestoneSlabMeta);

        ItemMeta cobblestoneStairsMeta = cobblestoneStairs.getItemMeta();
        cobblestoneStairsMeta.setDisplayName("Cobblestone Stairs - Cost: 1");
        cobblestoneStairs.setItemMeta(cobblestoneStairsMeta);

        ItemMeta stonePickaxeMeta = stonePickaxe.getItemMeta();
        stonePickaxeMeta.setDisplayName("Stone Pickaxe - Cost: 3");
        stonePickaxe.setItemMeta(stonePickaxeMeta);

        ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
        stoneAxeMeta.setDisplayName("Stone Axe - Cost: 3");
        stoneAxe.setItemMeta(stoneAxeMeta);

        ItemMeta stoneShovelMeta = stoneShovel.getItemMeta();
        stoneShovelMeta.setDisplayName("Stone Shovel - Cost: 1");
        stoneShovel.setItemMeta(stoneShovelMeta);

        ItemMeta stoneSwordMeta = stoneSword.getItemMeta();
        stoneSwordMeta.setDisplayName("Stone Sword - Cost: 2");
        stoneSword.setItemMeta(stoneSwordMeta);

        ItemMeta stoneHoeMeta = stoneHoe.getItemMeta();
        stoneHoeMeta.setDisplayName("Stone Hoe - Cost: 2");
        stoneHoe.setItemMeta(stoneHoeMeta);

        ItemMeta stoneStairsMeta = stoneStairs.getItemMeta();
        stoneStairsMeta.setDisplayName("Stone Stairs - Cost: 1");
        stoneStairs.setItemMeta(stoneStairsMeta);

        ItemMeta stoneSlabMeta = stoneSlab.getItemMeta();
        stoneSlabMeta.setDisplayName("Stone Slab - Cost: 1");
        stoneSlab.setItemMeta(stoneSlabMeta);

        ItemMeta stoneBrickMeta = stoneBrick.getItemMeta();
        stoneBrickMeta.setDisplayName("Stone Bricks - Cost: 1");
        stoneBrick.setItemMeta(stoneBrickMeta);

        ItemMeta stoneBrickStairsMeta = stoneBrickStairs.getItemMeta();
        stoneBrickStairsMeta.setDisplayName("Stone Brick Stairs - Cost: 1");
        stoneBrickStairs.setItemMeta(stoneBrickStairsMeta);

        ItemMeta stoneBrickSlabMeta = stoneBrickSlab.getItemMeta();
        stoneBrickSlabMeta.setDisplayName("Stone Brick Slab - Cost: 1");
        stoneBrickSlab.setItemMeta(stoneBrickSlabMeta);

        ItemMeta stoneBrickWallMeta = stoneBrickWall.getItemMeta();
        stoneBrickWallMeta.setDisplayName("Stone Brick Wall - Cost: 1");
        stoneBrickWall.setItemMeta(stoneBrickWallMeta);

        ItemMeta stoneButtonMeta = stoneButton.getItemMeta();
        stoneButtonMeta.setDisplayName("Stone Button - Cost: 1");
        stoneButton.setItemMeta(stoneButtonMeta);

        ItemMeta stonePressurePlateMeta = stonePressurePlate.getItemMeta();
        stonePressurePlateMeta.setDisplayName("Stone Pressure Plate - Cost: 2");
        stonePressurePlate.setItemMeta(stonePressurePlateMeta);

        ItemMeta furnaceMeta = furnace.getItemMeta();
        furnaceMeta.setDisplayName("Furnace - Cost: 8");
        furnace.setItemMeta(furnaceMeta);

        // Reinforced items
        ItemStack reinforcedStoneSword = createReinforcedStoneSword();
        ItemMeta rSwordMeta = reinforcedStoneSword.getItemMeta();
        rSwordMeta.setDisplayName("§7Reinforced Stone Sword - Cost: 20");
        reinforcedStoneSword.setItemMeta(rSwordMeta);

        ItemStack reinforcedStoneAxe = createReinforcedStoneAxe();
        ItemMeta rAxeMeta = reinforcedStoneAxe.getItemMeta();
        rAxeMeta.setDisplayName("§7Reinforced Stone Axe - Cost: 30");
        reinforcedStoneAxe.setItemMeta(rAxeMeta);

        inv.setItem(10, stone);
        inv.setItem(11, cobblestone);
        inv.setItem(12, cobblestoneWall);
        inv.setItem(13, cobblestoneSlab);
        inv.setItem(14, cobblestoneStairs);
        inv.setItem(15, stonePickaxe);
        inv.setItem(16, stoneAxe);
        inv.setItem(17, stoneShovel);
        inv.setItem(18, stoneSword);
        inv.setItem(19, stoneHoe);
        inv.setItem(20, stoneStairs);
        inv.setItem(21, stoneSlab);
        inv.setItem(22, stoneBrick);
        inv.setItem(23, stoneBrickStairs);
        inv.setItem(24, stoneBrickSlab);
        inv.setItem(25, stoneBrickWall);
        inv.setItem(26, stoneButton);
        inv.setItem(27, stonePressurePlate);
        inv.setItem(28, furnace);
        inv.setItem(33, reinforcedStoneSword);
        inv.setItem(34, reinforcedStoneAxe);

        opened = true;
        player.openInventory(inv);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (opened && event.getView().getTitle().contains("Stone Creation")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null) return;
            event.setCancelled(true);

            double cost = 0;
            boolean canBuy = false;

            // Check for reinforced items first
            if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
                String name = clicked.getItemMeta().getDisplayName();
                if (name.contains("Reinforced Stone Sword")) {
                    cost = 20;
                    canBuy = storaged >= cost;
                } else if (name.contains("Reinforced Stone Axe")) {
                    cost = 30;
                    canBuy = storaged >= cost;
                }
            }

            if (!canBuy) {
                switch (clicked.getType()) {
                    case STONE:
                    case COBBLESTONE:
                    case COBBLESTONE_STAIRS:
                    case COBBLESTONE_SLAB:
                    case COBBLESTONE_WALL:
                    case STONE_STAIRS:
                    case STONE_SLAB:
                    case STONE_BRICKS:
                    case STONE_BRICK_STAIRS:
                    case STONE_BRICK_SLAB:
                    case STONE_BRICK_WALL:
                        cost = 1 * clicked.getAmount();
                        canBuy = storaged >= cost;
                        break;
                    case STONE_SWORD:
                    case STONE_PICKAXE:
                    case STONE_AXE:
                        cost = 3;
                        canBuy = storaged >= cost;
                        break;
                    case STONE_SHOVEL:
                        cost = 1;
                        canBuy = storaged >= cost;
                        break;
                    case STONE_HOE:
                        cost = 2;
                        canBuy = storaged >= cost;
                        break;
                    case FURNACE:
                        cost = 8;
                        canBuy = storaged >= cost;
                        break;
                    case STONE_BUTTON:
                        cost = 1;
                        canBuy = storaged >= cost;
                        break;
                    case STONE_PRESSURE_PLATE:
                        cost = 2;
                        canBuy = storaged >= cost;
                        break;
                }
            }

            if (canBuy) {
                storaged -= cost;
                player.getInventory().addItem(clicked);
                player.sendMessage("§7Purchased " + clicked.getItemMeta().getDisplayName() + " for " + cost + " stone");
            } else {
                player.sendMessage("§cYou don't have enough stone to buy this item");
            }
            player.closeInventory();
            opened = false;
        }
    }

    public void controlStone() {
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
            if (!isStone(currentPL.getBlock()) && storaged > 0 &&
                    currentPL.getBlock().getType() != Material.BEDROCK) {
                currentPL.getBlock().setType(Material.STONE);
                storaged--;
            }
        }
    }

    public void stoneRise() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (!player.isSneaking()) {
            if (storaged > 3) {
                Location currentPL = player.getLocation().clone().add(0, 1, 0);
                Vector direction = player.getEyeLocation().getDirection();
                direction.setY(0);
                direction.normalize();

                Location currentPL1, currentPL2;
                if (direction.getX() > 0.1 || direction.getX() < -0.1) {
                    currentPL1 = currentPL.clone().add(0, 0, 1);
                    currentPL2 = currentPL.clone().add(0, 0, -1);
                } else {
                    currentPL1 = currentPL.clone().add(1, 0, 0);
                    currentPL2 = currentPL.clone().add(-1, 0, 0);
                }

                new BukkitRunnable() {
                    int i = 0;
                    Location pl = currentPL.clone().add(direction.clone().multiply(2));
                    Location pl1 = currentPL1.clone().add(direction.clone().multiply(2));
                    Location pl2 = currentPL2.clone().add(direction.clone().multiply(2));
                    
                    @Override
                    public void run() {
                        if (i > 2 || storaged < 3) {
                            cancel();
                            return;
                        }

                        // For each of the 3 lines, find ground and place 3-high dripstone ABOVE ground
                        Location l = pl.clone();
                        Location l1 = pl1.clone();
                        Location l2 = pl2.clone();
                        
                        // Find solid ground (max 3 blocks down) for each position
                        OPHLib.getSolidRelativeY(l, 3);
                        OPHLib.getSolidRelativeY(l1, 3);
                        OPHLib.getSolidRelativeY(l2, 3);
                        
                        // Check if ground was found
                        if (l == null || l1 == null || l2 == null) {
                            cancel();
                            return;
                        }
                        
                        // Place 3 high ABOVE ground (y+1, y+2, y+3) - small stalagmites rising from ground
                        for (int h = 1; h <= 2; h++) {
                            if (storaged < 2) {
                                cancel();
                                return;
                            }
                            Location loc = l.clone().add(0, h, 0);
                            Location loc1 = l1.clone().add(0, h, 0);
                            Location loc2 = l2.clone().add(0, h, 0);
                            
                            loc.getBlock().setType(Material.POINTED_DRIPSTONE);
                            loc1.getBlock().setType(Material.POINTED_DRIPSTONE);
                            loc2.getBlock().setType(Material.POINTED_DRIPSTONE);
                            
                            // Damage entities at each position
                            for (Location l3 : new Location[]{loc, loc1, loc2}) {
                                l3.getWorld().getNearbyEntities(l3, 1, 1, 1).forEach(entity -> {
                                    if (entity instanceof LivingEntity && !entity.equals(user.getPlayer())) {
                                        ((LivingEntity) entity).damage(5, (Entity) user.getPlayer());
                                    }
                                });
                            }
                            storaged -= 2;
                        }
                        // Move forward for next ring
                        pl.add(direction);
                        pl1.add(direction);
                        pl2.add(direction);
                    }
                }.runTaskTimer(OPhabs.getInstance(), 0, 4);
            }
        } else {
            // Sneaking: pull first stone block in line of sight
            Location currentPL = player.getLocation().clone().add(0, 1, 0);
            Vector direction = player.getEyeLocation().getDirection();
            new BukkitRunnable() {
                Location current = currentPL.clone();
                int i = 0;
                @Override
                public void run() {
                    if (isStone(current.add(direction).getBlock()) || i > 8) {
                        if (current.getBlock().getType() != Material.AIR) {
                            Entity floatingBlock = current.getWorld().spawnFallingBlock(current, current.getBlock().getBlockData());
                            floatingBlock.setGravity(false);
                            current.getBlock().setType(Material.AIR);
                            OPHLib.catchEntity(floatingBlock, player);
                        }
                        cancel();
                    }
                    current.add(direction);
                    i++;
                }
            }.runTaskTimer(OPhabs.getInstance(), 5, 3);
        }
    }

    private boolean isStone(Block block) {
        return block.getType().toString().contains("STONE") || (block.getType().toString().contains("ORE") && !block.getType().toString().contains("NETHERRACK"));
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        if (event.getEntity() instanceof Player) {
            OPUser user = this.getUser();
            if (user != null && event.getEntity().equals(user.getPlayer())) {
                double finalDamage = event.getDamage();
                if (storaged > 0) {
                    // Halve the damage first, then reduce by stored stone
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
                            player.spawnParticle(Particle.DUST, event.getEntity().getLocation(), 100, 0, 1, 0, stoneDust);
                        }
                    }
                }
            }
        }
    }
}
