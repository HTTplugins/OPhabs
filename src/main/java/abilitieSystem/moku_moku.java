package abilitieSystem;


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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.Biome;
import org.bukkit.entity.Vex;
import java.util.ArrayList;

public class moku_moku extends logia {
    final int numMaxSmokers=4;
    public int numSmokers=0;
    boolean smokeLegsON = false;

    public moku_moku(OPhabs plugin){
        super(plugin, Particle.CAMPFIRE_COSY_SMOKE);
        abilitiesNames.add("SmokeLegs");
        abilitiesCD.add(0);
        abilitiesNames.add("SmokeBody");
        abilitiesCD.add(0);
        abilitiesNames.add("SummonSmoker");
        abilitiesCD.add(0);
    }

    public void ability1(){
        if(abilitiesCD.get(0)==0){
            smokeLegs(user.getPlayer());
            abilitiesCD.set(0, 0);
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1)==0){
            logiaBody(user.getPlayer());
            abilitiesCD.set(1, 0);
        }
    }
    public void ability3(){
        if(abilitiesCD.get(2)==0){
            summonSmoker(user.getPlayer());
            abilitiesCD.set(2, 3);
        }
    }

    public void toggleFly(Player player){
        if (smokeLegsON || logiaBodyON){
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
    
    public void runParticles(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON || smokeLegsON) || user.getPlayer().isDead())
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

    }

    public void onEntityDamage(EntityDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            if (logiaBodyON && !(player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getType() != Material.LAVA)) {
                event.setCancelled(true);
            }
        }
    }
//    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(smokeLegsON)
            smokeLegs(player);
        if (logiaBodyON)
            logiaBody(player);
    }

    public boolean logiaBody(Player player) {
            if (!logiaBodyON) {
                if (smokeLegsON)
                    smokeLegs(player);
                logiaBodyON = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 2, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
                runParticles(player);
            } else {
                logiaBodyON = false;
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            toggleFly(player);
            return (logiaBodyON|| smokeLegsON);
    }

    public boolean smokeLegs(Player player){
        if (!logiaBodyON){
            if (!smokeLegsON) {
                smokeLegsON = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
                runParticles(player);
            } else {
                smokeLegsON = false;
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            toggleFly(player);
        }

        return (logiaBodyON || smokeLegsON) ;
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
