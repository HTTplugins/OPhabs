package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;

public class neko_neko_reoparudo extends zoan {

    public neko_neko_reoparudo(OPhabs plugin){
        super(plugin, castIdentification.castMaterialNekoReoparudo, castIdentification.castItemNameNekoReoparudo, fruitIdentification.fruitCommandNameNekoReoparudo,"https://s.namemc.com/i/18525c829f6918fe.png","reoparudo");

        abilitiesNames.add("FrontalAttack");
        abilitiesCD.add(0);
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            frontAttack();
            abilitiesCD.set(1, 3);
        }
    }


    public void transformation(){
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed){
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 2, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, false, false));
                }
                else{
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    //launch player in the lookin direction
    public void frontAttack(){
        Player player = user.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>5*2)
                    cancelTask();

                player.getWorld().getNearbyEntities(player.getLocation(), 1,2,1).forEach(entity -> {
                if(entity.getName() != player.getName() && entity instanceof LivingEntity)
                    ((LivingEntity) entity).damage(15);
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 10, 0.5, 1.5, 0.5, 0.1);
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 4);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5));

        
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

    public void onFall(EntityDamageEvent e) {
        if (super.user != null && e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getName() == super.user.getPlayer().getName()){
            e.setCancelled(true);
        }
    }


    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(player.getName() == user.getPlayer().getName()){
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
        }
    }
   
}

