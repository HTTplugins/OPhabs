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
import java.util.Random;

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
        if(level > 0){
            this.level = level;
            reloadPlayer();
        }

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
            if(((Player)event.getDamager()).getInventory().getItemInMainHand().getType() == Material.AIR){
                event.setDamage(event.getDamage()*5);
            }
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

    public void reloadPlayer(){
        user.getPlayer().sendMessage("§a§lHaki§r§a level: "+level);
        user.getPlayer().setMaxHealth(20+2*level);
        user.getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0+2*level);
    }

    public void playerOnWater(PlayerMoveEvent event){}
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){}
   
    /**
    * @brief Event listener for when a player takes damage
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
