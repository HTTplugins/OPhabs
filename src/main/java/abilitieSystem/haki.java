package abilitieSystem;


import htt.ophabs.OPhabs;
import castSystem.coolDown;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import java.util.ArrayList;

public class haki extends abilities {
    private abilityUser user;
    private int level;
    private double exp, health, armor;
    public haki(OPhabs plugin, abilityUser user, int level, int exp) {
        super(plugin);
        this.user = user;
        this.level = level;
        this.exp = exp;
    }

    public ArrayList<String> getAbilitiesNames(){
        return abilitiesNames;
    }
    
    public void setUser(abilityUser user){
        this.user = user;
    }
    public String getName(){
        return "Haki";
    }
    public int getLevel(){
        return level;
    }
    public double getExp(){
        return exp;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setExp(double exp){
        this.exp = exp;
    }

    public void levelUp(){
        if(exp>=(400+level*100)){
            exp = exp - (400+level*100);
            level++;
            user.getPlayer().sendMessage("§a§lHaki§r§a level up!");
            user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
            upHealth();
            upArmor();
        }
        plugin.getConfig().set("hakiPlayers."+user.getPlayer().getName()+".Level", level);
        plugin.getConfig().set("hakiPlayers."+user.getPlayer().getName()+".Exp", exp);
    }

    public void upHealth(){
        user.getPlayer().setMaxHealth(20+2*level);
        health = user.getPlayer().getMaxHealth();
    }

    public void upArmor(){
        if(level>=2){
            user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0+2*level);
            armor = user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).getBaseValue();
        }
    }

    public void onUserDamageAnotherEntity(EntityDamageByEntityEvent event){

        if(level>=5){
            event.setDamage(event.getDamage()+(event.getDamage()*level)/10);
        }

        exp += event.getDamage();
        levelUp();
    }

    public void onItemDamage(PlayerItemDamageEvent event){
        if(level >= 5){
            event.setCancelled(true);
        }
    }



    public void playerOnWater(PlayerMoveEvent event){}
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){}
    public void onFall(EntityDamageEvent e){}
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){}
    public void onEntityPickupItem(EntityPickupItemEvent event){}
    public void onPlayerEggThrow(PlayerEggThrowEvent event){}
    public void onEntityShootBow(EntityShootBowEvent event){}
    public void onInventoryClick(InventoryClickEvent event){}
    public void onPlayerRespawn(PlayerRespawnEvent event){
        user.getPlayer().setMaxHealth(health);
        user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(armor);
    }

}
