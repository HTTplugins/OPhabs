package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import fruitSystem.fruitIdentification;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class zushi_zushi extends paramecia{
    private static boolean heavyActivated;

    private static List<Entity> heavyEntity = null;
    private static List<Player> togglePlayer = new ArrayList<>();

    public zushi_zushi(OPhabs plugin){
        super(plugin, castIdentification.castMaterialZushi,castIdentification.castItemNameZushi,fruitIdentification.fruitCommandNameZushi);
        abilitiesNames.add("heavy");
        abilitiesCD.add(0);
        abilitiesNames.add("ab2");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);
        heavyActivated = false;
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            heavy(user.getPlayer());
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){

            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){

            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    public void heavy(Player player){

        World world = player.getWorld();
        new BukkitRunnable(){
            int ticks = 0;
            Random random = new Random();
            World world = player.getWorld();
            Location playerLoc = player.getLocation();
            double x;
            double z;
            @Override
            public void run() {
                ticks++;

                for(int i = 0; i < 30; i++){
                    x = (random.nextDouble() * 19.98) - 9.99;
                    z = (random.nextDouble() * 19.98) - 9.99;
                    world.spawnParticle(Particle.SPELL_WITCH,new Location(world,playerLoc.getX()+x,playerLoc.getY(),playerLoc.getZ() + z),2);
                }

                for(int i = 0; i < 60; i++){
                    x = (random.nextDouble() * 19.98) - 9.99;
                    z = (random.nextDouble() * 19.98) - 9.99;
                    world.spawnParticle(Particle.CRIT_MAGIC,new Location(world,playerLoc.getX()+x,playerLoc.getY() + 0.5,playerLoc.getZ() + z),0,0,-0.3,0);
                }
                if(ticks == 100) this.cancel();


            }
        }.runTaskTimer(plugin,0,1);

        heavyEntity = player.getNearbyEntities(20,50,20);

        for(Entity ent : heavyEntity){
            if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR)){
                ent.setVelocity(new Vector(0,-50,0));
            }

        }

        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {


                for(Entity ent : heavyEntity){
                    if(ent.getLocation().getY() == world.getHighestBlockAt(ent.getLocation()).getY()+1
                            && !togglePlayer.contains(ent)
                            && ent instanceof Player){

                        togglePlayer.add((Player) ent);
                        Location entiLoc = ent.getLocation();
                        ((Player)ent).teleport(new Location(world,entiLoc.getX(),entiLoc.getY(),entiLoc.getZ(),entiLoc.getYaw(),-10));
                        ((Player)ent).setGliding(true);

                    } else {
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10,10));
                    }
                }

                if(ticks == 100){
                    togglePlayer.clear();
                    heavyEntity.clear();

                    this.cancel();

                }

                ticks++;

            }
        }.runTaskTimer(plugin,0,1);
    }

    public static void onEntityToggleGlide(EntityToggleGlideEvent event){
        if(togglePlayer.contains((Player)(event.getEntity())))
            event.setCancelled(true);
    }

    public static void onPlayerMove(PlayerMoveEvent event){

        if(togglePlayer.contains(event.getPlayer())){
            event.setCancelled(true);
        }








    }

}
