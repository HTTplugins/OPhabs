package abilitieSystem;


import htt.ophabs.OPhabs;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import java.util.ArrayList;
import java.util.Random;



/**
* @brief Class that contains haki skills --> Ability subtype.
* @author Vaelico786.
*/
public class haki extends abilities {
    private abilityUser user;
    private int level, exp;
    private double health, armor;


    /**
     * @brief Haki constructor.
     * @param user User of the ability.
     * @param level Level of the ability.
     * @param exp Experience of the ability.
     * @author Vaelico786.
     */
    public haki(OPhabs plugin, abilityUser user, int level, int exp) {
        super(plugin);
        this.user = user;
        this.level = level;
        this.exp = exp;
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
            reloadPlayer();
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
            user.getPlayer().sendMessage("§a§lHaki§r§a level up!");
            user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
            upHealth();
            upArmor();
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
            user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0+2*level);
            armor = user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).getBaseValue();
        }
    }

    /**
     * @brief Reloads player's stats.
     * @author Vaelico786.
     */
    public void reloadPlayer(){
        user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
        user.getPlayer().setMaxHealth(20+2*level);
        user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0+2*level);
    }

    /**
    * @brief Event listener that activates when the user deals damage.
    * @param event The event that was triggered
    * @author Vaelico786.
    */
    public void onEntityDamageByUser(EntityDamageByEntityEvent event){
        if(level>=5){
            if(((Player)event.getDamager()).getInventory().getItemInMainHand().getType() == Material.AIR){
                event.setDamage(event.getDamage()*5);
            }
            event.setDamage(event.getDamage()+(event.getDamage()*level)/10);
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
        //Level 4 Observation haki, chance to dodge
        if(event instanceof EntityDamageByEntityEvent){
            if(level >= 4){
                if(((EntityDamageByEntityEvent) event).getDamager() instanceof Player){
                    Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
                    //When damage is done by a player probability is determined by the life of the player(The more life the player has the stronger he is
                    if(player.getMaxHealth() < user.getPlayer().getMaxHealth()){
                        if(player.getHealth() < user.getPlayer().getHealth()/2){
                            event.setDamage(0);
                        }
                        else{
                            event.setDamage(event.getDamage()/(user.getPlayer().getMaxHealth()-player.getMaxHealth()));
                        }
                    }
                    else{
                        event.setDamage(event.getDamage()/(player.getMaxHealth()-user.getPlayer().getMaxHealth()));
                    }
                }
                else{
                    //If the entity its not a player then a probability based on player level will be used
                    Random rand = new Random();
                    if(rand.nextInt(200)/5 <= level){
                        event.setDamage(0);
                    }
                }
            }
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
    public void onPlayerRespawn(PlayerRespawnEvent event){
        // user.getPlayer().setMaxHealth(health);
        // user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
    }

}
