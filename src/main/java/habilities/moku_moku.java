package habilities;


import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.Biome;
import org.bukkit.entity.Vex;
import java.util.ArrayList;

public class moku_moku extends logia {
    final int numMaxSmokers=4;
    public int numSmokers=0;
    ArrayList<String> smokeLegsON = new ArrayList<>();

    public moku_moku(OPhabs plugin){
        super(plugin);
        super.element = Particle.CAMPFIRE_COSY_SMOKE;
    }

    public void toggleFly(Player player){
        if (smokeLegsON.contains(player.getName()) || logiaBodyON.contains(player.getName()) ){
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
    
    public void runParticles(Player player, boolean oldP){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName())) || player.isDead())
                    cancelTask();

                player.getWorld().spawnParticle(element, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 3);
    }

//    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            if (logiaBodyON.contains(player.getName()) && !(player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getType() != Material.LAVA)) {
                event.setCancelled(true);
            }
        }
    }
//    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(smokeLegsON.contains(player.getName()))
            smokeLegs(player);
        if (logiaBodyON.contains(player.getName()))
            logiaBody(player);
    }

    public boolean logiaBody(Player player) {
            if (!logiaBodyON.contains(player.getName())) {
                if (smokeLegsON.contains(player.getName()))
                    smokeLegs(player);
                logiaBodyON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 2, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
            } else {
                logiaBodyON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            toggleFly(player);
            return (logiaBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName()));
        }

    public boolean smokeLegs(Player player){
        if (!logiaBodyON.contains(player.getName())){
            if (!smokeLegsON.contains(player.getName())) {
                smokeLegsON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            } else {
                smokeLegsON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            toggleFly(player);
        }

        return (logiaBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName())) ;
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
