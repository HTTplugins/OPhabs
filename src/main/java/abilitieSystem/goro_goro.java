package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import fruitSystem.fruitIdentification;

import static java.lang.Math.*;

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
    public goro_goro(OPhabs plugin){
        super(plugin, Particle.ELECTRIC_SPARK, castIdentification.castMaterialGoro, castIdentification.castItemNameGoro, fruitIdentification.fruitCommandNameGoro);
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
    public void runParticles(){
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
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
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
    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            elThor(user.getPlayer());
            abilitiesCD.set(0, 50);
        }
    }

    /**
     * @brief CORE ABILITY: creates a thunder ray thar destroy blocks and makes damage.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void elThor(Player player){

        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,2);

        new BukkitRunnable() {
            int tick = 0;
            double fin = 10;

            @Override
            public void run() {

                for(double prof = 0.5; prof < fin; prof+=0.5){
                    auxBiblio.circleEyeVector(1,0.5,prof,null, element,true,true, user.getPlayer());
                    auxBiblio.circleEyeVector(0.8,0.5,prof,null, element,true,true, user.getPlayer());
                    auxBiblio.circleEyeVector(0.6,0.5,prof,null, element,true,true, user.getPlayer());
                    auxBiblio.circleEyeVector(0.4,0.5,prof,null, element,true,true, user.getPlayer());
                    auxBiblio.circleEyeVector(0.2,0.5,prof,null, element,true,true, user.getPlayer());
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
    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            thunderStorm(user.getPlayer());
            abilitiesCD.set(1, 20);
        }
    }

    /**
     * @brief CORE ABILITY: Creates a thunderstorm that hits every entity in [10,10,10] blocks.
     * @param player Player that uses the ability.
     * @author RedRiotTank.
     */
    public void thunderStorm(Player player){

        Random random = new Random();

        World world = player.getWorld();
        for(Entity ent : player.getNearbyEntities(10,10,10)){
            world.strikeLightning(ent.getLocation());
            world.spawnParticle(Particle.ELECTRIC_SPARK,ent.getLocation(),3);
        }


        for(int i=0; i<15; i++){

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
    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            lightStep(user.getPlayer());
            abilitiesCD.set(2, 30);
        }
    }

    /**
     * @brief CORE ABILITY: dashed the player 5 blocks in the direction he is looking at,
     * leaving lightning in its wake.
     * @param player Player that uses the ability.
     * @todo refactor.
     * @author RedRiotTank.
     */
    public void lightStep(Player player){

        World world = player.getWorld();
        Location playerLoc = player.getLocation();


        world.playSound(playerLoc,Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);

        Vector dir = player.getLocation().getDirection();
        dir.setY(0.5);
        dir.setX(dir.getX() * 5);
        dir.setZ(dir.getZ() * 5);
        player.setVelocity(dir);

        new BukkitRunnable(){

            int tick = 0;

            @Override
            public void run() {
                world.strikeLightning(player.getLocation());
                tick++;

                if(tick == 6)
                    this.cancel();
            }
        }.runTaskTimer(plugin,0,2);

        // System.out.println(direction);
        //System.out.println(movement);

        player.setVelocity(dir);

    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "Discharge".
     * @see goro_goro#discharge(Player)
     * @author RedRiotTank.
     */
    public void ability4(){
        if(abilitiesCD.get(3) == 0){
            discharge(user.getPlayer());
            abilitiesCD.set(3, 100);
        }
    }

    /**
     * @brief CORE ABILITY: --
     * @param player Player that uses the ability.
     * @todo change the whole ability, I don't like it.
     * @author RedRiotTank.
     */
    public void discharge(Player player){

        final int Maximum = 3;
        int index = 0;

        List<Entity> entityList = player.getNearbyEntities(15,10,15);

        List<LivingEntity> Players = new ArrayList<>();
        List<LivingEntity> livingEntities = new ArrayList<>();

        for(Entity ent : entityList){
            if(ent instanceof Player)
                Players.add((LivingEntity) ent);
            else if(ent instanceof LivingEntity)
                livingEntities.add((LivingEntity) ent);
        }

        for(int i=0; i<Players.size() && index < Maximum; i++){
            sendStrike(Players.get(i));
            index++;
        }

        for(int i=0; i<livingEntities.size() && index < Maximum; i++){
            sendStrike(livingEntities.get(i));
            index++;
        }
    }

    public void sendStrike(LivingEntity ent){
        Location location = ent.getLocation();
        World world = ent.getWorld();


        world.playSound(ent.getLocation(),"discharge",1,1);
        new BukkitRunnable(){
            double y = 5;

            @Override
            public void run() {

                for (double i = 0; i < 2*PI; i += 0.05) {
                    double x = Math.cos(i);
                    double z = Math.sin(i);

                    for(double radius = 0.6; radius < 1.4; radius+=0.2){
                        location.add(x*radius, y, z*radius);

                        //Note: with a loop doesn't work.
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,0.8,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,0.6,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,0.4,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,0.2,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,-0.2,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,-0.4,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,-0.6,0), 0,0,0,0);
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location.add(0,-0.8,0), 0,0,0,0);

                        if(location.getBlock().getType() != Material.AIR)  location.getBlock().setType(Material.AIR);

                        ent.damage(0.5);

                        location.subtract(x*radius, y, z*radius);
                    }

                }
                y-=0.5;

                if(y==-3) this.cancel();
            }
        }.runTaskTimer(plugin,0,1);

    }

}
