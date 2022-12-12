
package habilities;

import habilities.zoan;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.ArrayList;

public class neko_neko_reoparudo extends zoan implements Listener {

    public neko_neko_reoparudo(OPhabs plugin){
        super(plugin);
        super.skinUrl = "https://s.namemc.com/i/18525c829f6918fe.png";
    }
    public void transformation(Player player){
        super.transformation(player);
        if(super.user == null)
            super.user = player;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, false, false));
                }
                else{
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    public Location climbWall(Player player){
        Location loc = player.getLocation();
        if(transformed && player.getEyeLocation().add(player.getLocation().getDirection()).getBlock().getType() != Material.AIR){
            loc.getBlock().setType(Material.VINE);
            player.setFallDistance(0);
        }
        return loc;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getName() == super.user.getName()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (super.user != null && e.getEntity() == super.user) {
            super.transformed = false;
            super.user = null;
        }
    }

    @EventHandler
     public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(player == super.user){
            new BukkitRunnable() {
                Location loc =climbWall(player);
                @Override
                public void run() {
                if (player.isSneaking()) {
                    if(transformed && player.getEyeLocation().add(player.getLocation().getDirection()).getBlock().getType() != Material.AIR)
                        loc = climbWall(player);
                }
                else
                    cancelTask();
                }
                public void cancelTask() {
                    player.sendMessage("You are no longer climbing");
                    loc.getBlock().breakNaturally();
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 3);


        /*if (!player.isSneaking()){
            if (super.user != null && e.getPlayer().getName() == super.user.getName()) {
                if (player.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR) {

                    player.sendMessage("You are now climbing");
                }
            }
        }*/
        }
    }
    
}

