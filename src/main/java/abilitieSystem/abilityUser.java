package abilitieSystem;

import org.bukkit.entity.Player;
import fruitSystem.devilFruit;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;


/**
 * @brief Abilities' users for fruits Class.
 * @author Vaelico786
 */

public class abilityUser {
    private final String playerName;
    public int actual;
    private devilFruit fruit;
    private df dfAbilities; 
    private haki hakiAbilities;
    private double damageBonus, armorBonus;

    // *********************************************** CONSTRUCTORS *******************************************************

    /**
     * @brief abilityUser constructor.
     * @param playerName Player's name.
     * @author Vaelico786.
     */
    public abilityUser(String playerName) {
        this.playerName = playerName;
        this.dfAbilities = null;
        this.fruit = null;
        this.hakiAbilities = null;

        damageBonus = 0;
        armorBonus = 0;
        actual = 0;
    }

    // *********************************************** DEVIL FRUIT *******************************************************

    /**
     * @brief Gets the actual player.
     * @author Vaelico786.
     */
    public Player getPlayer() {
        if(Bukkit.getPlayerExact(playerName) == null)
            return null;

        return Bukkit.getPlayerExact(playerName);
    }

    /**
     * @brief Gets the actual player's name.
     * @author Vaelico786.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @brief Set the actual player's fruit.
     * @author Vaelico786.
     */
    public void setFruit(df dfAbilities) {
        this.fruit = dfAbilities.getFruit();
        this.dfAbilities = dfAbilities;
        dfAbilities.setUser(this);

        reloadStats();
    }

    /**
     * @brief Gets the actual player's fruit.
     * @author Vaelico786.
     */
    public devilFruit getFruit() {
        return fruit;
    }

    /**
     * @brief Gets the actual player fruit's abilities.
     * @author Vaelico786.
     */
    public df getDFAbilities() {
        return dfAbilities;
    }

    /**
     * @brief Gets the actual player damage.
     * @author Vaelico786.
     */
    public double getDamage() {
        return damageBonus;
    }

    /**
     * @brief Gets the actual player resistance.
     * @author Vaelico786.
     */
    public double getArmor() {
        return armorBonus;
    }

    /**
     * @brief Returns true if player has a fruit.
     * @author Vaelico786.
     */
    public boolean hasFruit() {
        return fruit != null;
    }

    /**
     * @brief Removes the player's fruit.
     * @author Vaelico786.
     */
    public void removeFruit() {
        this.fruit = null;
        dfAbilities.setUser(null);
        this.dfAbilities = null;

        reloadStats();
    }

    /**
     * @brief Switch the current ability in game (scoreboard).
     * @author Vaelico786.
     */
    public void switchAbility() {
        if (dfAbilities == null)
            return;
        actual = (actual + 1) % getDFAbilities().getAbilitiesNames().size();
    }

    /**
     * @brief Activates the current ability selected on scoreboard.
     * @author Vaelico786.
     */
    public void abilityActive() {
        if (dfAbilities == null)
            return;
        if (getDFAbilities().active) {
            switch (actual) {
                case 0:
                    dfAbilities.ability1();
                    break;
                case 1:
                    dfAbilities.ability2();
                    break;
                case 2:
                    dfAbilities.ability3();
                    break;
                case 3:
                    dfAbilities.ability4();
                    break;
                case 4:
                    dfAbilities.ability5();
                    break;
                case 5:
                    dfAbilities.ability6();
                    break;
            }
        }
    }

    /**
     * @brief Gets all abilities' CDs of the current devil fruit.
     * @author Vaelico786.
     */
    public ArrayList<Integer> getAbilitiesCD() {
        if (dfAbilities == null)
            return null;
        return dfAbilities.abilitiesCD;
    }

    /**
     * @brief Returns fruit abilities' name.
     * @see df#getAbilitiesNames()
     * @author Vaelico786.
     */
    public ArrayList<String> getAbilitiesNames() {
        if (dfAbilities == null)
            return null;
        return dfAbilities.abilitiesNames;
    }

    // *********************************************** HAKI *******************************************************

    /**
     * @brief Sets Haki abilities' kit.
     * @author Vaelico786.
     */
    public void setHaki(haki hakiAbilities) {
        this.hakiAbilities = hakiAbilities;
        this.hakiAbilities.setUser(this);
        
        // reloadStats();
    }

    /**
     * @brief Gets Haki abilities' kit.
     * @author Vaelico786.
     */
    public haki getHakiAbilities() {
        return hakiAbilities;
    }

    /**
     * @brief Returns true if actual player has haki.
     * @author Vaelico786.
     */
    public boolean hasHaki() {
        return hakiAbilities != null;
    }

    /**
     * @brief Gets the actual player haki's level.
     * @author Vaelico786.
     */
    public int getHakiLevel() {
        if(hakiAbilities == null)
            return 0;

        return getHakiAbilities().getLevel();
    }

    /**
     * @brief Sets the actual player haki's level.
     * @author Vaelico786.
     */
    public void setHakiLevel(int level) {
        if(hakiAbilities == null)
            return;

        getHakiAbilities().setLevel(level);
    }

    /**
     * @brief Returns the haki abilities' experience.
     * @author Vaelico786.
     */
    public int getHakiExp() {
        if(hakiAbilities == null)
            return 0;

        return getHakiAbilities().getExp();
    }

    /**
     * @brief Sets the haki abilities' experience.
     * @author Vaelico786.
     */
    public void setHakiExp(int exp) {
        if(hakiAbilities == null)
            return;

        getHakiAbilities().setExp(exp);
    }
    
    // *********************************************** COMMON *******************************************************

    public void reloadStats(){
        double damage = 0;
        double armor = 0;

        if(dfAbilities != null){
            damage += dfAbilities.getDamage();
            armor += dfAbilities.getArmor();
        }

        if(hakiAbilities != null){
            hakiAbilities.reloadPlayer();
            damage += hakiAbilities.getDamage();
            armor += hakiAbilities.getArmor();
        }

        damageBonus = damage;
        armorBonus = armor;
    }

    public double calculateDamage(abilityUser damagedUser, double baseDamage){
        return ((getDamage()-damagedUser.getArmor())*2+baseDamage);
    }


    public double calculateDamage(double baseDamage){
        return getDamage()+baseDamage;
    }

    // *********************************************** PASSIVES *******************************************************

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onEntityDamage(EntityDamageEvent) 
     * @author Vaelico786.
     */
    public void onEntityDamage(EntityDamageEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onEntityDamage(event);
        if(dfAbilities != null)
            dfAbilities.onEntityDamage(event);

        if(armorBonus > 0)
            event.setDamage(event.getDamage()*(1 - 1/(100.0/armorBonus)));
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerDeath(PlayerDeathEvent)
     * @author Vaelico786.
     */
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onPlayerDeath(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerDeath(event);
        reloadStats();
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#playerOnWater(PlayerMoveEvent)
     * @author Vaelico786.
     */
    public void playerOnWater(PlayerMoveEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.playerOnWater(event);
        if(dfAbilities != null)
            dfAbilities.playerOnWater(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerToggleSneak(PlayerToggleSneakEvent)
     * @author Vaelico786.
     */
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        if(hakiAbilities != null)
            hakiAbilities.onPlayerToggleSneak(e);
        if(dfAbilities != null)
            dfAbilities.onPlayerToggleSneak(e);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onFall(EntityDamageEvent)
     * @author Vaelico786.
     */
    public void onFall(EntityDamageEvent e) {
        if(hakiAbilities != null)
            hakiAbilities.onFall(e);
        if(dfAbilities != null)
            dfAbilities.onFall(e);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerItemConsume(PlayerItemConsumeEvent)
     * @author Vaelico786.
     */
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onPlayerItemConsume(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerItemConsume(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onEntityPickupItem(EntityPickupItemEvent)
     * @author Vaelico786.
     */
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onEntityPickupItem(event);
        if(dfAbilities != null)
            dfAbilities.onEntityPickupItem(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerEggThrow(PlayerEggThrowEvent)
     * @author Vaelico786.
     */
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onPlayerEggThrow(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerEggThrow(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onEntityShootBow(EntityShootBowEvent)
     * @author Vaelico786.
     */
    public void onEntityShootBow(EntityShootBowEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onEntityShootBow(event);
        if(dfAbilities != null)
            dfAbilities.onEntityShootBow(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onInventoryClick(InventoryClickEvent)
     * @author Vaelico786.
     */
    public void onInventoryClick(InventoryClickEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onInventoryClick(event);
        if(dfAbilities != null)
            dfAbilities.onInventoryClick(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerRespawn(PlayerRespawnEvent)
     * @author Vaelico786.
     */
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(hakiAbilities != null)
            hakiAbilities.onPlayerRespawn(event);
    }

    /**
     * @brief Passive function when a player deals damage to another entity.
     * @author Vaelico786.
     */
    public void onEntityDamageByUser(EntityDamageByEntityEvent event) {
        if(hasHaki()) getHakiAbilities().onEntityDamageByUser(event);
        if(hasFruit()) getDFAbilities().onEntityDamageByUser(event);

        if(event.getEntity() instanceof Player && OPhabs.users.containsKey(event.getEntity().getName())) {
            abilityUser damaged = OPhabs.users.get(event.getEntity().getName());
            if(damaged.getArmor()>0)
                event.setDamage(calculateDamage(damaged, event.getDamage()));
            else
                event.setDamage(calculateDamage(+event.getDamage()));
        }else{
            event.setDamage(calculateDamage(+event.getDamage()));
        }
    }

    /**
     * @brief Passive function when a player is damaged by another entity.
     * @author Vaelico786.
     */
    public void onUserDamageByEntity(EntityDamageByEntityEvent event) {
        if(hasHaki()) getHakiAbilities().onUserDamageByEntity(event);
        if(hasFruit()) getDFAbilities().onUserDamageByEntity(event);

        if(armorBonus > 0)
            event.setDamage(event.getDamage()-armorBonus);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onItemDamage(PlayerItemDamageEvent)
     * @author Vaelico786.
     */
    public void onItemDamage(PlayerItemDamageEvent event) {
        if(hasHaki()) getHakiAbilities().onItemDamage(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerMove(PlayerMoveEvent)
     * @author Vaelico786, RedRiotTank
     */
    public void onPlayerMove(PlayerMoveEvent event) {
        if(hasFruit()) getDFAbilities().onPlayerMove(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onEntityToggleGlide(EntityToggleGlideEvent)
     * @author Vaelico786, RedRiotTank
     */
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if(hasFruit()) getDFAbilities().onEntityToggleGlide(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onPlayerJoin(PlayerJoinEvent)
     * @author Vaelico786, RedRiotTank
     */
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(hasFruit()) getDFAbilities().onPlayerJoin(event);
    }

    /**
     * @brief Same as "abilities" class.
     * @see abilities#onBlockBreak(BlockBreakEvent)
     * @author Vaelico786, RedRiotTank
     */
    public void onBlockBreak(BlockBreakEvent event) {}
}
