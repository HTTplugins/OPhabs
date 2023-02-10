package abilitieSystem;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;


/**
 * Common class created to make easier the development of abilities.
 *
 * In this class we put multiples parameterized functions that we create for a class and that we think other could use in theirs.
 */
public class auxBiblio {

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
    public static double searchGround(double x, double z, double initialY, World world){

        Location loc = new Location(world,x,initialY,z);

        for(int i=0; i < 40; i++)
            if(loc.getBlock().getRelative(0,-i,0).getType().isSolid())
                return initialY - i + 1;

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
        world.spawnParticle(Particle.REDSTONE, loc.add(0, entity.getHeight(), 0), 20, 0.5, 0.5, 0.5, 0.1, new Particle.DustOptions(Color.RED, 1.0f));
    }

    /**
     * @brief Checks if the player is looking at a living entity in a distance.
     * @param player player that raycast.
     * @param distance of the raycast vector (int).
     * @return The first living entity if it is found, null if nothing is found.
     * @author RedRiotTank
     */
    public static LivingEntity rayCastLivEnt(Player player, int distance){
        LivingEntity target = null;

        Vector direction = player.getEyeLocation().getDirection();
        RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), direction, distance, p -> !player.getUniqueId().equals(p.getUniqueId()));
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

        if (angleInDegrees <= 60 && angleInDegrees >= -32)
            return true;


        return false;
    }
}
