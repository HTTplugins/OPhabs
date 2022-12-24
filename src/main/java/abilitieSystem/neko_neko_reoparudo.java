package abilitieSystem;

import htt.ophabs.OPhabs;
import fruitSystem.devilFruitUser;
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
import org.bukkit.entity.LivingEntity;
import java.util.ArrayList;

public class neko_neko_reoparudo extends zoan implements Listener {

    public neko_neko_reoparudo(OPhabs plugin){
        super(plugin);
        super.skinUrl = "https://s.namemc.com/i/18525c829f6918fe.png";
    }
    public void transformation(){
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed){
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1, false, false));
                }
                else{
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    //launch player in the lookin direction
    public void frontAttack(){
        Player player = user.getPlayer();
        if(player.getLocation().getBlock().getRelative(0,-1,0).getType() != Material.AIR){
            player.setVelocity(player.getLocation().getDirection().multiply(2));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(player.getLocation().getBlock().getRelative(0,-1,0).getType() != Material.AIR)
                        cancelTask();

                    player.getWorld().getNearbyEntities(player.getLocation(), 1,2,1).forEach(entity -> {
                    if(entity.getName() != player.getName() && entity instanceof LivingEntity)
                        ((LivingEntity) entity).damage(15);
                    });
                }

                public void cancelTask(){
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 5, 5);
            player.setVelocity(player.getLocation().getDirection().multiply(1.5));

        }
    }


    public Location climbWall(){
        Player player = user.getPlayer();
        Location loc = player.getLocation();
        if(transformed && player.getEyeLocation().add(player.getLocation().getDirection()).getBlock().getType() != Material.AIR){
            loc.getBlock().setType(Material.VINE);
            player.setFallDistance(0);
        }
        return loc;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (super.user != null && e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getName() == super.user.getPlayer().getName()) {
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
                Location loc =climbWall();
                @Override
                public void run() {
                if (player.isSneaking()) {
                    if(transformed && player.getEyeLocation().add(player.getLocation().getDirection()).getBlock().getType() != Material.AIR)
                        loc = climbWall();
                }
                else
                    cancelTask();
                }
                public void cancelTask() {
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

