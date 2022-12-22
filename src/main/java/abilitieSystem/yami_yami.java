package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class yami_yami implements Listener {
    private OPhabs plugin;
     private final int
             radius = 3,
             maxRadiusAmplification = 9,
             voidExpansionDelay = 6,
             voidExpansionSpeed = 5, //less is faster
             damageAmount = 1,
             damageDelay = 0,
             damageSpeed = 15,
             dissappearVoidBlocksDelay = 90,
             maxHeight = 10;

    private final Material voidMaterial= Material.BLACK_CONCRETE;

    private List<Block> convertedToVoidBlocks = new ArrayList<>();
    public yami_yami(OPhabs plugin){this.plugin = plugin;}

    public void blackVoid(Player player) {
        player.getWorld().playSound(player, Sound.AMBIENT_CRIMSON_FOREST_MOOD, 10, 2);
        Location playerLocation = player.getLocation();

        bresenham(radius, playerLocation, true);

        BukkitTask circle = new BukkitRunnable() {
                int radiusCounter = radius;

                public void run() {
                    radiusCounter++;

                    bresenham(radiusCounter, playerLocation, true);
                    if (radiusCounter == radius + maxRadiusAmplification)
                        cancelTask();
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, voidExpansionDelay, voidExpansionSpeed);

            BukkitTask damage = new BukkitRunnable() {
                LivingEntity livEnt;

                public void run() {
                    List<LivingEntity> livingEntities = player.getWorld().getLivingEntities();

                    for(LivingEntity livEnt : player.getWorld().getLivingEntities()){
                        if (livEnt != player) {
                            Location downBlock = livEnt.getLocation();
                            downBlock.setY(downBlock.getBlockY() - 1);

                            if (downBlock.getBlock().getType().equals(voidMaterial))
                                    livEnt.damage(damageAmount);
                        }
                    }
                }
            }.runTaskTimer(plugin, damageDelay, damageSpeed);

            new BukkitRunnable() {
                @Override
                public void run() {
                    dissappearVoidBlocks();
                    damage.cancel();
                    player.getWorld().playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 2);
                }
            }.runTaskLater(plugin, dissappearVoidBlocksDelay);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Location downBlockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());

        if(downBlockLocation.getBlock().getType().equals(voidMaterial) )
            event.setCancelled(true);
    }

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

    public void setBlockAndFill(Location playerLocation, int x, int z,boolean fill){
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

    public void setBlockAndLineUP(Location perimeterPixel){
        World world = perimeterPixel.getWorld();

            Location downBlockLocation;
            boolean found = false;
            for(int i=0; i < 3 && !found; i++){
                downBlockLocation = new Location(perimeterPixel.getWorld(), perimeterPixel.getBlockX(),perimeterPixel.getBlockY() - i,perimeterPixel.getBlockZ());
                if( !downBlockLocation.getBlock().getType().equals(Material.AIR)){
                    found = true;
                    world.spawnParticle(Particle.SMOKE_NORMAL,downBlockLocation.getBlock().getRelative(0,-1,0).getLocation(),5);
                    downBlockLocation.getBlock().setType(voidMaterial);
                    convertedToVoidBlocks.add(downBlockLocation.getBlock());

                }
            }




        Location upBlockLocation = new Location(perimeterPixel.getWorld(), perimeterPixel.getBlockX(),perimeterPixel.getBlockY(),perimeterPixel.getBlockZ());
        for(int i=1; i<maxHeight; i++){
            upBlockLocation.setY(perimeterPixel.getBlockY() + i );

            if(!upBlockLocation.getBlock().getType().equals(Material.AIR)){
                Material matFallingBlock = upBlockLocation.getBlock().getType();
                upBlockLocation.getBlock().setType(Material.AIR);
                upBlockLocation.getWorld().spawnFallingBlock(upBlockLocation,matFallingBlock,(byte) 9);
            }

        }

    }

    public void dissappearVoidBlocks(){
        for(int i=0; i<convertedToVoidBlocks.size(); i++){
            convertedToVoidBlocks.get(i).setType(Material.AIR);
        }
    }

    public void livingVoid(Player player){

        player.playSound(player.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,0);
        World world = player.getWorld();

        double ajuste;
        double angle = -player.getLocation().getYaw();

        System.out.println(player.getLocation().getYaw());

        for(float i=0; i< 2*Math.PI*5; i+=0.01) {

            double x =  i*sin(i)/5;
            double y =  i*cos(i)/5;
            double z =  i;

            Location normal = new Location(player.getWorld(), x,y,z);

            double xr = player.getLocation().getX() + cos(toRadians(angle))*x + sin(Math.toRadians(angle))*z;
            double yr = player.getLocation().getY() + y;
            double zr = player.getLocation().getZ() + -sin(toRadians(angle))*x + cos(toRadians(angle))*z;

            Location rotation = new Location(player.getWorld(), xr,yr,zr);







            world.spawnParticle(Particle.FLAME,normal,0,0,0,0);
            world.spawnParticle(Particle.FLAME,rotation,0,0,0,0);

        }

        for(Entity ent : player.getNearbyEntities(10,10,10))
            if(ent instanceof LivingEntity && !player.equals(ent))
                livingVoidForEntity(ent,player);
    }

    public void livingVoidForEntity(Entity ent, Player player){
        World world = player.getWorld();

        BukkitTask attract = new BukkitRunnable(){
            Vector FirstVector;
            boolean fV = false;
            boolean entityInHand = false;
            @Override
            public void run() {
                if(ent.isDead() || player.isDead())
                    this.cancel();

                double vx,vy,vz;
                Location loc_aux ;

                loc_aux = ent.getLocation().clone().add(0,1,0);
                vx =  player.getLocation().getX() - ent.getLocation().getX();
                vy =  player.getLocation().getY()-ent.getLocation().getY();
                vz =  player.getLocation().getZ()-ent.getLocation().getZ();




                for(float i=0; i<1 && !ent.isDead(); i+=0.1) {
                    //world.spawnParticle(Particle.SMOKE_NORMAL,loc_aux.add(vx/5,vy/5,vz/5),0,0,0,0);
                    //world.spawnParticle(Particle.SMOKE_NORMAL,loc_aux.add(vx/5,vy/5,vz/5),1);
                    //Location loc = new Location(player.getWorld(), player.getLocation().getX() + i*sin(i),player.getLocation().getY() + 1 + i*cos(i),player.getLocation().getZ() + i);
                   // world.spawnParticle(Particle.FLAME,loc,0,0,0,0);

                }

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
                    if(player.isSneaking()){
                        this.cancel();
                        repealEntity(FirstVector,ent,player);
                    }
                }


            }


        }.runTaskTimer(plugin,0,1);

    }

    public void repealEntity(Vector direction, Entity ent, Player player){
        direction.setX(4*(direction.getX() * -1));
        direction.setZ(4*(direction.getZ() * -1));
        direction.setY(1);

        ent.getWorld().playSound(ent.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,2);


        /*
        new BukkitRunnable(){
            boolean isInGround = true;
            @Override
            public void run() {
                for(int i=0; i<100; i++) {
                    ent.getWorld().spawnParticle(Particle.ASH, ent.getLocation(), 0, 0, 0, 0);

                }

                if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR))
                    isInGround = false;

                if((!ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR) && !isInGround)|| ent.isDead()){
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin,0,1);

        */

        //ent.setVelocity(direction);
        Vector v = new Vector(1,1,1);


        Vector dir = player.getLocation().getDirection();

        dir.setY(1);
        dir.setX(dir.getX() % 5);
        dir.setZ(dir.getZ() % 5);

        System.out.println(dir);

        ent.setVelocity(dir);
    }


}
