package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @brief Logia --> Devil fruit Subtype.
 * @author Vaelico786.
 */
public abstract class logia extends df{
    protected Particle element;
    boolean logiaBodyON = false;

    /**
     * @brief Logia constructor.
     * @param plugin OPhabs plugin.
     * @param element main particle element of logia user.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @author Vaelico786.
     */
    public logia(OPhabs plugin, Particle element, Material castMaterial, String castName, String commandName) {
        super(plugin, castMaterial, castName, commandName);
        this.element = element;
    }

    /**
     * @brief Abstract function that forces an implementation of a logiaBody mode animation
     * (particles + sound).
     * @author RedRiotTank.
     */
    public abstract void runParticles();

//    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        Player player;

        if(event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)event).getDamager() instanceof Player){
            Player damager = (Player) ((EntityDamageByEntityEvent)event).getDamager();
            if(plugin.users.containsKey(damager.getName()) && plugin.users.get(damager.getName()).hasHaki())
                return;
        }

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

    /**
     * @brief Logia body mode.
     * @param player Player that activates the logiaBody Mode.
     * @author Vaelico786.
     */
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
