package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import fruitSystem.fruitIdentification;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class zushi_zushi extends paramecia{

    private final List<Material> materials = new ArrayList<>();


    private static boolean heavyActivated;

    private boolean exploded = false;

    private static List<Entity> heavyEntity = null;
    private static List<Player> togglePlayer = new ArrayList<>();
    private final Random random = new Random();


    public zushi_zushi(OPhabs plugin){
        super(plugin, castIdentification.castMaterialZushi,castIdentification.castItemNameZushi,fruitIdentification.fruitCommandNameZushi);
        abilitiesNames.add("Heavy Field");
        abilitiesCD.add(0);
        abilitiesNames.add("Meteor");
        abilitiesCD.add(0);
        abilitiesNames.add("Attraction");
        abilitiesCD.add(0);
        abilitiesNames.add("flyRock");
        abilitiesCD.add(0);


        //Meteor Materials initialization:
        materials.add(Material.COBBLESTONE);
        materials.add(Material.MAGMA_BLOCK);
        materials.add(Material.NETHERRACK);
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            heavy(user.getPlayer());
            abilitiesCD.set(0, 25); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            meteor(user.getPlayer());
            abilitiesCD.set(1, 50); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            attraction(user.getPlayer());
            abilitiesCD.set(2, 40); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            flyRock(user.getPlayer());
            abilitiesCD.set(3, 60); // Pon el cooldown en segundos
        }
    }

    public void heavy(Player player){

        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        world.playSound(playerLoc,"heavygravity",1,1);
        new BukkitRunnable(){
            int ticks = 0;
            Random random = new Random();
            World world = player.getWorld();
            Location playerLoc = player.getLocation();
            double x;
            double z;
            @Override
            public void run() {
                ticks++;

                for(int i = 0; i < 30; i++){
                    x = (random.nextDouble() * 19.98) - 9.99 + playerLoc.getX();
                    z = (random.nextDouble() * 19.98) - 9.99 + playerLoc.getZ();
                    double y = searchGround(x,z,playerLoc.getY(),world);
                    world.spawnParticle(Particle.SPELL_WITCH,new Location(world,x,y, z),2);
                }

                for(int i = 0; i < 60; i++){
                    x = (random.nextDouble() * 19.98) - 9.99 + playerLoc.getX();
                    z = (random.nextDouble() * 19.98) - 9.99 + playerLoc.getZ();
                    double y = searchGround(x,z,playerLoc.getY(),world);
                    world.spawnParticle(Particle.CRIT_MAGIC,new Location(world,x,y + 0.5,z),0,0,-0.3,0);
                }
                if(ticks == 100) this.cancel();


            }
        }.runTaskTimer(plugin,0,1);

        heavyEntity = player.getNearbyEntities(20,50,20);

        for(Entity ent : heavyEntity){
            if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR)){
                ent.setVelocity(new Vector(0,-50,0));
            }

        }

        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {


                for(Entity ent : heavyEntity){
                    if(ent.getLocation().getY() == world.getHighestBlockAt(ent.getLocation()).getY()+1
                            && !togglePlayer.contains(ent)
                            && ent instanceof Player){

                        togglePlayer.add((Player) ent);
                        Location entiLoc = ent.getLocation();
                        (ent).teleport(new Location(world,entiLoc.getX(),entiLoc.getY(),entiLoc.getZ(),entiLoc.getYaw(),-10));
                        ((Player)ent).setGliding(true);

                    } else if(ent instanceof LivingEntity){
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10,10));
                    }
                }

                if(ticks == 100){
                    togglePlayer.clear();
                    heavyEntity.clear();

                    this.cancel();

                }

                ticks++;

            }
        }.runTaskTimer(plugin,0,1);
    }

    public static void onEntityToggleGlide(EntityToggleGlideEvent event){
        if(togglePlayer.contains((Player)(event.getEntity())))
            event.setCancelled(true);
    }

    public static void onPlayerMove(PlayerMoveEvent event){

        if(togglePlayer.contains(event.getPlayer())){
            event.setCancelled(true);
        }








    }

    public void meteor(Player player){
        World world = player.getWorld();
        Location end = getTargetBlock(user.getPlayer(), 40);
        Location playerLoc = player.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                world.playSound(playerLoc,"magneticfield",1,-1);
            }
        }.runTaskLater(plugin,10);


        new BukkitRunnable(){
            public final Particle.DustOptions purple = new Particle.DustOptions(Color.PURPLE,1.0F);

            final double radius = 1.5;
            double x,y = 2,z;
            int ticks = 0;

            final Location playerLoc = player.getLocation();
            @Override
            public void run() {

                if(ticks < 100)
                    circle(playerLoc,y);
                else if(ticks % 2 == 0 && ticks < 190){
                    circle(playerLoc,y + 0.5);
                    circle(playerLoc,y+1);
                    y++;
                }

                if(ticks == 155){
                    Location start = playerLoc.clone();
                    start.setY(playerLoc.getY() + 50);
                    launchMeteore(start,end,5);
                }

                if(ticks == 191)
                    this.cancel();

                ticks++;
            }

            public void circle(Location loc,double y){
                for(double i=0; i<2*PI; i+=0.1){
                    x = radius * cos(i);
                    z = radius * sin(i);

                    loc.add(x,y,z);
                    world.spawnParticle(Particle.REDSTONE,loc,0,0,0,0,purple);
                    loc.subtract(x,y,z);
                }

            }
        }.runTaskTimer(plugin,0,1);

    }

    public void launchMeteore(Location start, Location end, int radius) {
        World world = start.getWorld();
        exploded = false;
        world.playSound(start,"flymeteor",10,1);

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++)
                    if (x * x + y * y + z * z <= 25) {
                        Location blockLocation = start.clone().add(x, y, z);
                        Material material = materials.get(random.nextInt(materials.size()));
                        FallingBlock fallingBlock = world.spawnFallingBlock(blockLocation,material.createBlockData());
                        Vector vector = end.toVector().subtract(blockLocation.toVector());
                        fallingBlock.setVelocity(vector.multiply(0.025));
                        fallingBlock.setDropItem(false);

                        new BukkitRunnable(){   //Check explosion
                            @Override
                            public void run() {
                                if(exploded) this.cancel();

                                if (fallingBlock.isDead() && !exploded) {
                                    world.createExplosion(fallingBlock.getLocation(), 15);
                                    exploded = true;
                                }
                            }
                        }.runTaskTimer(plugin,0,1);
                    }
    }

    public Location getTargetBlock(Player player, int range) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location current = player.getEyeLocation();

        for (int i = 0; i < range; i++) {
            current = current.add(direction);

            if (current.getBlock().getType().isSolid())
                return current;
        }

        return current;
    }

    public double searchGround(double x, double z, double initialY, World world){

        Location loc = new Location(world,x,initialY,z);

        for(int i=0; i < 40; i++)
            if(loc.getBlock().getRelative(0,-i,0).getType().isSolid())
                return initialY - i + 1;

        return initialY - 40;
    }

    public void attraction(Player player){

        Location playerLoc = player.getLocation();
        World world = player.getWorld();
        double iniY = playerLoc.getY();

        world.playSound(playerLoc,"magneticfield",1,1);

        for(Entity ent : player.getNearbyEntities(30,30,30))
            if(ent instanceof LivingEntity && !player.equals(ent))
                attractEntitie(ent,player);

        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                if (ticks==5)   this.cancel();
                for(int i=0; i<10; i++){
                    double x = random.nextInt(36) - 15 + playerLoc.getX();
                    double z = random.nextInt(36) - 15 + playerLoc.getZ();
                    Location centerLoc = new Location(world,x,iniY,z);
                    groundCircularWave(centerLoc);

                }

            }
        }.runTaskTimer(plugin,0,10);

    }

    public void groundCircularWave(Location center){

        new BukkitRunnable(){
            double radius = 1;
            double incrementRate = 0.25;
            @Override
            public void run() {
                circle(radius,center,  incrementRate);
                radius += 0.5;
                incrementRate = incrementRate - incrementRate/5;
                if (radius > 5) this.cancel();

            }

            public void circle(double radius, Location loc,double incrementRate){

                World world = loc.getWorld();

                for(double i=0; i<2*PI; i+=incrementRate){
                    double xParticle = radius * Math.cos(i) + loc.getX();
                    double zParticle = radius * Math.sin(i) + loc.getZ();
                    double yParticle = searchGround(xParticle,zParticle, loc.getY(), world);

                    Location particleLocation = new Location(world,xParticle,yParticle,zParticle);

                    world.spawnParticle(Particle.SPELL_WITCH,particleLocation,0,0,0,0);
                }

            }
        }.runTaskTimer(plugin,0,1);

    }

    public void attractEntitie(Entity ent, Player player){
        BukkitTask attract = new BukkitRunnable(){
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

                if(!fV){
                    FirstVector = movement.clone();
                    fV = true;
                }

                //Para levantar al mob si hay desnivel
                if(player.getLocation().getY() >= ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                ent.setVelocity(movement);

                if(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2) +  Math.pow(vz,2)) <= 1){
                    entityInHand = true;
                    this.cancel();
                    ent.setVelocity(new Vector(0,0,0));

                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    public void flyRock(Player player){
        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        double x = playerLoc.getX();
        double z = playerLoc.getZ();
        double y = playerLoc.getY();

        world.playSound(playerLoc, "rockmove",1,1);

        player.setAllowFlight(true);

        double yCenter = searchGround(x, z, y, world) - 1;
        Location center = new Location(world,x,yCenter, z);
        Material centerMat = center.getBlock().getType();

        double yDownCenter = searchGround(x, z, y-1, world) - 1;
        Location downcenter = new Location(world,x,yDownCenter-1, z);
        Material downcenterMat = downcenter.getBlock().getType();

        double yUP = searchGround(x+1, z, y, world) - 1;
        Location up = new Location(world,x+1,yUP, z);
        Material upMat = up.getBlock().getType();

        double yDown = searchGround(x-1, z, y, world) - 1;
        Location down = new Location(world,x-1,yDown, z);
        Material downMat = down.getBlock().getType();

        double yLeft = searchGround(x, z+1, y, world) - 1;
        Location left = new Location(world,x,yLeft, z+1);
        Material leftMat = left.getBlock().getType();

        double yRight = searchGround(x, z-1, y, world) - 1;
        Location right = new Location(world,x,yRight, z-1);
        Material rightMat = right.getBlock().getType();

        new BukkitRunnable(){
            Block previusCenterBlock = null, previusup = null, previusdown= null, previusleft = null, previusRight = null, previusDownCenterBlock = null;
            boolean firstiteration = true;

            int ticks = 0;
            @Override
            public void run() {
                ticks++;

                Block centerBlock = player.getLocation().getBlock().getRelative(0,-1,0);
                Block downcenterBlock = player.getLocation().getBlock().getRelative(0,-2,0);
                Block upBlock = player.getLocation().getBlock().getRelative(1,-1,0);
                Block downBlock = player.getLocation().getBlock().getRelative(-1,-1,0);
                Block leftBlock = player.getLocation().getBlock().getRelative(0,-1,1);
                Block rightBlock = player.getLocation().getBlock().getRelative(0,-1,-1);

                if(!firstiteration){

                        quitBlock(previusCenterBlock,centerBlock);
                        quitBlock(previusup,upBlock);
                        quitBlock(previusdown,downBlock);
                        quitBlock(previusleft,leftBlock);
                        quitBlock(previusRight,rightBlock);
                        quitBlock(previusDownCenterBlock,downcenterBlock);


                            previusCenterBlock.getRelative(0,-1,0).setType(Material.AIR);
                            previusup.getRelative(0,-1,0).setType(Material.AIR);
                            previusdown.getRelative(0,-1,0).setType(Material.AIR);
                            previusleft.getRelative(0,-1,0).setType(Material.AIR);
                            previusRight.getRelative(0,-1,0).setType(Material.AIR);
                            previusDownCenterBlock.getRelative(0,-1,0).setType(Material.AIR);



                } else
                    firstiteration = false;

                if(!player.isSneaking()) {
                    setBlock(centerBlock, centerMat);
                    setBlock(downcenterBlock, downcenterMat);
                    setBlock(upBlock, upMat);
                    setBlock(downBlock, downMat);
                    setBlock(leftBlock, leftMat);
                    setBlock(rightBlock, rightMat);
                } else {

                    centerBlock.setType(Material.AIR);
                    downcenterBlock.setType(Material.AIR);
                    upBlock.setType(Material.AIR);
                    downBlock.setType(Material.AIR);
                    leftBlock.setType(Material.AIR);
                    rightBlock.setType(Material.AIR);

                    centerBlock.getRelative(0,-1,0).setType(centerMat);
                    downcenterBlock.getRelative(0,-1,0).setType(downcenterMat);
                    upBlock.getRelative(0,-1,0).setType(upMat);
                    downBlock.getRelative(0,-1,0).setType(downMat);
                    leftBlock.getRelative(0,-1,0).setType(leftMat);
                    rightBlock.getRelative(0,-1,0).setType(rightMat);


                }

                previusCenterBlock = centerBlock;
                previusDownCenterBlock = downcenterBlock;
                previusup = upBlock;
                previusdown = downBlock;
                previusleft = leftBlock;
                previusRight = rightBlock;


                if(ticks > 500){
                    player.setAllowFlight(false);
                    centerBlock.setType(Material.AIR);
                    downcenterBlock.setType(Material.AIR);
                    upBlock.setType(Material.AIR);
                    downBlock.setType(Material.AIR);
                    leftBlock.setType(Material.AIR);
                    rightBlock.setType(Material.AIR);

                    world.spawnFallingBlock(downcenterBlock.getLocation(),downcenterMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(centerBlock.getLocation(),centerMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(upBlock.getLocation(),upMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(downBlock.getLocation(),downMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(leftBlock.getLocation(),leftMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(rightBlock.getLocation(),rightMat.createBlockData()).setVelocity(new Vector(0,-2,0));

                    this.cancel();
                }


            }

            public void setBlock(Block block, Material material){
                if(!block.getType().isSolid())
                    block.setType(material);
            }
            
            public void quitBlock(Block previusBlock, Block block){
                if(previusBlock != block)
                    previusBlock.setType(Material.AIR);
            }
        }.runTaskTimer(plugin,0,1);
    }




}
