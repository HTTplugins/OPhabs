package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.List;
import java.util.Random;

public class Goro_Goro extends Logia {

    public Goro_Goro(int id) {
        super(id, "goro_goro", "Goro Goro no Mi", "goro_goro", Particle.ELECTRIC_SPARK);

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("El THOR", 50, this::elThor));
        basicSet.addAbility(new CooldownAbility("ThunderStorm", 20, this::thunderStorm));
        basicSet.addAbility(new CooldownAbility("LightStep", 30, this::lightStep));
        basicSet.addAbility(new CooldownAbility("Discharge", 30, this::electricGround));
        this.abilitySets.add(basicSet);
    }

    public static int getFruitID() {
        return 1005;
    }

    @Override
    public void onAddFruit() {}

    @Override
    public void onRemoveFruit() {}

    @Override
    protected void logiaModeON() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;
        
        player.setAllowFlight(true);
    }

    @Override
    protected void logiaModeOFF() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;
        
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    // ---------------------------------------------- AB 1: El THOR ----------------------------------------------
    public void elThor() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.THUNDER, player.getLocation(), 1, 2);

        new BukkitRunnable() {
            int tick = 0;
            double fin = 10;

            @Override
            public void run() {
                if (user == null || !user.getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                Player p = user.getPlayer();
                
                for (double prof = 0.5; prof < fin; prof += 0.5) {
                    OPHLib.circleEyeVector(1, 0.5, prof, null, Particle.ELECTRIC_SPARK, true, true, p);
                    OPHLib.circleEyeVector(0.8, 0.5, prof, null, Particle.ELECTRIC_SPARK, true, true, p);
                    OPHLib.circleEyeVector(0.6, 0.5, prof, null, Particle.ELECTRIC_SPARK, true, true, p);
                    OPHLib.circleEyeVector(0.4, 0.5, prof, null, Particle.ELECTRIC_SPARK, true, true, p);
                    OPHLib.circleEyeVector(0.2, 0.5, prof, null, Particle.ELECTRIC_SPARK, true, true, p);
                }
                tick++;

                if (fin <= 20)
                    fin += 0.5;

                if (tick == 20)
                    this.cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    // ---------------------------------------------- AB 2: ThunderStorm ----------------------------------------------
    public void thunderStorm() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Random random = new Random();
        World world = player.getWorld();

        for (Entity ent : player.getNearbyEntities(10, 10, 10)) {
            if (ent.equals(player)) continue;
            world.strikeLightning(ent.getLocation());
            world.spawnParticle(Particle.ELECTRIC_SPARK, ent.getLocation(), 3);
        }

        for (int i = 0; i < 15; i++) {
            double xdecimals = random.nextDouble();
            double ydecimals = random.nextDouble();
            double zdecimals = random.nextDouble();

            double x = random.nextInt(4 + 4) - 4 + xdecimals + player.getLocation().getX();
            double y = random.nextInt(4 + 4) - 4 + ydecimals + player.getLocation().getY();
            double z = random.nextInt(4 + 4) - 4 + zdecimals + player.getLocation().getZ();

            double xdecimalsP = random.nextDouble();
            double ydecimalsP = random.nextDouble();
            double zdecimalsP = random.nextDouble();

            double xP = random.nextInt(4 + 4) - 4 + xdecimalsP + player.getLocation().getX();
            double yP = random.nextInt(4 + 4) - 4 + ydecimalsP + player.getLocation().getY();
            double zP = random.nextInt(4 + 4) - 4 + zdecimalsP + player.getLocation().getZ();
            
            world.strikeLightning(new Location(world, x, y, z));
            world.spawnParticle(Particle.CLOUD, new Location(world, xP, yP, zP), 30);
        }
    }

    // ---------------------------------------------- AB 3: LightStep ----------------------------------------------
    public void lightStep() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        world.playSound(playerLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);

        Vector dir = player.getLocation().getDirection();
        dir.setY(0.5);
        dir.setX(dir.getX() * 5);
        dir.setZ(dir.getZ() * 5);
        player.setVelocity(dir);

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (user == null || !user.getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                Player p = user.getPlayer();
                if (p != null) world.strikeLightning(p.getLocation().add(p.getLocation().getDirection().multiply(2)));
                tick++;

                if (tick == 6)
                    this.cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 2);
    }

    // ---------------------------------------------- AB 4: Discharge ----------------------------------------------
    public void electricGround() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        electricGroundAnimation(player);

        List<Entity> entities = player.getNearbyEntities(10, 10, 10);

        for (Entity ent : entities) {
            if (ent.equals(player)) continue;
            if (ent instanceof LivingEntity) {
                ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 10));
                new BukkitRunnable() {
                    final World world = ent.getWorld();
                    int ticks = 0;
                    
                    @Override
                    public void run() {
                        if (!ent.isValid()) {
                            cancel();
                            return;
                        }
                        ((LivingEntity) ent).damage(0.5, (Entity) user.getPlayer());
                        world.spawnParticle(Particle.ELECTRIC_SPARK, ent.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                        if (ticks == 5) this.cancel();
                        ticks++;
                    }
                }.runTaskTimer(OPhabs.getInstance(), 0, 2);
            }
        }
    }

    private void electricGroundAnimation(Player player) {
        new BukkitRunnable() {
            final World world = player.getWorld();
            double extension = 1;
            double density = 20;

            @Override
            public void run() {
                if (user == null || !user.getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                Player p = user.getPlayer();

                for (double i = 0; i < density; i++) {
                    double x = Math.random() * (p.getLocation().getX() + extension - (p.getLocation().getX() - extension)) + (p.getLocation().getX() - extension);
                    double z = Math.random() * (p.getLocation().getZ() + extension - (p.getLocation().getZ() - extension)) + (p.getLocation().getZ() - extension);
                    double y = OPHLib.searchGround(x, z, p.getLocation().getY(), world) + 0.2;

                    Location particle = new Location(world, x, y, z);
                    world.spawnParticle(Particle.ELECTRIC_SPARK, particle, 0, 0, 0, 0);
                }
                extension++;
                density *= 1.5;

                if (extension == 10) this.cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }
}
