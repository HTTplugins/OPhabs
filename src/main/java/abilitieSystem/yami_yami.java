package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

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

}
