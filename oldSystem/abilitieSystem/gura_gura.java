package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import static oldSystem.abilitieSystem.OPHLib.*;

/**
 * @brief Gura gura no mi ability Class.
 * @author Vaelico786.
 */
public class gura_gura extends paramecia {
    final int waveDistance = 8, radiusFloor = 4;
    final Material AIR = Material.AIR;
    final int earthquakeDelay = 5;
    final int earthquakeDamage = 5, waveDamage = 14, createWaveDamage = 9;
    final int earthquakeDuration = 5;
    int numberOfWaves = 0;
    final int numberOfWavesMax = 5;

    // ---------------------------------------------- CONSTRUCTORS ---------------------------------------------------------------------
    /**
     * @brief gura_gura constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public gura_gura(OPhabs plugin) {
        super(plugin, 8, 0, 3, "gura_gura", "Gura Gura no Mi", "Gura Gura caster", 7, 1.5);
        abilitiesNames.add("Earthquake");
        abilitiesCD.add(0);
        abilitiesNames.add("CreateWave");
        abilitiesCD.add(0);
        abilitiesNames.add("HandVibration");
        abilitiesCD.add(0);
        abilitiesNames.add("ExpansionWave");
        abilitiesCD.add(0);
    }


    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------
    /**
     * @brief Ability 1: "EARTHQUAKE".
     * @see gura_gura#earthquake(Player)
     * @author Vaelico786.
     */
    public void ability1() {
        if(abilitiesCD.get(0) == 0) {
            earthquake(user.getPlayer());
            abilitiesCD.set(0, 30);
        }
    }

    /**
     * @brief CORE ABILITY 1: "EARTHQUAKE". Creates an earthquake in a radius around actual player's location, damaging nearby
     * entities and setting an earthquake animation.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void earthquake(Player player) {
        Location currentPL = player.getLocation();
        ArrayList<Location> blocks = new ArrayList<>();
        Location pos1 = currentPL.clone().add(-radiusFloor, -radiusFloor, -radiusFloor),
                 pos2 = currentPL.clone().add(radiusFloor, radiusFloor, radiusFloor);

        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

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
                    ArrayList<LivingEntity> entities = new ArrayList<>();
                    for (Entity entity : block.getWorld().getNearbyEntities(block, 1, 1, 1))
                        if (entity instanceof LivingEntity) {
                            entities.add((LivingEntity) entity);
                        } else if (entity instanceof Projectile)
                            entity.setVelocity(new Vector(0, 0, 0));

                    if (block.getBlock().getType() != AIR && block.getBlock().getType() != Material.WATER) {
                        if (block.getBlock().getType().getHardness() < Material.DIRT.getHardness() &&
                                block.getBlock().getType() != Material.BEDROCK)
                            block.getBlock().breakNaturally();
                        else
                            if (block.getBlock().getType().getHardness() <= Material.STONE.getHardness() &&
                                    block.getBlock().getType() != Material.BEDROCK) {
                                Random rand = new Random(); // instance of random class
                                int int_random = rand.nextInt(25);
                                if (int_random == 1) block.getBlock().breakNaturally();
                            }
                        block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.clone().add(0, 1, 0), 10,
                                                       block.getBlock().getType().createBlockData());
                    }

                    // Damage all entities in the area of the wave
                    for (LivingEntity entity : entities)
                        if (entity != player) entity.damage(earthquakeDamage,(Entity) user.getPlayer());
                }
                i++;
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, earthquakeDelay, 15);
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------
    /**
     * @brief Ability 2: "Create Wave".
     * @see gura_gura#createWave(Player)
     * @author Vaelico786.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            createWave(user.getPlayer());
            abilitiesCD.set(1, 10);
        }
    }

    /**
     * @brief CORE ABILITY 2: "Create Wave". Creates a frontal wave that deals damage to entities.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void createWave(Player player) {
        Location currentPL = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection();
        currentPL.setY(currentPL.getY() + 1);
        ArrayList<Location> blocks = new ArrayList<>();

        for (int i = 0; i < waveDistance; i++) {
            blocks.add(currentPL.add(direction));
            blocks.addAll(positions(currentPL, direction));
        }
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), waveDistance, waveDistance, waveDistance)) {
            if (entity instanceof LivingEntity)
                if (entity != player) ((LivingEntity) entity).damage(createWaveDamage,(Entity) user.getPlayer());
            else if (entity instanceof Projectile)
                entity.setVelocity(new Vector(0, 0, 0));
        }

        for (Location b : blocks) {
            if (b.getBlock().getType() != AIR && b.getBlock().getType() != Material.WATER) {
                if (b.getBlock().getType().getHardness() < Material.DIRT.getHardness() && b.getBlock().getType() != Material.BEDROCK)
                    b.getBlock().breakNaturally();
                else if (b.getBlock().getType().getHardness() <= Material.GRASS_BLOCK.getHardness() && b.getBlock().getType() != Material.BEDROCK) {
                    if (b.getBlock().getType() == Material.GRASS_BLOCK) b.getBlock().setType(Material.DIRT);
                    Random rand = new Random(); // instance of random class
                    int int_random = rand.nextInt(15);
                    if (int_random == 1) b.getBlock().breakNaturally();
                }
            }
            if (b.getBlock().getType() == AIR) {
                Random rand = new Random(); // instance of random class
                int int_random = rand.nextInt(15);
                if(int_random == 1)
                    b.getWorld().spawnParticle(Particle.SONIC_BOOM, b, 1); // , Material.GLASS_PANE.createBlockData());
            } else
                b.getWorld().spawnParticle(Particle.BLOCK_DUST, b.clone().add(0, 1, 0), 4, b.getBlock().getType().createBlockData());

            setFallingBlock(b);
        }
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------
    /**
     * @brief Ability 3: "Hand Vibration".
     * @see gura_gura#handVibration(Player)
     * @author Vaelico786.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            handVibration(user.getPlayer());
            abilitiesCD.set(2, 10);
        }
    }

    /**
     * @brief CORE ABILITY 3: "Hand Vibration". Increases damage dealt by player.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public void handVibration(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 10, false,
                false));

        Location loc = player.getEyeLocation();
        loc.add(player.getLocation().getDirection());

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(player.isDead() || i > 3)
                    cancelTask();

                Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
                Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);

                loc.getWorld().spawnParticle(Particle.SONIC_BOOM,
                        player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset),
                        1, 0, 0, 0, 0);
                i++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "Expansion Wave".
     * @see gura_gura#expansionWaveBlocks(Player)
     * @author Vaelico786.
     */
    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            if(numberOfWaves == 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        abilitiesCD.set(3, 30);
                        numberOfWaves = 0;
                    }
                }.runTaskLater(plugin, 60);
            }
            expansionWaveBlocks(user.getPlayer());
        }
    }

    /**
     * @brief CORE ABILITY 4: "Expansion Wave". Create waves (earthquakes) and expand it dealing damage to all units on her radius.
     * @param player Fruit's user.
     * @see gura_gura#ExpansionWaveFallingBlock(Location, int)
     * @see gura_gura#ExpansionWaveDamage(Location, Location, int, int, int)
     * @author Vaelico786.
     */
    public void expansionWaveBlocks(Player player) {
        int deep = 3;
        Location loc = player.getLocation().add(0, -1, 0);
        if(loc.getBlock().getType() != AIR && numberOfWaves < numberOfWavesMax) {
            numberOfWaves++;
            new BukkitRunnable() {
                int i = 1;
                @Override
                public void run() {
                    //player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 1, 1);
                    for(int j=0; j<(i*2)+1; j++) {
                        ExpansionWaveFallingBlock(loc.clone().add(j-i, 0, -i), deep);

                        ExpansionWaveFallingBlock(loc.clone().add(-i, 0, j-i), deep);

                        ExpansionWaveFallingBlock(loc.clone().add(j-i, 0, i), deep);

                        ExpansionWaveFallingBlock(loc.clone().add(i, 0, j-i), deep);
                    }

                    if(i >= waveDistance) {     // Última distancia (más alejada).
                        for(int j=0; j<((i+1)*2)+1; j++) {
                            ExpansionWaveDamage(loc.clone().add(j-i, 0, -i), loc.clone(), deep, i, j);

                            ExpansionWaveDamage(loc.clone().add(j-i, 0, -i), loc.clone(), deep, i, j);

                            ExpansionWaveDamage(loc.clone().add(j-i, 0, -i), loc.clone(), deep, i, j);

                            ExpansionWaveDamage(loc.clone().add(j-i, 0, -i), loc.clone(), deep, i, j);
                        }
                        numberOfWaves--;
                        cancelTask();
                    }
                    i++;
                }
                public void cancelTask(){
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 5, 7);
        }
    }

    // ---------------------------------------------- FUNCTIONS ---------------------------------------------------------------------

    /**
     * @brief Auxiliary function for ability 4. Create the wave and deal damage.
     * @see gura_gura#expansionWaveBlocks(Player)
     * @author Vaelico786.
     */
    public void ExpansionWaveDamage(Location l, Location loc, int deep, int i, int j) {
        getSolidRelativeY(l, deep);

        if(isSolidBlock(l.getBlock())) {
            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0, l.getBlock().getBlockData());
            setFallingBlock(loc.clone().add(i,0,j-i));

            l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
                entity.setVelocity(new Vector(0, 2, 0));
                if(entity instanceof LivingEntity)
                    ((LivingEntity) entity).damage(waveDamage,(Entity) user.getPlayer());
            });
        }
    }

    /**
     * @brief Auxiliary function for ability 4. Create the wave and deal extra damage in the LAST RADIUS' LOCATION.
     * @see gura_gura#expansionWaveBlocks(Player)
     * @param l Actual player's location.
     * @param deep Deep point to get relative 'y' position.
     * @see OPHLib#getSolidRelativeY(Location, int)
     * @author Vaelico786.
     */
    public void ExpansionWaveFallingBlock(Location l, int deep) {
        l.getWorld().getNearbyEntities(l, 1, 4, 1).forEach((entity) -> {
            if(entity instanceof LivingEntity) {
                if(!entity.getName().equals(user.getPlayer().getName()))
                    ((LivingEntity) entity).damage(waveDamage/2,(Entity) user.getPlayer());
            }
        });

        getSolidRelativeY(l,deep);
        if(isSolidBlock(l.getBlock())) {
            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, l, 10, 0, 1, 0, 0,
                    l.getBlock().getBlockData());

            setFallingBlock(l).setVelocity(new Vector(0, 0.15, 0));
        }
    }

    /**
     * @brief Gets blocks in a pool around the location given.
     * @see gura_gura#expansionWaveBlocks(Player)
     * @param loc Actual player's location.
     * @param direction Direction player is looking at.
     * @author Vaelico786.
     */
    public ArrayList<Location> positions(Location loc, Vector direction) {
        // if faces north
        ArrayList<Location> blocks = new ArrayList<>();

        if(direction.getX() != 1) {
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
}
