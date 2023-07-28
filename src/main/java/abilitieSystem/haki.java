package abilitieSystem;


import htt.ophabs.OPhabs;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;



/**
* @brief Class that contains haki skills --> Ability subtype.
* @author Vaelico786.
*/
public class haki extends abilities {
    private abilityUser user;
    private int level, exp;
    private double health;


    /**
     * @brief Haki constructor.
     * @param user User of the ability.
     * @param level Level of the ability.
     * @param exp Experience of the ability.
     * @author Vaelico786.
     */
    public haki(OPhabs plugin, abilityUser user, int level, int exp) {
        super(plugin, 0, 0);
        this.user = user;
        this.level = level;
        this.exp = exp;

        reloadPlayer(); 
    }

    /**
     * @brief abilitiesNames getter.
     * @return List of all haki abilities names.
     * @author Vaelico786.
     */
    public ArrayList<String> getAbilitiesNames(){
        return abilitiesNames;
    }

    /**
     * @brief Name getter.
     * @return "Haki".
     * @author Vaelico786.
     */
    public String getName(){
        return "Haki";
    }

    /**
     * @brief Level getter.
     * @return Level of the Haki.
     * @author Vaelico786.
     */
    public int getLevel(){
        return level;
    }

    /**
     * @brief Exp getter.
     * @return Exp of the Haki.
     * @author Vaelico786.
     */
    public int getExp(){
        return exp;
    }

    /**
     * @brief Level setter.
     * @param level Level of the Haki.
     * @author Vaelico786.
     */
    public void setLevel(int level){
        if(level > 0){
            this.level = level;
            user.reloadStats();

            if(user.getPlayer().isOnline()){
                user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
            }
        }
    }

    /**
     * @brief Exp setter.
     * @param exp Experience to add to the Haki.
     * @author Vaelico786.
     */
    public void setExp(int exp){
        this.exp = exp;
    }

    /**
     * @brief Levels up haki.
     * @author Vaelico786.
     */
    public void levelUp(){
        if(exp>=(400+level*100)){
            exp = exp - (400+level*100);
            level++;
            if(user.getPlayer().isOnline()){
                user.getPlayer().sendMessage("§a§lHaki§r§a level up!");
                user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
            }
                user.reloadStats();

        }
    }

    /**
     * @brief Upgrades health of the player accord with haki level.
     * @author Vaelico786.
     */
    public void upHealth(){
        user.getPlayer().setMaxHealth(20+2*level);
        health = user.getPlayer().getMaxHealth();
    }

    /**
     * @brief Upgrades armor of the player accord with haki level.
     * @author Vaelico786.
     */
    public void upArmor(){
        if(level>=2){
            armorBonus = (1.0*level)/2;
        }
    }

    /**
     * @brief Upgrades damage of the player accord with haki level.
     * @author Vaelico786.
     */
    public void upDamage(){
        if(level>=5){
            damageBonus = (1.0*level)/2;
        }
    }

    /**
     * @brief Reloads player's stats.
     * @author Vaelico786.
     */
    public void reloadPlayer(){
        if(user.getPlayer() != null && user.getPlayer().isOnline()){
            upHealth();
        }
        upArmor();
        upDamage();


    }

    /**
    * @brief Event listener that activates when the user deals damage.
    * @param event The event that was triggered
    * @author Vaelico786.
    */
    public void onEntityDamageByUser(EntityDamageByEntityEvent event){
        if(level>=5){
            if(((Player)event.getDamager()).getInventory().getItemInMainHand().getType() == Material.AIR){
                event.setDamage(event.getDamage()+5);
            }
        }

        exp += event.getDamage();
        levelUp();
    }

    /**
     * @brief Event listener that activates when the user is damaged.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onItemDamage(PlayerItemDamageEvent event){
        if(level >= 5){
            event.setCancelled(true);
        }
    }


    /**
     * @brief Event listener that activates when the user is sneaking.
     * @param e The event that was triggered
     * @author Vaelico786.
     */
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){}
   
    /**
    * @brief Event listener that activates when a player takes damage
    * @param event The event that was triggered
    * @author Vaelico786.
    */
    public void onEntityDamage(EntityDamageEvent event){
        //If the entity its not a player then a probability based on player level will be used
        Random rand = new Random();
        if(rand.nextInt(100) <= level){
            event.setDamage(0);
        }
    }

    /**
     * @brief Event listener that activates when the user shots a proyectile.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onEntityShootBow(EntityShootBowEvent event){}

    /**
     * @brief Event listener that activates when the user respawn.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    /*
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Bukkit.getPlayer(user.getPlayerName()).setMaxHealth(health);

        user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armorBonus);
    }
    */

}
