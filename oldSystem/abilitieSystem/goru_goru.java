package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import weapons.weaponsItems;
import oldSystem.castSystem.castIdentification;

import java.util.ArrayList;
import java.util.Objects;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;
import java.lang.Math;

import static oldSystem.abilitieSystem.OPHLib.*;
import static org.bukkit.Material.GOLDEN_APPLE;
import static org.bukkit.Material.GOLDEN_CARROT;

/**
 * @brief Goru goru no mi ability Class.
 * @author Vaelico786.
 */
public class goru_goru extends paramecia {
    final int radiusFloor = 3;
    public static Particle.DustOptions gold = new Particle.DustOptions(Color.YELLOW,1.0F);
    int storaged, maxStoraged;
    String nameAbility1 = "Absorb Gold";
    boolean opened;
    boolean glide = false;

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief goru_goru constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public goru_goru(OPhabs plugin) {
        super(plugin, 0, 0, 9, "goru_goru", "Goru Goru no Mi", "Goru Goru caster", 7, 1.8);

        storaged = 0;
        maxStoraged = 1024;
        opened = false;
        setCanFly(true);

        runParticles();
        abilitiesNames.add(nameAbility1 + " (" + storaged + ")");
        abilitiesCD.add(0);
        abilitiesNames.add("Gold Creation");
        abilitiesCD.add(0);
        abilitiesNames.add("Control Gold");
        abilitiesCD.add(0);
        abilitiesNames.add("Gold Rise");
        abilitiesCD.add(0);
    }


    // public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
    //     if(glide)
    //         event.setCancelled(true);
    // }
    // *********************************************** ABILITIES *******************************************************

    /**
     * @brief Ability 1: "ABSORB GOLD".
     * @see goru_goru#absorb(Player)
     * @author Vaelico786.
     */
    public void ability1() {
        if(storaged < maxStoraged) {
            absorb(user.getPlayer());
            abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
        }
    }

    /**
     * @brief Ability 1 function --> If player is sneaking, absorbs an item on hand.
     * If he is not sneaking, absorb all blocks related to gold in a pool around player.
     * @see goru_goru#absorbObject(Player)
     * @see goru_goru#absorbBlock(Location)
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void absorb(Player player) {
        Location currentPL = player.getLocation();
        if(player.isSneaking())
            absorbObject(player);
        else {
            Location pos1, pos2;

            if(player.isSneaking()) {
                pos1 = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor);
            } else {
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
                        if(isGold(block.getBlock()) && storaged < maxStoraged) {
                            absorbBlock(block);
                        }
                    }
                }
            }
        }
    }

    /**
     * @brief Ability 2: "GOLD CREATION".
     * @see goru_goru#goldCreation(Player)
     * @author Vaelico786.
     */
    public void ability2() {
        if(!user.getPlayer().isSneaking()) {
            goldCreation(user.getPlayer());
        }
        else {
            if(storaged > 0) {
                if(storaged >=30) {
                    user.getPlayer().getInventory().addItem(weaponsItems.getReinforcedGoldAxe());
                    storaged -= 30;
                }
                else {
                    if(storaged > 20) {
                        //ReinforcedGoldSword
                        user.getPlayer().getInventory().addItem(weaponsItems.getReinforcedGoldSword());
                        storaged -= 20;
                    }
                    else {
                        if(storaged > 3) {
                            user.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_AXE));
                            storaged -= 3;
                        }
                        else {
                            user.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
                            storaged -= 2;
                        }
                    }
                }
            }
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
    }

    /**
     * @brief Ability 2 function --> Gold inventory. It can be seen as a shop.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void goldCreation(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, ("Gold Creation" + " (" + storaged + ")"));
        ItemStack nugget = new ItemStack(Material.GOLD_NUGGET, 9);
        ItemStack ingot = new ItemStack(Material.GOLD_INGOT, 1);
        ItemStack block = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemStack rawBlock = new ItemStack(Material.RAW_GOLD_BLOCK, 1);
        ItemStack goldPickaxe = new ItemStack(Material.GOLDEN_PICKAXE, 1);
        ItemStack goldAxe = new ItemStack(Material.GOLDEN_AXE, 1);
        ItemStack goldShovel = new ItemStack(Material.GOLDEN_SHOVEL, 1);
        ItemStack goldSword = new ItemStack(Material.GOLDEN_SWORD, 1);
        ItemStack plate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1);
        ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        ItemStack horseArmor = new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1);
        ItemStack goldCarrot = new ItemStack(GOLDEN_CARROT, 1);
        ItemStack goldApple = new ItemStack(GOLDEN_APPLE, 1);
        ItemStack reinforcedGoldSword = weaponsItems.getReinforcedGoldSword();
        ItemStack reinforcedGoldAxe = weaponsItems.getReinforcedGoldAxe();
        ItemStack reinforcedGoldPickaxe = weaponsItems.getReinforcedGoldPickaxe();

        ReinforcedGoldHelmet reinforcedGoldHelmet = new ReinforcedGoldHelmet();
        ReinforcedGoldChestplate reinforcedGoldChestplate = new ReinforcedGoldChestplate();
        ReinforcedGoldLeggings reinforcedGoldLeggings = new ReinforcedGoldLeggings();
        ReinforcedGoldBoots reinforcedGoldBoots = new ReinforcedGoldBoots();


        inv.setItem(0, nugget);
        nugget.setAmount(16);
        inv.setItem(9, nugget);
        nugget.setAmount(32);
        inv.setItem(18, nugget);
        nugget.setAmount(64);
        inv.setItem(27, nugget);

        inv.setItem(1, ingot);
        ingot.setAmount(16);
        inv.setItem(10, ingot);
        ingot.setAmount(32);
        inv.setItem(19, ingot);
        ingot.setAmount(64);
        inv.setItem(28, ingot);

        inv.setItem(2, block);
        block.setAmount(16);
        inv.setItem(11, block);
        block.setAmount(32);
        inv.setItem(20, block);
        block.setAmount(64);
        inv.setItem(29, block);

        inv.setItem(3, rawBlock);
        rawBlock.setAmount(16);
        inv.setItem(12, rawBlock);
        rawBlock.setAmount(32);
        inv.setItem(21, rawBlock);
        rawBlock.setAmount(64);
        inv.setItem(30, rawBlock);

        inv.setItem(8, goldSword);
        inv.setItem(17, goldAxe);
        inv.setItem(26, goldPickaxe);
        inv.setItem(35, goldShovel);

        inv.setItem(33, plate);
        plate.setAmount(16);
        inv.setItem(34, plate);


        inv.setItem(36, helmet);
        inv.setItem(37, chestplate);
        inv.setItem(38, leggings);
        inv.setItem(39, boots);

        inv.setItem(41, reinforcedGoldHelmet);
        inv.setItem(42, reinforcedGoldChestplate);
        inv.setItem(43, reinforcedGoldLeggings);
        inv.setItem(44, reinforcedGoldBoots);


        inv.setItem(6, horseArmor);
        inv.setItem(15, goldCarrot);
        inv.setItem(24, goldApple);

        inv.setItem(7, reinforcedGoldSword);
        inv.setItem(16, reinforcedGoldAxe);
        inv.setItem(25, reinforcedGoldPickaxe);


        opened = true;
        player.openInventory(inv);
    }

    /**
     * @brief Ability 3: "CONTROL GOLD".
     * @see goru_goru#controlGold(Player)
     * @author Vaelico786.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            controlGold(user.getPlayer());
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
    }

    /**
     * @brief Ability 3 function --> If player is sneaking, absorb the block he is looking at. If he is not sneaking,
     * transform into a gold block the block he is looking at.
     * @see goru_goru#absorbBlock(Location)
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void controlGold(Player player) {
        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();
        currentPL.setY(currentPL.getY() + 1);
        currentPL.add(direction.multiply(1.5));
        if(player.isSneaking())
            absorbBlock(currentPL);
        else {
            if(!isGold(currentPL.getBlock()) &&
                    storaged > 0 &&
                    currentPL.getBlock().getType().getHardness() < Material.COBBLED_DEEPSLATE.getHardness() &&
                    currentPL.getBlock().getType() != Material.BEDROCK) {

                currentPL.getBlock().setType(Material.GOLD_BLOCK);
                storaged--;
            }
        }
    }

    /**
     * @brief Ability 4: "GOLD RISE".
     * @see goru_goru#goldRise(Player)
     * @author Vaelico786.
     */
    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            goldRise(user.getPlayer());
            abilitiesCD.set(3, 0);
        }
        abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
    }

    /**
     * @brief Ability 4 function --> If player is sneaking, spawns gold block at the looking position with current cost.
     * If he is not sneaking, transform into falling block the first block made by gold in front of the player. ???
     * @see OPHLib#repealEntity(Entity, Player)
     * @see OPHLib#catchEntity(Entity, Player)
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void goldRise(Player player) {
        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();

        if(!user.getPlayer().isSneaking()) {
            if(storaged > 9) {
                Entity entity = player.getWorld().spawnFallingBlock(currentPL.add(0,2,0), Material.GOLD_BLOCK.createBlockData());

                entity.setGravity(false);
                entity.setVelocity(direction);
                repealEntity(entity, player);
                storaged-=9;
            }
        } else {
            currentPL.setY(currentPL.getY()+2);
            new BukkitRunnable() {
                final Location current = currentPL.clone();
                int i = 0;
                @Override
                public void run() {
                    if (isGold(current.add(direction).getBlock()) || i > 15) {
                        if(current.getBlock().getType() != Material.AIR) {
                            Entity floatingBlock = Objects.requireNonNull(currentPL.getWorld()).spawnFallingBlock(current, current.getBlock().getBlockData());

                            floatingBlock.setGravity(false);
                            current.getBlock().setType(Material.AIR);
                            catchEntity(floatingBlock, player);
                        }
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

    // *********************************************** FUNCTIONS *******************************************************

    /**
     * @brief Returns true if the given block has gold.
     * @param block Given block.
     * @author Vaelico786.
     */
    public Boolean isGold(Block block) {
        return block.getType().toString().contains("GOLD");
    }

    /**
     * @brief Absorb the block if it is made by gold. Increase the gold amount depend on the block.
     * @param block Block wanted to be absorbed.
     * @author Vaelico786.
     */
    public void absorbBlock(Location block) {
        switch(block.getBlock().getType()) {
            case GOLD_BLOCK:
                storaged += 9;
                block.getBlock().setType(Material.AIR);
                break;
            case GOLD_ORE:
                storaged += 2;
                block.getBlock().setType(Material.STONE);
                break;
            case NETHER_GOLD_ORE:
                storaged += 2;
                block.getBlock().setType(Material.NETHERRACK);
                break;
            case DEEPSLATE_GOLD_ORE:
                storaged += 2;
                block.getBlock().setType(Material.DEEPSLATE);
                break;
            case RAW_GOLD_BLOCK:
                storaged += 18;
                block.getBlock().setType(Material.AIR);
                break;
            default:
                if(block.getBlock().getType().name().contains("GOLD")) {
                    storaged += 1;
                    block.getBlock().setType(Material.AIR);
                }
                break;
        }
    }

    /**
     * @brief Absorb the item on hand if it is made by gold (not caster). Increase the gold amount depend on the item.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void absorbObject(Player player) {
        if(storaged < maxStoraged) {
            ItemStack hand=player.getInventory().getItemInOffHand();
            if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user))
                hand=player.getInventory().getItemInMainHand();

            switch (hand.getType()) {
                case GOLDEN_APPLE:
                    storaged += 15*hand.getAmount();
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
                    storaged += 0.111 * hand.getAmount();
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
                    if(hand.getItemMeta() == weaponsItems.getReinforcedGoldSword().getItemMeta()) {
                        storaged += 20;
                        hand.setAmount(0);
                    } else {
                        storaged += 2;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_PICKAXE:
                    if(hand.getItemMeta() == weaponsItems.getReinforcedGoldPickaxe().getItemMeta()) {
                        storaged += 30;
                        hand.setAmount(0);
                    } else {
                        storaged += 3;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_AXE:
                    if(hand.getItemMeta() == weaponsItems.getReinforcedGoldAxe().getItemMeta()) {
                        storaged += 30;
                        hand.setAmount(0);
                    } else {
                        storaged += 3;
                        hand.setAmount(0);
                    }
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
                    if(hand instanceof ReinforcedGoldHelmet) {
                        storaged += 45;
                        hand.setAmount(0);
                    }
                    else {
                        storaged += 5;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_CHESTPLATE:
                    if(hand instanceof ReinforcedGoldChestplate) {
                        storaged += 72;
                        hand.setAmount(0);
                    }
                    else {
                        storaged += 8;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_LEGGINGS:
                    if(hand instanceof ReinforcedGoldLeggings) {
                        storaged += 63;
                        hand.setAmount(0);
                    }
                    else {
                        storaged += 7;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_BOOTS:
                    if(hand instanceof ReinforcedGoldBoots) {
                        storaged += 36;
                        hand.setAmount(0);
                    }
                    else {
                        storaged += 4;
                        hand.setAmount(0);
                    }
                    break;
                case GOLDEN_HORSE_ARMOR:
                    storaged += 15;
                    hand.setAmount(0);
                    break;
            }
        }
    }

    /**
     * @brief Returns all the blocks (on a relative pool) that face north or east orientation.
     * @param loc Player's location.
     * @param direction Player's looking direction.
     * @author Vaelico786.
     */
    public ArrayList<Location> positions(Location loc, Vector direction) {
        // if faces north
        ArrayList<Location> blocks = new ArrayList<>();

        if(direction.getX() < -0.2 && direction.getX() > 0.2) {
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

    /**
     * @brief Runs current fruit particles if caster is on hand.
     * @author Vaelico786.
     */
    public void runParticles() {
        new BukkitRunnable(){
            @Override
            public void run() {
                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null && user.getPlayer().isOnline()){
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null){
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else {
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }
                        if(isCaster && Objects.requireNonNull(caster.getItemMeta()).getDisplayName().equals(fruit.getCasterName())){
                            if(!player.isFlying())
                                player.setAllowFlight(true);
                            if(player.isFlying()){
                                if(!glide){
                                    player.setGliding(true);
                                    glide=true;
                                }
                                summonParticles(player);
                            }
                            else
                                glide=false;
                                
                        } else {
                            assert player != null;
                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    /**
     * @brief Summon a particle animation.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void summonParticles(Player player) {
        Location playerLocation = player.getLocation();

        // Crear el círculo de partículas
        double radius = 1.1; // Radio del círculo
        double density = 0.3; // Ajusta la densidad de partículas, valores más bajos llenarán el círculo más densamente

        for (double x = -radius; x <= radius; x += density) {
            for (double z = -radius; z <= radius; z += density) {
                double distanceSquared = x * x + z * z;
                if (distanceSquared <= radius * radius) {
                    double y = playerLocation.getY(); // Aseguramos que las partículas estén a la altura de los pies del jugador

                    // Mostrar la partícula en la ubicación
                    Location particleLocation = new Location(player.getWorld(), playerLocation.getX() + x, y, playerLocation.getZ() + z);
                    player.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, 1,gold);
                }
            }
        }
    }

    // *********************************************** PASSIVES *******************************************************

    /**
     * @brief Passive function when an entity receives damage.
     * @see abilities#onInventoryClick(InventoryClickEvent)
     * @author Vaelico786.
     */
    public void onInventoryClick(InventoryClickEvent event) {
        if(opened && event.getView().getTitle().contains("Gold Creation")){
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            Boolean obtained = false;
            Inventory inventory = event.getInventory();
            if(clicked == null) return;
            event.setCancelled(true);

            if(clicked.getType() == Material.GOLD_NUGGET){
                if(storaged >= 0.111*clicked.getAmount()){
                    storaged -= 0.111*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.GOLD_INGOT){
                if(storaged >= clicked.getAmount()){
                    storaged -= clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.GOLD_BLOCK) {
                if(storaged >= 9*clicked.getAmount()) {
                    storaged -= 9*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.RAW_GOLD_BLOCK) {
                if(storaged >= 18*clicked.getAmount()) {
                    storaged -= 18*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                if(storaged >= 2*clicked.getAmount()) {
                    storaged -= 2*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.GOLDEN_PICKAXE) {
                if(clicked.getItemMeta() == weaponsItems.getReinforcedGoldPickaxe().getItemMeta()){
                    if(storaged >= 27) {
                        storaged -= 27;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else{
                    if(storaged >= 3) {
                        storaged -= 3;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_AXE) {
                if(clicked.getItemMeta() == weaponsItems.getReinforcedGoldAxe().getItemMeta()) {
                    if(storaged >= 27) {
                        storaged -= 27;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else {
                    if(storaged >= 3) {
                        storaged -= 3;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_SHOVEL) {
                if(storaged >= clicked.getAmount()) {
                    storaged -= clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == Material.GOLDEN_SWORD) {
                if(clicked.getItemMeta() == weaponsItems.getReinforcedGoldSword().getItemMeta()){
                    if(storaged >= 18) {
                        storaged -= 18;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else {
                    if(storaged >= 2) {
                        storaged -= 2;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_HELMET) {
                if(clicked instanceof ReinforcedGoldHelmet) {
                    if(storaged >= 45){
                        storaged -= 45;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else {
                    if(storaged >= 5) {
                        storaged -= 5;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_CHESTPLATE) {
                if(clicked instanceof ReinforcedGoldChestplate) {
                    if(storaged >= 72){
                        storaged -= 72;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else {
                    if(storaged >= 8) {
                        storaged -= 8;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_LEGGINGS) {
                if(clicked instanceof ReinforcedGoldLeggings) {
                    if(storaged >= 63){
                        storaged -= 63;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else {
                    if(storaged >= 7) {
                        storaged -= 7;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_BOOTS) {
                if(clicked instanceof ReinforcedGoldBoots) {
                    if(storaged >= 36){
                        storaged -= 36;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
                else{
                    if(storaged >= 4){
                        storaged -= 4;
                        player.getInventory().addItem(clicked);
                        obtained = true;
                    }
                }
            }
            if(clicked.getType() == Material.GOLDEN_HORSE_ARMOR) {
                if(storaged >= 4*clicked.getAmount()){
                    storaged -= 4*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == GOLDEN_CARROT){
                if(storaged >= 10*clicked.getAmount()){
                    storaged -= 10*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(clicked.getType() == GOLDEN_APPLE){
                if(storaged >= 15*clicked.getAmount()){
                    storaged -= 15*clicked.getAmount();
                    player.getInventory().addItem(clicked);
                    obtained = true;
                }
            }
            if(!obtained)
                player.sendMessage("You don't have enough gold to buy this item");
            player.closeInventory();
            opened = false;
        }
    }

    /**
     * @brief Passive function when player dies.
     * @see abilities#onPlayerDeath(PlayerDeathEvent)
     * @author Vaelico786.
     */
    public void onPlayerDeath(PlayerDeathEvent event) {
        super.onPlayerDeath(event);
        storaged = 5;
    }

    /**
     * @brief Passive function when an entity receives damage.
     * @see abilities#onEntityDamage(EntityDamageEvent)
     * @author Vaelico786.
     */
    public void onEntityDamage(EntityDamageEvent event){
        super.onEntityDamage(event);
        double damage = event.getDamage(), finalDamage;
        if(damage > 0){
            if(storaged > 0){
                finalDamage = damage/2 - storaged;
                if(finalDamage <= 0) {
                    finalDamage = 0;
                    storaged = storaged - (int) damage/2;
                    event.setCancelled(true);
                }
                else {
                    finalDamage = damage/2 - storaged;
                    storaged = 0;
                    event.setDamage(finalDamage);
                }
                ((Player) event.getEntity()).spawnParticle(Particle.REDSTONE, event.getEntity().getLocation(), 100, 0, 1, 0, gold);

                abilitiesNames.set(0, nameAbility1 + " (" + storaged + ")");
            }
        }
    }
}

// *********************************************** CUSTOM ARMORS *******************************************************
/**
 * @brief Goru goru no mi's custom golden helmet.
 * @author Vaelico786.
 */
class ReinforcedGoldHelmet extends ItemStack {

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief ReinforcedGoldHelmet constructor.
     * @author Vaelico786.
     */
    public ReinforcedGoldHelmet() {
        super(Material.GOLDEN_HELMET);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Gold Helmet");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 5,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        this.setItemMeta(meta);
    }
}

/**
 * @brief Goru goru no mi's custom golden chestplate.
 * @author Vaelico786.
 */
class ReinforcedGoldChestplate extends ItemStack {

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief ReinforcedGoldChestplate constructor.
     * @author Vaelico786.
     */
    public ReinforcedGoldChestplate() {
        super(Material.GOLDEN_CHESTPLATE);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Gold Chestplate");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        this.setItemMeta(meta);
    }
}

/**
 * @brief Goru goru no mi's custom golden leggings.
 * @author Vaelico786.
 */
class ReinforcedGoldLeggings extends ItemStack {

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief ReinforcedGoldLeggings constructor.
     * @author Vaelico786.
     */
    public ReinforcedGoldLeggings() {
        super(Material.GOLDEN_LEGGINGS);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Gold Leggings");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        this.setItemMeta(meta);
    }
}

/**
 * @brief Goru goru no mi's custom golden boots.
 * @author Vaelico786.
 */
class ReinforcedGoldBoots extends ItemStack {

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief ReinforcedGoldBoots constructor.
     * @author Vaelico786.
     */
    public ReinforcedGoldBoots() {
        super(Material.GOLDEN_BOOTS);
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("Reinforced Gold Boots");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        this.setItemMeta(meta);
    }

}
