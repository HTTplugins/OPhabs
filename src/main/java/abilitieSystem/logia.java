package abilitieSystem;


import htt.ophabs.OPhabs;
import castSystem.castIdentification;
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

public abstract class logia extends abilities {
    protected Particle element;
    boolean logiaBodyON = false;

    boolean pbON = false;

    public logia(OPhabs plugin, Particle element, Material castMaterial, String castName, String commandName) {
        super(plugin, castMaterial, castName, commandName);
        this.element = element;
    }



    public abstract void runParticles();

//    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        Player player;
        if(active){
            if (event.getEntity() instanceof Player) {
                player = (Player) event.getEntity();
                if (!(player.getLocation().getBlock().isLiquid()) && (castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)) || castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)) {
                    event.setCancelled(true);
                    player.getWorld().spawnParticle(element,player.getLocation(), 10, 0, 1, 0, 0.1);

                }
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
