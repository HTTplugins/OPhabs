package abilitieSystem;


import htt.ophabs.OPhabs;
import fruitSystem.devilFruitUser;
import castSystem.coolDown;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Map;

public class abilities {
    protected OPhabs plugin;
    protected devilFruitUser user=null;
    protected Integer actual;
    protected String commandName;
    public ArrayList<String> abilitiesNames = new ArrayList<>();
    public ArrayList<Integer> abilitiesCD = new ArrayList<>();
    protected coolDown cd;
    
    public Material caster;
    public String casterName;
    public abilities(OPhabs plugin, devilFruitUser user, Material castMaterial, String castName, String commandName){
        this.plugin = plugin;
        this.user = user;
        this.actual = 0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
    }
    public abilities(OPhabs plugin, Material castMaterial, String castName, String commandName){
        this.plugin = plugin;
        actual=0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
    }


    public String getItemName(){
        return this.casterName;
    }

    public Material getMaterial(){
        return this.caster;
    }
    public ArrayList<String> getAbilitiesNames(){
        return abilitiesNames;
    }
    
    public void setUser(devilFruitUser user){
        this.user = user;
    }
    public String getName(){
        return commandName;
    }
	public void ability1(){}
    public void ability2(){}
	public void ability3(){}
	public void ability4(){}
	public void ability5(){}
	public void ability6(){}
    public void onEntityDamage(EntityDamageEvent event){}
    public void onPlayerDeath(PlayerDeathEvent event){
        user = null;
    }
    public void playerOnWater(PlayerMoveEvent event){}
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){}
    public void onFall(EntityDamageEvent e){}
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){}
    public void onEntityPickupItem(EntityPickupItemEvent event){}
    public void onPlayerEggThrow(PlayerEggThrowEvent event){}
    public void onEntityShootBow(EntityShootBowEvent event){}
    public void onInventoryClick(InventoryClickEvent event){}

}
