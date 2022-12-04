package habilities;


import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.util.Vector;
import java.util.ArrayList;

public class gura_gura implements Listener {

    final int waveDistance = 8, radiusFloor = 4, radiusWall = 3;
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
                } else if (entity instanceof Projectile) {
                    entity.setVelocity(new Vector(0,0,0));
                }
            }
            if (b.getBlock().getType() != AIR) {
                if (b.getBlock().getType().getHardness() < Material.DIRT.getHardness())
                    b.getBlock().breakNaturally();
                else
                    if (b.getBlock().getType().getHardness() <= Material.GRASS_BLOCK.getHardness()) {
                        if (b.getBlock().getType() == Material.GRASS_BLOCK)
                            b.getBlock().setType(Material.DIRT);
                        Random rand = new Random(); //instance of random class
                        int int_random = rand.nextInt(15);
                        if (int_random == 1)
                            b.getBlock().breakNaturally();
                    }
            }

            if (b.getBlock().getType() == AIR) {
                Random rand = new Random(); //instance of random class
                int int_random = rand.nextInt(15);
                if (int_random == 1)
                    b.getWorld().spawnParticle(Particle.SONIC_BOOM, b, 1); //, Material.GLASS_PANE.createBlockData());
            }
            else
                b.getWorld().spawnParticle(Particle.BLOCK_DUST, b.clone().add(0,1,0), 4, b.getBlock().getType().createBlockData());

            //Damage all entities in the area of the wave
            for (LivingEntity entity : entities) {
                if (entity != player)
                    entity.damage(9);
            }
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.STICK) {
            if(player.isSneaking())
                earthquake(player);
            else
                createWaveEffect(player);
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


    public void earthquake(Player player) {
        Location currentPL = player.getLocation(), loc = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor);
        ArrayList<Location> blocks = new ArrayList<>();
        Location pos1 = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor), pos2=currentPL.clone().add(radiusFloor, radiusFloor, radiusFloor);


        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());


        System.out.println(x1+","+y1+","+z1+" : "+x2+","+y2+","+z2);
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    blocks.add(new Location(player.getWorld(),x,y,z));
                }
            }
        }

        for(Location block : blocks){
            ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();
            for (Entity entity : block.getWorld().getNearbyEntities(block, 1, 1, 1)) {
                if (entity instanceof LivingEntity) {
                    entities.add((LivingEntity) entity);
                }else if (entity instanceof Projectile) {
                    entity.setVelocity(new Vector(0,0,0));
                }
            }
            if (block.getBlock().getType() != AIR) {
                if (block.getBlock().getType().getHardness() < Material.GRASS_BLOCK.getHardness())
                    block.getBlock().breakNaturally();
                else {
                    if (block.getBlock().getType().getHardness() <= Material.STONE.getHardness()) {
                        Random rand = new Random(); //instance of random class
                        int int_random = rand.nextInt(15);
                        if (int_random == 1)
                            block.getBlock().breakNaturally();
                    }
                }
                block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.clone().add(0,1,0), 30, block.getBlock().getType().createBlockData());
            }

            //Damage all entities in the area of the wave
            for (LivingEntity entity : entities) {
                if (entity != player)
                    entity.damage(2);
            }
        }
    }
}
