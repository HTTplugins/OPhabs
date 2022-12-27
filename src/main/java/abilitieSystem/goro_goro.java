package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static java.lang.Math.*;

public class goro_goro extends logia {

    public goro_goro(OPhabs plugin){
        super(plugin, Particle.REDSTONE);
        abilitiesNames.add("El THOR");
        abilitiesCD.add(0);
        abilitiesNames.add("ThunderStorm");
        abilitiesCD.add(0);

    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            abSpark(user.getPlayer());
            abilitiesCD.set(0, 20); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            thund(user.getPlayer());
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }


    public void abSpark(Player player){

        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,2);


        new BukkitRunnable() {

            final Location playerLoc = player.getLocation();
            final double angle = -player.getLocation().getYaw();
            final World world = player.getWorld();

            int tick = 0;
            double fin = 10;


            double x,y, z = 0,xr,yr,zr;



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

                if(fin <= 20){
                    fin +=0.5;

                }

                if(tick == 20)
                    this.cancel();


            }

            public void harmingCircle(double z, double factor){

                for(double i=0; i<2*PI; i+= 0.5){

                    x = factor*sin(i);
                    y = factor*cos(i);

                    xr = playerLoc.getX() + cos(toRadians(angle))*x + sin(toRadians(angle))*z;
                    yr = 1 + playerLoc.getY() + y;
                    zr = playerLoc.getZ() + -sin(toRadians(angle))*x + cos(toRadians(angle))*z;

                    Location partLoc = new Location(world,xr,yr,zr);

                    if(!partLoc.getBlock().getType().equals(Material.AIR))
                        partLoc.getBlock().setType(Material.AIR);

                    for(Entity ent : world.getNearbyEntities(partLoc,2,2,2))
                        if(ent instanceof LivingEntity && !player.equals(ent))
                            ((LivingEntity)ent).damage(100);



                    world.spawnParticle(Particle.ELECTRIC_SPARK,partLoc,0,0,0,0);
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


}
