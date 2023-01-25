package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.devilFruitUser;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.MaterialData;
import java.util.UUID;
import java.lang.Math;

public class ishi_ishi extends paramecia {
    final int radiusFloor = 3, radiusWall = 3;
    public static Particle.DustOptions piedra = new Particle.DustOptions(Color.GRAY,1.0F);
    int storaged, maxStoraged;
    String nameAbility1 = "Absorb Stone";
    boolean opened;
    private int repealAnimationCounter = 0;

    public ishi_ishi(OPhabs plugin) {
        super(plugin);
        storaged = 0;
        maxStoraged = 1024;
        opened = false;

        //runParticles();
        abilitiesNames.add(nameAbility1 + " (" + storaged + ")");
        abilitiesCD.add(0);
        abilitiesNames.add("Stone Creation");
        abilitiesCD.add(0);
        abilitiesNames.add("Control Stone");
        abilitiesCD.add(0);
        abilitiesNames.add("Stone Rise");
        abilitiesCD.add(0);
    }

    public ishi_ishi(OPhabs plugin, devilFruitUser user) {
        super(plugin, user);
        storaged = 5;
        abilitiesNames.add(nameAbility1 + " (" + storaged + ")");
        abilitiesCD.add(0);
        abilitiesNames.add("Stone Creation");
        abilitiesCD.add(0);
        abilitiesNames.add("Control Stone");
        abilitiesCD.add(0);
        abilitiesNames.add("Stone Rise");
        abilitiesCD.add(0);
        }
   
    public void ability1(){
        if(storaged < maxStoraged){
            absorb(user.getPlayer());
            abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
        }
    }
    public void ability2(){
        if(!user.getPlayer().isSneaking()){
            stoneCreation(user.getPlayer());
        }
        else{
            if(storaged > 0){
                if(storaged >=30){
                    user.getPlayer().getInventory().addItem(new ReinforcedStoneAxe());
                    storaged -= 30;
                }
                else{
                    if(storaged > 20){
                        //ReinforcedStoneSword
                        user.getPlayer().getInventory().addItem(new ReinforcedStoneSword());
                        storaged -= 20;
                    }
                    else{
                        if(storaged > 3){
                            user.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_AXE));
                            storaged -= 3;
                        }
                        else{
                            user.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                            storaged -= 2;
                        }
                    }
                }
            }
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
    }

    public void ability3(){

        if(abilitiesCD.get(2) == 0){
            controlStone(user.getPlayer());
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");

    }
    public void ability4(){
        if(abilitiesCD.get(3) == 0){
            stoneRise(user.getPlayer());
            abilitiesCD.set(3, 0);
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
    }




    public Boolean isStone(Block block){
        boolean isStone = false;
        if(block.getType().toString().contains("STONE") || (block.getType().toString().contains("ORE") && !block.getType().toString().contains("NETHERRACK"))){
            isStone=true;
        }
        return isStone;
    }


    public Boolean isSolidBlock(Block block){
        return !(block.getType().getHardness() <= Material.TORCH.getHardness() || block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.LAVA);
    }

    public void getSolidRelativeY(Location loc, int y){
       if(isSolidBlock(loc.getBlock()))
            loc = getSolidRelativeUpper(loc, y);
        else
            loc = getSolidRelativeDown(loc.add(0,-1,0), y);
    }

    public Location getSolidRelativeUpper(Location loc, int y){
        Location loc2 = loc.clone().add(0,1,0);
        if(!isSolidBlock(loc2.getBlock()))
            return loc;
        else
            if(y > 0)
                return getSolidRelativeUpper(loc.add(0, 1, 0), y-1);
            else
                return null;
    }

    public Location getSolidRelativeDown(Location loc, int y){
        if(isSolidBlock(loc.getBlock()))
            return loc;
        else{
            if(y > 0)
               return getSolidRelativeDown(loc.add(0, -1, 0), y-1);
            else
                return null;
        } 
    }

    public void controlStone(Player player){
        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();
        currentPL.setY(currentPL.getY() + 1);
        currentPL.add(direction.multiply(1.5));
        if(player.isSneaking())
            absorbBlock(currentPL);
        else{
            if(!isStone(currentPL.getBlock()) && storaged > 0 && currentPL.getBlock().getType().getHardness() < Material.COBBLED_DEEPSLATE.getHardness() && currentPL.getBlock().getType() != Material.BEDROCK){
                currentPL.getBlock().setType(Material.STONE);
                storaged--;
            }
        }
    }
    
    public void stoneRise(Player player){
        Location currentPL = player.getLocation(), currentPL1, currentPL2;
        int xDir = 1, zDir= 1;
        Vector direction = player.getEyeLocation().getDirection();
        
        if(!user.getPlayer().isSneaking()){
            direction.setY(0);
            currentPL.add(direction);

            if(direction.getX() > 0.1 || direction.getX() < -0.1){
                currentPL1 = currentPL.clone().add(0, 0, 1);
                currentPL2 = currentPL.clone().add(0, 0, -1);
            }else{
                currentPL1 = currentPL.clone().add(1, 0, 0);
                currentPL2 = currentPL.clone().add(-1, 0, 0);
            }
            if(direction.getY() <= 0.5 && direction.getY() >= -0.5){
                new BukkitRunnable() {
                    Material type = Material.POINTED_DRIPSTONE;
                    int i = 0;
                    @Override
                    public void run() {
                        currentPL.add(direction);
                        currentPL1.add(direction);
                        currentPL2.add(direction);
                        if(i>6 || storaged < 3){
                            if(storaged>=3){
                                currentPL.getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL1.getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL2.getBlock().setType(Material.POINTED_DRIPSTONE);
                                storaged -= 3;
                            }
                            
                            if(storaged >= 3){
                                currentPL.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL1.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL2.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                storaged -= 3;
                            }
                            
                            if(storaged >= 3){
                                currentPL.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL1.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                currentPL2.add(0,1,0).getBlock().setType(Material.POINTED_DRIPSTONE);
                                storaged -= 3;
                            }
                            cancelTask();
                        }
                        if(storaged > 3){
                            if(currentPL.getBlock().getType() == Material.AIR || currentPL.getBlock().getType() == Material.WATER || currentPL.getBlock().getType() == Material.LAVA){
                                if(currentPL.clone().add(0,-1,0).getBlock().getType() != Material.AIR && currentPL.clone().add(0,-1,0).getBlock().getType() != Material.WATER && currentPL.clone().add(0,-1,0).getBlock().getType() != Material.LAVA){
                                    currentPL.getBlock().setType(Material.POINTED_DRIPSTONE);
                                    currentPL.getWorld().getNearbyEntities(currentPL,1,1,1).forEach(entity -> {
                                        if(entity instanceof LivingEntity){
                                            ((LivingEntity) entity).damage(5);
                                        }
                                    });
                                }
                            }

                            if(currentPL1.getBlock().getType() == Material.AIR || currentPL1.getBlock().getType() == Material.WATER || currentPL1.getBlock().getType() == Material.LAVA){
                                if(currentPL1.clone().add(0,-1,0).getBlock().getType() != Material.AIR && currentPL1.clone().add(0,-1,0).getBlock().getType() != Material.WATER && currentPL1.clone().add(0,-1,0).getBlock().getType() != Material.LAVA){
                                    currentPL1.getBlock().setType(Material.POINTED_DRIPSTONE);
                                    currentPL1.getWorld().getNearbyEntities(currentPL1,1,1,1).forEach(entity -> {
                                        if(entity instanceof LivingEntity){
                                            ((LivingEntity) entity).damage(5);
                                        }
                                    });
                                }
                            }

                            if(currentPL2.getBlock().getType() == Material.AIR || currentPL2.getBlock().getType() == Material.WATER || currentPL2.getBlock().getType() == Material.LAVA){
                                if(currentPL2.clone().add(0,-1,0).getBlock().getType() != Material.AIR && currentPL2.clone().add(0,-1,0).getBlock().getType() != Material.WATER && currentPL2.clone().add(0,-1,0).getBlock().getType() != Material.LAVA){
                                    currentPL2.getBlock().setType(Material.POINTED_DRIPSTONE);
                                    currentPL2.getWorld().getNearbyEntities(currentPL2,1,1,1).forEach(entity -> {
                                        if(entity instanceof LivingEntity){
                                            ((LivingEntity) entity).damage(5);
                                        }
                                    });
                                }
                            }
                            
                        storaged-=3;
                        i++;
                        }
                    }

                    public void cancelTask() {
                        Bukkit.getScheduler().cancelTask(this.getTaskId());
                    }
                }.runTaskTimer(plugin, 0, 4);
            }

        }else{
                currentPL.setY(currentPL.getY()+1);
                new BukkitRunnable() {
                    Location current = currentPL.clone();
                    Material type = current.getBlock().getType();
                    int i = 0;
                    @Override
                    public void run() {
                        if (isStone(current.add(direction).getBlock()) || i > 8){
                            Entity floatingBlock = currentPL.getWorld().spawnFallingBlock(current, current.getBlock().getBlockData());
                            current.getBlock().setType(Material.AIR);
                            catchEntity(floatingBlock, player);
                            cancelTask();
                        }
                        current.add(direction);
                        i++;
                    }

                    public void cancelTask() {
                        Bukkit.getScheduler().cancelTask(this.getTaskId());
                    }
                }.runTaskTimer(plugin, 5, 3);
            }

    }
     
    public void absorb(Player player) {
        Location currentPL = player.getLocation(), loc;
        if(player.isSneaking())
            loc = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor);
        else
            loc = currentPL.clone().add(-radiusFloor, 0, -radiusFloor);
        ArrayList<Location> blocks = new ArrayList<>();
        Location pos1, pos2;

        if(player.isSneaking()){
            pos1 = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor);
        }
        else{
            pos1 = currentPL.clone().add(-radiusFloor, 0, -radiusFloor);
        }
        pos2 = currentPL.clone().add(radiusFloor, radiusFloor, radiusFloor);
       

        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Location block = new Location(player.getWorld(), x, y, z);
                    if(isStone(block.getBlock()) && storaged < maxStoraged){
                        absorbBlock(block);
                    }
                }
            }
        }
    }

    public void absorbBlock(Location block){
        switch(block.getBlock().getType()){
            case STONE:
            case DEEPSLATE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                break;
            case COBBLESTONE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                break;
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.RAW_IRON));
                break;
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.RAW_GOLD));
                break;
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.DIAMOND));
                break;
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.REDSTONE));
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.REDSTONE));
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.REDSTONE));
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.REDSTONE));
                break;
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.LAPIS_LAZULI));
                break;
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.COAL));
                break;
            case COPPER_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.RAW_COPPER));
                break;
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                block.getWorld().dropItemNaturally(block, new ItemStack(Material.EMERALD));
                break;
            case COBBLED_DEEPSLATE:
                storaged += 1;
                block.getBlock().setType(Material.AIR);
                break;
            default:
                if( block.getBlock().getType().name().contains("DEEPSLATE") || (block.getBlock().getType().name().contains("STONE") && !block.getBlock().getType().name().contains("REDSTONE"))){
                    storaged += 1;
                    block.getBlock().setType(Material.AIR);
                }
                break;
        }
    }

    public void stoneCreation(Player player){
        Inventory inv = Bukkit.createInventory(null, 45, ("Stone Creation" + " (" + storaged + ")"));
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

        ReinforcedStoneSword reinforcedStoneSword= new ReinforcedStoneSword();
        ReinforcedStoneAxe reinforcedStoneAxe = new ReinforcedStoneAxe();

        inv.setItem(0, cobblestone);
        inv.setItem(1, cobblestoneStairs);
        inv.setItem(2, cobblestoneSlab);
        inv.setItem(3, cobblestoneWall);

        cobblestone.setAmount(64);
        cobblestoneStairs.setAmount(64);
        cobblestoneSlab.setAmount(64);
        cobblestoneWall.setAmount(64);
        
        inv.setItem(5, cobblestone);
        inv.setItem(6, cobblestoneStairs);
        inv.setItem(7, cobblestoneSlab);
        inv.setItem(8, cobblestoneWall);

        inv.setItem(9, stone);
        inv.setItem(10, stoneStairs);
        inv.setItem(11, stoneSlab);

        stone.setAmount(64);
        stoneStairs.setAmount(64);
        stoneSlab.setAmount(64);

        inv.setItem(14, stone);
        inv.setItem(15, stoneStairs);
        inv.setItem(16, stoneSlab);

        inv.setItem(18, stoneBrick);
        inv.setItem(19, stoneBrickStairs);
        inv.setItem(20, stoneBrickSlab);
        inv.setItem(21, stoneBrickWall);

        stoneBrick.setAmount(64);
        stoneBrickStairs.setAmount(64);
        stoneBrickSlab.setAmount(64);
        stoneBrickWall.setAmount(64);

        inv.setItem(23, stoneBrick);
        inv.setItem(24, stoneBrickStairs);
        inv.setItem(25, stoneBrickSlab);
        inv.setItem(26, stoneBrickWall);

        inv.setItem(27, stoneSword);
        inv.setItem(28, stoneAxe);
        inv.setItem(29, stonePickaxe);
        inv.setItem(30, stoneShovel);
        inv.setItem(32, stoneHoe);
        inv.setItem(33, reinforcedStoneSword);
        inv.setItem(34, reinforcedStoneAxe);

        inv.setItem(36, stoneButton);
        inv.setItem(37, stonePressurePlate);
        inv.setItem(38, furnace);

        stoneButton.setAmount(64);
        stonePressurePlate.setAmount(64);
        furnace.setAmount(64);

        inv.setItem(41, stoneButton);
        inv.setItem(42, stonePressurePlate);
        inv.setItem(43, furnace);

        opened = true;
        player.openInventory(inv);
    }
    
    public void onEntityDamage(EntityDamageEvent event){
        double damage = event.getDamage(), finalDamage;
        if(storaged > 0){
            finalDamage = damage - storaged;
            if(finalDamage <= 0) {
                finalDamage = 0;
                storaged = storaged - (int) damage;
                event.setCancelled(true);
            }
            else {
                finalDamage = damage - storaged;
                storaged = 0;
                event.setDamage(finalDamage);
            }
            ((Player) event.getEntity()).spawnParticle(Particle.REDSTONE, event.getEntity().getLocation(), 100, 0, 1, 0, piedra);

            abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
        }
    }

    public void onInventoryClick(InventoryClickEvent event){
        if(opened){
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            Inventory inventory = event.getInventory();
            event.setCancelled(true);
            if(clicked.getType() == Material.STONE){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.COBBLESTONE){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.COBBLESTONE_STAIRS){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.COBBLESTONE_SLAB){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.COBBLESTONE_WALL){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_STAIRS){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_SLAB){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_BRICKS){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_BRICK_STAIRS){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_BRICK_SLAB){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_BRICK_WALL){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_SWORD){
                if(clicked.getItemMeta().getDisplayName().equals("Reinforced Stone Sword")){
                    if(storaged >= 20*clicked.getAmount()){
                        storaged -= 20*clicked.getAmount();
                        player.getInventory().addItem(clicked);
                        player.closeInventory();
                    }
                    else{
                        player.sendMessage("You don't have enough stone to buy this item");
                        player.closeInventory();
                    }
                }
                else{
                    if(storaged >= 2*clicked.getAmount()){
                        storaged -= 2*clicked.getAmount();
                        player.getInventory().addItem(clicked);
                        player.closeInventory();
                    }
                    else{
                        player.sendMessage("You don't have enough stone to buy this item");
                        player.closeInventory();
                    }
               }
            }
            if(clicked.getType() == Material.STONE_AXE){
                if(clicked.getItemMeta().getDisplayName().equals("Reinforced Stone Axe")){
                    if(storaged >= 30*clicked.getAmount()){
                        storaged -= 30*clicked.getAmount();
                        player.getInventory().addItem(clicked);
                        player.closeInventory();
                    }
                    else{
                        player.sendMessage("You don't have enough stone to buy this item");
                        player.closeInventory();
                    }
               }
                else{
                    if(storaged >= 3*clicked.getAmount()){
                        storaged -= 3*clicked.getAmount();
                        player.getInventory().addItem(clicked);
                        player.closeInventory();
                    }
                    else{
                        player.sendMessage("You don't have enough stone to buy this item");
                        player.closeInventory();
                    }
               }
            }
            if(clicked.getType() == Material.STONE_PICKAXE){
                if(storaged >= 3*clicked.getAmount()){
                    storaged -= 3*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_SHOVEL){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_HOE){
                if(storaged >= 2*clicked.getAmount()){
                    storaged -= 2*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.FURNACE){
                if(storaged >= 8*clicked.getAmount()){
                    storaged -= 8*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_BUTTON){
                if(storaged >= 1*clicked.getAmount()){
                    storaged -= 1*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
            if(clicked.getType() == Material.STONE_PRESSURE_PLATE){
                if(storaged >= 2*clicked.getAmount()){
                    storaged -= 2*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    player.closeInventory();
                }
                else{
                    player.sendMessage("You don't have enough stone to buy this item");
                    player.closeInventory();
                }
            }
        }
        opened = false;
    }

    public void onPlayerDeath(PlayerDeathEvent event){
        super.onPlayerDeath(event);
        storaged = 5;
    }

  public Entity setFallingBlock(Location block) {
    if (block.getBlock().getType() != Material.AIR && block.getBlock().getType() != Material.WATER) {
      Material matFallingBlock = block.getBlock().getType();
      block.getBlock().setType(Material.AIR);
      return block.getWorld().spawnFallingBlock(block, matFallingBlock, (byte) 9);
    }
    return null;

  }


  public ArrayList<Location> positions(Location loc, Vector direction) {
    // if faces north
    ArrayList<Location> blocks = new ArrayList<Location>();

    if (direction.getX() < -0.2 && direction.getX() > 0.2) {
      blocks.add(loc.clone().add(1, 1, 0));
      blocks.add(loc.clone().add(0, 1, 0));
      blocks.add(loc.clone().add(-1, 1, 0));
      blocks.add(loc.clone().add(1, 0, 0));
      blocks.add(loc.clone().add(-1, 0, 0));
      blocks.add(loc.clone().add(1, -1, 0));
      blocks.add(loc.clone().add(0, -1, 0));
      blocks.add(loc.clone().add(-1, -1, 0));
    }
    // if faces east
    if (direction.getX() < -0.2 && direction.getX() > 0.2) {
      blocks.add(loc.clone().add(0, 1, 1));
      blocks.add(loc.clone().add(0, 1, 0));
      blocks.add(loc.clone().add(0, 1, -1));
      blocks.add(loc.clone().add(0, 0, 1));
      blocks.add(loc.clone().add(0, 0, -1));
      blocks.add(loc.clone().add(0, -1, 1));
      blocks.add(loc.clone().add(0, -1, 0));
      blocks.add(loc.clone().add(0, -1, -1));
    }
    return blocks;
  }

    //From Yami, modified forLivingVoidEntity
    public void catchEntity(Entity ent, Player player){

        BukkitTask attract = new BukkitRunnable(){
            Vector FirstVector;
            boolean fV = false;
            boolean entityInHand = false;
            double vx,vy,vz;
            int i = 0, j = 0;
            Location loc = player.getLocation().add(0,1,0);
            @Override
            public void run() {

                Location loc = player.getLocation().add(0,2,0);
                if(player.isDead() || ent.isOnGround())
                    this.cancel();

                vx =  loc.getX() - ent.getLocation().getX();
                vy =  loc.getY() - ent.getLocation().getY();
                vz =  loc.getZ() - ent.getLocation().getZ();

                Vector movement = loc.clone().toVector().subtract(ent.getLocation().toVector()).normalize();

                if(!fV){
                    FirstVector = movement.clone();
                    fV = true;
                }

                //Para levantar al mob si hay desnivel
                if(loc.getY() > ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                if(!entityInHand){
                    ent.setVelocity(movement);
                    i++;
                }
                else{
                    ent.teleport(player.getLocation().add(0,2,0));
                    ent.setVelocity(new Vector(0,0.042,0));
                    j++;
                }

                if(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2) +  Math.pow(vz,2)) <= 1){
                    entityInHand = true;
                    if(!player.isSneaking()){
                        this.cancel();
                        repealEntity(ent,player);
                    }
                }

                if(!player.isSneaking()){
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    //From Yami
    public void repealEntity(Entity ent, Player player ){
        repealAnimationCounter++;
        World world = player.getWorld();

        ent.getWorld().playSound(ent.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,2);

        Vector dir = player.getLocation().getDirection();
        ent.setVelocity(dir.multiply(3));

        //timer
        new BukkitRunnable(){

            Vector aux = dir;

            Location loc = ent.getLocation();
            int i = 0;
            @Override
            public void run() {


                if(ent.getVelocity().getX() != aux.getX() || ent.getVelocity().getZ() != aux.getZ()){
                    aux = ent.getVelocity();

                    ent.getWorld().getNearbyEntities(ent.getLocation(), 2,2,2).forEach(entity -> {
                        if(entity instanceof LivingEntity && entity != player && entity != ent){
                            ((LivingEntity) entity).damage(9);
                        }
                    });
                }

                if(ent.isOnGround()){
                    cancelTask();
                }
                i++;
            }
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 2);

    }




/*
    public void runParticles() {
        new BukkitRunnable(){
            int ticks = 0;
            double i = 0;
            double y = 0;

            Random random = new Random();
            @Override
            public void run() {

                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null){
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null){
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameIshi)){
                            if(!player.isFlying())
                                player.setAllowFlight(true);
                            if(player.isFlying())
                                summonParticles(player);


                        }else {
                            player.setAllowFlight(false);
                            player.setFlying(false);

                        }
                    }
            }
        }.runTaskTimer(plugin, 0, 1);

    }

    public void summonParticles(Player player) {
    Location loc = player.getLocation();
        for (int radius = 1; radius >= 0; radius--) {
            for (double x = -radius; x <= radius; x += 0.2) {
                for (double z = -Math.sqrt(radius*radius - x*x); z <= Math.sqrt(radius*radius - x*x); z += 0.2) {
                    loc.add(x, 0, z);
                    for (int i = 0; i < 7; i++) {
                       player.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, piedra);
                    }
                    loc.subtract(x, 0, z);
                }
            }
        }
    }
*/
}

 
//Custom sword
class ReinforcedStoneSword extends ItemStack {
    public ReinforcedStoneSword() {
        super(Material.STONE_SWORD);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Stone Sword");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        this.setItemMeta(meta);
    }
}

//Custom axe
class ReinforcedStoneAxe extends ItemStack {
    public ReinforcedStoneAxe() {
        super(Material.STONE_AXE);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Stone Axe");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        this.setItemMeta(meta);
    }
}



