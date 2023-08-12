package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;

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

    public logia(OPhabs plugin, double damageBonus, double armorBonus, Particle element, int id, String fruitCommandName, String fruitItemName, String casterName, double casterDamage, double casterSpeed) {
        super(plugin, damageBonus, armorBonus, id, fruitCommandName, fruitItemName, casterName, casterDamage, casterSpeed);
        this.element = element;
        setCanFly(true);
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
            if(OPhabs.users.containsKey(damager.getName())){
                abilityUser userDamager = OPhabs.users.get(damager.getName());
                if(userDamager.hasHaki() || (userDamager.hasFruit() && userDamager.getDFAbilities() instanceof logia)){
                    return;
                }
            }
        }

        if(active){
            if (event.getEntity() instanceof Player) {
                player = (Player) event.getEntity();
                if (!(player.getLocation().getBlock().isLiquid()) && (castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user) || castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user) || logiaBodyON)) {
                    event.setCancelled(true);
                    player.getWorld().spawnParticle(element,player.getLocation(), 10, 0, 1, 0, 0.1);
                }
            }
        }
    }

//    @EventHandler(ignoreCancelled = true)
    public void superOnEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
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
