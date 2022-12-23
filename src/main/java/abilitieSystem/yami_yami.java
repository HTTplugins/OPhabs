package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
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
import java.util.Stack;

import static java.lang.Math.*;

public class yami_yami implements Listener {
    private final OPhabs plugin;
    private int repealAnimationCounter = 0;

     private final int
             radius = 3;
    private final int maxRadiusAmplification = 9;
    private final int damageAmount = 1;

    private final Material voidMaterial= Material.BLACK_CONCRETE;

    private Stack<Material> absorbedMaterials = new Stack<>();

    private List<Block> convertedToVoidBlocks = new ArrayList<>();
    public yami_yami(OPhabs plugin){this.plugin = plugin;}

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
                            livEnt.damage(damageAmount);


                    for(double x = playerLocation.getX() - maxRadiusAmplification; x < playerLocation.getX() + maxRadiusAmplification; x++ ){
                        for(double z = playerLocation.getZ() - maxRadiusAmplification; z < playerLocation.getZ() + maxRadiusAmplification; z++ ){
                            boolean foundY=false;
                            for(double y = playerLocation.getBlockY() - 1;  y > playerLocation.getBlockY() - 3 && !foundY; y--){
                                Block block = new Location(player.getWorld(), x,y,z).getBlock();
                                if(block.getType().equals(voidMaterial))
                                    player.getWorld().spawnParticle(abilitiesIdentification.yamiParticle,block.getRelative(0,1,0).getLocation(),0,0,1,0,abilitiesIdentification.yamiDO);
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

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getBlock().getRelative(0,-1,0).getType().equals(voidMaterial)) {

           // absorbedMaterials.add(event.getBlock().getType());
            //System.out.println(event.getBlock().getType());
            event.setCancelled(true);
        }
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

    public void dissappearVoidBlocks(){
        for(Block convertedTVblock : convertedToVoidBlocks)
            convertedTVblock.setType(Material.AIR);

    }

    public void livingVoid(Player player){

        player.playSound(player.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,0);
        World world = player.getWorld();

        absorbAnimation(player);

        for(Entity ent : player.getNearbyEntities(10,10,10))
            if(ent instanceof LivingEntity && !player.equals(ent))
                livingVoidForEntity(ent,player);

        repealAnimationCounter = 0;
    }

    public void absorbAnimation(Player player){
        new BukkitRunnable(){
            double angle = -player.getLocation().getYaw();
            World world = player.getWorld();

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

                    Location rotation = new Location(player.getWorld(), xr,yr,zr);



                    world.spawnParticle(abilitiesIdentification.yamiParticle,rotation,0,0,0,0,abilitiesIdentification.yamiDO);

                }
                start = finish;

                finish = finish -  2* PI*5/10;


                if(finish < 0) this.cancel();

            }
        }.runTaskTimer(plugin,0,1);
    }

    public void repealAnimation(Player player){

        new BukkitRunnable(){
            final double angle = -player.getLocation().getYaw();
            World world = player.getWorld();

            double start = 0;
            double finish = 0 + 2* PI*5/10;
            @Override
            public void run() {

                for(double i=start; i<finish ; i+=0.05) {

                    double x = i * sin(i) / 5;
                    double y = i * cos(i) / 5;
                    double z = i;

                    double xr = player.getLocation().getX() + cos(toRadians(angle))*x + sin(toRadians(angle))*z;
                    double yr = 1 + player.getLocation().getY() + y;
                    double zr = player.getLocation().getZ() + -sin(toRadians(angle))*x + cos(toRadians(angle))*z;

                    Location rotation = new Location(player.getWorld(), xr,yr,zr);



                    world.spawnParticle(abilitiesIdentification.yamiParticle,rotation,0,0,0,0,abilitiesIdentification.yamiDO);

                }
                start = finish;

                finish = finish +  2* PI*5/10;


                if(finish < 0) this.cancel();

            }
        }.runTaskTimer(plugin,0,1);

    }
    public void livingVoidForEntity(Entity ent, Player player){

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
                    if(player.isSneaking()){
                        this.cancel();
                        repealEntity(ent,player);
                    }
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    public void repealEntity(Entity ent, Player player ){
        repealAnimationCounter++;
        World world = player.getWorld();

        if(repealAnimationCounter == 1){
            repealAnimation(player);
        }

        ent.getWorld().playSound(ent.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,2);

        Vector dir = player.getLocation().getDirection();
        dir.setY(1);
        dir.setX(dir.getX() % 5);
        dir.setZ(dir.getZ() % 5);
        ent.setVelocity(dir);

    }
    public void liberateAbsorptions(Player player){

        new BukkitRunnable(){
            int tickCounter = 0;
            @Override
            public void run() {
                tickCounter++;
                repealAnimation(player);
                if(tickCounter == 3) this.cancel();
            }
        }.runTaskTimer(plugin,0,20);
        
        new BukkitRunnable(){
            int i = 0;

            World world = player.getWorld();

            Material usedMAterial;
            @Override
            public void run() {

                if(!absorbedMaterials.empty()){
                    usedMAterial = absorbedMaterials.pop();

                    FallingBlock block = world.spawnFallingBlock(player.getLocation().add(player.getLocation().getBlock().getRelative(0,1,0).getLocation().getDirection()),usedMAterial.createBlockData());

                    block.setVelocity(player.getLocation().getDirection().add(new Vector(0,0.5,0)));
                }

                i++;

                if(i == 10) this.cancel();
            }
        }.runTaskTimer(plugin,0,4);
    }
}
