package habilities;


import java.util.Random;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;

public class moku_moku implements Listener{
    final Material AIR = Material.AIR;
    final Particle SMOKE = Particle.CLOUD;
    private OPhabs plugin;
    ArrayList<String> smokeBodyON = new ArrayList<>(), smokeLegsON = new ArrayList<>();

    public moku_moku(OPhabs plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.STONE_SWORD) {
            if(player.isSneaking())
                smokeBody(player);
            else
                smokeLegs(player);
            if (smokeLegsON.contains(player.getName()) || smokeBodyON.contains(player.getName()) ){
                event.getPlayer().setAllowFlight(true);
                event.getPlayer().setFlying(true);
            } else {
                event.getPlayer().setAllowFlight(false);
                event.getPlayer().setFlying(false);
            }
        }
        new BukkitRunnable(){
            @Override
            public void run(){
                if(!smokeLegsON.contains(player.getName()) && !smokeBodyON.contains(player.getName()))
                    cancelTask();

                player.getWorld().spawnParticle(SMOKE, player.getLocation(), 30);
            }
            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event){
        Player player;
        if(event.getEntity() instanceof  Player){
            player = (Player) event.getEntity();
            if(smokeBodyON.contains(player.getName()))
                event.setCancelled(true);
        }
    }

    public void smokeBody(Player player){
        if (!smokeBodyON.contains(player.getName())) {
            if(smokeLegsON.contains(player.getName()))
                smokeLegs(player);
            smokeBodyON.add(player.getName());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 2, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
        }else {
            smokeBodyON.remove(player.getName());
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    public void smokeLegs(Player player){
        if(!smokeBodyON.contains(player.getName())) {
            if (!smokeLegsON.contains(player.getName())) {
                smokeLegsON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            } else {
                smokeLegsON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }
}
