package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.Random;

public class Mera_Mera extends Logia {

    private static final int FIRE_POOL_COOLDOWN = 15;
    private static final int FIREBALL_STORM_COOLDOWN = 5;
    private static final int FAIYABU_COOLDOWN = 5;
    private static final int FIREBREATH_COOLDOWN = 10;

    public static int getFruitID() {
        return 1006;
    }

    public Mera_Mera(int id) {
        super(id, "Mera_Mera", "Mera Mera no Mi", "Mera_Mera", Particle.FLAME);

        // Basic Set - Logia body
        AbilitySet basicSet = new AbilitySet("Logia Body");

        // Fire Pool
        basicSet.addAbility(new CooldownAbility("Fire Pool", FIRE_POOL_COOLDOWN, this::firePool));

        // Fireball Storm
        basicSet.addAbility(new CooldownAbility("Fireball Storm", FIREBALL_STORM_COOLDOWN, this::fireballStorm));

        // Faiyabu
        basicSet.addAbility(new CooldownAbility("Faiyabu", FAIYABU_COOLDOWN, this::faiyabu));

        // Fire Breath
        basicSet.addAbility(new CooldownAbility("Fire Breath", FIREBREATH_COOLDOWN, this::fireBreath));

        this.abilitySets.add(basicSet);
    }

    @Override
    protected void logiaModeON() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
            }
        }
    }

    @Override
    protected void logiaModeOFF() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
        }
        super.logiaModeOFF();
    }

    @Override
    public void onAddFruit() {
        OPUser user = this.getUser();
        if (user != null) {
            user.getPlayer().setFireTicks(0);
        }
    }

    @Override
    public void onRemoveFruit() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
        }
    }

    // Fire Pool - Creates fire around player
    public void firePool() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.HEAVYGRAVITY, player.getLocation(), 1, 1);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 3) { cancel(); return; }

                OPHLib.circleEyeVector(3, 0.5, 0, null, Particle.FLAME, false, false, player);
                OPHLib.circleEyeVector(4, 0.5, 0, null, Particle.LAVA, false, false, player);
                OPHLib.circleEyeVector(5, 0.5, 0, null, Particle.FLAME, false, false, player);

                // Damage nearby entities
                for (Entity ent : player.getNearbyEntities(5, 3, 5)) {
                    if (ent instanceof LivingEntity && !ent.equals(player)) {
                        ((LivingEntity) ent).setFireTicks(100);
                        ((LivingEntity) ent).damage(4, player);
                    }
                }

                ticks++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 20);
    }

    // Fireball Storm - Launch fireballs at nearby enemies
    public void fireballStorm() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);

        for (Entity ent : player.getNearbyEntities(10, 6, 10)) {
            if (ent instanceof LivingEntity && !ent.equals(player)) {
                Vector dir = ent.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                
                for (int i = 0; i < 3; i++) {
                    Location spawnLoc = player.getLocation().clone().add(0, 1.5, 0).add(dir.clone().multiply(3 + i * 2));
                    Fireball fireball = player.getWorld().spawn(spawnLoc, Fireball.class);
                    fireball.setVelocity(dir.multiply(1.5));
                    fireball.setShooter(player);
                    fireball.setYield(2.0f);
                }
            }
        }
    }

    // Faiyabu - Fire beam attack
    public void faiyabu() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection();
        World world = player.getWorld();

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, loc, 1, 1);

        new BukkitRunnable() {
            int steps = 0;
            Location current = loc.clone().add(0, -0.5, 0);
            
            @Override
            public void run() {
                if (steps > 30) { cancel(); return; }

                current.add(direction.clone().multiply(1.5));
                
                // Particle trail
                world.spawnParticle(Particle.FLAME, current, 10, 0.5, 0.5, 0.5, 0.1);
                world.spawnParticle(Particle.LAVA, current, 5, 0.3, 0.3, 0.3, 0);
                
                // Damage entities in path
                for (Entity ent : world.getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (ent instanceof LivingEntity && !ent.equals(player)) {
                        LivingEntity target = (LivingEntity) ent;
                        target.setFireTicks(100);
                        target.damage(8, player);
                        world.createExplosion(current, 2.0f, true, false, player);
                    }
                }
                
                steps++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 0);
    }

    // Fire Breath - Cone attack
    public void fireBreath() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        // Custom fire breath implementation
        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection();
        World world = player.getWorld();

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, loc, 1, 1);

        new BukkitRunnable() {
            int steps = 0;
            Location current = loc.clone();
            
            @Override
            public void run() {
                if (steps > 20) { cancel(); return; }

                current.add(direction.clone().multiply(1.2));
                
                // Cone of fire
                for (double r = 0.5; r < 8; r += 0.5) {
                    Location coneLoc = player.getEyeLocation().clone().add(direction.clone().multiply(r));
                    double radius = r * 0.3;
                    world.spawnParticle(Particle.FLAME, coneLoc, 5, radius, radius, radius, 0);
                    world.spawnParticle(Particle.LAVA, coneLoc, 2, radius, radius, radius, 0);
                    
                    // Damage in cone
                    for (Entity ent : world.getNearbyEntities(coneLoc, radius, radius, radius)) {
                        if (ent instanceof LivingEntity && !ent.equals(player)) {
                            LivingEntity target = (LivingEntity) ent;
                            target.setFireTicks(60);
                            target.damage(3, player);
                        }
                    }
                }
                
                steps++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 0);
    }

    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        // Call parent for water check
        super.onPlayerMove(event);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (isFruitActive() && event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
            event.setCancelled(true);
        }
    }
}
