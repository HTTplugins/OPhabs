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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.List;

public class Neko_Neko_Reoparudo extends Zoan {
    private final int FrontalAttackCD = 3, TsumeCD = 4, FearsomeCD = 5;
    private final List<LivingEntity> golpeadosHabilidades = new ArrayList<>();

    public static int getFruitID() {
        return 1013;
    }

    public Neko_Neko_Reoparudo(int id) {
        super(id, "neko_neko_reoparudo", "Neko Neko no Mi: Model Leopard", "neko_neko_reoparudo");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Frontal Attack", FrontalAttackCD, this::frontAttack));
        basicSet.addAbility(new CooldownAbility("Tsume", TsumeCD, this::tsume));
        basicSet.addAbility(new CooldownAbility("Neko Fearsome", FearsomeCD, this::nekoFearsome));
        basicSet.addAbility(new CooldownAbility("Transformation", 5, this::transformation));
        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {}

    @Override
    public void onRemoveFruit() {}

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = e.getPlayer();
        if (player.getName().equals(user.getPlayer().getName())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isSneaking()) {
                        climbWall(player);
                    } else {
                        cancel();
                    }
                }
                public void cancelTask() {
                    cancel();
                }
            }.runTaskTimer(OPhabs.getInstance(), 0, 3);
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent e) {
        OPUser user = this.getUser();
        if (user == null) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getName().equals(user.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    // Launch player in the looking direction
    public void frontAttack() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i % 2 == 0) player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                if (i > 5 * 2) {
                    cancel();
                    return;
                }

                player.getWorld().getNearbyEntities(player.getLocation(), 1, 2, 1).forEach(entity -> {
                    if (entity instanceof LivingEntity && !golpeadosHabilidades.contains(entity) && !entity.equals(player)) {
                        LivingEntity target = (LivingEntity) entity;
                        target.damage(15, user.getPlayer());
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
                        golpeadosHabilidades.add(target);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 10, 0.5, 1.5, 0.5, 0.1);
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 4);
    }

    public void tsume() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Location loc = player.getLocation();
        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 100, 10);
        giroTsume(player);
        animacionPlayerTsume(player.getWorld(), player.getLocation().clone());

        for (Entity entity : player.getWorld().getNearbyEntities(loc, 4, 2, 4)) {
            if (entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), entity.getLocation().getY() - loc.getY(),
                        entity.getLocation().getZ() - loc.getZ()).normalize();

                ((LivingEntity) entity).damage(15, (Entity) user.getPlayer());
                player.getWorld().playSound(loc, Sound.ENTITY_CAT_PURREOW, 100, 10);
                animacionTsume(player.getWorld(), ((LivingEntity) entity).getEyeLocation());
                entity.setVelocity(dir.multiply(3));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
            }
        }
    }

    public void giroTsume(Player player) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i > 4)
                    cancel();

                Location loc = player.getLocation().clone().setDirection(player.getLocation().getDirection().rotateAroundY(24));
                player.teleport(loc);
                i++;
            }

            public void cancelTask() {
                cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 0);
    }

    public void animacionTsume(World mundo, Location loc) {
        new BukkitRunnable() {
            int i = 0;
            final Particle particula = Particle.CRIT;
            @Override
            public void run() {
                if (i > 10)
                    cancel();

                for (double j = -2; j < 2; j += 0.5) {
                    mundo.spawnParticle(particula, loc.clone().add(j, j, j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-j, j, j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(j, j, -j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-j, j, -j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(j, -j, j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-j, -j, j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(j, -j, -j), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, loc.clone().add(-j, -j, -j), 0, 0, 0, 0);
                }
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 0);
    }

    public void animacionPlayerTsume(World mundo, Location loc) {
        final Particle particula = Particle.CRIT;
        for (double i = 0; i < 2.0; i += 0.3) {
            for (double j = -2 * Math.PI; j < 2 * Math.PI; j += 0.05) {
                double x = Math.sin(j),
                        z = Math.cos(j);
                mundo.spawnParticle(particula, loc.clone().add(x, i, z), 0, 0, 0, 0);
            }
        }
    }

    public void nekoFearsome() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Location loc = player.getLocation();
        for (Entity entity : player.getNearbyEntities(10, 5, 10)) {
            if (entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 200, 3));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
            }
        }
        animacionNekoFearsome(player.getWorld(), player.getEyeLocation().clone());
    }

    public void animacionNekoFearsome(World mundo, Location loc) {
        for (int j = 0; j < 10; j++)
            for (int i = 0; i < 10; i++) {
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(i) * j, 0, Math.cos(i) * j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(-i) * j, 0, Math.cos(i) * j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(i) * j, 0, Math.cos(i) * j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(-i) * j, 0, Math.cos(i) * j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(i) * j, 0, Math.cos(i) / 2 * j), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.ENCHANT, loc.clone().add(Math.sin(-i) * j, 0, Math.cos(i) / 2 * j), 0, 0, 0, 0);
            }
    }

    public void climbWall(Player player) {
        Location loc = player.getEyeLocation().add(player.getLocation().getDirection());
        if (transformed && loc.getBlock().getType() != Material.AIR) {
            Vector vector = new Vector(0, 0.5, 0);
            player.setVelocity(vector);
        }
    }
}
