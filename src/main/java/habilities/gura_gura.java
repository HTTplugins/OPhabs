package habilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Player;
import org.bukkit.event.entity.PlayerInteractEvent;
import org.bukkit.event.World;

public class gura_gura implements Listener {

    final float waveDistance=8, radiusFloor = 6, radiusWall=2;
    final Material NETHERRACK = Material.NETHERRACK;
    final Material AIR = Material.AIR;
    final Material GREEN_DIRT = Material.GRASS_BLOCK;

    public void createWaveEffect(Player player){
        Location currentPL = player.getLocation();
        direction = player.getEyeLocation().getDirection();
        currentPL=currentPL.setY(currentPL.getY+1);
        List<Location> blocks = new ArrayList<Location>();

        for(int i=0;i<waveDistance;i++){
            blocks.add(currentPL.add(direction));
            blocks.addAll(bresenham(radiusWall,blocks.end(),currentPL.add(direction)));
        }

    for(Location block : blocks){
            if(block.getBlock().getType() == AIR){
                block.getWorld().spawnParticle(Particle.CLOUD, block, 100);
            }else{
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block, 100, new MaterialData(block.getBlock().getType()));
            }
        }

    }
    

    public List bresenham(int radius, Location center){
        /*
        Bresenham algorithm modification.
        radius must be 2n+1 with 1<=n.
        */
        List<Block> blocks = new ArrayList<Block>();
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
        blocks.add(center.getBlock().getRelative(x,0,z));
        }
        return blocks;
    }

}

