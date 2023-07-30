package abilitieSystem;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

import static java.lang.Math.*;


/**
 * Common class created to make easier the development of abilities.
 * In this class we put multiples parameterized functions that we create for a class and that we think other could use in theirs.
 */
public class OPHLib {

    /**
     * @brief Given a 2D position X Z, search the Y position that's the ground relative to an initial Y position.
     * @param x X position where you want to search ground.
     * @param z Z position where you want to search ground.
     * @param initialY Y "relative-to" position.
     * @return Y position of the ground with a maximum distance of 40 units.
     * @note You can't use world.getHighestBlockAt() because you wouldn't get the ground if you have some kind of platform above you,
     * for example, if you're inside a house.
     * @author RedRiotTank
     */
    public static double searchGround(double x, double z, double initialY, World world) {
        Location loc = new Location(world,x,initialY,z);

        for(int i=0; i < 40; i++)
            if(loc.getBlock().getRelative(0,-i,0).getType().isSolid()){
                for(int j=0; j<5 && loc.getBlock().getRelative(0,j,0).getType().isSolid();j++ ){
                    if(!loc.getBlock().getRelative(0,j+1,0).getType().isSolid())   {
                        return loc.getBlock().getRelative(0,j,0).getY();
                    }
                }
                return initialY - i;
            }


        return initialY - 40;
    }

    /**
     * @brief Raycast-like function. Calculate the location a player is looking in a maximum range sized vector.
     * @param player player who is "raycasting"
     * @param range  Maximum distance the player can reach.
     * @return Location the player is looking in that maximum range, if there is nothing in that "ray-cast", returns the maximum.
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
     * @brief Spawns a small amount of blood-like particle around the entity.
     * @param entity Entity you want to spawn blood in front of.
     * @author RedRiotTank
     */
    public static void spawnBloodParticles(Entity entity) {
        Location loc = entity.getLocation();
        World world = entity.getWorld();
        world.spawnParticle(Particle.REDSTONE, loc.add(0, entity.getHeight(), 0), 20, 0.5, 0.5, 0.5,
                0.1, new Particle.DustOptions(Color.RED, 1.0f));
    }

    /**
     * @brief Checks if the player is looking at a living entity in a distance.
     * @param player player that raycast.
     * @param distance of the raycast vector (int).
     * @return The first living entity if it is found, null if nothing is found.
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
     * @brief Checks if the player is looking at the back of a living entity.
     * @param player player who is looking at back.
     * @param livingEntity LivingEntity that's been seen.
     * @return true if player is looking at livingEntity's back. false if not.
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
     * @brief Creates a circle of particles in the direction the player is looking at.
     * @param radius Radius of the circle.
     * @param separation Separation between the particles.
     * @param prof How far away from the player you want the circle to appear.
     * @param dustOption Dust Option of the particle. Set it to null if your particle don't use dust options.
     * @param particle ParticleType of the circle.
     * @param harms true if particles harms Living Entities when touching them.
     * @param destroy true if particles destroy blocks when touching them.
     * @param player player that generates the circle next to.
     * @author RedRiotTank
     */
    public static void circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
                                       Particle particle, boolean harms, boolean destroy, Player player) {

        final Location playerLoc = player.getLocation();
        double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;
        final double yaw = -player.getLocation().getYaw(),
                pitch = player.getLocation().getPitch();
        final World world = player.getWorld();

        for(double i=0; i<2*PI; i+= separation) {
            x = radius*cos(i);
            y = radius*sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch))* y - sin(toRadians(pitch))* prof;
            zX = sin(toRadians(pitch))* y + cos(toRadians(pitch))* prof;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = playerLoc.getX() + xY;
            yL = 1 + playerLoc.getY() + yY;
            zL = playerLoc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);

            if(destroy)
                if(!partLoc.getBlock().getType().equals(Material.AIR))
                    partLoc.getBlock().setType(Material.AIR);

            if(harms)
                for(Entity ent : world.getNearbyEntities(partLoc,2,2,2))
                    if(ent instanceof LivingEntity && !player.equals(ent))
                        ((LivingEntity)ent).damage(100,(Entity) player);

            world.spawnParticle(particle,partLoc,0,0,0,0,dustOption);
        }
    }

    /**
     * @brief Creates a circle of particles in the direction the player is looking at.
     * @param radius Radius of the circle.
     * @param separation Separation between the particles.
     * @param prof How far away from the player you want the circle to appear.
     * @param dustOption Dust Option of the particle. Set it to null if your particle don't use dust options.
     * @param particle ParticleType of the circle.
     * @param player player that generates the circle next to.
     * @param loc center of the circle
     *
     * @see OPHLib#circleEyeVector()
     * @author RedRiotTank / Vaelico786
     */
    public static void circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
                                       Particle particle, Player player, Location loc) {

        double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;
        final double yaw = -player.getLocation().getYaw(),
                pitch = player.getLocation().getPitch();
        final World world = player.getWorld();

        for(double i=0; i<2*PI; i+= separation) {
            x = radius*cos(i);
            y = radius*sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch))* y - sin(toRadians(pitch))* prof;
            zX = sin(toRadians(pitch))* y + cos(toRadians(pitch))* prof;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = loc.getX() + xY;
            yL = 0.3 + loc.getY() + yY;
            zL = loc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);

            world.spawnParticle(particle,partLoc,0,0,0,0,dustOption);
        }
    }

    /**
     * @brief Returns true if given block is solid (Not air, water, ...).
     * @param block Given block.
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
     * @brief Transform loc into location (above or down) where it would be a normal block. It means that loc is on a solid
     * block, and it has air above him.
     * @param loc Actual Location.
     * @param y Actual 'y' we want to compare with.
     * @see OPHLib#getSolidRelativeUpper(Location, int)
     * @see OPHLib#getSolidRelativeDown(Location, int)
     * @author Vaelico786.
     */
    public static void getSolidRelativeY(Location loc, int y) {
        if(isSolidBlock(loc.getBlock()))
            loc = getSolidRelativeUpper(loc, y);
        else
            loc = getSolidRelativeDown(loc.add(0,-1,0), y);
    }

    /**
     * @brief Returns the first location starting at loc where it has an air block above.
     * @param loc Actual Location.
     * @param y Actual 'y' we want to compare with.
     * @author Vaelico786.
     */
    public static Location getSolidRelativeUpper(Location loc, int y) {
        Location loc2 = loc.clone().add(0,1,0);
        if(!isSolidBlock(loc2.getBlock()))
            return loc;
        else
        if(y > 0)
            return getSolidRelativeUpper(loc.add(0, 1, 0), y-1);
        else
            return null;
    }

    /**
     * @brief Returns the first location starting at loc where it has a solid block down.
     * @param loc Actual Location.
     * @param y Actual 'y' we want to compare with.
     * @author Vaelico786.
     */
    public static Location getSolidRelativeDown(Location loc, int y) {
        if(isSolidBlock(loc.getBlock()))
            return loc;
        else {
            if(y > 0)
                return getSolidRelativeDown(loc.add(0, -1, 0), y-1);
            else
                return null;
        }
    }

    /**
     * @brief Returns the falling block created. Transform block given into an equal falling block.
     * @param block Block we want to transform.
     * @author Vaelico786.
     */
    public static Entity setFallingBlock(Location block) {
        if (block.getBlock().getType() != Material.AIR && block.getBlock().getType() != Material.WATER &&
                block.getBlock().getType() != Material.BEDROCK) {

            if(block.getBlock().getState() instanceof Container) {
                block.getBlock().breakNaturally();
                return null;
            }
            Material matFallingBlock = block.getBlock().getType();
            block.getBlock().setType(Material.AIR);
            return block.getWorld().spawnFallingBlock(block, matFallingBlock, (byte) 9);
        }
        return null;
    }

    /**
     * @brief Modified yami-yami's livingVoidForEntity function. ???
     * @see yami_yami#livingVoidForEntity(Entity, Player)
     * @param ent Entity wanted to be repealed.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public static void catchEntity(Entity ent, Player player) {
        new BukkitRunnable() {
            Vector FirstVector;
            boolean fV = false;
            boolean entityInHand = false;
            double vx,vy,vz;
            int i = 0, j = 0;
            @Override
            public void run() {
                Location loc = player.getLocation().add(0,2,0);
                if(player.isDead() || ent.isOnGround())
                    this.cancel();

                vx =  loc.getX() - ent.getLocation().getX();
                vy =  loc.getY() - ent.getLocation().getY();
                vz =  loc.getZ() - ent.getLocation().getZ();

                Vector movement = loc.clone().toVector().subtract(ent.getLocation().toVector()).normalize();

                if(!fV) {
                    FirstVector = movement.clone();
                    fV = true;
                }

                //Para levantar al mob si hay desnivel
                if(loc.getY() >= ent.getLocation().getY() && !entityInHand)
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);

                if(!entityInHand) {
                    ent.setVelocity(movement);
                    i++;
                }
                else {
                    ent.teleport(player.getLocation().add(0,2,0));
                    ent.setVelocity(new Vector(0,0.042,0));
                    j++;
                }

                if(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2) +  Math.pow(vz,2)) <= 1) {
                    entityInHand = true;
                    if(!player.isSneaking()) {
                        this.cancel();
                        repealEntity(ent,player);
                    }
                }

                if(!player.isSneaking()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(abilities.plugin,0,1);
    }

    /**
     * @brief Modified yami-yami's repealEntity function. ???
     * @see yami_yami#repealEntity(Entity, Player)
     * @param ent Entity wanted to be repealed.
     * @param player Fruit's user.
     * @author Vaelico786.
     */
    public static void repealEntity(Entity ent, Player player) {
        ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT,10,2);

        Vector dir = player.getLocation().getDirection();
        ent.setVelocity(dir.multiply(3));

        //timer
        new BukkitRunnable() {
            Vector aux = dir;
            int i = 0;
            @Override
            public void run() {
                if(ent.getVelocity().getX() != aux.getX() || ent.getVelocity().getZ() != aux.getZ()) {
                    aux = ent.getVelocity();

                    ent.getWorld().getNearbyEntities(ent.getLocation(), 2,2,2).forEach(entity -> {
                        if(entity instanceof LivingEntity && entity != player && entity != ent) {
                            ((LivingEntity) entity).damage(14, (Entity) player);
                        }
                    });
                }
                if(ent.isOnGround() || i >= 120) {
                    cancelTask();
                }
                i++;
            }
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(abilities.plugin, 0, 2);
    }

    public static void breath(abilityUser user,Vector pos,Vector dir, int delay, int maxLength, int maxTicks, int dmg,double startAmpl, int startAmountParticles, double maxAmplitude, int period, String sound) {


        int iterations = maxTicks/period;



        new BukkitRunnable() {
            double density = startAmountParticles/startAmpl;
            double amplitude = startAmpl;
            double distance = 0;
            Player player = user.getPlayer();
            World world = player.getWorld();


            double particleAmount;
            int i = 0;
            @Override
            public void run() {

                Vector direction = player.getEyeLocation().getDirection().add(dir);
                Location location = player.getEyeLocation().add(pos);

                if(i==0 && !Objects.equals(sound, "none"))    //Play sound on first iteration
                    world.playSound(location, sound, 1, 1);



                if(i>iterations || user == null) {
                    cancelTask();
                }





                for(int j = 1; j<=distance; j++) {

                    if(amplitude < maxAmplitude)
                        amplitude += maxAmplitude /maxLength;


                    location.add(direction);
                    particleAmount = density * amplitude;
                    world.spawnParticle(Particle.BLOCK_CRACK, location, (int) particleAmount ,amplitude ,amplitude ,amplitude ,
                            0, Material.PACKED_ICE.createBlockData());



                    world.getNearbyEntities(location, amplitude, amplitude, amplitude).forEach(entity -> {
                        if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.damage(dmg, player);
                            if(livingEntity.getFreezeTicks() <= 0){
                                livingEntity.setFreezeTicks(600);

                            }
                        }
                    });

                }

                amplitude = startAmpl;

                System.out.println(distance);
                if(distance < maxLength)
                    distance+= ((double) maxLength)/(((double) iterations)/6.0);

                i++;

            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(abilities.plugin, delay, period);
    /**
     * @brief Generates an item on an invisible invulnerable stand
     * @author Vaelico786.
     */
    public static ArmorStand generateCustomFloatingItem(Location loc, ItemStack item, boolean gravity){
            ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
            // Establecer el objeto con Custom Model Data en la mano del armor stand
            armorStand.getEquipment().setHelmet(item);

            armorStand.setVisible(false);
            armorStand.setGravity(gravity);
            armorStand.setArms(false);
            armorStand.setInvulnerable(true);

            return armorStand;
    }
}
