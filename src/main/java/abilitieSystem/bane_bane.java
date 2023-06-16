package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import static java.lang.Math.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
/**
 * @brief Bane bane no mi ability Class.
 * @author Vaelico786.
 */
public class bane_bane extends paramecia {
    private boolean resort;
    // ---------------------------------------------- CONSTRUCTORS ---------------------------------------------------------------------
    /**
     * @brief bane_bane constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public bane_bane(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialBane, castIdentification.castItemNameBane, fruitIdentification.fruitCommandNameBane);
        abilitiesNames.add("Resort");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch Storm");
        abilitiesCD.add(0);


        resort=false;
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------
    /**
     * @brief Ability 1: "RESORT".
     * @see gura_gura#resort()
     * @author Vaelico786.
     */
    public void ability1() {
        resort();
    }

    /**
     * @brief CORE ABILITY 1: "RESORT". Convert user on a resort man, making him invulnerable to fall damage and
     * some extra effects more.
     * @author Vaelico786.
     */
    public void resort() {
        if(!resort){
            resort = true;
            user.getPlayer().sendMessage("Resort mode activated");
            new BukkitRunnable() {
                Player player = user.getPlayer();
                float jumpVelocity = 0;
                @Override
                public void run() {
                    if(!resort){
                        this.cancel();
                    }
                    if(player.isDead()){
                        resort = false;
                        this.cancel();
                    }
                    if(player.isOnGround() && player.isSneaking()){
                        if(jumpVelocity < 10){
                            jumpVelocity += 0.5;
                        }
                    }
                    
                    if(player.isOnGround() && !player.isSneaking()){
                        Vector velocity = player.getVelocity();
                        if(jumpVelocity > 0)
                            velocity.setY(jumpVelocity);
                        
                        if(player.isOnGround() && player.getFallDistance() > 0){
                                double v = (-3+sqrt(9-8*(-(player.getFallDistance()))))/4;
                                velocity.setY(v);
                        }
                        
                        if(player.isSprinting()){
                            velocity.setX(player.getEyeLocation().getDirection().getX() * 3);
                            velocity.setZ(player.getEyeLocation().getDirection().getZ() * 3);
                            if(velocity.getY() < 2)
                                velocity.setY(player.getEyeLocation().getDirection().getY() * 3);
                        }
                        player.setVelocity(velocity);
                        jumpVelocity = 0;
                    }
                }
            }.runTaskTimer(plugin, 0, 2);
            
        }
        else{
            user.getPlayer().sendMessage("Resort mode deactivated");
            resort = false;
        }
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------
    /**
     * @brief Ability 2: "Resort punch".
     * @see bane_bane#resortPunch()
     * @author Vaelico786.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            resortPunch(user.getPlayer().getLocation().add(user.getPlayer().getEyeLocation().clone().getDirection()).add(new Vector(0,0.7,0)));
            abilitiesCD.set(1, 10);
        }
    }

    /**
     * @brief CORE ABILITY 2: "Resort Punch". Unleash a punch in front of the player that deals extra damage.
     * @author Vaelico786.
     */
    public void resortPunch(Location loc) {
        new BukkitRunnable(){
            Entity entity = user.getPlayer().getWorld().spawnFallingBlock(loc, Material.SLIME_BLOCK.createBlockData());

            Location origin=entity.getLocation();

            Particle.DustOptions particle = new Particle.DustOptions(Color.fromRGB(128, 128, 128),0.5F);
            int ticks = 0;
            boolean first = true, back=false;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            @Override
            public void run() {
                if(first){
                    entity.setGravity(false);
                    entity.setVelocity(dir);
                    first=false;
                }

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent) {
                        ((LivingEntity) ent).damage(14);
                    }
                });

                aux = new Vector((entity.getLocation().getX()-origin.getX()), (entity.getLocation().getY()-origin.getY()), (entity.getLocation().getZ()-origin.getZ()));


                if(aux.length() < 4 && !back){
                circleEyeVector(0.4,0.1,-2,particle, Particle.REDSTONE,user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-1.8,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-1.6,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-1.4,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-1.2,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-1,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-0.8,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-0.6,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,-0.4,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                 circleEyeVector(0.4,0.1,-0.2,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());
                circleEyeVector(0.4,0.1,0,particle, Particle.REDSTONE, user.getPlayer(), entity.getLocation());

               }

                if(entity.isDead()){
                    entity.getLocation().getBlock().setType(Material.AIR);
                    // entity.getLocation().add(dir).getBlock().breakNaturally();
                    cancelTask();
                }
                if(aux.length() > 7 ){//|| aux.length() < 1)
                    entity.setVelocity(dir.multiply(-1));
                    
                    // entity.getLocation().add(dir).getBlock().breakNaturally();
                    back=true;
                }

                if(aux.length() < 0.5 && ticks>5 || ticks > 20){
                    
                    entity.remove();
                    // entity.getLocation().getBlock().breakNaturally();

                    
                    cancelTask();
                }
                
                ticks++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 2, 2);
    }


    /*
    public void springAnimation(double radius, double loops, double prof, Particle.DustOptions dustOption,
                                       Particle particle, Player player, Vector direction) {
        double separation = 0.1;
        final Location playerLoc = player.getLocation();
        double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;
        final double yaw = -(asin(direction.getX()) + acos(direction.getZ())),
                pitch = asin(direction.getY());
        final World world = player.getWorld();

        for(double i=0; i<2*PI*loops; i+= separation) {
            x = sin(i)/radius;
            y = cos(i)/radius;

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch))* y - sin(toRadians(pitch))+ i*separation;
            zX = sin(toRadians(pitch))* y + cos(toRadians(pitch))* i*separation;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = playerLoc.getX() + xY;
            yL = 1 + playerLoc.getY() + yY;
            zL = playerLoc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);
            

            world.spawnParticle(particle,partLoc,0,0,0,0,dustOption);
        }
    }
    */

    
    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------
    /**
     * @brief Ability 3: "Resort punch storm".
     * @see bane_bane#resortPunchStorm()
     * @author Vaelico786.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            new BukkitRunnable(){
                int attacks=7, count=0;
                double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;

                Location loc = user.getPlayer().getLocation();
                final double yaw = -loc.getYaw(),
                        pitch = loc.getPitch();
                final World world = user.getPlayer().getWorld();
                double separation=1, radius=0.4;
                double i=0;

                @Override
                public void run() {

        
            x = radius*cos(i);
            y = radius*sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch))* y - sin(toRadians(pitch));
            zX = sin(toRadians(pitch))* y + cos(toRadians(pitch));

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = loc.getX() + xY;
            yL = 1 + loc.getY() + yY;
            zL = loc.getZ() + zY;
            i+=separation;

            Location partLoc = new Location(world, xL, yL, zL);

                    resortPunch(partLoc);
                    count++;
                    if(count>=attacks)
                        cancelTask();
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 2, 3);



            abilitiesCD.set(2, 50);
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
    // ---------------------------------------------- PASSIVES ---------------------------------------------------------------------
    /**
     * @brief On fall listener.
     * @see bane_bane#onFall()
     * @author Vaelico786.
     */
    public void onFall(EntityDamageEvent event) {
        if(active){
            if(resort){
                event.setCancelled(true);
                Player player = user.getPlayer();
                if(!player.isSneaking()){
                    Vector velocity = player.getVelocity();
                    double v = (-3.7+sqrt(9-8*(-(player.getFallDistance()))))/4;
                    velocity.setY(v);
                    if(player.isSprinting()){
                        velocity.setX(player.getEyeLocation().getDirection().getX() * 4);
                        velocity.setZ(player.getEyeLocation().getDirection().getZ() * 4);
                    }
                    player.setVelocity(velocity);
                }
                user.getPlayer().setFallDistance(0);
            }
       } 
    }



    /**
     * @brief Cancels fallingblock --> block change to make the absorbing effect, and saves the blocks materials information.
     * @param event EntityChangeBlockEvent that Minecraft client sends to server to convert blocks.
     * @see bane_bane#resortPunch(boolean)
     * @note Called in his correspondent in the caster, as a listener.
     * @author RedRiotTank.
     */
    public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getBlock().getType().equals(Material.SLIME_BLOCK)) {
            event.setCancelled(true);
        }
    }

}
