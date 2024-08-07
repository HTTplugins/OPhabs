package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ryu_ryu_allosaurs extends zoan {
    private ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList();
    private ArrayList<LivingEntity> StompedList;

    public ryu_ryu_allosaurs(OPhabs plugin) {
        super(plugin, 6.0, 9.0, 11, "ryu_ryu_allosaurs", "Ryu Ryu no Mi Moderu Allosaurs", "Ryu Ryu Allosaurs caster", 7.0, 2.0, "http://novask.in/5915273049.png", "allosaurs");
        this.abilitiesNames.add("FrontalCrunch");
        this.abilitiesCD.add(0);
        this.abilitiesNames.add("TailSpin");
        this.abilitiesCD.add(0);
        this.abilitiesNames.add("Stomp");
        this.abilitiesCD.add(0);
    }

    public void ability2() {
        if ((Integer)this.abilitiesCD.get(1) == 0) {
            this.frontCrunch();
            this.abilitiesCD.set(1, 3);
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
            Stomp(user.getPlayer());
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
                }
                else{
                    user.getPlayer().removePotionEffect(PotionEffectType.SLOW);
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
                player.getWorld().playSound(player.getLocation(), "crunch", 1, 1);
                if(i>5*2)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !golpeadosHabilidades.contains(entity)) {
                        ((LivingEntity) entity).damage(15, (Entity) user.getPlayer());
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
                ((LivingEntity) entity).damage(15, (Entity) user.getPlayer());
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

    public void Stomp(final Player player) {
        final World world = player.getWorld();
        this.StompedList = new ArrayList();
        player.setVelocity(new Vector(0, -50, 0));
        (new BukkitRunnable() {
            int tickNumber = 0;

            public void run() {
                Location loc = player.getLocation();
                ++this.tickNumber;
                if (!player.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    ryu_ryu_allosaurs.this.animationStomp(world, player.getLocation());
                    world.playSound(player.getLocation(), "stomp", 1, 1);
                    ryu_ryu_allosaurs.this.StompedList.forEach((livingEntity) -> {
                        livingEntity.damage(15.0, player);
                    });
                    this.cancel();
                }

                player.setFallDistance(0);

                world.getNearbyEntities(loc, 5.0, 1.0, 5.0).forEach((entity) -> {
                    if (entity instanceof LivingEntity && !ryu_ryu_allosaurs.this.StompedList.contains(entity) && !entity.equals(player)) {
                        ryu_ryu_allosaurs.this.StompedList.add((LivingEntity)entity);
                        if (entity instanceof Player) {
                            ((Player)entity).setFlying(false);
                        }

                        entity.setVelocity(new Vector(0, -50, 0));
                    }

                });
            }
        }).runTaskTimer(plugin, 0L, 1L);
        this.StompedList.clear();
    }

    public void animationStomp(World mundo, Location loc) {
        for(int j = 0; j < 5; ++j) {
            for(int i = 0; i < 10; ++i) {
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)i) * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)i) * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)(-i)) * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)(-i)) * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)i) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)i) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)(-i)) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                mundo.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)(-i)) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
            }
        }
    }
}
