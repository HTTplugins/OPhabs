package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import skin.skinsChanger;


public class zoan extends df{
    public boolean transformed = false;
    skinsChanger skinC = new skinsChanger();
    String skinUrl="", skinAwakenUrl="", skinName="", skinAwakenName="";

    public zoan(OPhabs plugin, Material castMaterial, String castName, String commandName, String skinUrl, String skinName){
        super(plugin, castMaterial, castName, commandName);
        skinC.setSkin(skinName, skinUrl);
        this.skinUrl = skinUrl;
        this.skinName = skinName;

        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
    }

    public zoan(OPhabs plugin, abilityUser user, Material castMaterial, String castName, String commandName) {
        super(plugin, user, castMaterial, castName, commandName);
        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
    }


    public void transformation(){
        Player player = user.getPlayer();
        if(!transformed){
            skinC.changeSkin(player, skinName);
            transformed = true;
        }
        else{
            transformed = !transformed;
            skinC.resetSkin(player);
        }
    }

    public void ability1(){
        transformation();
    }

    public void playerOnDeath(PlayerDeathEvent event){
        if(transformed){
            transformed = false;
            skinC.resetSkin(event.getEntity());
            super.user = null;
        }
    }

    public void Sangrado(LivingEntity entity) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i > 5)
                    this.cancel();
                entity.damage(2);

                for(int i = 0; i < 4; i++)
                    if(!entity.isDead()) {
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.25, 0, 0.25),
                                0, 0, 0, 0);
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.25, 0, -0.25),
                                0, 0, 0, 0);
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(-0.25, 0, -0.25),
                                0, 0, 0, 0);
                    }
                i++;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}

