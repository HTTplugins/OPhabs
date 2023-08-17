package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Material;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class inu_inu_urufu extends zoan {
    String regular = "regular_wolf", rage = "rage_wolf";
    Boolean raged, crunch;
    int rageTime = 60; // Seconds
    private ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList<>();

    public inu_inu_urufu(OPhabs plugin) {
        super(plugin, 3.5, 2, 17, "inu_inu_urufu", "Inu Inu no Mi Moderu Urufu", "Inu Inu Urufu caster", 7, 2,"http://novask.in/6399673459.png","regular_wolf");

        abilitiesNames.add("Crunch");
        abilitiesCD.add(0);
        abilitiesNames.add("Multiple Crunches");
        abilitiesCD.add(0);
        abilitiesNames.add("Angry Roar");
        abilitiesCD.add(0);

        raged = false;
        crunch = false;


        skinC.setSkin(rage, "http://novask.in/6279193716.png");
    }

    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            abilitiesCD.set(1, 5);
            frontCrunch();
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            abilitiesCD.set(2, 15);
            multipleCrunch();
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            abilitiesCD.set(3, 10);
            angryRoar();

        }
    }

    public void transformation() {
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed) {
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 2, false, false));
                }
                else {
                    if(raged){
                        raged = false;
                        user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                        user.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                        user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                        user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                    else{
                        user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                        user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    }
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    public void multipleCrunch() {

        new BukkitRunnable() {
            int i = 0;
            int nAttacks = 5, ticksPerAttack = 10;

            @Override
            public void run() {
                if(i>nAttacks*ticksPerAttack || user == null || !user.getPlayer().isOnline()) {
                    cancelTask();
                }
                if(i%ticksPerAttack == 0)
                    frontCrunch();

                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 5, 1);
        
    }
   


    //launch player in the looking direction
    public void frontCrunch() {
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(3));
        crunch = true;

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i % 4 == 0) player.getWorld().playSound(player.getLocation(), "crunch", 1, 1);
                if(i>5*8)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(15,(Entity) user.getPlayer());
                        Sangrado(((LivingEntity) entity), 200);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
                i++;
            }
            public void cancelTask() {
                crunch = false;
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 1);
        Vector direction = player.getLocation().getDirection();
        player.setVelocity(direction.multiply(3));
        golpeadosHabilidades.clear();
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() == user.getPlayer()) {
            if(event.getCause() == DamageCause.FALL && crunch) {
                event.setCancelled(true);
            }

            if(user.getPlayer().getHealth() < user.getPlayer().getMaxHealth()/2)
                if(!raged)
                    rageMode();
        }
    }

    public void rageMode(){
        raged = true;
        user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "roar", 1, 1);

        skinC.changeSkin(user.getPlayer(), rage);
        
        new BukkitRunnable() {
            @Override
            public void run() {
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2, false, false));
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1, false, false));
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 2, false, false));
            }
        }.runTaskLater(plugin, 20);

        //Turn off on 60 seconds

        new BukkitRunnable() {
            @Override
            public void run() {
                if(raged){
                    raged = false;
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);

                    transformed =false;
                    transformation();
                }
            }
        }.runTaskLater(plugin, rageTime*20);
    }

    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Material material = event.getItem().getType();
        if (material == Material.BEEF || 
            material == Material.CHICKEN || 
            material == Material.PORKCHOP ||
            material == Material.RABBIT || 
            material == Material.MUTTON) 
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1, false, false));
    }

    public void angryRoar(){
        ArrayList<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.SLOW, 100, 1));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));

        roar(user.getPlayer(), 10, effects);
    }

}

