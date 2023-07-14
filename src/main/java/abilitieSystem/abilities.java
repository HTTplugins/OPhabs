package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

/**
 * @brief Abilities for fruits Class.
 * @author Vaelico786
 */

public class abilities {
    protected static OPhabs plugin;
    protected abilityUser user=null;
    public ArrayList<String> abilitiesNames = new ArrayList<>();
    public ArrayList<Integer> abilitiesCD = new ArrayList<>();
    protected double damageBonus, armorBonus;

    
    /**
     * @brief abilities constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public abilities(OPhabs plugin, double damageBonus, double armorBonus) {
        this.plugin = plugin;
        this.damageBonus = damageBonus;
        this.armorBonus = armorBonus;
    }

    /**
     * @brief Fruit's ability 1.
     * @author Vaelico786.
     */
	public void ability1() {}

    /**
     * @brief Fruit's ability 2.
     * @author Vaelico786.
     */
    public void ability2() {}

    /**
     * @brief Fruit's ability 3.
     * @author Vaelico786.
     */
	public void ability3() {}

    /**
     * @brief Fruit's ability 4.
     * @author Vaelico786.
     */
	public void ability4() {}

    /**
     * @brief Fruit's ability 5.
     * @author Vaelico786.
     */
	public void ability5() {}

    /**
     * @brief Fruit's ability 6.
     * @author Vaelico786.
     */
	public void ability6() {}

    
    /**
     * @brief user setter.
     * @author Vaelico786.
     */
    public void setUser(abilityUser user){
        this.user = user;
    }

    /**
     * @brief Damage setter.
     * @author Vaelico786.
     */
    public void setDamage(double damageBonus){
        this.damageBonus = damageBonus;
    }

    /**
     * @brief Armor setter.
     * @author Vaelico786.
     */
    public void setArmor(double armorBonus){
        this.armorBonus = armorBonus;
    }

    /**
     * @brief user getter.
     * @author Vaelico786.
     */
    public abilityUser getUser(){
        return user;
    }

    /**
     * @brief Damage bonus getter.
     * @author Vaelico786.
     */
    public double getDamage(){
        return damageBonus;
    }

    /**
     * @brief Damage bonus getter.
     * @author Vaelico786.
     */
    public double getArmor(){
        return armorBonus;
    }

    /**
     * @brief Passive function when a player dies.
     * @param event Player's death event.
     * @author Vaelico786.
     */
    public void onPlayerDeath(PlayerDeathEvent event) {}

    /**
     * @brief Passive function when a player is in water.
     * @param event Player's movement event.
     * @author Vaelico786.
     */
    public void playerOnWater(PlayerMoveEvent event) {}

    /**
     * @brief Passive function when a player "shifts".
     * @param e Player's shift event.
     * @author Vaelico786.
     */
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {}

    /**
     * @brief Passive function when a player falls from so high.
     * @param e Player's fall event.
     * @author Vaelico786.
     */
    public void onFall(EntityDamageEvent e) {}

    /**
     * @brief Passive function when a player consumes a consumable item.
     * @param event Player's consume event.
     * @author Vaelico786.
     */
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {}

    /**
     * @brief Passive function when a player picks up an item on his inventory.
     * @param event Player's pick up event.
     * @author Vaelico786.
     */
    public void onEntityPickupItem(EntityPickupItemEvent event) {}

    /**
     * @brief Passive function when a player throws an egg.
     * @param event Player's throw egg event.
     * @author Vaelico786.
     */
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {}

    /**
     * @brief Passive function when an entity shoots a bow.
     * @param event Player's shoot bow event.
     * @author Vaelico786.
     */
    public void onEntityShootBow(EntityShootBowEvent event) {}

    /**
     * @brief Passive function when a player clicks into his inventory.
     * @param event Player's click inventory event.
     * @author Vaelico786.
     */
    public void onInventoryClick(InventoryClickEvent event) {}

    /**
     * @brief Passive function when an entity receives damage.
     * @param event Player's receive damage event.
     * @author Vaelico786.
     */
    public void onEntityDamage(EntityDamageEvent event) {}

    /**
     * @brief Passive function when a player respawns after die.
     * @param event Player's respawn event.
     * @author Vaelico786.
     */
    public void onPlayerRespawn(PlayerRespawnEvent event) {}

    /**
     * @brief Passive function when a player deals damage with an item.
     * @param event Player's item damage event.
     * @author Vaelico786.
     */
    public void onItemDamage(PlayerItemDamageEvent event) {}

    /**
     * @brief Passive function when a player damages an entity.
     * @param event Entity's damage event.
     * @author Vaelico786.
     */
    public void onEntityDamageByUser(EntityDamageByEntityEvent event){}

    /**
     * @brief Passive function when an entity damages an player.
     * @param event Entity's damage event.
     * @author Vaelico786.
     */
    public void onUserDamageByEntity(EntityDamageByEntityEvent event) {}

    /**
     * @brief Passive function when a player joins to the server.
     * @param event Player's join event.
     * @author Vaelico786.
     */
    public void onPlayerJoin(PlayerJoinEvent event) {}

    /**
     * @brief Passive function when a player moves.
     * @param event Player's moves event.
     * @author Vaelico786.
     */
    public void onPlayerMove(PlayerMoveEvent event) {}

    /**
     * @brief Passive function when an entity toggles his glide.
     * @param event Entity's glide event.
     * @author Vaelico786.
     */
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {}    


    /**
     * @brief Passive function that triggers when a block is broken
     * @param event Block's breaks event.
     * @author Vaelico786.
     */
    public void onBlockBreak(BlockBreakEvent event) {}

    }
