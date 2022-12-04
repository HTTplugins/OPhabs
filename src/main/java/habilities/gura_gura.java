package habilities;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import java.util.ArrayList;

public class gura_gura implements Listener {

    final int waveDistance=8, radiusFloor = 6, radiusWall=3;
    final Material AIR = Material.AIR;

    public void createWaveEffect(Player player) {
        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();
        currentPL.setY(currentPL.getY() + 1);
        ArrayList<Location> blocks = new ArrayList<Location>();


        for (int i = 0; i < waveDistance; i++) {
            blocks.add(currentPL.add(direction));
            blocks.addAll(positions(currentPL, direction));
        }

            for (Location b : blocks) {
                ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();
                for (Entity entity : b.getWorld().getNearbyEntities(b, 1, 1, 1)) {
                    if (entity instanceof LivingEntity) {
                        entities.add((LivingEntity) entity);
                    }
                }
                if (b.getBlock().getType() != AIR)
                    if(b.getBlock().getType().getHardness() < Material.STONE.getHardness())
                        b.getBlock().breakNaturally();

                if(b.getBlock().getType() == AIR)
                    b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b, 3, Material.GLASS_PANE.createBlockData());
                else
                    b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b, 4, b.getBlock().getType().createBlockData());

                //Damage all entities in the area of the wave
                for (LivingEntity entity : entities) {
                    if(entity != player)
                        entity.damage(9);
                }
            }

        }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if(player.getItemInHand().getType() == Material.STICK){
            createWaveEffect(player);
            Bukkit.getConsoleSender().sendMessage("Hability lunch");

        }
    }

    //Create a wall radius x radius of blocks in the direction of the player
    public ArrayList<Location> positions(Location loc, Vector direction) {
        //if faces north
        ArrayList<Location> blocks = new ArrayList<Location>();

        if (direction.getX() != 1) {
            blocks.add(loc.clone().add(1, 1, 0));
            blocks.add(loc.clone().add(0, 1, 0));
            blocks.add(loc.clone().add(-1, 1, 0));
            blocks.add(loc.clone().add(1, 0, 0));
            blocks.add(loc.clone().add(-1, 0, 0));
            blocks.add(loc.clone().add(1, -1, 0));
            blocks.add(loc.clone().add(0, -1, 0));
            blocks.add(loc.clone().add(-1, -1, 0));
        }
        //if faces east
        if (direction.getX() != 0) {
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
    public ArrayList<Location> bresenham(int radius, Location center){
        /*
        Bresenham algorithm modification.
        radius must be 2n+1 with 1<=n.
        */
        ArrayList<Location> blocks = new ArrayList<Location>();
        int x,z,d;

        x=0;
        z=radius;
        d = 3-2*radius;

        while(x < z){

            if(d < 0)
                d=d+4*x+6;
            else {
                d=d+4*(x-z) + 10;
                z--;
            }
            x++;

        blocks.add(center.clone().add(x,0,z));
        }
        return blocks;
    }

}

