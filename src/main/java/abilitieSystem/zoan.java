package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import skin.skinsChanger;


/**
 * @brief Zoan --> Devil fruit Subtype.
 * @author Vaelico786, MiixZ.
 */
public class zoan extends df{
    public boolean transformed = false;
    skinsChanger skinC = new skinsChanger();
    String skinUrl="", skinAwakenUrl="", skinName="", skinAwakenName="";

    /**
     * @brief Zoan constructor.
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @param skinUrl skin link for transformation.
     * @param skinName skin name for transformation.
     * @author Vaelico786.
     */
    public zoan(OPhabs plugin, Material castMaterial, String castName, String commandName, String skinUrl, String skinName){
        super(plugin, castMaterial, castName, commandName);
        skinC.setSkin(skinName, skinUrl);
        this.skinUrl = skinUrl;
        this.skinName = skinName;

        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
    }

    /**
     * @brief Zoan constructor (simplified and with user).
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @param user Zoan user.
     * @author Vaelico786.
     */
    public zoan(OPhabs plugin, abilityUser user, Material castMaterial, String castName, String commandName) {
        super(plugin, user, castMaterial, castName, commandName);
        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
    }

    /**
     * @brief Transformation from normal to zoan (Skin changer).
     * @author Vaelico786.
     */
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

    /**
     * @brief CORE ABILITY: transforms the player in to his zoan transformation.
     * @author Vaelico786.
     */
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

    /**
     * @brief General bleeding function for zoans type.
     * @param entity entity that's going to bleed.
     * @param ticks bleeding time.
     * @author MiixZ.
     */
    public void Sangrado(LivingEntity entity, int ticks) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i > ticks/20)
                    this.cancel();
                entity.damage(2);

                for(int i = 0; i < 4; i++)
                    if(!entity.isDead()) {
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.2, 0, 0.2),
                                0, 0, 0, 0);
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(0.2, 0, -0.2),
                                0, 0, 0, 0);
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getEyeLocation().add(-0.2, 0, -0.2),
                                0, 0, 0, 0);
                    }
                i++;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}

