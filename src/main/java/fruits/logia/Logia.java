package fruits.logia;

import cast.FruitCaster;
import fruits.DevilFruit;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import users.OPUser;

import java.util.ArrayList;

public abstract class Logia extends DevilFruit {
    protected Particle element;
    private static final ArrayList<Logia> LOGIAFRUITS = new ArrayList<>();
    private static BukkitTask logiaBodyTask = null;

    private BukkitTask logiaModeTask = null;
    private boolean logiaBodyActive = false;
    public boolean ignoreLogiaOff = false;

    public Logia(int id, String name, String displayName, String commandName, Particle element) {
        super(id, name, displayName, commandName);
        this.element = element;

        if(logiaBodyTask == null) {
            logiaBodyTask = new BukkitRunnable(){
                @Override
                public void run() {
                    for(Logia fruit : LOGIAFRUITS){
                        OPUser user = fruit.getUser();

                        if(user != null && user.getPlayer() != null){
                            FruitCaster caster = (FruitCaster) user.getActiveCasters().get(fruit.getID());
                            Player player = user.getPlayer();

                            boolean hasCaster = caster != null && caster.isOwnedBy(user) && (caster.isThisItem(player.getInventory().getItemInMainHand())
                                    || caster.isThisItem(player.getInventory().getItemInOffHand()));

                            if(hasCaster) {
                                if(!fruit.logiaBodyActive) {
                                    fruit.logiaBodyActive = true;
                                    fruit.logiaModeON();
                                }
                            } else if(fruit.logiaBodyActive && !fruit.ignoreLogiaOff) {
                                fruit.logiaBodyActive = false;
                                fruit.logiaModeOFF();
                            }
                            
                            // Extra safety: ensure flight matches caster state
                            if (fruit.logiaBodyActive && !hasCaster) {
                                fruit.logiaBodyActive = false;
                                fruit.logiaModeOFF();
                            }
                        }
                    }
                }
            }.runTaskTimer(OPhabs.getInstance(),20,3);
        }

        LOGIAFRUITS.add(this);
    }

    protected abstract void logiaModeON();
    
    protected void logiaModeOFF(){
        OPUser user = this.getUser();
        if (user != null && user.getPlayer() != null) {
            user.getPlayer().setAllowFlight(false);
            user.getPlayer().setFlying(false);
        }
        if(logiaModeTask != null) {
            logiaModeTask.cancel();
            logiaModeTask = null;
        }
    }

    protected void logiaModeON(BukkitTask animation) {
        OPUser user = this.getUser();
        if (user != null && user.getPlayer() != null) {
            user.getPlayer().setAllowFlight(true);
        }
        logiaModeTask = animation;
    }

    public boolean isLogiaBodyActive() {
        return logiaBodyActive;
    }

    // Called from fruit's onPlayerMove to ensure flight is only enabled with caster in hand
    protected void updateFlightState() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        FruitCaster caster = (FruitCaster) user.getActiveCasters().get(this.getID());
        boolean hasCaster = caster != null && caster.isOwnedBy(user) && (caster.isThisItem(player.getInventory().getItemInMainHand())
                || caster.isThisItem(player.getInventory().getItemInOffHand()));

        if (hasCaster && !logiaBodyActive) {
            logiaBodyActive = true;
            logiaModeON();
        } else if (!hasCaster && logiaBodyActive) {
            logiaBodyActive = false;
            logiaModeOFF();
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        // Check if logia body is active (caster in hand or logiaMode)
        boolean hasCaster = false;
        FruitCaster caster = (FruitCaster) user.getActiveCasters().get(this.getID());
        if (caster != null && caster.isOwnedBy(user)) {
            if (caster.isThisItem(player.getInventory().getItemInMainHand()) 
                    || caster.isThisItem(player.getInventory().getItemInOffHand())) {
                hasCaster = true;
            }
        }

        if (!logiaBodyActive && !hasCaster) {
            return; // Logia body not active
        }

        // Check if in water
        if (player.getLocation().getBlock().isLiquid()) {
            return; // No immunity in water
        }

        // Check if damaged by another player with Haki or Logia
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                Player damagerPlayer = (Player) damager;
                OPUser damagerUser = OPhabs.newUsers.getUser(damagerPlayer.getUniqueId());
                if (damagerUser != null) {
                    if (damagerUser.haki != null && damagerUser.haki.getLevel() > 0) {
                        return; // Haki bypasses logia immunity
                    }
                    if (damagerUser.hasDevilFruit() && damagerUser.getDevilFruit() instanceof Logia) {
                        return; // Logia vs Logia bypasses immunity
                    }
                }
            }
        }

        // Cancel damage and show particles
        event.setCancelled(true);
        player.getWorld().spawnParticle(element, player.getLocation(), 10, 0, 1, 0, 0.1);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        super.onPlayerMove(event);
        updateFlightState();
    }
}
