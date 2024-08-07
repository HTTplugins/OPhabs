package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static java.lang.Math.*;

/**
 * @brief Yami yami no mi ability Class.
 * @author RedRiotTank
 */
public class yami_yami extends logia {
    public static Particle.DustOptions yamiDO = new Particle.DustOptions(Color.BLACK,1.0F);
    private int repealAnimationCounter = 0;

    private final int radius = 3;
    private final int maxRadiusAmplification = 9;
    private final int damageAmount = 1;

    private static final Material voidMaterial= Material.BLACK_CONCRETE;

    private Stack<Material> absorbedMaterials = new Stack<>();

    private List<Block> convertedToVoidBlocks = new ArrayList<>();

    /**
     * @brief yami_yami constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank.
     */
    public yami_yami(OPhabs plugin){
        super(plugin, 0, 0, Particle.REDSTONE,  1, "yami_yami", "Yami Yami no Mi", "Yami Yami caster", 8, 1.6);
        abilitiesNames.add("Black Void");
        abilitiesCD.add(0);
        abilitiesNames.add("LiberateVoid");
        abilitiesCD.add(0);
        abilitiesNames.add("LivingVoid");
        abilitiesCD.add(0);
        abilitiesNames.add("voidMeteore");
        abilitiesCD.add(0);
        this.runParticles();
    }

    /**
     * @brief yami_yami logia form particles.
     * @todo add sound.
     * @author RedRiotTank.
     */
    public void runParticles(){
        new BukkitRunnable(){
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

                            double yaw = player.getLocation().getYaw();
                            double y = player.getLocation().getY();

                            spawnBackParticle(player,yaw - 90,y+0.5);
                            spawnBackParticle(player, yaw - 45,y+0.5);
                            spawnBackParticle(player, yaw - 135,y+0.5);

                            spawnBackParticle(player,yaw - 90,y+1);
                            spawnBackParticle(player, yaw - 45,y+1);
                            spawnBackParticle(player, yaw - 135,y+1);

                            player.setAllowFlight(true);

                        }else {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }
            }

            public void spawnBackParticle(Player player, double yaw, double y){
                double x = cos(toRadians(yaw)) / 2;
                double z = sin(toRadians(yaw)) / 2;
                //Behind
                double xBehind = player.getLocation().getX() + x;
                double zBehind = player.getLocation().getZ() + z;
                Location pBehind = new Location(player.getWorld(),xBehind,y,zBehind);
                player.getWorld().spawnParticle(Particle.SMOKE_LARGE,pBehind,0,0,0,0);
            }
        }.runTaskTimer(plugin,0,1);

    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------

    /**
     * @brief Ability 1 caller: "Black Void".
     * @see yami_yami#blackVoid(Player)
     * @author RedRiotTank.
     */
    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            blackVoid(user.getPlayer());
            abilitiesCD.set(0, 20); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Changes ground blocks for black-themed blocks and "absorbs" all of them (converts them
     * into falling blocks, and cancels the change falling block --> block). It also hurts entities above the blocks.
     * @param player User that creates the Black void.
     * @todo refactor, remember to avoid absorving fluids.
     * @author RedRiotTank.
     */
    public void blackVoid(Player player) {
        player.getWorld().playSound(player, Sound.AMBIENT_CRIMSON_FOREST_MOOD, 10, 2);
        Location playerLocation = player.getLocation();

        bresenham(radius, playerLocation, true);

        int voidExpansionDelay = 6;

        int voidExpansionSpeed = 5; //less is faster
        BukkitTask circle = new BukkitRunnable() {
            int radiusCounter = radius;

            public void run() {
                radiusCounter++;

                bresenham(radiusCounter, playerLocation, true);
                if (radiusCounter == radius + maxRadiusAmplification)
                    this.cancel();
            }
        }.runTaskTimer(plugin, voidExpansionDelay, voidExpansionSpeed);

        int damageDelay = 0;
        int damageSpeed = 15;
        BukkitTask damageAndParticles = new BukkitRunnable() {
            public void run() {
                for(LivingEntity livEnt : player.getWorld().getLivingEntities())
                    if (livEnt != player && livEnt.getLocation().getBlock().getRelative(0,-1,0).getType().equals(voidMaterial))
                        livEnt.damage(damageAmount, (Entity) user.getPlayer());


                for(double x = playerLocation.getX() - maxRadiusAmplification; x < playerLocation.getX() + maxRadiusAmplification; x++ ){
                    for(double z = playerLocation.getZ() - maxRadiusAmplification; z < playerLocation.getZ() + maxRadiusAmplification; z++ ){
                        boolean foundY=false;
                        for(double y = playerLocation.getBlockY() - 1;  y > playerLocation.getBlockY() - 3 && !foundY; y--){
                            Block block = new Location(player.getWorld(), x,y,z).getBlock();
                            if(block.getType().equals(voidMaterial))
                                player.getWorld().spawnParticle(element,block.getRelative(0,1,0).getLocation(),0,0,1,0,yamiDO);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, damageDelay, damageSpeed);

        int dissappearVoidBlocksDelay = 90;
        new BukkitRunnable() {
            @Override
            public void run() {
                dissappearVoidBlocks();
                damageAndParticles.cancel();
                player.getWorld().playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 2);
            }
        }.runTaskLater(plugin, dissappearVoidBlocksDelay);
    }

    /**
     * @brief Cancels fallingblock --> block change to make the absorbing effect, and saves the blocks materials information.
     * @param event EntityChangeBlockEvent that Minecraft client sends to server to convert blocks.
     * @see yami_yami#blackVoid(Player)
     * @note Called in his correspondent in the caster, as a listener.
     * @author RedRiotTank.
     */
    public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getBlock().getRelative(0,-1,0).getType().equals(voidMaterial)) {
            event.setCancelled(true);
        }
    }

    /**
     * @brief calculates pixelation of a circle and calls setblock functions.
     * @param radius radius of the blackvoid.
     * @param playerLocation location of the player, center of the black void.
     * @param fill true if fill the bresenham circle, false if not
     * @see yami_yami#blackVoid(Player)
     * @author RedRiotTank.
     */
    public void bresenham(int radius, Location playerLocation, boolean fill){
        /*
        Bresenham algorithm modification.
        radius must be 2n+1 with 1<=n.
        */

        int x,z,d;

        x=0;
        z=radius;
        d = 3-2*radius;

        setBlockAndFill(playerLocation,x,z,fill);
        setBlockAndFill(playerLocation,z,x,fill);

        while(x < z){

            if(d < 0)
                d=d+4*x+6;
            else {
                d=d+4*(x-z) + 10;
                z--;
            }
            x++;
            setBlockAndFill(playerLocation,x,z,fill);
            setBlockAndFill(playerLocation,z,x,fill);
        }
    }

    /**
     * @brief Puts block in the x,z location, calculating y position.
     * @param playerLocation location of the player, center of the black void.
     * @param x true if fill the bresenham circle, false if not
     * @param z true if fill the bresenham circle, false if not
     * @param fill true if fill the bresenham circle, false if not
     * @see yami_yami#blackVoid(Player)
     * @author RedRiotTank.
     */
    public void setBlockAndFill(Location playerLocation, int x, int z,boolean fill) {
        Location perimeterPixel = new Location(playerLocation.getWorld(),0,0,0);

        perimeterPixel.setY( playerLocation.getBlockY() - 1);
        perimeterPixel.setX( playerLocation.getBlockX() + x);
        perimeterPixel.setZ(playerLocation.getBlockZ() + z);
        if(!fill)
            setBlockAndLineUP(perimeterPixel);
        else
            for(Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() >= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ()-1))
                setBlockAndLineUP(fillBlock);


        perimeterPixel.setX( playerLocation.getBlockX() - x);
        perimeterPixel.setZ(playerLocation.getBlockZ() + z);
        if(!fill)
            setBlockAndLineUP(perimeterPixel);
        else
            for(Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() >= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ()-1))
                setBlockAndLineUP(fillBlock);


        perimeterPixel.setX( playerLocation.getBlockX() + x);
        perimeterPixel.setZ(playerLocation.getBlockZ() - z);
        if(!fill)
            setBlockAndLineUP(perimeterPixel);
        else
            for(Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() <= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ()+1))
                setBlockAndLineUP(fillBlock);


        perimeterPixel.setX( playerLocation.getBlockX() - x);
        perimeterPixel.setZ(playerLocation.getBlockZ() - z);
        if(!fill)
            setBlockAndLineUP(perimeterPixel);
        else
            for(Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() <= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ()+1))
                setBlockAndLineUP(fillBlock);

    }

    /**
     * @brief raytrace a colum to check ground (calculation of the Y).
     * @param perimeterPixel location of x,z.
     * @see yami_yami#blackVoid(Player)
     * @author RedRiotTank.
     */
    public void setBlockAndLineUP(Location perimeterPixel) {
        boolean found = false;
        for(int i=0; i < 3 && !found; i++){


            if( !perimeterPixel.getBlock().getRelative(0,-i,0).getType().equals(Material.AIR)){
                found = true;
                perimeterPixel.getBlock().getRelative(0,-i,0).setType(voidMaterial);
                convertedToVoidBlocks.add(perimeterPixel.getBlock().getRelative(0,-i,0));

            }
        }

        Location upBlockLocation = new Location(perimeterPixel.getWorld(), perimeterPixel.getBlockX(),perimeterPixel.getBlockY(),perimeterPixel.getBlockZ());
        int maxHeight = 10;
        for(int i = 1; i< maxHeight; i++){
            upBlockLocation.setY(perimeterPixel.getBlockY() + i );

            if(!upBlockLocation.getBlock().getType().equals(Material.AIR)){
                Material matFallingBlock = upBlockLocation.getBlock().getType();
                absorbedMaterials.add(matFallingBlock);
                upBlockLocation.getBlock().setType(Material.AIR);
                upBlockLocation.getWorld().spawnFallingBlock(upBlockLocation,matFallingBlock,(byte) 9);
            }

        }

    }

    /**
     * @brief Make the voidBlocks placed disappear.
     * @see yami_yami#blackVoid(Player)
     * @author RedRiotTank.
     */
    public void dissappearVoidBlocks() {
        for(Block convertedTVblock : convertedToVoidBlocks)
            convertedTVblock.setType(Material.AIR);
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    /**
     * @brief Ability 2 caller: "Liberate Absorptions".
     * @see yami_yami#liberateAbsorptions(Player)
     * @author RedRiotTank.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0){
            liberateAbsorptions(user.getPlayer());
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Liberates absorbed blocks with livingVoid.
     * @param player User that liberates the blocks.
     * @todo add potion effects when liberating.
     * @author RedRiotTank.
     */
    public void liberateAbsorptions(Player player) {

        World world = player.getWorld();
        Random random = new Random();

        BukkitTask particles = new BukkitRunnable(){
            int tickTime = 0;
            @Override
            public void run() {
                tickTime++;
                for(int i=0; i<100; i++){

                    double xdecimals = random.nextDouble();
                    double ydecimals = random.nextDouble();
                    double zdecimals = random.nextDouble();

                    double x = random.nextInt(4 + 4 ) - 4 + xdecimals;
                    double y = random.nextInt(4 + 4 ) - 4 + xdecimals;
                    double z = random.nextInt(4 + 4 ) - 4 + zdecimals;
                    player.getWorld().spawnParticle(element,player.getLocation().getBlock().getRelative(0,2,0).getLocation().add(x,y,z),0,0,1,0,yamiDO);

                }

            }

        }.runTaskTimer(plugin,0,4);



        new BukkitRunnable(){
            int i = 0;

            World world = player.getWorld();

            Material usedMAterial;
            @Override
            public void run() {

                if(!absorbedMaterials.empty()){
                    usedMAterial = absorbedMaterials.pop();

                    FallingBlock block = world.spawnFallingBlock(player.getLocation().getBlock().getRelative(0,2,0).getLocation(),usedMAterial.createBlockData());

                    Random random = new Random();

                    double xdecimals = random.nextDouble();
                    double zdecimals = random.nextDouble();

                    double x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                    double z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                    block.setVelocity(new Vector(x,0.5,z));
                }

                i++;

                if(i == 85){
                    particles.cancel();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,0,4);
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    /**
     * @brief Ability 3 caller: "Living Void".
     * @see yami_yami#livingVoid(Player)
     * @author RedRiotTank.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            livingVoid(user.getPlayer());
            abilitiesCD.set(2, 20); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Absorbs an entity and let you launch it.
     * @param player User that uses the ability.
     * @todo refactor it to use in multiple places.
     * @author RedRiotTank.
     */
    public void livingVoid(Player player) {
        player.playSound(player.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,0);
        World world = player.getWorld();

        absorbAnimation(player);

        for(Entity ent : player.getNearbyEntities(10,10,10))
            if(ent instanceof LivingEntity && !player.equals(ent))
                livingVoidForEntity(ent,player);

        repealAnimationCounter = 0;
    }

    /**
     * @brief ANIMATION: spiral animation for absorving.
     * @param player User that uses the ability.
     * @author RedRiotTank.
     */
    public void absorbAnimation(Player player) {
        new BukkitRunnable(){
            final double angle = -player.getLocation().getYaw();
            final World world = player.getWorld();

            double start = 2* PI*5;
            double finish = 2* PI*5 - 2* PI*5/10;
            @Override
            public void run() {
                for(double i=start; i>finish ; i-=0.05) {
                    double x = i * sin(i) / 5;
                    double y = i * cos(i) / 5;
                    double z = i;
                    double xr = player.getLocation().getX() + cos(toRadians(angle))*x + sin(toRadians(angle))*z;
                    double yr = 1 + player.getLocation().getY() + y;
                    double zr = player.getLocation().getZ() + -sin(toRadians(angle))*x + cos(toRadians(angle))*z;

                    Location rotation = new Location(player.getWorld(), xr, yr, zr);
                    world.spawnParticle(element,rotation,0,0,0,0,yamiDO);
                }
                start = finish;
                finish = finish -  2* PI*5/10;
                if(finish < 0) this.cancel();
            }
        }.runTaskTimer(plugin,0,1);
    }

    /**
     * @brief ANIMATION: spiral animation for repealing.
     * @param player User that uses the ability.
     * @author RedRiotTank.
     */
    public void repealAnimation(Player player) {
        new BukkitRunnable() {
            final double angle = -player.getLocation().getYaw();
            final World world = player.getWorld();
            double start = 0;
            double finish = 0 + 2* PI*5/10;

            @Override
            public void run() {
                for(double i = start; i < finish ; i += 0.05) {
                    double x = i * sin(i) / 5;
                    double y = i * cos(i) / 5;
                    double z = i;

                    double xr = player.getLocation().getX() + cos(toRadians(angle)) * x + sin(toRadians(angle)) * z;
                    double yr = 1 + player.getLocation().getY() + y;
                    double zr = player.getLocation().getZ() + -sin(toRadians(angle)) * x + cos(toRadians(angle)) * z;

                    Location rotation = new Location(player.getWorld(), xr, yr, zr);
                    world.spawnParticle(element, rotation, 0, 0, 0, 0, yamiDO);
                }
                start = finish;
                finish = finish + 2* PI*5/10;

                if(finish < 0) this.cancel();
            }
        }.runTaskTimer(plugin,0,1);
    }

    /**
     * @brief Attracts a designed entity to a player's position.
     * @param ent Entity that's going to be moved.
     * @param player Player to which the entity is to be drawn.
     * @todo refactor it in the multiple biblio (zushi also uses this).
     * @author RedRiotTank
     */
    public void livingVoidForEntity(Entity ent, Player player) {
        BukkitTask attract = new BukkitRunnable() {
            Vector FirstVector;
            boolean fV = false;
            boolean entityInHand = false;
            double vx,vy,vz;
            @Override
            public void run() {
                if(ent.isDead() || player.isDead())
                    this.cancel();

                vx =  player.getLocation().getX() - ent.getLocation().getX();
                vy =  player.getLocation().getY() - ent.getLocation().getY();
                vz =  player.getLocation().getZ() - ent.getLocation().getZ();

                Vector movement = player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize();

                if(!fV) {
                    FirstVector = movement.clone();
                    fV = true;
                }

                //Para levantar al mob si hay desnivel
                if(player.getLocation().getY() >= ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                ent.setVelocity(movement);

                if(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2) +  Math.pow(vz,2)) <= 1){
                    entityInHand = true;
                    if(player.isSneaking()){
                        this.cancel();
                        repealEntity(ent,player);
                    }
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    /**
     * @brief repeals a designed entity to a player's position in the players looking direction.
     * @param ent Entity that's going to be moved.
     * @param player Player to which the entity is to be repealed.
     * @todo refactor it in the multiple biblio (zushi and goru also uses this).
     * @author RedRiotTank
     */
    public void repealEntity(Entity ent, Player player) {
        repealAnimationCounter++;

        if(repealAnimationCounter == 1) {
            repealAnimation(player);
        }

        ent.getWorld().playSound(ent.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,2);

        Vector dir = player.getLocation().getDirection();
        dir.setY(1);
        dir.setX(dir.getX() % 5);
        dir.setZ(dir.getZ() % 5);
        ent.setVelocity(dir);
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------

    /**
     * @brief Ability 4 caller: "Void meteore".
     * @see yami_yami#voidMeteore(Player)
     * @author RedRiotTank.
     */
    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            voidMeteore(user.getPlayer());
            abilitiesCD.set(3, 20); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: sends a black particle meteor to the location player is looking.
     * @param player Player who launchs the meteor.
     * @todo refactor to multi biblio.
     * @author RedRiotTank.
     */
    public void voidMeteore(Player player) {
        World world = player.getWorld();
        new BukkitRunnable() {
            double x, y, z, xr, yr, zr;
            Location startPosition = player.getLocation().add(0,3,0);

            Vector direction = new Vector(0,0,0);

            Vector facingDirection = player.getLocation().getDirection();
            double sumX = facingDirection.getX();
            double sumY = facingDirection.getY();
            double sumZ = facingDirection.getZ();

            int ticks = 0;

            int numplarticles = 0;

            Location spawnBallPos;
            @Override
            public void run() {
                ticks++;
                numplarticles = 0;

                for (double i = 0; i < 2*PI*4; i+=0.05) {
                    x = sin(i/10*PI);
                    y = sin((i%10)*PI) * cos(i/10*PI);
                    z =cos((i%10)*PI)*cos(i/10*PI);

                    xr = startPosition.getX() + x ;
                    yr = startPosition.getY() + y ;
                    zr = startPosition.getZ() + z ;

                    spawnBallPos = new Location(player.getWorld(), xr,yr,zr);

                    spawnBallPos.add(direction);
                    if(spawnBallPos.getBlock().getType().equals(Material.AIR)) {
                        world.spawnParticle(element, spawnBallPos, 0, 0, 0, 0, yamiDO);
                        numplarticles++;
                    }

                }

                for(Entity ent : world.getNearbyEntities(spawnBallPos,2,2,2))
                    if(ent instanceof LivingEntity && !player.equals(ent))
                        ((LivingEntity)ent).damage(10, (Entity) user.getPlayer());

                direction.add(new Vector(sumX,sumY,sumZ));

                if(numplarticles < 25 || ticks > 50) {
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin,0,1);
    }


    public void onEntityDamage(EntityDamageEvent event) {
        super.superOnEntityDamage(event);
        Player player;

        if(event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)event).getDamager() instanceof Player){
            Player damager = (Player) ((EntityDamageByEntityEvent)event).getDamager();
            if(OPhabs.users.containsKey(damager.getName()) && OPhabs.users.get(damager.getName()).hasHaki())
                return;
        }

        if(active){
            if (event.getEntity() instanceof Player) {
                player = (Player) event.getEntity();
                if (!(player.getLocation().getBlock().isLiquid()) && (castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)) || castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)) {
                    event.setCancelled(true);
                    player.getWorld().spawnParticle(element,player.getLocation(), 10, 0, 1, 0, 0.1,yamiDO);
                }
            }
        }
    }
}

