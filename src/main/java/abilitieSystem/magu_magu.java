package abilitieSystem;

import castSystem.castIdentification;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.data.type.Bed.Part;
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
        super(plugin, 0, 0, Particle.FLAME, 6, "magu_magu", "Magu Magu no Mi", "Magu Magu caster", 9, 1);
        abilitiesNames.add("Lava Meteors");
        abilitiesCD.add(0);
        abilitiesNames.add("Lava Ground");
        abilitiesCD.add(0);
        abilitiesNames.add("Lava Rise");
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
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(fruit.getCasterName())){
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
                        ((LivingEntity)ent).damage(2,(Entity) user.getPlayer());
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
                        ((LivingEntity)ent).damage(2,(Entity) user.getPlayer());
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
     * @author Vaelico786.
     */
    public void ability3() {
        if (abilitiesCD.get(2) == 0) {
            lavaRise();
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

    public void lavaRise(){
        int nLavaGeyser=8;
        int area=10;
        Random random = new Random();
        Player player = user.getPlayer();
        World world = player.getWorld();
        Stack<Location> locations = new Stack<>();
        for(int i=0; i<nLavaGeyser; i++){
            Location loc = player.getLocation().add(random.nextInt(area + area)-area, 0, random.nextInt(area + area)-area);
            
            loc.setY(OPHLib.searchGround(loc.getX(), loc.getZ(), loc.getY(), world));
            locations.push(loc);
        }


        new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = 250;
            List<Location> activeGeyser = new ArrayList<>();

            @Override
            public void run() {

                if(ticks >= maxTicks || player == null || player.isDead()){
                    cancel();
                }

                if(ticks % 50 == 0 && !activeGeyser.isEmpty())
                    activeGeyser.remove(0);
                if(ticks % 20 == 0 && locations.size() > 0){
                    activeGeyser.add(locations.pop());
                }
                System.out.println(activeGeyser.size() + " and " + locations.size());
                activeGeyser.forEach( location -> {
                    // System.out.println("Location: " + location);
                    world.spawnParticle(Particle.LAVA, location, 10, 1, 2, 1, 0);
                    world.spawnParticle(Particle.LAVA, location.clone().add(0, 2, 0), 5, 2, 1, 2,3);
                    world.getNearbyEntities(location.clone().add(0, 1, 0), 2, 2, 2).forEach(entity -> {
                        if(entity instanceof LivingEntity && !entity.equals(player)){
                            ((LivingEntity) entity).damage(4, player);
                            entity.setFireTicks(100);
                        }
                    });                
                });

                ticks+=5;

            }

        }.runTaskTimer(plugin,0,5);

    }



    public void onEntityDamage(EntityDamageEvent event){
        super.onEntityDamage(event);

        if(active && event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
            event.setCancelled(true);
        }

    }
}

