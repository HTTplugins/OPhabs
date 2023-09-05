package libs;

import htt.ophabs.OPhabs;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import runnables.OphAnimTextureRunnable;
import runnables.OphRunnable;
import textures.OPTexture;

import java.util.*;
import java.util.function.BiConsumer;

import static java.lang.Math.*;

/**
 * Common class created to make easier the development of abilities.
 * In this class we put multiples parameterized functions that we create for a class and that we think other could use in theirs.
 */
public class OPHLib {
    private static final Random random = new Random();

    public static Random getRandom() {
        return random;
    }

    public static Monster createMonster(EntityType entType, Location loc, double health, boolean invisible, ItemStack mainhandItem ){
        World world = loc.getWorld();

        Monster monster =  (Monster) world.spawnEntity(loc,entType);
        monster.setHealth(health);
        monster.getEquipment().setItemInMainHand(mainhandItem);


        if(invisible)
            monster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, false, false));

        return monster;
    }
    public static boolean setMonsterTarget(Mob mob, Player mobOwner, double targetRange, Set<LivingEntity> ignoredEntities) {
        LivingEntity playerTarget = null;
        LivingEntity monsterTarget = null;
        LivingEntity livEnt = null;

        for (Entity e : mob.getNearbyEntities(targetRange, targetRange, targetRange)) {
            if (!e.equals(mobOwner)  && !ignoredEntities.contains(e)) {
                if (e instanceof Player) {
                    playerTarget = (LivingEntity) e;
                    break;
                } else if (e instanceof Monster) {
                    monsterTarget = (LivingEntity) e;
                } else if (e instanceof LivingEntity) {
                    livEnt = (LivingEntity) e;
                }
            }
        }

        if(playerTarget != null){
            mob.setTarget(playerTarget);
            return true;
        } else if(monsterTarget != null){
            mob.setTarget(monsterTarget);
            return true;
        } else if(livEnt != null){
            mob.setTarget(livEnt);
            return true;
        }

        mob.setTarget(null);
        return false;
    }

    public static void affectAreaLivingEntitiesRunnable(Player player,double duration, long period, double x, double y, double z, Consumer<LivingEntity> entityAction) {
        new OphRunnable() {
            @Override
            public void OphRun() {
                if (getCurrentRunIteration() >= duration) {this.ophCancel();}

                for (Entity entity : player.getNearbyEntities(x, y, z)) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity livEnt = (LivingEntity) entity;
                        entityAction.accept(livEnt);
                    }

                }
            }


        }.ophRunTaskTimer(0, period);


    }


    public static void changeCasterTexture(int textureid, ItemStack item){

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(textureid);
        item.setItemMeta(meta);
    }

    public static void changeCasterTexture(OPTexture texture, ItemStack item){

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(OPhabs.textureMapper.getMappedID(texture));
        item.setItemMeta(meta);
    }

    public static void dash(Player player, double distance ){
        Location location = player.getEyeLocation();

        Vector direction = location.getDirection();
        direction.multiply(distance);
        player.setVelocity(direction);
        OPHSoundLib.OphPlaySound(OPHSounds.FASTSORU,player.getLocation(),1,1);
    }

    public static Location getBack(Player player, double yaw,  double height){
        double x = cos(toRadians(yaw)) / 2;
        double z = sin(toRadians(yaw)) / 2;

        double xBehind = player.getLocation().getX() + x;
        double zBehind = player.getLocation().getZ() + z;
        return new Location(player.getWorld(),xBehind,height,zBehind);
    }


    public static void attractEntitie(Entity ent, Player player){
        new OphRunnable(){
            Vector FirstVector;
            boolean isFirstVector = false;
            boolean entityInHand = false;
            double vx,vy,vz;
            @Override
            public void OphRun() {
                if(ent.isDead() || player.isDead())
                    this.ophCancel();

                vx =  player.getLocation().getX() - ent.getLocation().getX();
                vy =  player.getLocation().getY() - ent.getLocation().getY();
                vz =  player.getLocation().getZ() - ent.getLocation().getZ();

                Vector movement = player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize();

                if(!isFirstVector){
                    FirstVector = movement.clone();
                    isFirstVector = true;
                }

                //Para levantar al mob si hay desnivel
                if(player.getLocation().getY() >= ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                ent.setVelocity(movement);

                if(sqrt(pow(vx,2) + pow(vy,2) +  pow(vz,2)) <= 1){
                    entityInHand = true;
                    this.cancel();
                    ent.setVelocity(new Vector(0,0,0));
                }
            }
        }.ophRunTaskTimer(0,1);
    }

    /**
     * @param x        X position where you want to search ground.
     * @param z        Z position where you want to search ground.
     * @param initialY Y "relative-to" position.
     * @return Y position of the ground with a maximum distance of 40 units.
     * @brief Given a 2D position X Z, search the Y position that's the ground relative to an initial Y position.
     * @note You can't use world.getHighestBlockAt() because you wouldn't get the ground if you have some kind of platform above you,
     * for example, if you're inside a house.
     * @note Remember if you use this with particles, it will be different for each particle, so add to that Y a bit more than needed to get ground depending on the particle.
     * @author RedRiotTank
     */
    public static double searchGround(double x, double z, double initialY, World world) {
        Location loc = new Location(world, x, initialY, z);

        for (int i = 0; i < 40; i++)
            if (loc.getBlock().getRelative(0, -i, 0).getType().isSolid()) {
                for (int j = 0; j < 5 && loc.getBlock().getRelative(0, j, 0).getType().isSolid(); j++) {
                    if (!loc.getBlock().getRelative(0, j + 1, 0).getType().isSolid()) {
                        return loc.getBlock().getRelative(0, j, 0).getY();
                    }
                }
                return initialY - i;
            }


        return initialY - 40;
    }

    /**
     * @param player player who is "raycasting"
     * @param range  Maximum distance the player can reach.
     * @return Location the player is looking in that maximum range, if there is nothing in that "ray-cast", returns the maximum.
     * @brief Raycast-like function. Calculate the location a player is looking in a maximum range sized vector.
     * @note Clarification: if there is a block in the ray-cast, that's the returned location.
     * @author RedRiotTank
     */
    public static Location getTargetBlock(Player player, int range) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location current = player.getEyeLocation();

        for (int i = 0; i < range; i++) {
            current = current.add(direction);

            if (current.getBlock().getType().isSolid())
                return current;
        }

        return current;
    }

    /**
     * @param entity Entity you want to spawn blood in front of.
     * @brief Spawns a small amount of blood-like particle around the entity.
     * @author RedRiotTank
     */
    public static void spawnBloodParticles(Entity entity) {
        Location loc = entity.getLocation();
        World world = entity.getWorld();
        world.spawnParticle(Particle.REDSTONE, loc.add(0, entity.getHeight(), 0), 20, 0.5, 0.5, 0.5,
                0.1, new Particle.DustOptions(Color.RED, 1.0f));
    }

    /**
     * @param player   player that raycast.
     * @param distance of the raycast vector (int).
     * @return The first living entity if it is found, null if nothing is found.
     * @brief Checks if the player is looking at a living entity in a distance.
     * @author RedRiotTank
     */
    public static LivingEntity rayCastLivEnt(Player player, int distance) {
        LivingEntity target = null;

        Vector direction = player.getEyeLocation().getDirection();
        RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), direction, distance,
                p -> !player.getUniqueId().equals(p.getUniqueId()));

        if (result != null && result.getHitEntity() != null)
            target = (LivingEntity) result.getHitEntity();

        return target;
    }

    /**
     * @param player       player who is looking at back.
     * @param livingEntity LivingEntity that's been seen.
     * @return true if player is looking at livingEntity's back. false if not.
     * @brief Checks if the player is looking at the back of a living entity.
     * @note The cross product is between looking direction, so it could have variation
     * depending on where the Living entity is looking at.
     * @author RedRiotTank
     */
    public static boolean isLookingBack(Player player, LivingEntity livingEntity) {
        Vector pDir = player.getLocation().getDirection();
        Vector eDir = livingEntity.getLocation().getDirection();
        double xv = pDir.getX() * eDir.getZ() - pDir.getZ() * eDir.getX();
        double zv = pDir.getX() * eDir.getX() + pDir.getZ() * eDir.getZ();
        double angle = Math.atan2(xv, zv); // Value between -π and +π
        double angleInDegrees = (angle * 180) / Math.PI;

        return angleInDegrees <= 60 && angleInDegrees >= -32;
    }

    /**
     * @param radius     Radius of the circle.
     * @param separation Separation between the particles.
     * @param prof       How far away from the player you want the circle to appear.
     * @param dustOption Dust Option of the particle. Set it to null if your particle don't use dust options.
     * @param particle   ParticleType of the circle.
     * @param harms      true if particles harms Living Entities when touching them.
     * @param destroy    true if particles destroy blocks when touching them.
     * @param player     player that generates the circle next to.
     * @brief Creates a circle of particles in the direction the player is looking at.
     * @author RedRiotTank
     */
    public static void circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
                                       Particle particle, boolean harms, boolean destroy, Player player) {

        final Location playerLoc = player.getLocation();
        double x, y, xY, yY, zY, xX, yX, zX, xL, yL, zL;
        final double yaw = -player.getLocation().getYaw(),
                pitch = player.getLocation().getPitch();
        final World world = player.getWorld();

        for (double i = 0; i < 2 * PI; i += separation) {
            x = radius * cos(i);
            y = radius * sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch)) * y - sin(toRadians(pitch)) * prof;
            zX = sin(toRadians(pitch)) * y + cos(toRadians(pitch)) * prof;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw)) * xX + sin(toRadians(yaw)) * zX;
            yY = yX;
            zY = -sin(toRadians(yaw)) * xX + cos(toRadians(yaw)) * zX;

            //Final (sum of player position)
            xL = playerLoc.getX() + xY;
            yL = 1 + playerLoc.getY() + yY;
            zL = playerLoc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);

            if (destroy)
                if (!partLoc.getBlock().getType().equals(Material.AIR))
                    partLoc.getBlock().setType(Material.AIR);

            if (harms)
                for (Entity ent : world.getNearbyEntities(partLoc, 2, 2, 2))
                    if (ent instanceof LivingEntity && !player.equals(ent))
                        ((LivingEntity) ent).damage(100, (Entity) player);

            world.spawnParticle(particle, partLoc, 0, 0, 0, 0, dustOption);
        }
    }




    /**
     * @param radius     Radius of the circle.
     * @param separation Separation between the particles.
     * @param prof       How far away from the player you want the circle to appear.
     * @param dustOption Dust Option of the particle. Set it to null if your particle don't use dust options.
     * @param particle   ParticleType of the circle.
     * @param player     player that generates the circle next to.
     * @param loc        center of the circle
     * @brief Creates a circle of particles in the direction the player is looking at.
     * @author RedRiotTank / Vaelico786
     * @see OPHLib#circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
     *                                        Particle particle, boolean harms, boolean destroy, Player player)
     */
    public static void circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
                                       Particle particle, Player player, Location loc) {

        double x, y, xY, yY, zY, xX, yX, zX, xL, yL, zL;
        final double yaw = -player.getLocation().getYaw(),
                pitch = player.getLocation().getPitch();
        final World world = player.getWorld();

        for (double i = 0; i < 2 * PI; i += separation) {
            x = radius * cos(i);
            y = radius * sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch)) * y - sin(toRadians(pitch)) * prof;
            zX = sin(toRadians(pitch)) * y + cos(toRadians(pitch)) * prof;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw)) * xX + sin(toRadians(yaw)) * zX;
            yY = yX;
            zY = -sin(toRadians(yaw)) * xX + cos(toRadians(yaw)) * zX;

            //Final (sum of player position)
            xL = loc.getX() + xY;
            yL = 0.3 + loc.getY() + yY;
            zL = loc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);

            world.spawnParticle(particle, partLoc, 0, 0, 0, 0, dustOption);
        }
    }

    /**
     * @param block Given block.
     * @brief Returns true if given block is solid (Not air, water, ...).
     * @author Vaelico786.
     */
    public static Boolean isSolidBlock(Block block) {
        return !(block.getType().getHardness() <= Material.TORCH.getHardness() ||
                block.getType() == Material.AIR ||
                block.getType() == Material.WATER ||
                block.getType() == Material.LAVA ||
                block.getType() == Material.BEDROCK);
    }

    /**
     * @param loc Actual Location.
     * @param y   Actual 'y' we want to compare with.
     * @brief Transform loc into location (above or down) where it would be a normal block. It means that loc is on a solid
     * block, and it has air above him.
     * @author Vaelico786.
     * @see OPHLib#getSolidRelativeUpper(Location, int)
     * @see OPHLib#getSolidRelativeDown(Location, int)
     */
    public static void getSolidRelativeY(Location loc, int y) {
        if (isSolidBlock(loc.getBlock()))
            loc = getSolidRelativeUpper(loc, y);
        else
            loc = getSolidRelativeDown(loc.add(0, -1, 0), y);
    }

    /**
     * @param loc Actual Location.
     * @param y   Actual 'y' we want to compare with.
     * @brief Returns the first location starting at loc where it has an air block above.
     * @author Vaelico786.
     */
    public static Location getSolidRelativeUpper(Location loc, int y) {
        Location loc2 = loc.clone().add(0, 1, 0);
        if (!isSolidBlock(loc2.getBlock()))
            return loc;
        else if (y > 0)
            return getSolidRelativeUpper(loc.add(0, 1, 0), y - 1);
        else
            return null;
    }

    /**
     * @param loc Actual Location.
     * @param y   Actual 'y' we want to compare with.
     * @brief Returns the first location starting at loc where it has a solid block down.
     * @author Vaelico786.
     */
    public static Location getSolidRelativeDown(Location loc, int y) {
        if (isSolidBlock(loc.getBlock()))
            return loc;
        else {
            if (y > 0)
                return getSolidRelativeDown(loc.add(0, -1, 0), y - 1);
            else
                return null;
        }
    }

    /**
     * @param block Block we want to transform.
     * @brief Returns the falling block created. Transform block given into an equal falling block.
     * @author Vaelico786.
     */
    public static Entity setFallingBlock(Location block) {
        if (block.getBlock().getType() != Material.AIR && block.getBlock().getType() != Material.WATER &&
                block.getBlock().getType() != Material.BEDROCK) {

            if (block.getBlock().getState() instanceof Container) {
                block.getBlock().breakNaturally();
                return null;
            }
            Material matFallingBlock = block.getBlock().getType();
            block.getBlock().setType(Material.AIR);
            return block.getWorld().spawnFallingBlock(block, matFallingBlock, (byte) 9);
        }
        return null;
    }


    public static void catchEntity(Entity ent, Player player) {
        new BukkitRunnable() {
            Vector FirstVector;
            boolean fV = false;
            boolean entityInHand = false;
            double vx, vy, vz;
            int i = 0, j = 0;

            @Override
            public void run() {
                Location loc = player.getLocation().add(0, 2, 0);
                if (player.isDead() || ent.isOnGround())
                    this.cancel();

                vx = loc.getX() - ent.getLocation().getX();
                vy = loc.getY() - ent.getLocation().getY();
                vz = loc.getZ() - ent.getLocation().getZ();

                Vector movement = loc.clone().toVector().subtract(ent.getLocation().toVector()).normalize();

                if (!fV) {
                    FirstVector = movement.clone();
                    fV = true;
                }

                //Para levantar al mob si hay desnivel
                if (loc.getY() >= ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                if (!entityInHand) {
                    ent.setVelocity(movement);
                    i++;
                } else {
                    ent.teleport(player.getLocation().add(0, 2, 0));
                    ent.setVelocity(new Vector(0, 0.042, 0));
                    j++;
                }

                if (Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(vz, 2)) <= 1) {
                    entityInHand = true;
                    if (!player.isSneaking()) {
                        this.cancel();
                        repealEntity(ent, player);
                    }
                }

                if (!player.isSneaking()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }


    public static void repealEntity(Entity ent, Player player) {
        ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 10, 2);

        Vector dir = player.getLocation().getDirection();
        ent.setVelocity(dir.multiply(3));

        //timer
        new BukkitRunnable() {
            Vector aux = dir;
            int i = 0;

            @Override
            public void run() {
                if (ent.getVelocity().getX() != aux.getX() || ent.getVelocity().getZ() != aux.getZ()) {
                    aux = ent.getVelocity();

                    ent.getWorld().getNearbyEntities(ent.getLocation(), 2, 2, 2).forEach(entity -> {
                        if (entity instanceof LivingEntity && entity != player && entity != ent) {
                            ((LivingEntity) entity).damage(14, (Entity) player);
                        }
                    });
                }
                if (ent.isOnGround() || i >= 120) {
                    cancelTask();
                }
                i++;
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 2);
    }

    public static void breath(Player player, Vector pos, Vector dir, int delay, int maxLength, int maxTicks, double startAmpl, int startAmountParticles, double maxAmplitude, int period, OPHSounds sound, Particle particle, Object extra, BiConsumer<Location, Vector> areaEffect) {
        int iterations = maxTicks / period;

        new OphRunnable(iterations+10) {
            double density = startAmountParticles / startAmpl;
            double amplitude = startAmpl;
            double distance = 0;

            World world = player.getWorld();

            double particleAmount;
            int i = 0;

            @Override
            public void OphRun() {
                if(getCurrentRunIteration() > iterations) ophCancel();
                Vector direction = player.getEyeLocation().getDirection().add(dir);
                Location location = player.getEyeLocation().add(pos);

                if (getCurrentRunIteration() == 0 && !Objects.equals(sound, "none"))    //Play sound on first iteration
                    OPHSoundLib.OphPlaySound(sound, location, 1, 1);


                for (int j = 1; j <= distance; j++) {
                    if (amplitude < maxAmplitude)
                        amplitude += maxAmplitude / maxLength;


                    location.add(direction);
                    particleAmount = density * amplitude;
                    world.spawnParticle(particle, location, (int) particleAmount,
                                        amplitude, amplitude, amplitude, 0, extra);

                    areaEffect.accept(location, new Vector(amplitude, amplitude, amplitude));
                }

                amplitude = startAmpl;

                if (distance < maxLength)
                    distance += ((double) maxLength) / (((double) iterations) / 6.0);
            }
        }.ophRunTaskTimer(delay, period);
    }

    /**
     * @brief General roar function for zoans type.
     * @param entity entity that's going to roar.
     * @param effects array list of effects to be applied on targets
     * @author MiixZ, Vaelico786.
     */
    public static void roar(LivingEntity entity, double damage, int area, ArrayList<PotionEffect> effects){
       entity.getWorld().playSound(entity.getLocation(),"roar", 1, 1);

        entity.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation().add(0,1,0), 100, 1, 1, 1, 1);
        entity.getNearbyEntities(area, area, area).forEach(ent -> {
            if(ent instanceof LivingEntity && ent!=entity) {
                ((LivingEntity) ent).damage(damage,entity);
                effects.forEach(effect -> ((LivingEntity) ent).addPotionEffect(effect));
            }
        });

        entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(),  10, 0, 0, 0, 0);
    }

    public static ArmorStand generateCustomFloatingItem(Location loc, ItemStack item, boolean gravity) {
        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        // Establecer el objeto con Custom Model Data en la mano del armor stand
        armorStand.getEquipment().setHelmet(item);

        armorStand.setVisible(false);
        armorStand.setGravity(gravity);
        armorStand.setArms(false);
        armorStand.setInvulnerable(true);

        return armorStand;
    }

    public static void bleeding(LivingEntity entity, int ticks, Player user) {
        new OphRunnable(ticks) {
            @Override
            public void OphRun() {
                if(entity.isDead() || entity.getUniqueId().equals(user.getUniqueId()) || 
                  (entity.getCustomName() != null && entity.getCustomName().contains("cosmeticArmor_"))) ophCancel();
                entity.damage(2, user);
                entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.2, 0, 0.2),
                        4, 0, 0, 0);
                entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.2, 0, -0.2),
                        4, 0, 0, 0);
                entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(-0.2, 0, -0.2),
                        4, 0, 0, 0);
            }
 
        }.ophRunTaskTimer(0, 20);
    }
}
