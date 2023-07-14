package abilitieSystem;

import htt.ophabs.OPhabs;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static java.lang.Math.*;

public class neko_neko_reoparudo extends zoan {
    public final int FrontalAttackCD = 3, TsumeCD = 4, FearsomeCD = 5;

    private final ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList<>();

    public neko_neko_reoparudo(OPhabs plugin) {
        super(plugin, 4, 2, 5, "neko_neko_reoparudo", "Neko Neko no Mi Moderu Reoparudo", "Neko Neko Reoparudo caster", 7, 2,"https://s.namemc.com/i/18525c829f6918fe.png","reoparudo");

        abilitiesNames.add("FrontalAttack");
        abilitiesCD.add(0);
        abilitiesNames.add("Tsume");
        abilitiesCD.add(0);
        abilitiesNames.add("Neko Fearsome");
        abilitiesCD.add(0);
    }

    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            frontAttack();
            abilitiesCD.set(1, FrontalAttackCD);
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            Tsume(user.getPlayer());
            abilitiesCD.set(2, TsumeCD);
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            NekoFearsome(user.getPlayer());
            abilitiesCD.set(3, FearsomeCD);
        }
    }

    public void transformation() {
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed) {
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, false, false));
                }
                else {
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    // Launch player in the looking direction.

    public void frontAttack() {
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>5*2)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getLocation(), 1,2,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !golpeadosHabilidades.contains(entity)) {
                        ((LivingEntity) entity).damage(15);
                        Sangrado((LivingEntity) entity, 100);
                        golpeadosHabilidades.add((LivingEntity) entity);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 10, 0.5, 1.5, 0.5, 0.1);
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 4);

        golpeadosHabilidades.clear();
        player.setVelocity(player.getLocation().getDirection().multiply(1.5));
    }

    public void Tsume(Player player) {
        Location loc = player.getLocation();

        giroTsume(player);
        animacionPlayerTsume(player.getWorld(), player.getLocation().clone());

        player.getWorld().getNearbyEntities(loc, 4, 2, 4).forEach(entity -> {
            if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), entity.getLocation().getY() - loc.getY(),
                        entity.getLocation().getZ() - loc.getZ()).normalize();

                ((LivingEntity) entity).damage(15);

                player.getWorld().playSound(loc, Sound.ENTITY_CAT_PURREOW, 100, 10);

                animacionTsume(player.getWorld(), ((LivingEntity) entity).getEyeLocation());
                entity.setVelocity(dir.multiply(3));
                Sangrado((LivingEntity) entity, 100);
            }
        });
    }

    public void giroTsume(Player player) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i > 4)
                    cancelTask();

                Location loc = player.getLocation().clone().setDirection(player.getLocation().getDirection().rotateAroundY(24));

                player.teleport(loc);
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 0);
    }

    public void animacionTsume(World mundo, Location loc) {
        new BukkitRunnable() {
            int i = 0;
            final Particle particula = Particle.CRIT;
            @Override
            public void run() {
                if(i > 10)
                    this.cancel();

                for(double i = -2; i < 2; i+=0.5) {
                    mundo.spawnParticle(particula, loc.clone().add(i, i, i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-i, i, i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(i, i, -i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-i, i, -i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(i, -i, i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-i, -i, i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(i, -i, -i), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-i, -i, -i), 0, 0, 0, 0);
                }

                i++;
            }
        }.runTaskTimer(plugin, 0, 0);
    }

    public void animacionPlayerTsume(World mundo, Location loc) {
        final Particle particula = Particle.CRIT;

        for(double i = 0; i < 2.0; i+=0.3) {
            for(double j = -2*PI; j < 2*PI; j+=0.05) {
                double  x = sin(j),
                        z = cos(j);

                mundo.spawnParticle(particula, loc.clone().add(x, i, z), 0, 0, 0, 0);
            }
        }
    }

    public void NekoFearsome(Player player) {
        Location loc = player.getLocation();

        player.getWorld().getNearbyEntities(loc, 10, 5, 10).forEach(entity -> {
            if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
            }
        });

        animacionNekoFearsome(player.getWorld(), player.getEyeLocation().clone());
    }

    public void animacionNekoFearsome(World mundo, Location loc) {
        for(int j = 0; j < 10; j++)
            for(int i = 0; i < 10; i++) {
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(i)*j, 0, cos(i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(-i)*j, 0, cos(i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(i)*j, 0, cos(-i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(-i)*j, 0, cos(-i)*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(i)*j, 0, cos(i)/2*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(-i)*j, 0, cos(i)/2*j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(sin(i)*j, 0, cos(-i)/2*j), 0, 0, 0, 0);
            }
    }

    public void climbWall(Player player) {
        Location loc = player.getEyeLocation().add(player.getLocation().getDirection()); 
        if(transformed && loc.getBlock().getType()!= Material.AIR){
            Vector vector = new Vector(0, 0.5, 0);
            player.setVelocity(vector);
        }
    }

    public void onFall(EntityDamageEvent e) {
        if (super.user != null && e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getName() == super.user.getPlayer().getName()){
            e.setCancelled(true);
        }
    }

    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(player.getName() == user.getPlayer().getName()){
            new BukkitRunnable() {
                @Override
                public void run() {
                if (player.isSneaking()) {
                    climbWall(player);
                }
                else
                    cancelTask();
                }
                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 3);
        }
    }
}

