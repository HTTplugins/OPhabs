package abilitieSystem;


import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.Biome;
import org.bukkit.entity.Vex;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class moku_moku extends logia {
    final int numMaxSmokers=4;
    public int numSmokers=0;

    public moku_moku(OPhabs plugin){
        super(plugin, Particle.CLOUD);
        abilitiesNames.add("SmokeBody");
        abilitiesCD.add(0);
        abilitiesNames.add("SummonSmoker");
        abilitiesCD.add(0);
        runParticles();
    }

    public void ability1(){
        if(abilitiesCD.get(0)==0){
            logiaBody(user.getPlayer());
            abilitiesCD.set(0, 0);
        }
    }
    public void ability2(){
        if(abilitiesCD.get(1)==0){
            summonSmoker(user.getPlayer());
            abilitiesCD.set(1, 3);
        }
    }

    public void toggleFly(Player player){
        if (logiaBodyON){
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
    
    public void runParticles(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON || user.getPlayer().isDead()))
                    cancelTask();
                player.getWorld().spawnParticle(element, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 5, 3);
    }

    @Override
    public void runParticles() {
        new BukkitRunnable(){
            int ticks = 0;
            double i = 0;
            double y = 0;

            Random random = new Random();
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

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameMoku)){
                            double x = sin(i)/2;
                            double z = cos(i)/2;

                            double xr = player.getLocation().getX() + x;
                            double yr = player.getLocation().getY() + y;
                            double zr = player.getLocation().getZ() + z;

                            Location partLoc = new Location(player.getWorld(),xr,yr,zr);
                            player.spawnParticle(Particle.CLOUD,partLoc, 0,0,0,0);

                            summonParticle(0.5,player);
                            
                            player.setAllowFlight(true);

                        }else {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }

                i+= 0.5;
                y+=0.05;

                if(y > 2)
                    y = 0;



            }

            public void summonParticle(double y,Player player){

                double xdecimals;
                double ydecimals;
                double zdecimals;

                double x,z;


                xdecimals = random.nextDouble();
                ydecimals = random.nextDouble();
                zdecimals = random.nextDouble();

                x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                y += random.nextInt(1 + 1 ) - 1 + ydecimals ;
                z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                player.getWorld().spawnParticle(element,player.getLocation().add(x,y,z),0,0,0,0);

            }


        }.runTaskTimer(plugin,0,1);

    }
//    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        super.onPlayerDeath(event);
        Player player = event.getEntity();
        if (logiaBodyON)
            logiaBody(player);
    }

    public boolean logiaBody(Player player) {
            if (!logiaBodyON) {
                logiaBodyON = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
                runParticles(player);
            } else {
                logiaBodyON = false;
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            return logiaBodyON;
    }

    public void setTarget(Vex entity, Player player){
        Location loc = entity.getLocation();
        boolean found = false;
          for (Entity e : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)){
            if(e instanceof LivingEntity && e != player && e != entity && !(e instanceof Vex)){
                entity.setTarget(((LivingEntity)e));
                found = true;
                break;
            }
          }
        if(!found){
            entity.setTarget(null);
        }

    }
    public void summonSmoker(Player player){
        if(numSmokers<=numMaxSmokers){
            Location loc = player.getLocation();
            Vex smoker = ((Vex) player.getWorld().spawnEntity(loc,EntityType.VEX));
            smoker.setCustomName("Smoker");
            smoker.setHealth(0.5);            
            numSmokers++;
            smoker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 2, false, false));
            smoker.getEquipment().setItemInMainHand(null); 
            new BukkitRunnable() {
                @Override
                public void run() {
                    //if smoker is dead or player is dead or has been 20 seconds
                    if (smoker.isDead() || player.isDead() || smoker.getTicksLived() > 400 ){//|| setTarget(((Vex)smoker), player) == null ) {
                        smoker.setHealth(0);
                        numSmokers--;
                        cancelTask();
                    }
                    if(smoker.getTarget() == null || smoker.getTarget().isDead() || smoker.getTarget() == player ){
                        setTarget(smoker, player);
                    }
                    smoker.getWorld().spawnParticle(element, smoker.getLocation(), 10, 0.5, 0.5, 0.5, 0);
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 3); 
        }
    }
}
