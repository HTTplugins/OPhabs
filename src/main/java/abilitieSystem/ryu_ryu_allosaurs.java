//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package abilitieSystem;

import htt.ophabs.OPhabs;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        if ((Integer)this.abilitiesCD.get(2) == 0) {
            this.tailSpin();
            this.abilitiesCD.set(2, 5);
        }

    }

    public void ability4() {
        if ((Integer)this.abilitiesCD.get(3) == 0) {
            this.Stomp(this.user.getPlayer());
            this.abilitiesCD.set(3, 5);
        }

    }

    public void transformation() {
        super.transformation();
        (new BukkitRunnable() {
            public void run() {
                if (ryu_ryu_allosaurs.this.transformed) {
                    ryu_ryu_allosaurs.this.user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1, false, false));
                } else {
                    ryu_ryu_allosaurs.this.user.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                }

            }
        }).runTaskLater(plugin, 20L);
    }

    public void frontCrunch() {
        final Player player = this.user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        (new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (this.i > 10) {
                    this.cancelTask();
                }

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1.0, 1.0, 1.0).forEach((entity) -> {
                    if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !ryu_ryu_allosaurs.this.golpeadosHabilidades.contains(entity)) {
                        ((LivingEntity)entity).damage(15.0, ryu_ryu_allosaurs.this.user.getPlayer());
                        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                        ryu_ryu_allosaurs.this.golpeadosHabilidades.add((LivingEntity)entity);
                    }

                });
                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0.0, 0.0, 0.0, 0.0);
                ++this.i;
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }).runTaskTimer(plugin, 0L, 4L);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        player.setVelocity(direction.multiply(1.5));
        this.golpeadosHabilidades.clear();
    }

    public void tailSpin() {
        final Player player = this.user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        player.getWorld().getNearbyEntities(player.getLocation(), 3.0, 1.0, 3.0).forEach((entity) -> {
            if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                ((LivingEntity)entity).damage(15.0, this.user.getPlayer());
            }

        });
        int radius = 3;
        int particleAmount = 3;
        World world = player.getWorld();

        for(double angle = 0.0; angle < (double)particleAmount; angle += 0.01) {
            double x = (double)radius * Math.cos(angle);
            double z = (double)radius * Math.sin(angle);
            double y = Math.log(angle);
            Location particleLoc = player.getLocation().add(x, y, z);
            world.spawnParticle(Particle.REDSTONE, particleLoc, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.GREEN, 1.0F));
        }

        (new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (this.i > 16) {
                    this.cancelTask();
                }

                Location loc = player.getLocation();
                loc.setYaw(loc.getYaw() - 20.0F);
                player.teleport(loc);
                ++this.i;
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }).runTaskTimer(plugin, 0L, 1L);
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
                    ryu_ryu_allosaurs.this.StompedList.forEach((livingEntity) -> {
                        livingEntity.damage(15.0, player);
                    });
                    this.cancel();
                }

                world.getNearbyEntities(loc, 5.0, 1.0, 5.0).forEach((entity) -> {
                    if (entity instanceof LivingEntity && !ryu_ryu_allosaurs.this.StompedList.contains(entity)) {
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
