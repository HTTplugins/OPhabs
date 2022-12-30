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

public abstract class logia extends abilities {
    protected Particle element;
    boolean logiaBodyON = false;

    boolean pbON = false;

    public logia(OPhabs plugin, Particle element){
        super(plugin);
        this.element = element;
    }



     public abstract void runParticles();

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
