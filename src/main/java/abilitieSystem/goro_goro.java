package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @brief Goro goro no mi ability Class.
 * @author RedRiotTank
 */
public class goro_goro extends logia {

    /**
     * @brief goro_goro constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank.
     */
    public goro_goro(OPhabs plugin) {
        super(plugin, 0, 0, Particle.ELECTRIC_SPARK, 7, "goro_goro", "Goro Goro no Mi", "Goro Goro caster",  6, 2);

        abilitiesNames.add("El THOR");
        abilitiesCD.add(0);
        abilitiesNames.add("ThunderStorm");
        abilitiesCD.add(0);
        abilitiesNames.add("LightStep");
        abilitiesCD.add(0);
        abilitiesNames.add("Discharge");
        abilitiesCD.add(0);
        this.runParticles();
    }

    /**
     * @brief goro_goro logia form particles + sound.
     * @todo fix the line.
     * @author RedRiotTank.
     */
    @Override
    public void runParticles() {
        /*
        new BukkitRunnable(){

            @Override
            public void run() {

                Player player = null;

                if(user != null)
                    if(user.getPlayer() != null){

                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null){
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameGoro)){
                            double angle = toRadians(-player.getLocation().getYaw());

                            for(double i=0; i < 2*PI; i+=0.05){
                                double x = cos(i);
                                double y = sin(i);
                                double z = 0;

                                double xr = cos(angle)*x + sin(angle)*z;
                                double yr = y;
                                double zr = -sin(angle)*x + cos(angle)*z;

                                double xF = player.getLocation().getX() + xr-0.25*sin(angle);
                                double yF = player.getLocation().getY() + yr + 2;
                                double zF = player.getLocation().getZ() + zr-0.25*cos(angle);

                                Location loc = new Location(player.getWorld(),xF,yF,zF);
                                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,loc,0,0,0,0);

                                for(Entity ent : player.getWorld().getNearbyEntities(loc,0.5,0.5,0.5))
                                    if (ent instanceof LivingEntity)
                                            ((LivingEntity) ent).damage(1);

                            }
                            player.setAllowFlight(true);
                        }else {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }
            }


        }.runTaskTimer(plugin,0,2);
        */
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------

    /**
     * @brief Ability 1: "EL THOR".
     * @see goro_goro#elThor(Player)
     * @author RedRiotTank.
     */
    public void ability1() {
        if(abilitiesCD.get(0) == 0) {
            elThor(user.getPlayer());
            abilitiesCD.set(0, 50);
        }
    }

    /**
     * @brief CORE ABILITY: creates a thunder ray thar destroy blocks and makes damage.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void elThor(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,2);

        new BukkitRunnable() {
            int tick = 0;
            double fin = 10;

            @Override
            public void run() {
                for(double prof = 0.5; prof < fin; prof+=0.5) {
                    OPHLib.circleEyeVector(1,0.5,prof,null, element,true,true, user.getPlayer());
                    OPHLib.circleEyeVector(0.8,0.5,prof,null, element,true,true, user.getPlayer());
                    OPHLib.circleEyeVector(0.6,0.5,prof,null, element,true,true, user.getPlayer());
                    OPHLib.circleEyeVector(0.4,0.5,prof,null, element,true,true, user.getPlayer());
                    OPHLib.circleEyeVector(0.2,0.5,prof,null, element,true,true, user.getPlayer());
                }
                tick++;

                if(fin <= 20)
                    fin +=0.5;

                if(tick == 20)
                    this.cancel();
            }
        }.runTaskTimer(plugin,0,1);
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    /**
     * @brief Ability 2: "ThunderStorm".
     * @see goro_goro#thunderStorm(Player)
     * @author RedRiotTank.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            thunderStorm(user.getPlayer());
            abilitiesCD.set(1, 20);
        }
    }

    /**
     * @brief CORE ABILITY: Creates a thunderstorm that hits every entity in [10,10,10] blocks.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void thunderStorm(Player player) {
        Random random = new Random();

        World world = player.getWorld();
        for(Entity ent : player.getNearbyEntities(10,10,10)) {
            world.strikeLightning(ent.getLocation());
            world.spawnParticle(Particle.ELECTRIC_SPARK,ent.getLocation(),3);
        }

        for(int i=0; i<15; i++) {
            double xdecimals = random.nextDouble();
            double ydecimals = random.nextDouble();
            double zdecimals = random.nextDouble();

            double x = random.nextInt(4 + 4 ) - 4 + xdecimals + player.getLocation().getX();
            double y = random.nextInt(4 + 4 ) - 4 + ydecimals + player.getLocation().getY();
            double z = random.nextInt(4 + 4 ) - 4 + zdecimals + player.getLocation().getZ();


            double xdecimalsP = random.nextDouble();
            double ydecimalsP = random.nextDouble();
            double zdecimalsP = random.nextDouble();

            double xP = random.nextInt(4 + 4 ) - 4 + xdecimalsP + player.getLocation().getX();
            double yP = random.nextInt(4 + 4 ) - 4 + ydecimalsP + player.getLocation().getY();
            double zP = random.nextInt(4 + 4 ) - 4 + zdecimalsP + player.getLocation().getZ();
            world.strikeLightning(new Location(world,x,y,z));
            world.spawnParticle(Particle.CLOUD,new Location(world,xP,yP,zP),30);
        }
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    /**
     * @brief Ability 3: "Light Step".
     * @see goro_goro#lightStep(Player)
     * @author RedRiotTank.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            lightStep(user.getPlayer());
            abilitiesCD.set(2, 30);
        }
    }

    /**
     * @brief CORE ABILITY: dashes the player 5 blocks in the direction he is looking at,
     * leaving lightning in its wake.
     * @param player Player that uses the ability.
     * @todo refactor.
     * @author RedRiotTank.
     */
    public void lightStep(Player player) {

        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        world.playSound(playerLoc,Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);

        Vector dir = player.getLocation().getDirection();
        dir.setY(0.5);
        dir.setX(dir.getX() * 5);
        dir.setZ(dir.getZ() * 5);
        player.setVelocity(dir);

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                world.strikeLightning(player.getLocation());
                tick++;

                if(tick == 6)
                    this.cancel();
            }
        }.runTaskTimer(plugin,0,2);

        player.setVelocity(dir);
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "Electric Ground".
     * @see goro_goro#electricGround(Player)
     * @author RedRiotTank.
     */
    public void ability4() {
        if(abilitiesCD.get(3) == 0){
            electricGround(user.getPlayer());
            abilitiesCD.set(3, 30);
        }
    }

    /**
     * @brief CORE ABILITY: Creates an electric discharge in the ground that affects all entities arround.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void electricGround(Player player) {
        player.getWorld().playSound(player.getLocation(),"discharge",1,1);
        electricGroundAnimation(player);

        List<Entity> entities  = player.getNearbyEntities(10,10,10);

        for(Entity ent : entities) {
            if(ent instanceof LivingEntity) {
                ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10,10));
                new BukkitRunnable() {
                    final World world = ent.getWorld();
                    int ticks = 0;
                    @Override
                    public void run() {
                        ((LivingEntity) ent).damage(0.5);
                        world.spawnParticle(Particle.ELECTRIC_SPARK,ent.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                        if(ticks == 5) this.cancel();
                        ticks++;
                    }
                }.runTaskTimer(plugin,0,2);
            }
        }
    }

    /**
     * @brief ANIMATION: spawns electric sparks particles randombly in the ground progressively over a larger area.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void electricGroundAnimation(Player player) {
        new BukkitRunnable() {
            final World world = player.getWorld();
            double extension = 1;
            double density = 20;

            @Override
            public void run() {

                for(double i=0; i < density; i++){
                    double x = generarNumeroAleatorio(player.getLocation().getX() - extension, player.getLocation().getX() + extension);
                    double z = generarNumeroAleatorio(player.getLocation().getZ() - extension, player.getLocation().getZ() + extension);
                    double y = OPHLib.searchGround(x,z,player.getLocation().getY(),world) + 0.2;

                    Location particle = new Location(world,x,y,z);
                    world.spawnParticle(Particle.ELECTRIC_SPARK,particle,0,0,0,0);
                }
                extension++;
                density*=1.5;

                if(extension == 10) this.cancel();
            }

            public double generarNumeroAleatorio(double minimo, double maximo) {
                return Math.random() * (maximo - minimo) + minimo;
            }
        }.runTaskTimer(plugin,0,1);
    }
}
