package fruits.paramecia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.Particle;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.Color;
import users.OPUser;

import java.util.Random;

public class Suke_Suke extends Paramecia {
    private static final int EXPLORATION_DURATION = 6000;
    private static final int ATTACK_DURATION = 400;
    private boolean invisible = false;
    private boolean exploration = false;
    private boolean cancelStopInvisibleTask = false;
    private boolean failedBackstab = false;

    public static int getFruitID() {
        return 1015;
    }

    public Suke_Suke(int id) {
        super(id, "suke_suke", "Suke Suke no Mi", "suke_suke");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Invisible Exploration", 7000, this::invisibleExploration));
        basicSet.addAbility(new CooldownAbility("BackStab", 15, this::backStab));
        basicSet.addAbility(new CooldownAbility("Invisible Attack", 50, this::invisibleAttack));
        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {
        invisible = false;
        exploration = false;
    }

    @Override
    public void onRemoveFruit() {
        invisible = false;
        exploration = false;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void animation(Player player, double rad) {
        new BukkitRunnable() {
            double radius = rad;
            World world = player.getWorld();
            double yDiscount = 0;
            double incrementRate = 0.25;
            double ticks = 0;

            @Override
            public void run() {
                circle(player.getLocation().getY() + 1, yDiscount, incrementRate);
                incrementRate = incrementRate - incrementRate / 5;
                yDiscount += 0.2;
                radius += 0.08;
                ticks++;
                if (ticks == 5) cancel();
            }

            public void circle(double yParticle, double yDiscount, double increment) {
                for (double i = 0; i < 2 * Math.PI; i += 0.2) {
                    double xParticle = radius * Math.cos(i) + player.getLocation().getX();
                    double zParticle = radius * Math.sin(i) + player.getLocation().getZ();
                    Location particleLocationUP = new Location(world, xParticle, yParticle + yDiscount, zParticle);
                    Location particleLocationDOWN = new Location(world, xParticle, yParticle - yDiscount, zParticle);
                    world.spawnParticle(Particle.CLOUD, particleLocationUP, 0, 0, 0, 0);
                    world.spawnParticle(Particle.CLOUD, particleLocationDOWN, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    public void invisibility(Player player) {
        invisible = true;
        int time = exploration ? 6000 : 400;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, time, 99, false, false));
        animation(player, 0.5);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player other : Bukkit.getOnlinePlayers())
                    other.hidePlayer(OPhabs.getInstance(), player);
            }
        }.runTaskLater(OPhabs.getInstance(), 5);
    }

    public void finishInvisibility(Player player) {
        invisible = false;
        exploration = false;
        animation(player, 0.5);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player other : Bukkit.getOnlinePlayers())
                    other.showPlayer(OPhabs.getInstance(), player);
            }
        }.runTaskLater(OPhabs.getInstance(), 5);
    }

    public void invisibleExploration() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (invisible) {
            player.sendMessage("You are already invisible.");
            return;
        }

        exploration = true;
        invisibility(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cancelStopInvisibleTask)
                    finishInvisibility(player);
                cancelStopInvisibleTask = false;
            }
        }.runTaskLater(OPhabs.getInstance(), 6000);
    }

    public void cancelExploration(Player player) {
        if (exploration) {
            finishInvisibility(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        } else
            player.sendMessage("You are not in exploration mode");
    }

    public void backStab() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        LivingEntity target = OPHLib.rayCastLivEnt(player, 2);
        if (target != null) {
            boolean backLooking = OPHLib.isLookingBack(player, target);
            if (backLooking) {
                spawnParticleBehind(target);
                player.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                if (exploration) finishInvisibility(player);
                target.damage(5, user.getPlayer());
            } else
                failedBackstab = true;
        } else
            failedBackstab = true;
    }

    private static void spawnParticleBehind(LivingEntity entity) {
        Location entityLocation = entity.getLocation().add(0, 1, 0);
        Vector direction = entityLocation.getDirection().normalize().multiply(-1);
        Location particleLocation = entityLocation.clone().add(direction);
        entity.getWorld().spawnParticle(Particle.DUST, particleLocation, 0, 0, 0, 0,
                new Particle.DustOptions(Color.RED, 2.0f));
    }

    public void invisibleAttack() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (invisible) {
            player.sendMessage("You are already invisible.");
            return;
        }

        invisibility(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                finishInvisibility(player);
            }
        }.runTaskLater(OPhabs.getInstance(), 400);
    }

    @Override
    public void onEntityDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        OPUser user = this.getUser();
        if (exploration && user != null) {
            cancelStopInvisibleTask = true;
            user.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            finishInvisibility(user.getPlayer());
        }
    }

}
