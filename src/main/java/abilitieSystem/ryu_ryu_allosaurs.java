package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static java.lang.Math.*;

public class ryu_ryu_allosaurs extends zoan {
    private ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList<>();

    public ryu_ryu_allosaurs(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialRyuAllosaurs, castIdentification.castItemNameRyuAllosaurs,
                fruitIdentification.fruitCommandNameRyuAllosaurs,"http://novask.in/5915273049.png","allosaurs");

        abilitiesNames.add("FrontalCrunch");
        abilitiesCD.add(0);
        abilitiesNames.add("TailSpin");
        abilitiesCD.add(0);
        abilitiesNames.add("Splash");
        abilitiesCD.add(0);
    }

    public void ability2() {
        if(abilitiesCD.get(1) == 0){
            frontCrunch();
            abilitiesCD.set(1, 3);
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            tailSpin();
            abilitiesCD.set(2, 5);
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            Splash(user.getPlayer());
            abilitiesCD.set(3, 5);
        }
    }

    public void transformation() {
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed){
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 4, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2, false, false));
                }
                else{
                    user.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                    user.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    //launch player in the looking direction
    public void frontCrunch() {
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>5*2)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !golpeadosHabilidades.contains(entity)) {
                        ((LivingEntity) entity).damage(15);
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                        golpeadosHabilidades.add((LivingEntity) entity);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
                i++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 4);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        player.setVelocity(direction.multiply(1.5));
        golpeadosHabilidades.clear();
    }

    public void tailSpin() {
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));

        player.getWorld().getNearbyEntities(player.getLocation(), 3,1,3).forEach(entity -> {
            if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity)
                ((LivingEntity) entity).damage(15);
        });

        int radius = 3, particleAmount = 3;
        World world = player.getWorld();
        for (double angle = 0; angle < particleAmount; angle += 0.01) {
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = Math.log(angle);
                    
            Location particleLoc = player.getLocation().add(x, y, z);
            world.spawnParticle(Particle.REDSTONE, particleLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 1));
        }


        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>16)
                    cancelTask();

                Location loc = player.getLocation();
                loc.setYaw(loc.getYaw()-20);

                player.teleport(loc);
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void Splash(Player player) {
        animacionSplash(player.getWorld(), player.getLocation().clone().add(0, 0.25, 0));
        efectoSplash(player.getWorld(), player.getLocation());
    }

    public void animacionSplash(World mundo, Location loc) {
        for(int j = 0; j < 5; j++)
            for(int i = 0; i < 10; i++) {
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(i)*j, 0, cos(i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(-i)*j, 0, cos(i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(i)*j, 0, cos(-i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(-i)*j, 0, cos(-i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(i)*j, 0, cos(i)/2*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(-i)*j, 0, cos(i)/2*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(i)*j, 0, cos(-i)/2*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(sin(-i)*j, 0, cos(-i)/2*j), 0, 0, 0, 0);
            }
    }

    public void efectoSplash(World mundo, Location loc) {
        mundo.getNearbyEntities(loc, 5, 3, 5).forEach(entity -> {
            if (!entity.getName().equals(user.getPlayerName()) && entity instanceof LivingEntity) {
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), entity.getLocation().getY() - loc.getY(),
                                        entity.getLocation().getZ() - loc.getZ()).normalize();

                ((LivingEntity) entity).damage(10);
                entity.setVelocity(dir.add(new Vector(0, 2, 0)));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
            }
        });
    }
}
