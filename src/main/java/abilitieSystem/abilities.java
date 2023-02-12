package abilitieSystem;


import htt.ophabs.OPhabs;
import castSystem.coolDown;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.ArrayList;

public class abilities {
    protected OPhabs plugin;
    protected abilityUser user=null;
    public ArrayList<String> abilitiesNames = new ArrayList<>();
    public ArrayList<Integer> abilitiesCD = new ArrayList<>();
    
    
    public abilities(OPhabs plugin, abilityUser user){
        this.plugin = plugin;
        this.user = user;
    }
    public abilities(OPhabs plugin){
        this.plugin = plugin;
    }
	public void ability1(){}
    public void ability2(){}
	public void ability3(){}
	public void ability4(){}
	public void ability5(){}
	public void ability6(){}
    public void onPlayerDeath(PlayerDeathEvent event){}
    public void playerOnWater(PlayerMoveEvent event){}
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){}
    public void onFall(EntityDamageEvent e){}
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){}
    public void onEntityPickupItem(EntityPickupItemEvent event){}
    public void onPlayerEggThrow(PlayerEggThrowEvent event){}
    public void onEntityShootBow(EntityShootBowEvent event){}
    public void onInventoryClick(InventoryClickEvent event){}
    public void onEntityDamage(EntityDamageEvent event){}
    public void onPlayerRespawn(PlayerRespawnEvent event){}
    public void onItemDamage(PlayerItemDamageEvent event){}

}
