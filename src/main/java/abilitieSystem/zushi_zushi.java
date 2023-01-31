package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import fruitSystem.fruitIdentification;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class zushi_zushi extends paramecia{

    private final List<Material> materials = new ArrayList<>();

    private static boolean heavyActivated;

    private boolean exploded = false;

    private static List<Entity> heavyEntity = null;
    private static List<Player> togglePlayer = new ArrayList<>();
    private final Random random = new Random();


    public zushi_zushi(OPhabs plugin){
        super(plugin, castIdentification.castMaterialZushi,castIdentification.castItemNameZushi,fruitIdentification.fruitCommandNameZushi);
        abilitiesNames.add("Heavy");
        abilitiesCD.add(0);
        abilitiesNames.add("Meteor");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);
        heavyActivated = false;

        //Meteor Materials initialization:
        materials.add(Material.COBBLESTONE);
        materials.add(Material.MAGMA_BLOCK);
        materials.add(Material.NETHERRACK);
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            heavy(user.getPlayer());
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            meteor(user.getPlayer());
            abilitiesCD.set(1, 0); // Pon el cooldown en segundos
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

    public void meteor(Player player){
        World world = player.getWorld();
        Location end = getTargetBlock(user.getPlayer(), 40);

        new BukkitRunnable(){
            public final Particle.DustOptions purple = new Particle.DustOptions(Color.PURPLE,1.0F);

            final double radius = 1.5;
            double x,y = 2,z;
            int ticks = 0;

            final Location playerLoc = player.getLocation();
            @Override
            public void run() {

                if(ticks < 100)
                    circle(playerLoc,y);
                else if(ticks % 2 == 0 && ticks < 190){
                    circle(playerLoc,y + 0.5);
                    circle(playerLoc,y+1);
                    y++;
                }

                if(ticks == 155){
                    Location start = playerLoc.clone();
                    start.setY(playerLoc.getY() + 50);
                    launchMeteore(start,end,5);
                }

                if(ticks == 191)
                    this.cancel();

                ticks++;
            }

            public void circle(Location loc,double y){
                for(double i=0; i<2*PI; i+=0.1){
                    x = radius * cos(i);
                    z = radius * sin(i);

                    loc.add(x,y,z);
                    world.spawnParticle(Particle.REDSTONE,loc,0,0,0,0,purple);
                    loc.subtract(x,y,z);
                }

            }
        }.runTaskTimer(plugin,0,1);

    }

    public void launchMeteore(Location start, Location end, int radius) {
        World world = start.getWorld();
        exploded = false;

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++)
                    if (x * x + y * y + z * z <= 25) {
                        Location blockLocation = start.clone().add(x, y, z);
                        Material material = materials.get(random.nextInt(materials.size()));
                        FallingBlock fallingBlock = world.spawnFallingBlock(blockLocation,material.createBlockData());
                        Vector vector = end.toVector().subtract(blockLocation.toVector());
                        fallingBlock.setVelocity(vector.multiply(0.025));
                        fallingBlock.setDropItem(false);

                        new BukkitRunnable(){   //Check explosion
                            @Override
                            public void run() {
                                if(exploded) this.cancel();

                                if (fallingBlock.isDead() && !exploded) {
                                    world.createExplosion(fallingBlock.getLocation(), 15);
                                    exploded = true;
                                }
                            }
                        }.runTaskTimer(plugin,0,1);
                    }
    }

    public Location getTargetBlock(Player player, int range) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location current = player.getEyeLocation();

        for (int i = 0; i < range; i++) {
            current = current.add(direction);

            if (current.getBlock().getType().isSolid())
                return current;
        }

        return current;
    }



}
