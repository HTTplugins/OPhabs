package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static java.lang.Math.*;

/**
 * @brief Magu magu no mi ability Class.
 * @author RedRiotTank
 */
public class magu_magu extends logia {
    public static Particle.DustOptions maguOrange = new Particle.DustOptions(Color.ORANGE,1.0F);
    public static Particle.DustOptions maguBlack = new Particle.DustOptions(Color.BLACK,1.0F);
    public static Particle.DustOptions maguRed = new Particle.DustOptions(Color.RED,1.0F);
    public static ArrayList<LivingEntity> LavaSoldiers = new ArrayList<>();
    public static ArrayList<LivingEntity> TargetsGhast = new ArrayList<>();
    public static LivingEntity TargetGhastActual = null;

    public Random rand = new Random();

    /**
     * @brief magu_magu constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank.
     */
    public magu_magu(OPhabs plugin) {
        super(plugin, Particle.FLAME, castIdentification.castMaterialMagu, castIdentification.castItemNameMagu,
                fruitIdentification.fruitCommandNameMagu);
        abilitiesNames.add("Lava Meteors");
        abilitiesCD.add(0);
        abilitiesNames.add("Lava Ground");
        abilitiesCD.add(0);
        abilitiesNames.add("Lava Soldiers");
        abilitiesCD.add(0);

        this.runParticles();
    }

    /**
     * @brief magu_magu logia form particles + sound.
     * @author RedRiotTank.
     */
    @Override
    public void runParticles() {

        new BukkitRunnable() {
            int ticks = 0;

            Random random = new Random();
            boolean lavaAmbiance = false;
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

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameMagu)){
                            double xdecimals = random.nextDouble();
                            double zdecimals = random.nextDouble();

                            double x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                            double z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(element,player.getLocation().add(x,1,z),0,0,0,0);

                            xdecimals = random.nextDouble();
                            zdecimals = random.nextDouble();

                            x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                            z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(Particle.LAVA,player.getLocation().add(x,1,z),0,0,0,0);



                            if (!lavaAmbiance){
                                lavaAmbiance = true;
                                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_AMBIENT,100,1);

                                double r = random.nextDouble();

                                if(r < 0.5)
                                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP,100,1);
                            }

                            player.setAllowFlight(true);


                        }else {
                            lavaAmbiance = false;

                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }

                if(ticks == 80){
                    lavaAmbiance = false;
                    ticks = 0;
                }

                ticks++;

            }

        }.runTaskTimer(plugin,0,2);

    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------

    /**
     * @brief Ability 1: "Lava Meteors".
     * @see magu_magu#lavaMeteors(Player)
     * @author RedRiotTank.
     */
    public void ability1() {
        if (abilitiesCD.get(0) == 0) {
            lavaMeteors(user.getPlayer());
            abilitiesCD.set(0, 180); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Creates a "Lava Meteors rain" in a [15,10,15] box. The meteors Explode when reach an entity or when a % of particles dissapear
     * in blocks. Meteors automatically target entities.
     * @param player User that creates the "Lava Meteors rain".
     * @note It has a max of 3 meteors.
     * @author RedRiotTank.
     */
    public void lavaMeteors(Player player) {
        final int Maximum = 3;
        int index = 0;

        List<Entity> entityList = player.getNearbyEntities(15,10,15);

        List<LivingEntity> Players = new ArrayList<>();
        List<LivingEntity> livingEntities = new ArrayList<>();

        for(Entity ent : entityList) {
            if (ent instanceof Player)
                Players.add((LivingEntity) ent);
            else if (ent instanceof LivingEntity)
                livingEntities.add((LivingEntity) ent);
        }

        for(int i=0; i<Players.size() && index < Maximum; i++) {
            sendMeteorite(player,Players.get(i));
            index++;
        }

        for(int i=0; i<livingEntities.size() && index < Maximum; i++) {
            sendMeteorite(player, livingEntities.get(i));
            index++;
        }
    }

    /**
     * @brief Sends the meteor (particle circle) and meanwhile do the finish conditions checks.
     * @param player User that creates the "Lava Meteors rain".
     * @note It has a max of 3 meteors.
     * @author RedRiotTank.
     */
    public void sendMeteorite(LivingEntity player, LivingEntity ent) {
        Location playerLoc = player.getLocation();
        Location upHead = playerLoc.add(0,7,0);
        World world = player.getWorld();

        new BukkitRunnable() {
            double x, y, z, xr, yr, zr;
            final double tenPI = 10*PI;
            double angle, modAngle;
            Location spawnBallPos;
            final Vector direction = new Vector(0,0,0);
            int numplarticles = 0;

            boolean reachTarget = false;

            @Override
            public void run() {
                numplarticles = 0;
                Vector vec = new Vector(ent.getLocation().getX() - upHead.getX(), ent.getLocation().getY() - upHead.getY(),
                        ent.getLocation().getZ() - upHead.getZ());
                vec.normalize();

                double sumX = vec.getX();
                double sumY = vec.getY();
                double sumZ = vec.getZ();

                for (double i = 0; i < 2*PI*4; i+=0.05) {
                    angle = i/10*PI;
                    modAngle = (i%10)*PI;
                    x = sin(angle);
                    y = sin(modAngle) * cos(angle);
                    z = cos(modAngle) * cos(angle);

                    xr = upHead.getX() + x ;
                    yr = upHead.getY() + y ;
                    zr = upHead.getZ() + z ;

                    spawnBallPos = new Location(world, xr,yr,zr);

                    spawnBallPos.add(direction);

                    int option = rand.nextInt(3);

                    Particle.DustOptions maguDO = new Particle.DustOptions(Color.PURPLE,1.0F);;

                    switch (option){
                        case 0: maguDO = maguOrange; break;
                        case 1: maguDO = maguBlack; break;
                        case 2: maguDO = maguRed; break;
                    }

                    if(spawnBallPos.getBlock().getType().equals(Material.AIR)) {
                        world.spawnParticle(Particle.REDSTONE, spawnBallPos, 0, 0, 0, 0,maguDO);
                        numplarticles++;
                    }

                }

                direction.add(new Vector(sumX,sumY,sumZ));

                for(Entity ent : world.getNearbyEntities(spawnBallPos,2,2,2))
                    if(ent instanceof LivingEntity && !player.equals(ent)){
                        ((LivingEntity)ent).damage(2);
                        reachTarget = true;
                    }

                if(numplarticles < 25 || reachTarget){
                    world.createExplosion(ent.getLocation(),6);
                    /*
                    ent.getLocation().getBlock().setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(1,0,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(1,0,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(0,1,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(-2,-1,0).setType(Material.LAVA)
                    */

                    for(int i=0; i< 15; i++){
                        double randX = rand.nextDouble() - rand.nextDouble() ;
                        double randY = rand.nextDouble() - rand.nextDouble();
                        double randZ = rand.nextDouble() - rand.nextDouble();

                        int option = rand.nextInt(3);

                        Material randomMaterial = Material.AIR;

                        switch (option) {
                            case 0: randomMaterial = Material.LAVA; break;
                            case 1: randomMaterial = Material.MAGMA_BLOCK; break;
                            case 2: randomMaterial = Material.COBBLESTONE; break;
                        }
                        (world.spawnFallingBlock(spawnBallPos,randomMaterial.createBlockData())).setVelocity(new Vector(randX,randY,randZ));
                    }
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin,0,1);
    }

    public void sendMeteoriteCustom(LivingEntity player, LivingEntity ent) {
        user.getPlayer().sendMessage("METEORITE :D.");
        Location playerLoc = player.getLocation();
        Location upHead = playerLoc.add(0, -1, 0);
        World world = player.getWorld();

        new BukkitRunnable() {
            double x, y, z, xr, yr, zr;
            final double tenPI = 10*PI;
            double angle, modAngle;
            Location spawnBallPos;
            final Vector direction = new Vector(0,0,0);
            int numplarticles = 0;
            boolean reachTarget = false;

            @Override
            public void run() {
                numplarticles = 0;
                Vector vec = new Vector(ent.getLocation().getX() - upHead.getX(), ent.getLocation().getY() - upHead.getY(),
                        ent.getLocation().getZ() - upHead.getZ());
                vec.normalize();

                double sumX = vec.getX();
                double sumY = vec.getY();
                double sumZ = vec.getZ();

                for (double i = 0; i < 2*PI*4; i += 0.05) {
                    angle = i/10*PI;
                    modAngle = (i%10)*PI;
                    x = sin(angle);
                    y = sin(modAngle) * cos(angle);
                    z = cos(modAngle) * cos(angle);

                    xr = upHead.getX() + x;
                    yr = upHead.getY() + y;
                    zr = upHead.getZ() + z;

                    spawnBallPos = new Location(world, xr,yr,zr);

                    spawnBallPos.add(direction);

                    int option = rand.nextInt(3);

                    Particle.DustOptions maguDO = new Particle.DustOptions(Color.PURPLE,1.0F);;

                    switch (option) {
                        case 0: maguDO = maguOrange; break;
                        case 1: maguDO = maguBlack; break;
                        case 2: maguDO = maguRed; break;
                    }

                    if (spawnBallPos.getBlock().getType().equals(Material.AIR)) {
                        world.spawnParticle(Particle.REDSTONE, spawnBallPos, 0, 0, 0, 0,maguDO);
                        numplarticles++;
                    }
                }

                direction.add(new Vector(sumX,sumY,sumZ));

                for (Entity ent : world.getNearbyEntities(spawnBallPos,2,2,2))
                    if (ent instanceof LivingEntity && !player.equals(ent)) {
                        ((LivingEntity)ent).damage(2);
                        reachTarget = true;
                    }

                if (numplarticles < 25 || reachTarget) {
                    world.createExplosion(ent.getLocation(),6);

                    for (int i=0; i < 15; i++) {
                        double randX = rand.nextDouble() - rand.nextDouble() ;
                        double randY = rand.nextDouble() - rand.nextDouble();
                        double randZ = rand.nextDouble() - rand.nextDouble();

                        int option = rand.nextInt(3);

                        Material randomMaterial = Material.AIR;

                        switch (option) {
                            case 0: randomMaterial = Material.LAVA; break;
                            case 1: randomMaterial = Material.MAGMA_BLOCK; break;
                            case 2: randomMaterial = Material.COBBLESTONE; break;
                        }
                        (world.spawnFallingBlock(spawnBallPos,randomMaterial.createBlockData())).setVelocity(new Vector(randX, randY, randZ));
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    /**
     * @brief Ability 2: "Lava ground".
     * @see magu_magu#lavaGround(Player)
     * @author RedRiotTank.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0){
            lavaGround(user.getPlayer());
            abilitiesCD.set(1, 30); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: If player is shifting, creates a lava lake around him, else, create a lava river in the direction he is looking at.
     * @param player User that creates the "Lava ground".
     * @see magu_magu#lavaLake(Player)
     * @see magu_magu#lavaRiver(Player)
     * @author RedRiotTank.
     */
    public void lavaGround(Player player){
        if(player.isSneaking())
            lavaRiver(player);
         else
            lavaLake(player);

    }

    /**
     * @brief Creates a lava river in the direction the player is looking at.
     * @param player User that creates the "Lava river".
     * @author RedRiotTank.
     */
    public void lavaRiver(Player player) {
        new BukkitRunnable(){
            final Vector facingDirection = player.getLocation().getDirection();
            int index = 2;
            double randomQuote = 1;

            Location playerLoc = player.getLocation();
            @Override
            public void run() {
                int x = (int)(facingDirection.getX()*index);
                int y = 0;
                int z = (int)(facingDirection.getZ()*index);

                if(x == 0){
                    putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                    putLavaBlock(x-1,y,z,randomQuote,playerLoc);
                } else if( z == 0){
                    putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                    putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                } else {
                    if(x > 0 && z > 0){
                        putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                    } else if(x > 0 && z < 0){

                        putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                    } else if(x<0 && z > 0){
                        putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                        putLavaBlock(x-1,y,z,randomQuote,playerLoc);

                    } else if( x < 0 && z < 0) {
                        putLavaBlock(x-1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                    }
                }

                putLavaBlock(x,y,z,randomQuote,playerLoc);

                index++;
                randomQuote-=0.02;

                if(index==12) this.cancel();

            }
        }.runTaskTimer(plugin,0,3);
    }

    /**
     * @brief Creates a lava lave around player's location.
     * @param player User that creates the "Lava lake".
     * @author RedRiotTank.
     */
    public void lavaLake(Player player ) {
        new BukkitRunnable(){
                int index = 1;

                final Location playerLoc = player.getLocation();

                double randomQuote = 1;

                @Override
                public void run() {
                    for(double i=-index; i <= index; i++ ){
                        putLavaBlock((int)i,0,-index,randomQuote,playerLoc);
                        putLavaBlock((int)i,0,index,randomQuote,playerLoc);
                    }

                    for(double i=-index; i <= index; i++ ){
                        putLavaBlock(-index,0,(int)i,randomQuote,playerLoc);
                        putLavaBlock(index,0,(int)i,randomQuote,playerLoc);
                    }
                    randomQuote-=0.1;
                    index++;

                    if(index == 5) this.cancel();
                }


            }.runTaskTimer(plugin,0,5);


    }

    /**
     * @brief Puts a random Lava-themed block in the x,y,z RELATIVE location to a player location.
     * @param x Relative x location to the player.
     * @param y Relative y location to the player.
     * @param z Relative z location to the player.
     * @param randomQuote random quote possibility of placing the block.
     * @param playerLoc Location of the player.
     * @author RedRiotTank.
     */
    public void putLavaBlock(int x,int y, int z, double randomQuote, Location playerLoc) {
        Random Rand = new Random();
        double aleatory = rand.nextDouble();
        double randMagmaBlock = rand.nextDouble();

       boolean putBlock = false;
       Location putBlockLocation = null;

        Material blockMaterial = Material.LAVA;

        if(randMagmaBlock < 0.1) blockMaterial = Material.MAGMA_BLOCK;

        if(airOrSimilar(playerLoc.getBlock().getRelative(x,y,z).getType()) && aleatory < randomQuote) {

            if (!playerLoc.getBlock().getRelative(x, y - 1, z).getType().equals(Material.AIR)){
                putBlock = true;
                putBlockLocation = playerLoc.getBlock().getRelative(x, y - 1, z).getLocation();
                playerLoc.getBlock().getRelative(x, y - 1, z).setType(blockMaterial);
            }
            else if(airOrSimilar(playerLoc.getBlock().getRelative(x,y-1,z).getType()) && aleatory < randomQuote)
                if(!playerLoc.getBlock().getRelative(x,y-2,z).getType().equals(Material.AIR)) {
                    putBlock = true;
                    putBlockLocation = playerLoc.getBlock().getRelative(x, y - 2, z).getLocation();
                    playerLoc.getBlock().getRelative(x, y - 2, z).setType(blockMaterial);
                }

        } else if(airOrSimilar(playerLoc.getBlock().getRelative(x,y+1,z).getType()) && aleatory < randomQuote)
            if(!playerLoc.getBlock().getRelative(x,y,z).getType().equals(Material.AIR)){
                putBlock = true;
                putBlockLocation = playerLoc.getBlock().getRelative(x,y,z).getLocation();
                playerLoc.getBlock().getRelative(x,y,z).setType(blockMaterial);
            }

        if(putBlock)
            playerLoc.getWorld().playSound(putBlockLocation,Sound.BLOCK_LAVA_EXTINGUISH,1,1);

    }

    /**
     * @brief this function have to be changed.
     * @todo change this function for .isSolid().
     * @author RedRiotTank.
     */
    public boolean airOrSimilar(Material mat) {
        return mat.equals(Material.AIR)
                || mat.equals(Material.DEAD_BUSH)
                || mat.equals(Material.POTTED_AZALEA_BUSH)
                || mat.equals(Material.ROSE_BUSH)
                || mat.equals(Material.SWEET_BERRY_BUSH)
                || mat.equals(Material.POTTED_DEAD_BUSH)
                || mat.equals(Material.POTTED_FLOWERING_AZALEA_BUSH)
                || mat.equals(Material.BROWN_MUSHROOM)
                || mat.equals(Material.RED_MUSHROOM)
                || mat.equals(Material.DANDELION)
                || mat.equals(Material.POPPY)
                || mat.equals(Material.BLUE_ORCHID)
                || mat.equals(Material.ALLIUM)
                || mat.equals(Material.AZURE_BLUET)
                || mat.equals(Material.RED_TULIP)
                || mat.equals(Material.ORANGE_TULIP)
                || mat.equals(Material.WHITE_TULIP)
                || mat.equals(Material.PINK_TULIP)
                || mat.equals(Material.OXEYE_DAISY)
                || mat.equals(Material.SUNFLOWER)
                || mat.equals(Material.LILAC)
                || mat.equals(Material.PEONY)
                || mat.equals(Material.TALL_GRASS)
                || mat.equals(Material.GRASS)
                || mat.equals(Material.LARGE_FERN)
                || mat.equals(Material.CORNFLOWER)
                || mat.equals(Material.LILY_OF_THE_VALLEY)
                || mat.equals(Material.WITHER_ROSE)
                || mat.equals(Material.CRIMSON_FUNGUS)
                || mat.equals(Material.WARPED_FUNGUS)
                || mat.equals(Material.CRIMSON_ROOTS)
                || mat.equals(Material.WARPED_ROOTS)
                || mat.equals(Material.NETHER_SPROUTS)
                || mat.equals(Material.AZALEA)
                || mat.equals(Material.FLOWERING_AZALEA)
                || mat.equals(Material.SPORE_BLOSSOM);
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    /**
     * @brief Ability 3: "".
     * @author MiixZ.
     */
    public void ability3() {
        if (abilitiesCD.get(2) == 0) {
            if (LavaSoldiers.size() < 3) {
                GenerateSoldier(user.getPlayer());
            } else {
                GenerateLavaGhast(user.getPlayer());
            }

            if (LavaSoldiers.size() < 3) {
                abilitiesNames.set(2, "Lava Soldiers");
                abilitiesCD.set(2, 2); // Pon el cooldown en segundos
            } else {
                abilitiesNames.set(2, "LAVA GHAST");
                abilitiesCD.set(2, 2); // Pon el cooldown en segundos
            }
        }
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "".
     * @author -.
     */
    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            abilitiesCD.set(3, 180); // Pon el cooldown en segundos
        }
    }

    public void GenerateSoldier(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        Zombie soldado = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
        soldado.setCustomName("Lava Soldier");

        soldado.setInvisible(true);
        soldado.setTarget(null);
        soldado.setVelocity(user.getPlayer().getLocation().getDirection().normalize().multiply(2));

        LavaSoldiers.add(soldado);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (soldado.isDead() || !checkSoldiers(player)) {
                    if (soldado.isDead())
                        LavaSoldiers.remove(soldado);

                    cancel();
                }

                soldado.setFireTicks(-50);

                for (double i = 0.5; i < 5; i += 0.1) {
                    soldado.getWorld().spawnParticle(Particle.DRIP_LAVA, soldado.getLocation().add(i / 5,1,i/ 5),
                            1, 0,1,0,0);
                    soldado.getWorld().spawnParticle(Particle.DRIP_LAVA, soldado.getLocation().add(i/ 5,1,-i/ 5),
                            1, 0,1,0,0);
                    soldado.getWorld().spawnParticle(Particle.DRIP_LAVA, soldado.getLocation().add(-i/ 5,1,i/ 5),
                            1, 0,1,0,0);
                    soldado.getWorld().spawnParticle(Particle.DRIP_LAVA, soldado.getLocation().add(-i/ 5,1,-i/ 5),
                            1, 0,1,0,0);
                }

                for (Entity entity : soldado.getNearbyEntities(5, 5, 5)) {
                    if (entity instanceof LivingEntity && !entity.getName().equals(user.getPlayer().getName()) &&
                            !entity.getName().equals("Lava Soldier")) {
                        soldado.setTarget((LivingEntity) entity);
                    }
                }
            }
        }.runTaskTimer(plugin,0,0);

        // Desprende partículas alrededor del soldado.
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
        world.spawnParticle(Particle.REDSTONE, loc, 100, 0.5, 0.5, 0.5, dustOptions);

        ItemStack espadaSoldado = new ItemStack(Material.STONE_SWORD);
        ItemMeta espadaMeta = espadaSoldado.getItemMeta();

        assert espadaMeta != null;
        espadaMeta.addEnchant(Enchantment.FIRE_ASPECT, 4, true);
        espadaMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        espadaMeta.addEnchant(Enchantment.DURABILITY, 4, true);
        espadaMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
        espadaSoldado.setItemMeta(espadaMeta);

        Objects.requireNonNull(soldado.getEquipment()).setItemInMainHand(espadaSoldado);
        soldado.getEquipment().setItemInMainHandDropChance(0);

        soldado.setCustomNameVisible(true);
    }

    public void GenerateLavaGhast(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        Ghast ghast = (Ghast) world.spawnEntity(loc, EntityType.GHAST);
        ghast.setCustomName("Lava Ghast");

        ghast.setInvisible(true);
        ghast.setTarget(null);
        ghast.setVelocity(new Vector(0,3,0));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (ghast.isDead() || !player.isOnline()) {
                    ghast.remove();
                    cancel();
                }

                for (double i = 0.5; i < 10; i += 0.1) {
                    ghast.getWorld().spawnParticle(Particle.LAVA, ghast.getLocation().add(i / 5,1,i / 5),
                            1, 0,1,0,0);
                    ghast.getWorld().spawnParticle(Particle.LAVA, ghast.getLocation().add(i / 5,1,-i/ 5),
                            1, 0,1,0,0);
                    ghast.getWorld().spawnParticle(Particle.LAVA, ghast.getLocation().add(-i / 5,1,i / 5),
                            1, 0,1,0,0);
                    ghast.getWorld().spawnParticle(Particle.LAVA, ghast.getLocation().add(-i / 5,1,-i / 5),
                            1, 0,1,0,0);
                }

                for (Entity entity : ghast.getNearbyEntities(30, 30, 30)) {
                    if (entity instanceof LivingEntity && !entity.getName().equals(user.getPlayer().getName()) &&
                            !entity.getName().equals("Lava Soldier") && !entity.getName().equals("Lava Ghast")) {
                        TargetsGhast.add((LivingEntity) entity);
                    }
                }
            }
        }.runTaskTimer(plugin,0,0);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (ghast.isDead() || !player.isOnline()) {
                    TargetsGhast.clear();
                    TargetGhastActual = null;
                    cancel();
                }

                if (TargetsGhast.size() > 0) {
                    if (TargetGhastActual == null || TargetGhastActual.isDead()) {
                        if (TargetGhastActual != null && TargetGhastActual.isDead())
                            TargetsGhast.remove(TargetGhastActual);

                        if (TargetsGhast.size() > 0) {
                            TargetGhastActual = TargetsGhast.get(0);
                        }
                    }

                    if (TargetGhastActual != null)
                        sendMeteoriteCustom(ghast, TargetGhastActual);
                } else {
                    TargetGhastActual = null;
                }
            }
        }.runTaskTimer(plugin,40,100);
    }

    public boolean checkSoldiers(Player player) {
        if (!player.isOnline()) {
            for (LivingEntity entity : LavaSoldiers) {
                entity.setHealth(0);
            }

            return false;
        }

        for (LivingEntity entity : LavaSoldiers) {
            if (entity.getLocation().distance(user.getPlayer().getLocation()) > 50) {
                entity.setHealth(0);
            }
        }

        return true;
    }

    public void onEntityDamage(EntityDamageEvent event){
        super.onEntityDamage(event);

        if(active && event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
            event.setCancelled(true);
        }

    }
}