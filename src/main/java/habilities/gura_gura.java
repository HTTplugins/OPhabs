package habilities;

import htt.ophabs.OPhabs;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class gura_gura {
  final int waveDistance = 8, radiusFloor = 4, radiusWall = 3;
  final Material AIR = Material.AIR;
  final int earthquakeDelay = 5;
  final int earthquakeSpeed = 10;
  final int earthquakeDamage = 2;
  final int earthquakeDuration = 5;
  private OPhabs plugin;
    boolean  enabledVH = true;
  public gura_gura(OPhabs plugin) {
    this.plugin = plugin;
  }
    
  public void handVibration(Player player){
    if(enabledVH){
        enabledVH = false;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 10, false, false));
        //Particles following player hand
        Location loc = player.getEyeLocation();
        loc.add(player.getLocation().getDirection());

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run(){
                if(player.isDead() || i > 3){ 
                    player.sendMessage("Hability cooldown 20 seconds");
                    cancelTask();
                }
                Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
                Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                loc.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset), 1, 0, 0, 0, 0);
                i++;
            }
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            } 
        }.runTaskTimer(plugin, 0, 20);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run(){
                if(player.isDead() || i > 25){
                    enabledVH = true;
                    player.sendMessage("Hability is now available");
                    cancelTask();
                }
                i++;
            }
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            } 
        }.runTaskTimer(plugin, 0, 20);

    }


  }


  public void expansionWaveBlocks(Location loc){
    if(loc.getBlock().getType() != AIR){
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(AIR);
            }
            //cancelTask
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 5, 20);
    }
  }


  public void createWave(Player player) {
    Location currentPL = player.getLocation();
    Vector direction = player.getEyeLocation().getDirection();
    currentPL.setY(currentPL.getY() + 1);
    ArrayList<Location> blocks = new ArrayList<Location>();
    for (int i = 0; i < waveDistance; i++) {
      blocks.add(currentPL.add(direction));
      blocks.addAll(positions(currentPL, direction));
    }
    for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), waveDistance, waveDistance, waveDistance)) {
        if (entity instanceof LivingEntity) {
            if (entity != player) ((LivingEntity) entity).damage(9);
        } else if (entity instanceof Projectile) {
            entity.setVelocity(new Vector(0, 0, 0));
        }
    }

    for (Location b : blocks) {
      if (b.getBlock().getType() != AIR && b.getBlock().getType() != Material.WATER) {
        if (b.getBlock().getType().getHardness() < Material.DIRT.getHardness())
          b.getBlock().breakNaturally();
        else if (b.getBlock().getType().getHardness() <= Material.GRASS_BLOCK.getHardness()) {
          if (b.getBlock().getType() == Material.GRASS_BLOCK) b.getBlock().setType(Material.DIRT);
          Random rand = new Random(); // instance of random class
          int int_random = rand.nextInt(15);
          if (int_random == 1) b.getBlock().breakNaturally();
        }
      }
      if (b.getBlock().getType() == AIR) {
        Random rand = new Random(); // instance of random class
        int int_random = rand.nextInt(15);
        if (int_random == 1)
          b.getWorld().spawnParticle(Particle.SONIC_BOOM, b, 1); // , Material.GLASS_PANE.createBlockData());
      } else
        b.getWorld().spawnParticle(Particle.BLOCK_DUST, b.clone().add(0, 1, 0), 4, b.getBlock().getType().createBlockData());

      setFallingBlock(b);
    }
  }

  public void setFallingBlock(Location block) {
    if (block.getBlock().getType() != AIR && block.getBlock().getType() != Material.WATER) {
      Material matFallingBlock = block.getBlock().getType();
      block.getBlock().setType(Material.AIR);
      block.getWorld().spawnFallingBlock(block, matFallingBlock, (byte) 9);
    }
  }


  public ArrayList<Location> positions(Location loc, Vector direction) {
    // if faces north
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
    // if faces east
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
    Location currentPL = player.getLocation(),
    loc = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor);
    ArrayList<Location> blocks = new ArrayList<>();
    Location pos1 = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor),
    pos2 = currentPL.clone().add(radiusFloor, radiusFloor, radiusFloor);

    int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
    int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
    int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
    int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
    int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

    System.out.println(x1 + "," + y1 + "," + z1 + " : " + x2 + "," + y2 + "," + z2);
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        for (int z = z1; z <= z2; z++) {
            Location block = new Location(player.getWorld(), x, y, z);
            blocks.add(block);
          if(block.getBlock().getType() != Material.WATER)
              setFallingBlock(new Location(player.getWorld(), x, y, z));
        }
      }
    }
    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 50, false, false));
    new BukkitRunnable() {
      int i = 0;

      public void run() {
        if (i >= earthquakeDuration) {
          cancelTask();
        }

        for (Location block : blocks) {
          ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();
          for (Entity entity : block.getWorld().getNearbyEntities(block, 1, 1, 1)) {
            if (entity instanceof LivingEntity) {
              entities.add((LivingEntity) entity);
            } else if (entity instanceof Projectile) {
              entity.setVelocity(new Vector(0, 0, 0));
            }
          }
          if (block.getBlock().getType() != AIR && block.getBlock().getType() != Material.WATER) {
            if (block.getBlock().getType().getHardness() < Material.DIRT.getHardness())
              block.getBlock().breakNaturally();
            else {
              if (block.getBlock().getType().getHardness() <= Material.STONE.getHardness()) {
                Random rand = new Random(); // instance of random class
                int int_random = rand.nextInt(25);
                if (int_random == 1) block.getBlock().breakNaturally();
              }
            }
            block
                .getWorld()
                .spawnParticle(
                    Particle.BLOCK_DUST,
                    block.clone().add(0, 1, 0),
                    10,
                    block.getBlock().getType().createBlockData());
          }

          // Damage all entities in the area of the wave
          for (LivingEntity entity : entities) {
            if (entity != player) entity.damage(earthquakeDamage);
          }
        }
        i++;
      }

      public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
      }
    }.runTaskTimer(plugin, earthquakeDelay, 15);
  }
}
