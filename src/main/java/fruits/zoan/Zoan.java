package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import fruits.DevilFruit;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.List;

public abstract class Zoan extends DevilFruit {
    protected String regularSkin;
    protected String rageSkin;
    protected boolean transformed = false;
    protected String regularModel;
    protected String rageModel;
    protected int rageTime = 60;

    public Zoan(int id, String name, String displayName, String commandName) {
        super(id, name, displayName, commandName);
        this.regularModel = name;
        this.rageModel = name + "_rage";
    }

    public Zoan(int id, String name, String displayName, String commandName, String particle) {
        super(id, name, displayName, commandName);
        this.regularModel = name;
        this.rageModel = name + "_rage";
    }

    public void transformation() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        transformed = !transformed;
        if (transformed) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 2, false, false));
            OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        }
    }

    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {}

    @Override
    public void onEntityDamage(EntityDamageEvent event) {}

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {}

    public void onAddFruit() {}

    public void onRemoveFruit() {}

    public void roar(Player player, int radius, ArrayList<org.bukkit.potion.PotionEffect> effects) {
        // Spawn roar particles at player location
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getLocation().add(0, 1.5, 0), 50, 2, 1, 2, 0.5);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1.5, 0), 30, 2, 1, 2, 0.2);
        
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other != player && other.getLocation().distance(player.getLocation()) <= 20) {
                for (org.bukkit.potion.PotionEffect effect : effects) {
                    other.addPotionEffect(effect);
                }
            }
        }
    }
}
