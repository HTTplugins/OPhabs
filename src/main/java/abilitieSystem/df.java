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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;

import java.util.ArrayList;

public class df extends abilities {
    protected String commandName;
    protected int actual;
    protected coolDown cd;
    public boolean active;
    
    public Material caster;
    public String casterName;
    public df(OPhabs plugin, abilityUser user, Material castMaterial, String castName, String commandName){
        super(plugin, user);
        this.user = user;
        this.actual = 0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
        this.active = true;
    }
    public df(OPhabs plugin, Material castMaterial, String castName, String commandName){
        super(plugin);
        actual=0;
        this.caster = castMaterial;
        this.casterName = castName;
        this.commandName = commandName;
        this.active = true;
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
    
    public void setUser(abilityUser user){
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
    public void onEntityDamage(EntityDamageEvent event){
        if(event instanceof EntityDamageByEntityEvent){
            if(((EntityDamageByEntityEvent)event).getDamager() instanceof LivingEntity){
                LivingEntity damager = (LivingEntity) ((EntityDamageByEntityEvent)event).getDamager();
                if(damager.getEquipment().getItemInMainHand().getItemMeta() != null && damager.getEquipment().getItemInMainHand().getItemMeta().getLore() != null && damager.getEquipment().getItemInMainHand().getItemMeta().getLore().contains("Material:Kairōseki")){
                    active = false;
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            active = true;
                        }
                    }.runTaskLater(plugin, 20*8);
                    Player player = user.getPlayer();

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*8, 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*8, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20*8, 1));           
                }
            }
        }


    }
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
