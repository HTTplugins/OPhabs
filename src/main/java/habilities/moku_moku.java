package habilities;


import htt.ophabs.OPhabs;
import org.bukkit.*;
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
import java.util.ArrayList;

public class moku_moku implements Listener{
    final Material AIR = Material.AIR;
    final Particle SMOKE = Particle.CLOUD;
    boolean particlesON;
    private OPhabs plugin;
    ArrayList<String> smokeBodyON = new ArrayList<>(), smokeLegsON = new ArrayList<>();

    public moku_moku(OPhabs plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleSwim(EntityToggleSwimEvent event) {
        if(event.getEntity() instanceof Player)
            ((Player) event.getEntity()).damage(2);
    }

    @EventHandler
    public void playerOnWater(PlayerMoveEvent event) {
        if ((isInWaterBlock(event.getPlayer()))) {
            event.getPlayer().damage(1);
        }
        /*if(particlesON && isInRain(event.getPlayer()))
            event.getPlayer().damage(3);*/
    }

    public boolean isInRain(Player player) {
        Boolean in = false;
        if (player.getWorld().isThundering() || player.getWorld().hasStorm()) {
            int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());
            if (blockLocation <= player.getLocation().getY()) {
                in = true;
            }
        }
        player.sendMessage("RAIN");
        return in;
    }
    public boolean isInWaterBlock(Player player) {
        Boolean in = false;
        if (player.getLocation().getBlock().getType() == Material.WATER)
            in = true;

        return in;
    }

    public boolean isInWater(Player player) {
        return (isInWaterBlock(player));// || isInRain(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        boolean oldP=particlesON;
        if (player.getItemInHand().getType() == Material.STONE_SWORD) {
            if (player.isSneaking())
                particlesON = smokeBody(player);
            else
                particlesON = smokeLegs(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!particlesON || (oldP == particlesON))
                        cancelTask();

                    player.getWorld().spawnParticle(SMOKE, player.getLocation(), 30);
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }

    public void toggleFly(Player player){
        if (smokeLegsON.contains(player.getName()) || smokeBodyON.contains(player.getName()) ){
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event){
        Player player;
        if(event.getEntity() instanceof  Player){
            player = (Player) event.getEntity();
            if(smokeBodyON.contains(player.getName()) && !isInWater(player))
                event.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(smokeLegsON.contains(player.getName()))
            smokeLegs(player);
        if (smokeBodyON.contains(player.getName()))
            smokeBody(player);
        particlesON=false;
    }

        public boolean smokeBody(Player player) {
            if (!smokeBodyON.contains(player.getName())) {
                if (smokeLegsON.contains(player.getName()))
                    smokeLegs(player);
                smokeBodyON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 2, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
            } else {
                smokeBodyON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            toggleFly(player);
            return (smokeBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName()));
        }

    public boolean smokeLegs(Player player){
        if (!smokeBodyON.contains(player.getName())){
            if (!smokeLegsON.contains(player.getName())) {
                smokeLegsON.add(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            } else {
                smokeLegsON.remove(player.getName());
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            toggleFly(player);
        }

        return (smokeBodyON.contains(player.getName()) || smokeLegsON.contains(player.getName())) ;
    }
}
