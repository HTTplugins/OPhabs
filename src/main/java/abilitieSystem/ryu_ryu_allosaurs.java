package abilitieSystem;

import htt.ophabs.OPhabs;
import fruitSystem.devilFruitUser;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import java.util.ArrayList;

public class ryu_ryu_allosaurs extends zoan {

    public ryu_ryu_allosaurs(OPhabs plugin){
        super(plugin, castIdentification.castMaterialRyuAllosaurs, castIdentification.castItemNameRyuAllosaurs, fruitIdentification.fruitCommandNameRyuAllosaurs,"http://novask.in/5915273049.png","allosaurs");

        abilitiesNames.add("FrontalCrunch");
        abilitiesCD.add(0);
        abilitiesNames.add("TailSpin");
        abilitiesCD.add(0);
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            frontCrunch();
            abilitiesCD.set(1, 3);
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            tailSpin();
            abilitiesCD.set(2, 5);
        }
    }


    public void transformation(){
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed){
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 4, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 7, false, false));
                }
                else{
                    user.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                    user.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    //launch player in the lookin direction
    public void frontCrunch(){
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>5*2)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                if(entity.getName() != player.getName() && entity instanceof LivingEntity)
                    ((LivingEntity) entity).damage(15);
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 4);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        player.setVelocity(direction.multiply(1.5));
    }

    public void tailSpin(){
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
                player.getWorld().getNearbyEntities(player.getLocation(), 3,1,3).forEach(entity -> {
                    if(entity.getName() != player.getName() && entity instanceof LivingEntity)
                        ((LivingEntity) entity).damage(15);
                });



        new BukkitRunnable() {
            int i = 0, radius = 3, particleAmount = 5;

            @Override
            public void run() {
                if(i>16)
                    cancelTask();

                World world = player.getWorld();
                for (double angle = 0; angle < particleAmount; angle += 0.01) {
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.log(angle);
                    
                    Location particleLoc = player.getLocation().add(x, y, z);
                    world.spawnParticle(Particle.REDSTONE, particleLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 1));
                }

                Location loc = player.getLocation();
                loc.setYaw(loc.getYaw()+20);

                player.teleport(loc);


                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 1);

    }

}
