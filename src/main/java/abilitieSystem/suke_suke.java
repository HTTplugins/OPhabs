package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static java.lang.Math.PI;


public class suke_suke extends paramecia {

    private int explorationDuration = 6000;

    private static boolean invisible = false;
    public static boolean exploration = false;

    public static boolean cancelStopInvisibleTask = false;

    public static OPhabs plug;

    public suke_suke(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialSuke, castIdentification.castItemNameSuke, fruitIdentification.fruitCommandNameSuke);
        abilitiesNames.add("Invisible Exploration");
        abilitiesCD.add(0);
        abilitiesNames.add("BackStab");
        abilitiesCD.add(0);
        abilitiesNames.add("Invisible Crew");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);
        plug = plugin;
    }

    public void ability1() {

        if (!user.getPlayer().isSneaking()) {
            if (abilitiesCD.get(0) == 0) {
                invisibleExploration(user.getPlayer());
                abilitiesCD.set(0, 7000); // Pon el cooldown en segundos
            }
        } else {
            cancelExploration(user.getPlayer());
        }
    }

    public void ability2() {
        if (abilitiesCD.get(1) == 0) {
            backStab(user.getPlayer());

            abilitiesCD.set(1, 15); // Pon el cooldown en segundos
        }
    }

    public void ability3() {
        if (abilitiesCD.get(2) == 0) {
            animation(user.getPlayer(),0.5);
            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    public void invisibleExploration(Player player) {
        if (!invisible) {
            exploration = true;

            invisibility(player);

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!cancelStopInvisibleTask)
                        uninvisibility(player);


                    cancelStopInvisibleTask = false;

                }
            }.runTaskLater(plugin, explorationDuration);


        } else {
            player.sendMessage("You are alredy invisible.");
        }
    }

    public static boolean getInvisible() {
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

                if (ticks == 5) this.cancel();
            }

            public void circle(double yParticle, double yDiscount, double increment) {
                for (double i = 0; i < 2 * PI; i += 0.2) {
                    double xParticle = radius * Math.cos(i) + player.getLocation().getX();
                    double zParticle = radius * Math.sin(i) + player.getLocation().getZ();

                    Location particleLocationUP = new Location(world, xParticle, yParticle + yDiscount, zParticle);
                    Location particleLocationDOWN = new Location(world, xParticle, yParticle - yDiscount, zParticle);
                    world.spawnParticle(Particle.CLOUD, particleLocationUP, 0, 0, 0, 0);
                    world.spawnParticle(Particle.CLOUD, particleLocationDOWN, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void invisibility(Player player) {
        invisible = true;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, explorationDuration, 99, false, false));
        animation(player,0.5);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player other : Bukkit.getOnlinePlayers())
                    other.hidePlayer(plugin, player);
            }
        }.runTaskLater(plugin, 5);


    }

    public void uninvisibility(Player player) {
        invisible = false;
        exploration = false;

        animation(player,0.5);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    other.showPlayer(plugin, player);
                }
            }
        }.runTaskLater(plugin, 5);


    }

    public void cancelExploration(Player player) {

        if (exploration) {
            uninvisibility(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        } else
            player.sendMessage("You are not in exploration mode");

    }

    public void backStab(Player player) {

        LivingEntity target;

        Vector direction = player.getEyeLocation().getDirection();
        RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), direction, 2, p -> !player.getUniqueId().equals(p.getUniqueId()));
        if (result != null && result.getHitEntity() != null) {
            target = (LivingEntity) result.getHitEntity();

            boolean backLooking = isLookingBack(player, target);

            if (backLooking){
                spawnParticleBehind(target);
                player.getWorld().playSound(target.getLocation(),"swordcut",1,1);
                target.damage(5);
            }
        }

    }

    public boolean isLookingBack(Player player, LivingEntity livingEntity) {


        Vector pDir = player.getLocation().getDirection();
        Vector eDir = livingEntity.getLocation().getDirection();
        double xv = pDir.getX() * eDir.getZ() - pDir.getZ() * eDir.getX();
        double zv = pDir.getX() * eDir.getX() + pDir.getZ() * eDir.getZ();
        double angle = Math.atan2(xv, zv); // Value between -π and +π
        double angleInDegrees = (angle * 180) / Math.PI;

        if (angleInDegrees <= 60 && angleInDegrees >= -32)
            return true;


        return false;
    }

    public static void spawnParticleBehind(LivingEntity entity) {
        Location entityLocation = entity.getLocation().add(0,1,0);
        Vector direction = entityLocation.getDirection().normalize().multiply(-1);
        Location particleLocation = entityLocation.clone().add(direction);
        entity.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0,0,0,0,new Particle.DustOptions(Color.RED, 2.0f));
    }

}