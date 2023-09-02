package runnables;


import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public abstract class OphControlledEntRunnable  extends OphRunnable implements Listener {
    private final Monster controlledEntity;
    private final int maxLiveTicks;
    private final double entDmg;


    public OphControlledEntRunnable(Monster controlledEntity, int maxLiveTicks, double entDmg) {
        super(maxLiveTicks+1);
        this.controlledEntity = controlledEntity;
        this.maxLiveTicks = maxLiveTicks;
        this.entDmg = entDmg;
    }


    @Override
    public void run(){
        if(getCurrentLiveTicks() > getMaxLiveTicks() || controlledEntity.isDead()){
            this.ophCancel();
            return;
        }
        this.particleAnimation();
        super.run();
    }

    @Override
    public void ophCancel() {
        onDeath();
        // Posible animación de muerte
        controlledEntity.setHealth(0);
        super.ophCancel();
    }

    public abstract void particleAnimation();
    public abstract void onDeath();

    public Monster getControlledEntity() {
        return controlledEntity;
    }

    public int getMaxLiveTicks() {
        return maxLiveTicks;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        UUID ownerUUID = this.getOwnerUUID();

        if(ownerUUID != null){
            Player player = Bukkit.getPlayer(ownerUUID);
            ((LivingEntity) event.getEntity()).damage(entDmg, player);
            event.setCancelled(true);
        }
    }

    public int getCurrentLiveTicks() {
        return controlledEntity.getTicksLived();
    }


}
