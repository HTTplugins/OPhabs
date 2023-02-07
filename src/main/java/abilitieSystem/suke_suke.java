package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static java.lang.Math.PI;


public class suke_suke extends paramecia {

    private int explorationDuration = 60;

    private static boolean invisible = false;
    public static boolean exploration = false;

    public static boolean cancelStopInvisibleTask = false;

    public static OPhabs plug;

    public suke_suke(OPhabs plugin){
        super(plugin, castIdentification.castMaterialSuke,castIdentification.castItemNameSuke, fruitIdentification.fruitCommandNameSuke);
        abilitiesNames.add("Invisible Exploration");
        abilitiesCD.add(0);
        abilitiesNames.add("ab2");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);
        plug = plugin;
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            invisibleExploration(user.getPlayer());
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            uninvisibility(user.getPlayer());
            abilitiesCD.set(1, 0); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            animation(user.getPlayer());
            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    public void invisibleExploration(Player player){
        if(!invisible){
            exploration = true;

            invisibility(player);

            new BukkitRunnable(){

                @Override
                public void run() {

                    if(!cancelStopInvisibleTask)
                        uninvisibility(player);


                    cancelStopInvisibleTask = false;

                }
            }.runTaskLater(plugin,explorationDuration);


        } else {
            player.sendMessage("You are alredy invisible.");
        }
    }

    public static boolean getInvisible() {
        return invisible;
    }

    public void animation(Player player){
        new BukkitRunnable(){
            double radius = 0.5;
            World world = player.getWorld();
            double yDiscount = 0;
            double incrementRate = 0.25;

            double ticks = 0;
            @Override
            public void run() {

                circle(player.getLocation().getY()+1,yDiscount,incrementRate);

                incrementRate = incrementRate - incrementRate/5;
                yDiscount+=0.2;
                radius += 0.08;

                ticks++;

                if(ticks == 5) this.cancel();
            }

            public void circle(double yParticle, double yDiscount,double increment){
                for(double i=0; i<2*PI; i+=0.2){
                    double xParticle = radius * Math.cos(i) + player.getLocation().getX();
                    double zParticle = radius * Math.sin(i) + player.getLocation().getZ();

                    Location particleLocationUP = new Location(world,xParticle,yParticle+yDiscount,zParticle);
                    Location particleLocationDOWN = new Location(world,xParticle,yParticle-yDiscount,zParticle);
                    world.spawnParticle(Particle.CLOUD,particleLocationUP,0,0,0,0);
                    world.spawnParticle(Particle.CLOUD,particleLocationDOWN,0,0,0,0);
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    public void invisibility(Player player){
        invisible = true;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,explorationDuration,99,false,false));
        animation(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player other : Bukkit.getOnlinePlayers())
                    other.hidePlayer(plugin,player);
            }
        }.runTaskLater(plugin,5);


    }

    public void uninvisibility(Player player){
        invisible = false;
        exploration = false;

        animation(player);

        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player other : Bukkit.getOnlinePlayers()){
                    other.showPlayer(plugin,player);
                }
            }
        }.runTaskLater(plugin,5);


    }



}
