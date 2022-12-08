package abilitieSystem;


import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.Biome;
import java.util.ArrayList;

public class moku_moku implements Listener{
    final Material AIR = Material.AIR;
    final Particle SMOKE = Particle.CAMPFIRE_COSY_SMOKE;
    boolean particlesON;
    private OPhabs plugin;
    ArrayList<String> smokeBodyON = new ArrayList<>(), smokeLegsON = new ArrayList<>();

    public moku_moku(OPhabs plugin){
        this.plugin = plugin;
    }

/*    @EventHandler
    public void playerOnWater(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getBlock().isLiquid() && event.getPlayer().getLocation().getBlock().getType() != Material.LAVA) {
            Player player = event.getPlayer();
            if(particlesON) {
                player.damage(2);
            }
            if(isInSeaWater(event.getPlayer())){
                    if(smokeBodyON.contains(player.getName())){
                        smokeBody(player);
                    }
                    if(smokeLegsON.contains(player.getName())){
                        smokeLegs(player);
                    }
                particlesON = false;                    
            }
        }
    }
*/
    public boolean isInSeaWater(Player player) {
        Boolean in = false;
        Biome biome = player.getLocation().getBlock().getBiome();
        if(biome.name().contains("OCEAN") || biome.name().contains("BEACH")) {
              if(player.getLocation().getBlock().getType() == Material.WATER) {
                  in = true;
              }
          }
        return in;
    }


    public void toggleFly(Player player){
        if (smokeLegsON.contains(player.getName()) || smokeBodyON.contains(player.getName()) ){
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
                if (!(smokeBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName())))
                    cancelTask();

                player.getWorld().spawnParticle(SMOKE, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            if (smokeBodyON.contains(player.getName()) && !(player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getType() != Material.LAVA)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(smokeLegsON.contains(player.getName()))
            smokeLegs(player);
        if (smokeBodyON.contains(player.getName()))
            smokeBody(player);
        particlesON=false;
    }

    public boolean smokeBody(Player player) {
            if (!smokeBodyON.contains(player.getName())) {
                if (smokeLegsON.contains(player.getName()))
                    smokeLegs(player);
                smokeBodyON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 2, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
            } else {
                smokeBodyON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            toggleFly(player);
            return (smokeBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName()));
        }

    public boolean smokeLegs(Player player){
        if (!smokeBodyON.contains(player.getName())){
            if (!smokeLegsON.contains(player.getName())) {
                smokeLegsON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            } else {
                smokeLegsON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            toggleFly(player);
        }

        return (smokeBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName())) ;

    }
}
