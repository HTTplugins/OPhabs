package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import skin.changer.skinsChanger;


public class zoan extends abilities{
    public boolean transformed = false;
    skinsChanger skinC = new skinsChanger();
    String skinUrl="", skinAwakenUrl="";
    public zoan(OPhabs plugin){
        super(plugin);
    }

    public void transformation(Player player){
        if(!transformed){
            skinC.changeSkin(player, skinUrl);
            transformed = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isDead() || !transformed) {
                        skinC.resetSkin(player);
                        cancelTask();
                    }
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 10);
        }
        else
            transformed = !transformed;    
    }
}

