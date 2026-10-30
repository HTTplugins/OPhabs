package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;

public class Inu_Inu_Urufu extends Zoan {
    private String regular = "regular_wolf";
    private String rage = "rage_wolf";
    private boolean raged = false;
    private boolean crunch = false;
    private int rageTime = 60; // Seconds
    private ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList<>();

    public static int getFruitID() {
        return 1007;
    }

    public Inu_Inu_Urufu(int id) {
        super(id, "inu_inu_urufu", "Inu Inu no Mi: Model Wolf", "inu_inu_urufu");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Crunch", 5, this::frontCrunch));
        basicSet.addAbility(new CooldownAbility("Multiple Crunches", 15, this::multipleCrunch));
        basicSet.addAbility(new CooldownAbility("Angry Roar", 10, this::angryRoar));
        basicSet.addAbility(new CooldownAbility("Transformation", 5, this::transformation));
        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {
        raged = false;
        crunch = false;
    }

    @Override
    public void onRemoveFruit() {
        raged = false;
        crunch = false;
    }

    @Override
    public void transformation() {
        super.transformation();

        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (transformed) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 2, false, false));
                } else {
                    if (raged) {
                        raged = false;
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    } else {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                    }
                }
            }
        }.runTaskLater(OPhabs.getInstance(), 20);
    }

    public void multipleCrunch() {
        new BukkitRunnable() {
            int i = 0;
            int nAttacks = 5, ticksPerAttack = 10;

            @Override
            public void run() {
                OPUser user = Inu_Inu_Urufu.this.getUser();
                if (i > nAttacks * ticksPerAttack || user == null || !user.getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                if (i % ticksPerAttack == 0)
                    frontCrunch();

                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 5, 1);
    }

    // Launch player in the looking direction
    public void frontCrunch() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        double multiplier = player.isOnGround() ? 1.5 : 0.75;
        player.setVelocity(player.getLocation().getDirection().multiply(multiplier));
        crunch = true;

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                OPUser user = Inu_Inu_Urufu.this.getUser();
                if (user == null || !user.getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                Player player = user.getPlayer();
                
                if (i % 4 == 0) player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                if (i > 5 * 8) {
                    cancel();
                    return;
                }

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1, 1, 1).forEach(entity -> {
                    if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.damage(15, (Entity) player);
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
                i++;
            }
            public void cancelTask() {
                crunch = false;
                cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
        
        Vector direction = player.getLocation().getDirection();
        player.setVelocity(direction.multiply(1.5));
        golpeadosHabilidades.clear();
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (event.getEntity() == player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && crunch) {
                event.setCancelled(true);
            }

            if (player.getHealth() < player.getMaxHealth() / 2)
                if (!raged)
                    rageMode();
        }
    }

    public void rageMode() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        raged = true;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 3, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 2, false, false));
            }
        }.runTaskLater(OPhabs.getInstance(), 20);

        // Turn off after rageTime seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (raged) {
                    raged = false;
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.REGENERATION);
                    player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);

                    transformed = false;
                    transformation();
                }
            }
        }.runTaskLater(OPhabs.getInstance(), rageTime * 20);
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Material material = event.getItem().getType();
        if (material == Material.BEEF || 
            material == Material.CHICKEN || 
            material == Material.COOKED_BEEF || 
            material == Material.COOKED_CHICKEN ||
            material == Material.COOKED_MUTTON ||
            material == Material.COOKED_PORKCHOP ||
            material == Material.COOKED_RABBIT ||
            material == Material.PORKCHOP ||
            material == Material.RABBIT || 
            material == Material.MUTTON) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1, false, false));
        }
    }

    public void angryRoar() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        ArrayList<org.bukkit.potion.PotionEffect> effects = new ArrayList<>();
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 1));
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS, 100, 1));
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NAUSEA, 100, 1));

        roar(player, 10, effects);
    }
}
