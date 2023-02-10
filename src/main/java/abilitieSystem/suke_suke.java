package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static java.lang.Math.PI;

/**
 * @brief suke_suke no mi ability Class.
 * @author RedRiotTank
 */
public class suke_suke extends paramecia {

    private final int explorationDuration = 6000;

    private static boolean invisible = false;
    public static boolean exploration = false;

    public static boolean cancelStopInvisibleTask = false;

    public static OPhabs plug;

    /**
     * @brief suke_suke constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank.
     */
    public suke_suke(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialSuke, castIdentification.castItemNameSuke, fruitIdentification.fruitCommandNameSuke);
        abilitiesNames.add("Invisible Exploration");
        abilitiesCD.add(0);
        abilitiesNames.add("BackStab");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);
        plug = plugin;
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------

    /**
     * @brief Ability 1 caller: "Invisible Exploration". Shift + click to cancel it.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
    public void ability1() {
        if (!user.getPlayer().isSneaking()) {
            if (abilitiesCD.get(0) == 0) {
                invisibleExploration(user.getPlayer());
                abilitiesCD.set(0, 7000); // Pon el cooldown en segundos
            }
        } else 
            cancelExploration(user.getPlayer());
        
    }

    /**
     * @brief CORE ABILITY: Makes players invisible during explorationDuration time. If the players attacks,
     * the invisibility gets canceled.
     * @param player User that gets invisible.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
    public void invisibleExploration(Player player) {
        if (!invisible) {
            exploration = true;

            invisibility(player);

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!cancelStopInvisibleTask)
                        finishInvisibility(player);


                    cancelStopInvisibleTask = false;

                }
            }.runTaskLater(plugin, explorationDuration);


        } else {
            player.sendMessage("You are alredy invisible.");
        }
    }

    /**
     * @brief CORE ABILITY: Invisible boolean getter.
     * @return true if any kind of invisibility of the fruit is activated.
     * @author RedRiotTank.
     */
    public static boolean getInvisible() {
        return invisible;
    }

    /**
     * @brief ANIMATION: creates a progressive cylinder around the player.
     * @param player User that gets animation.
     * @param rad initial radius of the cylinder.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
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

    /**
     * @brief make the player invisible.
     * @param player User that gets invisible.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
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

    /**
     * @brief make the player visible again.
     * @param player User that stops being invisible.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
    public void finishInvisibility(Player player) {
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

    /**
     * @brief Cancel Invisible Exploration ability.
     * @param player User that cancel exploration mode.
     * @see suke_suke#invisibleExploration(Player)
     * @author RedRiotTank.
     */
    public void cancelExploration(Player player) {

        if (exploration) {
            finishInvisibility(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        } else
            player.sendMessage("You are not in exploration mode");

    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    /**
     * @brief Ability 2 caller: "BackStab".
     * @see suke_suke#backStab(Player)
     * @author RedRiotTank.
     */
    public void ability2() {
        if (abilitiesCD.get(1) == 0) {
            backStab(user.getPlayer());
            abilitiesCD.set(1, 15); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Backstab an entity the player is looking.
     * @param player User that uses the ability.
     * @author RedRiotTank.
     */
    public void backStab(Player player) {

        LivingEntity target = auxBiblio.rayCastLivEnt(player,2);

        if (target != null) {
            boolean backLooking = auxBiblio.isLookingBack(player, target);

            if (backLooking){
                spawnParticleBehind(target);
                player.getWorld().playSound(target.getLocation(),"swordcut",1,1);
                target.damage(5);
            }
        }

    }

    /**
     * @brief spawns a single red redstone particle behind the entity.
     * @param entity Entity you want to spawn blood.
     * @author RedRiotTank.
     */
    public static void spawnParticleBehind(LivingEntity entity) {
        Location entityLocation = entity.getLocation().add(0,1,0);
        Vector direction = entityLocation.getDirection().normalize().multiply(-1);
        Location particleLocation = entityLocation.clone().add(direction);
        entity.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0,0,0,0,new Particle.DustOptions(Color.RED, 2.0f));
    }


    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------
    /**
     * @brief Ability 3 caller: "-".
     * @author RedRiotTank.
     */
    public void ability3() {
        if (abilitiesCD.get(2) == 0) {

            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4 caller: "-".
     * @author RedRiotTank.
     */
    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }





}