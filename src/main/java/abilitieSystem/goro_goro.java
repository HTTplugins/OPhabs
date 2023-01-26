package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import static java.lang.Math.*;

public class goro_goro extends logia {

    public goro_goro(OPhabs plugin){
        super(plugin, Particle.ELECTRIC_SPARK, castIdentification.castMaterialGoro, castIdentification.castItemNameGoro, fruitIdentification.fruitCommandNameGoro);
        abilitiesNames.add("El THOR");
        abilitiesCD.add(0);
        abilitiesNames.add("ThunderStorm");
        abilitiesCD.add(0);
        abilitiesNames.add("LightStep");
        abilitiesCD.add(0);
        this.runParticles();
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            elThor(user.getPlayer());
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            thund(user.getPlayer());
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            lightStep(user.getPlayer());
            abilitiesCD.set(2, 20); // Pon el cooldown en segundos
        }
    }


    public void elThor(Player player){

        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,2);


        new BukkitRunnable() {

            final Location playerLoc = player.getLocation();
            final double yaw = -player.getLocation().getYaw();
             double pitch = player.getLocation().getPitch();
            final World world = player.getWorld();

            int tick = 0;
            double fin = 10;


            double x,y, z = 0, xY, yY, zY, xX, yX, zX,xL,yL,zL;

            boolean changedPitch = false;



            @Override
            public void run() {

                for(double prof = 0.5; prof < fin; prof+=0.5){
                    harmingCircle(prof,1);
                    harmingCircle(prof,0.8);
                    harmingCircle(prof,0.6);
                    harmingCircle(prof,0.4);
                    harmingCircle(prof,0.2);
                }

                tick++;

                if(fin <= 20)
                    fin +=0.5;

                if(tick == 20)
                    this.cancel();

            }

            public void harmingCircle(double z, double factor){

                for(double i=0; i<2*PI; i+= 0.5){

                    x = factor*sin(i);
                    y = factor*cos(i);

                    //horizontally (Arround Y)
                    xY = cos(toRadians(yaw))*x + sin(toRadians(yaw))*z;
                    yY =  y;
                    zY = -sin(toRadians(yaw))*x + cos(toRadians(yaw))*z;

                    if(((yaw < -90 && yaw > -180) || ( yaw < 180 && yaw > 90)) && !changedPitch){
                        changedPitch = true;
                        pitch = pitch * -1;
                    }

                    //Vertically (Arround X)
                    xX = xY;
                    yX = cos(toRadians(pitch))* yY - sin(toRadians(pitch))* zY;
                    zX = sin(toRadians(pitch))* yY + cos(toRadians(pitch))* zY;


                    //Final (sum of player position.)
                    xL = playerLoc.getX() + xX;
                    yL = 1 + playerLoc.getY() + yX;
                    zL = playerLoc.getZ() + zX;

                    Location partLoc = new Location(world, xL, yL, zL);

                    if(!partLoc.getBlock().getType().equals(Material.AIR))
                        partLoc.getBlock().setType(Material.AIR);

                    for(Entity ent : world.getNearbyEntities(partLoc,2,2,2))
                        if(ent instanceof LivingEntity && !player.equals(ent))
                            ((LivingEntity)ent).damage(100);



                    world.spawnParticle(element,partLoc,0,0,0,0);
                }

            }
        }.runTaskTimer(plugin,0,1);

    }

    public void thund(Player player){

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

    public void lightStep(Player player){
        World world = player.getWorld();

        Location iniLoc = player.getLocation();

        Location finLoc = player.getLocation();

        Vector sumVec = finLoc.getDirection().normalize().multiply(15);
        finLoc = finLoc.add(sumVec);

        Location finalFinLoc = finLoc;
        // new BukkitRunnable(){
        //     Vector vec = finalFinLoc.toVector().subtract(iniLoc.toVector());


            // @Override
            // public void run() {
            //     System.out.println(vec);


            // }
        // }.runTaskTimer(plugin,0,1);


        player.teleport(finLoc);
        world.playSound(player.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);




    }

    @Override
    public void runParticles(){
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

    }
}
