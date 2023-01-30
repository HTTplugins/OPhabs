package abilitieSystem;

import org.bukkit.entity.Player;
import fruitSystem.devilFruit;
import castSystem.coolDown;
import abilitieSystem.abilities;
import abilitieSystem.df;
import abilitieSystem.haki;


import org.bukkit.Bukkit;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class abilityUser{
    private String playerName;
    public int actual;
    private devilFruit fruit;
    private abilities dfAbilities, hakiAbilities;

    public abilityUser(String playerName){
        this.playerName = playerName;
        this.dfAbilities = null;
        this.fruit = null;
        this.hakiAbilities = null;

        actual = 0;
    }

    public Player getPlayer(){
        if(Bukkit.getPlayerExact(playerName) == null)
            return null;

        return Bukkit.getPlayerExact(playerName);
    }
    
    public String getPlayerName(){
        return playerName;
    }

    public void setFruit(devilFruit fruit, abilities dfAbilities){
        this.fruit = fruit;
        this.dfAbilities = dfAbilities;
        dfAbilities.user=this;
    }

    public void setHaki(abilities hakiAbilities){
        this.hakiAbilities = hakiAbilities;
        this.hakiAbilities.user=this;
    }

    public devilFruit getFruit(){
        return fruit;
    }

    public df getDFAbilities(){
        return (df) dfAbilities;
    }

    public haki getHakiAbilities(){
        return (haki) hakiAbilities;
    }

    public boolean hasFruit(){
        return fruit != null;
    }

    public boolean hasHaki(){
        return hakiAbilities != null;
    }

    public int getHakiLevel(){
        if(hakiAbilities == null)
            return 0;

        return getHakiAbilities().getLevel();
    }

    public void setHakiLevel(int level){
        if(hakiAbilities == null)
            return;

        getHakiAbilities().setLevel(level);
    }

    public double getHakiExp(){
        if(hakiAbilities == null)
            return 0;

        return getHakiAbilities().getExp();
    }

    public void setHakiExp(double exp){
        if(hakiAbilities == null)
            return;

        getHakiAbilities().setExp(exp);
    }

    public void switchAbility() {
        if (dfAbilities == null)
            return;
        actual = (actual + 1) % getDFAbilities().getAbilitiesNames().size();

    }

    public int abilityActive() {
        if (dfAbilities == null)
            return -1;
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
        return dfAbilities.abilitiesCD.get(actual);
    }

    public ArrayList<Integer> getAbilitiesCD() {
        if (dfAbilities == null)
            return null;
        return dfAbilities.abilitiesCD;
    }

    public ArrayList<String> getAbilitiesNames() {
        if (dfAbilities == null)
            return null;
        return dfAbilities.abilitiesNames;
    }

    public void onEntityDamage(EntityDamageEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onEntityDamage(event);
        if(dfAbilities != null)
            dfAbilities.onEntityDamage(event);
    }
    public void onPlayerDeath(PlayerDeathEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onPlayerDeath(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerDeath(event);
    }
    public void playerOnWater(PlayerMoveEvent event){
        if(hakiAbilities != null)
            hakiAbilities.playerOnWater(event);
        if(dfAbilities != null)
            dfAbilities.playerOnWater(event);
    }
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){
        if(hakiAbilities != null)
            hakiAbilities.onPlayerToggleSneak(e);
        if(dfAbilities != null)
            dfAbilities.onPlayerToggleSneak(e);
    }
    public void onFall(EntityDamageEvent e){
        if(hakiAbilities != null)
            hakiAbilities.onFall(e);
        if(dfAbilities != null)
            dfAbilities.onFall(e);
    }
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onPlayerItemConsume(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerItemConsume(event);
    }
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onEntityPickupItem(event);
        if(dfAbilities != null)
            dfAbilities.onEntityPickupItem(event);
    }
    public void onPlayerEggThrow(PlayerEggThrowEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onPlayerEggThrow(event);
        if(dfAbilities != null)
            dfAbilities.onPlayerEggThrow(event);
    }
    public void onEntityShootBow(EntityShootBowEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onEntityShootBow(event);
        if(dfAbilities != null)
            dfAbilities.onEntityShootBow(event);
    }
    public void onInventoryClick(InventoryClickEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onInventoryClick(event);
        if(dfAbilities != null)
            dfAbilities.onInventoryClick(event);
    }

    public void onPlayerRespawn(PlayerRespawnEvent event){
        if(hakiAbilities != null)
            hakiAbilities.onPlayerRespawn(event);
    }

    public void onUserDamageAnotherEntity(EntityDamageByEntityEvent event){
        if(hasHaki()) getHakiAbilities().onUserDamageAnotherEntity(event);
    }
    public void onItemDamage(PlayerItemDamageEvent event){
        if(hasHaki()) getHakiAbilities().onItemDamage(event);
    }

}
