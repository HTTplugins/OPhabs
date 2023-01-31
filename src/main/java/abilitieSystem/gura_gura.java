package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;

public class gura_gura extends paramecia {
    final int waveDistance = 8, radiusFloor = 4, radiusWall = 3;
    final Material AIR = Material.AIR;
    final int earthquakeDelay = 5;
    final int earthquakeSpeed = 10;
    final int earthquakeDamage = 2;
    final int earthquakeDuration = 5;
    int numberOfWaves = 0;
    final int numberOfWavesMax = 5;
    public gura_gura(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialGura, castIdentification.castItemNameGura, fruitIdentification.fruitCommandNameGura);
        abilitiesNames.add("Earthquake");
        abilitiesCD.add(0);
        abilitiesNames.add("CreateWave");
        abilitiesCD.add(0);
        abilitiesNames.add("HandVibration");
        abilitiesCD.add(0);
        abilitiesNames.add("ExpansionWave");
        abilitiesCD.add(0);
    }
    public gura_gura(OPhabs plugin, abilityUser user) {
        super(plugin, user, castIdentification.castMaterialGura, castIdentification.castItemNameGura, fruitIdentification.fruitCommandNameGura);
        abilitiesNames.add("Earthquake");
        abilitiesCD.add(0);
        abilitiesNames.add("CreateWave");
        abilitiesCD.add(0);
        abilitiesNames.add("HandVibration");
        abilitiesCD.add(0);
        abilitiesNames.add("ExpansionWave");
        abilitiesCD.add(0);
    }
   
    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            earthquake(user.getPlayer());
            abilitiesCD.set(0, 30);
        }
    }
    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            createWave(user.getPlayer());
            abilitiesCD.set(1, 10);
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            handVibration(user.getPlayer());
            abilitiesCD.set(2, 10);
        }
    }
    public void ability4(){
        if(abilitiesCD.get(3) == 0){
            expansionWaveBlocks(user.getPlayer());
            abilitiesCD.set(3, 10);
        }
    }

    public void handVibration(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 10, false, false));
            //Particles following player hand
            Location loc = player.getEyeLocation();
            loc.add(player.getLocation().getDirection());

            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run(){
                    if(player.isDead() || i > 3){ 
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
    }


    public Boolean isSolidBlock(Block block){
        return !(block.getType().getHardness() <= Material.TORCH.getHardness() || block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.LAVA);
    }

    public void getSolidRelativeY(Location loc, int y){
       if(isSolidBlock(loc.getBlock()))
            loc = getSolidRelativeUpper(loc, y);
        else
            loc = getSolidRelativeDown(loc.add(0,-1,0), y);
    }

    public Location getSolidRelativeUpper(Location loc, int y){
        Location loc2 = loc.clone().add(0,1,0);
        if(!isSolidBlock(loc2.getBlock()))
            return loc;
        else
            if(y > 0)
                return getSolidRelativeUpper(loc.add(0, 1, 0), y-1);
            else
                return null;
    }

    public Location getSolidRelativeDown(Location loc, int y){
        if(isSolidBlock(loc.getBlock()))
            return loc;
        else{
            if(y > 0)
               return getSolidRelativeDown(loc.add(0, -1, 0), y-1);
            else
                return null;
        } 
    }

    public void expansionWaveBlocks(Player player){
        int deep = 3;
        Location loc = player.getLocation().add(0, -1, 0);
        if(loc.getBlock().getType() != AIR && numberOfWaves < numberOfWavesMax){
            numberOfWaves++;
            new BukkitRunnable() {
                int i = 1;
                @Override
                public void run() {
                    Location l= null;
                    for(int j=0; j<(i*2)+1; j++){
                        l = loc.clone().add(j-i, 0, -i);
                        l.getWorld().getNearbyEntities(l, 1, deep*2, 1).forEach((entity) -> {
                            if(entity instanceof LivingEntity){
                                if(entity.getName() != player.getName())
                                    ((LivingEntity) entity).damage(3);
                            }
                        });
                        getSolidRelativeY(l,deep);
                        if( l != null && isSolidBlock(l.getBlock())){
                            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                            setFallingBlock(l).setVelocity(new Vector(0, 0.15, 0));
                        }

                        l = loc.clone().add(-i, 0, j-i);
                        l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                            if(entity instanceof LivingEntity){
                                if(entity.getName() != player.getName())
                                    ((LivingEntity) entity).damage(3);
                            }
                        });
                        getSolidRelativeY(l,deep);
                        if( l != null && isSolidBlock(l.getBlock())){
                            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                            setFallingBlock(l).setVelocity(new Vector(0, 0.15, 0));
                        }

                        l = loc.clone().add(j-i, 0, i);
                         l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                            if(entity instanceof LivingEntity){
                                if(entity.getName() != player.getName())
                                    ((LivingEntity) entity).damage(3);
                            }
                        });
                        getSolidRelativeY(l,deep);
                        if( l != null && isSolidBlock(l.getBlock())){
                            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                            setFallingBlock(l).setVelocity(new Vector(0, 0.15, 0));
                        }

                        l = loc.clone().add(i, 0, j-i);
                        l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                            if(entity instanceof LivingEntity){
                                if(entity.getName() != player.getName())
                                   ((LivingEntity) entity).damage(3);
                            }
                        });
                        getSolidRelativeY(l,deep);
                        if( l != null && isSolidBlock(l.getBlock())){
                            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                            setFallingBlock(l).setVelocity(new Vector(0, 0.15, 0));
                        }
                    }

                    if(i >= waveDistance){
                        for(int j=0; j<((i+1)*2)+1; j++){
                            l = loc.clone().add(j-i, 0, -i);
                            getSolidRelativeY(l,deep);
                            if( l != null && isSolidBlock(l.getBlock())){
                                l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                                setFallingBlock(loc.clone().add(j-i,0,-i));
                                l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                                   entity.setVelocity(new Vector(0, 2, 0));
                                    if(entity instanceof LivingEntity){
                                        ((LivingEntity) entity).damage(30);
                                    }
                                });
                            }

                            l = loc.clone().add(-i, 0, j-i);
                            getSolidRelativeY(l,deep);
                            if( l != null && isSolidBlock(l.getBlock())){
                                l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                                setFallingBlock(loc.clone().add(-i,0,j-i));
                                l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                                    entity.setVelocity(new Vector(0, 2, 0));
                                    if(entity instanceof LivingEntity){
                                        ((LivingEntity) entity).damage(30);
                                    }
                                });
                            }

                            l = loc.clone().add(j-i, 0, i);
                            getSolidRelativeY(l,deep);
                            if( l != null && isSolidBlock(l.getBlock())){
                                l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                                setFallingBlock(loc.clone().add(j-i,0,i));
                                l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                                    entity.setVelocity(new Vector(0, 2, 0));
                                    if(entity instanceof LivingEntity){
                                        ((LivingEntity) entity).damage(30);
                                    }
                                });
                            }

                            l = loc.clone().add(i, 0, j-i);
                            getSolidRelativeY(l,deep);
                            if( l != null && isSolidBlock(l.getBlock())){
                                l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
                                setFallingBlock(loc.clone().add(i,0,j-i));
                                l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                                    entity.setVelocity(new Vector(0, 2, 0));
                                    if(entity instanceof LivingEntity){
                                        ((LivingEntity) entity).damage(30);
                                    }
                                });
                            }
                        }
                        numberOfWaves--;
                        cancelTask();
                    }
                    i++;
                }
                //cancelTask
                public void cancelTask(){
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 5, 7);
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

  public Entity setFallingBlock(Location block) {
    if (block.getBlock().getType() != AIR && block.getBlock().getType() != Material.WATER) {
      Material matFallingBlock = block.getBlock().getType();
      block.getBlock().setType(Material.AIR);
      return block.getWorld().spawnFallingBlock(block, matFallingBlock, (byte) 9);
    }
    return null;

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
