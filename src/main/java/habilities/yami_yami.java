package habilities;

import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Registry.MATERIAL;

public class yami_yami implements Listener {
    OPhabs plugin;
     final int radius = 3;
     final int maxRadiusAmplification = 5;
     final int voidExpansionDelay = 8;
     final int voidExpansionSpeed = 5;     //less is faster
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

    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Location downBlockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());

        if(downBlockLocation.getBlock().getType().equals(Material.BLACK_CONCRETE) ){
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
        perimeterPixel.getBlock().setType(Material.BLACK_CONCRETE);

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

}
