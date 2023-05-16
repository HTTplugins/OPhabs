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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import static abilitieSystem.OPHLib.*;
import static java.lang.Math.*;

/**
 * @brief Bane bane no mi ability Class.
 * @author Vaelico786.
 */
public class bane_bane extends paramecia {
    private boolean resort;
    private Vector fallVelocity;
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
                double high = 0;
                boolean down = false;
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
            resortPunch();
            abilitiesCD.set(1, 0);
        }
    }

    /**
     * @brief CORE ABILITY 2: "Resort Punch". Unleash a punch in front of the player that deals extra damage.
     * @author Vaelico786.
     */
    public void resortPunch() {
        double finish = 2* PI*5 - 2* PI*5/10;
        new BukkitRunnable(){
            Entity entity = user.getPlayer().getWorld().spawnFallingBlock(user.getPlayer().getEyeLocation().add(user.getPlayer().getEyeLocation().getDirection()), Material.SLIME_BLOCK.createBlockData());
            boolean first = true;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            @Override
            public void run() {
                if(first){
                    entity.setGravity(false);
                    entity.setVelocity(dir);
                }



                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent) {
                        ((LivingEntity) ent).damage(14);
                    }
                });

                aux = new Vector((entity.getLocation().getX()-user.getPlayer().getLocation().getX()), (entity.getLocation().getY()-user.getPlayer().getLocation().getY()), (entity.getLocation().getZ()-user.getPlayer().getLocation().getZ()));

                springAnimation(4, 10, 10, new Particle.DustOptions(Color.BLACK,0.5F), Particle.REDSTONE, user.getPlayer(), aux);
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 2, 2);
    


        user.getPlayer().sendMessage("Resort punch!");
    }

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
}
