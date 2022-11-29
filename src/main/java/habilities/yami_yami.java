package habilities;

import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.graalvm.compiler.lir.dfa.LocationMarkerPhase;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Registry.MATERIAL;

public class yami_yami implements Listener {
    OPhabs plugin;
     final int radius = 3;
     final int maxRadiusAmplification = 9;
     final int voidExpansionDelay = 6;
     final int voidExpansionSpeed = 5;     //less is faster

    final int damageAmount = 1;

    final int damageDelay = 0;
    final int damageSpeed = 15;

    final Material voidMaterial= Material.BLACK_CONCRETE;

    List<Block> convertedToVoidBlocks = new ArrayList<>();
    final int dissappearVoidBlocksDelay = 160;
    public yami_yami(OPhabs plugin){this.plugin = plugin;}

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Location playerLocation = event.getPlayer().getLocation();

        bresenham(radius,playerLocation, true);

        new BukkitRunnable(){
            int radiusCounter = radius;

            public void run(){
                radiusCounter++;

                bresenham(radiusCounter, playerLocation, true);
                if(radiusCounter == radius + maxRadiusAmplification)
                    cancelTask();
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin,voidExpansionDelay,voidExpansionSpeed);

        new BukkitRunnable(){
            Player player;
            LivingEntity livEnt;

            public void run(){

                List<LivingEntity> livingEntities = event.getPlayer().getWorld().getLivingEntities();

                for(int i=0; i < livingEntities.size(); i++){
                    livEnt = livingEntities.get(i);
                    if( !event.getPlayer().equals(livEnt)){
                        Location downBlock = livEnt.getLocation();
                        downBlock.setY(downBlock.getBlockY() - 1);

                        if (downBlock.getBlock().getType().equals(voidMaterial))
                            livEnt.damage(damageAmount);
                    }
                }

            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }

        }.runTaskTimer(plugin,damageDelay,damageSpeed);

        new BukkitRunnable(){

            @Override
            public void run() {
                dissappearVoidBlocks();
            }
        }.runTaskLater(plugin,dissappearVoidBlocksDelay);



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

            Location downBlockLocation;
            boolean found = false;
            for(int i=0; i < 3 && !found; i++){
                downBlockLocation = new Location(perimeterPixel.getWorld(), perimeterPixel.getBlockX(),perimeterPixel.getBlockY() - i,perimeterPixel.getBlockZ());
                if( !downBlockLocation.getBlock().getType().equals(Material.AIR)){
                    found = true;
                    downBlockLocation.getBlock().setType(voidMaterial);
                    convertedToVoidBlocks.add(downBlockLocation.getBlock());

                }
            }




        Location upBlockLocation = new Location(perimeterPixel.getWorld(), perimeterPixel.getBlockX(),perimeterPixel.getBlockY(),perimeterPixel.getBlockZ());
        for(int i=1; i<30; i++){
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

}
