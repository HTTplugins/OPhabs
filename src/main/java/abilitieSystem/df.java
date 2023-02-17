package abilitieSystem;


import htt.ophabs.OPhabs;
import castSystem.coolDown;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @brief Devil Fruit (abilities) --> Ability subtype.
 * @author Vaelico786.
 */
public class df extends abilities {
    protected String commandName;
    protected int actual;
    protected coolDown cd;
    public boolean active;
    
    public Material caster;
    public String casterName;

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief Devil fruit constructor.
     * @param plugin OPhabs plugin.
     * @param user Devil fruit user.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @author Vaelico786.
     */
    public df(OPhabs plugin, abilityUser user, Material castMaterial, String castName, String commandName) {
        super(plugin, user);
        this.user = user;
        this.actual = 0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
        this.active = true;
    }

    /**
     * @brief Devil fruit constructor.
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @author Vaelico786.
     */
    public df(OPhabs plugin, Material castMaterial, String castName, String commandName) {
        super(plugin);
        actual=0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
        this.active = true;
    }

    // *********************************************** SETTERS/GETTERS *******************************************************

    /**
     * @brief Returns caster's name.
     * @author Vaelico786.
     */
    public String getItemName() {
        return this.casterName;
    }

    /**
     * @brief Returns fruit's caster.
     * @author Vaelico786.
     */
    public Material getMaterial() {
        return this.caster;
    }

    /**
     * @brief Returns fruit abilities' name.
     * @author Vaelico786.
     */
    public ArrayList<String> getAbilitiesNames() {
        return abilitiesNames;
    }

    /**
     * @brief Sets fruit's user.
     * @author Vaelico786.
     */
    public void setUser(abilityUser user) {
        this.user = user;
    }

    /**
     * @brief Gets fruit's name.
     * @author Vaelico786.
     */
    public String getName() {
        return commandName;
    }

    // *********************************************** PASSIVES *******************************************************

    /**
     * @brief Passive function when an entity receives damage.
     * @see abilities#onEntityDamage(EntityDamageEvent)
     * @author Vaelico786.
     */
    public void onEntityDamage(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) {
            if(((EntityDamageByEntityEvent)event).getDamager() instanceof LivingEntity) {
                LivingEntity damager = (LivingEntity) ((EntityDamageByEntityEvent)event).getDamager();
                if(Objects.requireNonNull(damager.getEquipment()).getItemInMainHand().getItemMeta() != null &&
                        damager.getEquipment().getItemInMainHand().getItemMeta().getLore() != null &&
                        damager.getEquipment().getItemInMainHand().getItemMeta().getLore().contains("Material:Kairōseki")) {

                    active = false;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            active = true;
                        }
                    }.runTaskLater(plugin, 20*8);
                    Player player = user.getPlayer();

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*8, 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20*8, 1));           
                }
            }
        }
    }

    /**
     * @brief Passive function when player dies.
     * @see abilities#onPlayerDeath(PlayerDeathEvent)
     * @author Vaelico786.
     */
    public void onPlayerDeath(PlayerDeathEvent event) {
        user = null;
    }
}
