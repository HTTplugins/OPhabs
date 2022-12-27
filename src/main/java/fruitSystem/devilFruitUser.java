package fruitSystem;

import org.bukkit.entity.Player;
import abilitieSystem.abilities;
import castSystem.coolDown;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayList;

public class devilFruitUser{
    private String playerName;
    public int actual;
    public devilFruit fruit;
    protected abilities ability;
    private boolean awakened;


    public devilFruitUser(String playerName, devilFruit fruit, abilities ability){
        this.playerName = playerName;
        this.ability = ability;
        this.ability.setUser(this);
        this.fruit = fruit;
        actual = 0;
        awakened=false;
    }

    public Player getPlayer(){
        return Bukkit.getPlayerExact(playerName);
    }
    
    public String getPlayerName(){
        return playerName;
    }

    public void playerAwakens(){
        awakened=true;
    }
		
    public boolean isAwaken(){
        return awakened;
    }
    public devilFruit getFruit(){
        return fruit;
    }
    public void switchAbility(){
        actual++;
        actual = actual % ability.getAbilitiesNames().size();
    }
    public int abilityActive(){
        switch (actual){
            case 0:
                ability.ability1();
                break;
            case 1:
                ability.ability2();
                break;
            case 2:
                ability.ability3();
                break;
            case 3:
                ability.ability4();
                break;
            case 4:
                ability.ability5();
                break;
            case 5:
                ability.ability6();
                break;
        }
        return ability.abilitiesCD.get(actual);
    }

    public ArrayList<Integer> getAbilitiesCD(){
        return ability.abilitiesCD;
    }
    public ArrayList<String> getAbilitiesNames(){
        return ability.getAbilitiesNames();
    }

    public void onEntityDamage(EntityDamageEvent event){
        ability.onEntityDamage(event);
    }
    public void onPlayerDeath(PlayerDeathEvent event){
        ability.onPlayerDeath(event);
    }
    public void playerOnWater(PlayerMoveEvent event){
        ability.playerOnWater(event);
    }
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){
        ability.onPlayerToggleSneak(e);
    }
    public void onFall(EntityDamageEvent e){
        ability.onFall(e);
    }
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        ability.onPlayerItemConsume(event);
    }
    public void onEntityPickupItem(EntityPickupItemEvent event){
        ability.onEntityPickupItem(event);
    }
    public void onPlayerEggThrow(PlayerEggThrowEvent event){
        ability.onPlayerEggThrow(event);
    }
    public void onEntityShootBow(EntityShootBowEvent event){
        ability.onEntityShootBow(event);
    }
    public static void onEntityChangeBlock(EntityChangeBlockEvent event){

    }
}
