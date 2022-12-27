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

public class logia extends abilities {
    protected Particle element;
    boolean logiaBodyON = false;

    public logia(OPhabs plugin, Particle element){
        super(plugin);
        this.element = element;
    }

/*@EventHandler
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

     public void runParticles(Player player, boolean oldP){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON) || user.getPlayer().isDead()){
                    cancelTask();
                    logiaBodyON = false;
                }

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
            if (logiaBodyON && !(player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getType() != Material.LAVA)) {
                event.setCancelled(true);
            }
        }
    }

    public boolean logiaBody(Player player) {
            if (!logiaBodyON) {
                logiaBodyON = true;
            } else {
                logiaBodyON = false;
            }
            player.sendMessage("Logia Body: " + logiaBodyON);
            return (logiaBodyON);
    }

}
