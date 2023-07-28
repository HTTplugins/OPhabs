package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
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
    public zoan(OPhabs plugin, double damageBonus, double armorBonus, int id, String fruitCommandName, String fruitItemName, String casterName, double casterDamage, double casterSpeed, String skinUrl, String skinName){
        super(plugin, damageBonus, armorBonus, id, fruitCommandName, fruitItemName, casterName, casterDamage, casterSpeed);
        skinsChanger.setSkin(skinName, skinUrl);
        this.skinUrl = skinUrl;
        this.skinName = skinName;

        abilitiesNames.add("Transform");
        abilitiesCD.add(0);

    }

    /**
     * @brief Transformation from normal to zoan (Skin changer).
     * @author Vaelico786.
     */
    public void transformation() {
        Player player = user.getPlayer();
        if(!transformed) {
            skinsChanger.changeSkin(player, skinName);
            transformed = true;
            setDamage(damageBonus*2);
            setArmor(armorBonus*2);
        }
        else {
            transformed = false;
            skinsChanger.resetSkin(player);
            setDamage(damageBonus/2);
            setArmor(armorBonus/2);
        }
        user.reloadStats();
    }

    /**
     * @brief CORE ABILITY: transforms the player in to his zoan transformation.
     * @author Vaelico786.
     */
    public void ability1() {
        transformation();
    }

    public void playerOnDeath(PlayerDeathEvent event) {
        if(transformed){
            transformed = false;
            skinsChanger.resetSkin(event.getEntity());
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
                entity.damage(2, (Entity) user.getPlayer());

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

