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

public class Ryu_Ryu_Allosaurs extends Zoan {
    private final int FRONTAL_CRUNCH_COOLDOWN = 3;
    private final int TAIL_SPIN_COOLDOWN = 5;
    private final int STOMP_COOLDOWN = 5;
    private final List<LivingEntity> golpeadosHabilidades = new ArrayList<>();
    private List<LivingEntity> stompedList;

    public static int getFruitID() {
        return 1017;
    }

    public Ryu_Ryu_Allosaurs(int id) {
        super(id, "ryu_ryu_allosaurs", "Ryu Ryu no Mi: Model Allosaurus", "ryu_ryu_allosaurs");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Frontal Crunch", FRONTAL_CRUNCH_COOLDOWN, this::frontCrunch));
        basicSet.addAbility(new CooldownAbility("Tail Spin", TAIL_SPIN_COOLDOWN, this::tailSpin));
        basicSet.addAbility(new CooldownAbility("Stomp", STOMP_COOLDOWN, this::stomp));
        basicSet.addAbility(new CooldownAbility("Transformation", 5, this::transformation));
        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {}

    @Override
    public void onRemoveFruit() {}

    public void frontCrunch() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                if (i > 5 * 2) { cancel(); return; }

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1, 1, 1).forEach(entity -> {
                    if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !golpeadosHabilidades.contains(entity)) {
                        ((LivingEntity) entity).damage(15, user.getPlayer());
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                        golpeadosHabilidades.add((LivingEntity) entity);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 4);
        
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        player.setVelocity(direction.multiply(1.5));
        golpeadosHabilidades.clear();
    }

    public void tailSpin() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.setVelocity(player.getLocation().getDirection().multiply(2));

        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 3, 1, 3)) {
            if (entity instanceof LivingEntity && !entity.equals(player))
                ((LivingEntity) entity).damage(15, user.getPlayer());
        }

        int radius = 3;
        World world = player.getWorld();
        for (double angle = 0; angle < 2 * Math.PI; angle += 0.01) {
            double x = radius * Math.cos(angle);
            double y = Math.log(angle + 1);
            double z = radius * Math.sin(angle);
            
            Location particleLoc = player.getLocation().add(x, y, z);
            world.spawnParticle(Particle.ENCHANT, particleLoc, 1, 0, 0, 0, 0);
        }

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i > 16) { cancel(); return; }

                Location loc = player.getLocation();
                loc.setYaw(loc.getYaw() - 20);
                player.teleport(loc);
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    public void stomp() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        final World world = player.getWorld();
        stompedList = new ArrayList<>();
        player.setVelocity(new Vector(0, -50, 0));
        
        new BukkitRunnable() {
            int tickNumber = 0;

            @Override
            public void run() {
                Location loc = player.getLocation();
                ++tickNumber;
                
                if (!player.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    animationStomp(world, player.getLocation());
                    world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    
                    stompedList.forEach(livingEntity -> {
                        livingEntity.damage(15, player);
                    });
                    cancel();
                }

                player.setFallDistance(0);

                world.getNearbyEntities(loc, 5.0, 1.0, 5.0).forEach(entity -> {
                    if (entity instanceof LivingEntity && !stompedList.contains(entity) && !entity.equals(player)) {
                        stompedList.add((LivingEntity) entity);
                        if (entity instanceof Player) {
                            ((Player) entity).setFlying(false);
                        }
                        entity.setVelocity(new Vector(0, -50, 0));
                    }
                });
            }
        }.runTaskTimer(OPhabs.getInstance(), 0L, 1L);
        stompedList.clear();
    }

    private void animationStomp(World world, Location loc) {
        for (int j = 0; j < 5; ++j) {
            for (int i = 0; i < 10; ++i) {
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(i) * j, 1.0, Math.cos(i) * j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(-i) * j, 1.0, Math.cos(i) * j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(i) * j, 1.0, Math.cos(-i) * j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(-i) * j, 1.0, Math.cos(-i) * j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(i) * j, 1.0, Math.cos(i) / 2.0 * j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin(-i) * j, 1.0, Math.cos(-i) / 2.0 * j), 0, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        super.onPlayerMove(event);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
    }

    @Override
    public void onPlayerToggleSneak(org.bukkit.event.player.PlayerToggleSneakEvent e) {
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
            }.runTaskTimer(OPhabs.getInstance(), 0, 3);
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
