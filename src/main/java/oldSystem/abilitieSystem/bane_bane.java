package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static java.lang.Math.*;
import java.util.ArrayList;

/**
 * @brief Bane bane no mi ability Class.
 * @author Vaelico786.
 */
public class bane_bane extends paramecia {
    private boolean resort;
    private ItemStack glove;

    private int resortPersecutionCD = 20;
    // ---------------------------------------------- CONSTRUCTORS ---------------------------------------------------------------------
    /**
     * @brief bane_bane constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public bane_bane(OPhabs plugin) {
        super(plugin, 4, 3, 15, "bane_bane", "Bane Bane no Mi" ,"Bane Bane caster", 5, 2);
        abilitiesNames.add("Resort");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch Storm");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Persecution");
        abilitiesCD.add(0);

        // Crear el ItemStack con el objeto "raw iron"
        glove = new ItemStack(castIdentification.castMaterial);
        ItemMeta itemMeta = glove.getItemMeta();
        itemMeta.setCustomModelData(getFruit().getCustomModelDataId()); // Establecer el Custom Model Data
        glove.setItemMeta(itemMeta);
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
        if (!resort) {
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
                        if (jumpVelocity > 0) {
                            player.getWorld().playSound(player.getLocation(), "resort", 1, 1);
                            velocity.setY(jumpVelocity);
                        }
                        
                        if (player.isOnGround() && player.getFallDistance() > 0) {
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
            
        } else {
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
            abilitiesCD.set(1, 5);
        }
    }

    /**
     * @brief CORE ABILITY 2: "Resort Punch". Unleash a punch in front of the player that deals extra damage.
     * @author Vaelico786.
     */
    public void resortPunch(Location loc) {

            ArmorStand armorStand = OPHLib.generateCustomFloatingItem(user.getPlayer().getLocation().add(0,-0.5,0), glove, false);

        user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "resort", 1, 1);

        new BukkitRunnable(){

            // Configurar las propiedades del armor stand

            Entity entity = armorStand;
            Location origin=entity.getLocation();

            final Particle.DustOptions particle = new Particle.DustOptions(Color.fromRGB(128, 128, 128),0.5F);
            int ticks = 0;
            boolean first = true, back=false;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            double distancePerTick = 5.0 / 7.0, distanceFromPlayer = 0;
            
            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void run() {
                distanceFromPlayer+=distancePerTick;
                Vector movement = dir.clone().multiply(distanceFromPlayer);
                entity.teleport(origin.clone().add(movement));

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent && !(ent instanceof ArmorStand)) {
                        ((LivingEntity) ent).damage(14,(Entity) user.getPlayer());
                    }
                });

                aux = new Vector((entity.getLocation().getX()-origin.getX()), (entity.getLocation().getY()-origin.getY()), (entity.getLocation().getZ()-origin.getZ()));


                if(!back){
                    Location temp = entity.getLocation().add(0,1,0);
                    for(double i = -2; i<0; i+=0.2)
                        OPHLib.circleEyeVector(0.4,0.4,i,particle, Particle.REDSTONE,user.getPlayer(), temp);
               }

                if (entity.isDead()) {
                    entity.getLocation().getBlock().setType(Material.AIR);

                    cancelTask();
                }

                if (aux.length() > 7 ){//|| aux.length() < 1)
                    distancePerTick*=-1;
                    back=true;
                }

                if (aux.length() < 0.5 && ticks>10 || ticks > 40) {
                    entity.remove();
                    cancelTask();
                }
                
                ticks++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 2, 1);
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------
    /**
     * @brief Ability 3: "Resort punch storm".
     * @see bane_bane#resortPunchStorm()
     * @author Vaelico786.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            new BukkitRunnable(){
                final int attacks=7;
                int count=0;
                double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;

                final Location loc = user.getPlayer().getLocation();
                final double yaw = -loc.getYaw(),
                        pitch = loc.getPitch();
                final World world = user.getPlayer().getWorld();
                final double separation=1;
                final double radius=0.4;
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

            abilitiesCD.set(2, 30);
        }
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "Resort Persecution".
     * @see bane_bane#resortPersecution(Player)
     * @author MiixZ.
     */
    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            resortPersecution(user.getPlayer());
            abilitiesCD.set(3, resortPersecutionCD);
        }
    }

    /**
     * @brief CORE ABILITY 4: "Resort Persecution". Selecciona a todas las entidades en línea recta para hacerles un ataque frontal.
     * @param player User player
     * @author MiixZ, Vaelico786.
     */
    public void resortPersecution(Player player) {
        Location loc = player.getEyeLocation().clone();
        ArrayList<LivingEntity> selected = new ArrayList<>();

        loc.add(0, -0.5, 0);


        player.getWorld().getNearbyEntities(loc, 20, 2, 20).forEach(entity -> {
            if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
                selected.add((LivingEntity) entity);
            }
        });


        player.setVelocity(new Vector(0, 1, 0));
        
        if(selected.size()>0)
        new BukkitRunnable() {
            int time = 0;
            int timeOut=8;
            LivingEntity entity = selected.remove(0);
            public void run() {
                if (selected.size() == 0 || entity == null)
                    this.cancel();
                if(entity.isDead()) {
                    entity = selected.remove(0);
                    user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "resort", 1, 1);
                }

                Vector dir = entity.getLocation().toVector().subtract(player.getLocation().toVector());
                dir.normalize();
                player.setVelocity(dir.clone().multiply(2));
                for(Entity ent : player.getNearbyEntities(2, 3, 2)){
                    if(ent instanceof LivingEntity && ent != player && !(entity instanceof ArmorStand)){
                        ((LivingEntity) ent).damage(20, player);

                        if(ent == entity){
                            if(selected.size()>0) {
                                user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "resort", 1, 1);
                                entity = selected.remove(0);
                            }
                            else
                                entity = null;
                        }
                        else{
                            if(selected.contains(ent)){
                                if(selected.size()>0) {
                                    user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "resort", 1, 1);
                                    selected.remove(ent);
                                }
                            }
                        }
                    }
                    time = 0;
                }
                if(entity != null){
                    if(player.getLocation().add(dir).getBlock().getType() != Material.AIR || time == timeOut){
                        if(selected.size()>0) {
                            user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "resort", 1, 1);
                            entity = selected.remove(0);
                        }
                        time = 0;
                    }
                }
                time++;
            }
        }.runTaskTimer(plugin, 20, 1);
    }


    // ---------------------------------------------- PASSIVES ---------------------------------------------------------------------
    /**
     * @brief On fall listener.
     * @see bane_bane#onFall()
     * @author Vaelico786.
     */
    public void onFall(EntityDamageEvent event) {
        if (active) {
            if (resort) {
                event.setCancelled(true);
                Player player = user.getPlayer();
                if (!player.isSneaking()) {
                    player.playSound(player.getLocation(), "resort", 1, 1);
                    Vector velocity = player.getVelocity();
                    double v = (-3.7+sqrt(9-8*(-(player.getFallDistance()))))/4;
                    velocity.setY(v);
                    if (player.isSprinting()) {
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
