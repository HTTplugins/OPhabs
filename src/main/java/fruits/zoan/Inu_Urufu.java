package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import runnables.OphRunnable;
import skin.skinsChanger;

public class Inu_Urufu extends Zoan{
    final String regular = "regular_wolf", rage = "rage_wolf";
    Boolean raged;
    final int rageTime = 60; // Seconds
    
    public static int getFruitID()
    {
        return 1007;
    }

    public Inu_Urufu(int id) {
        super(id, "Inu_Urufu", "Inu Inu Urufu", "Inu_Urufu", "regular_wolf", "http://novask.in/6399673459.png");


        raged = false;
        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Transform into hybrid mode
        basicSet.addAbility(transform);
        
        // Crunch
        basicSet.addAbility(new CooldownAbility("Crunch", 10, () -> {
            this.frontCrunch(3);
        }));
        // Multiple Crunches
        basicSet.addAbility(new CooldownAbility("Multiple Crunches", 100, () -> {
            this.multipleCrunch(2);
        }));
        //Angry Roar
        basicSet.addAbility(new CooldownAbility("Angry Roar", 10, () -> {
            this.angryRoar(5);
        }));

        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);

        skinsChanger.setSkin(rage, "http://novask.in/6279193716.png");
    }


    public void angryRoar(double dmg){
        ArrayList<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.SLOW, 100, 1));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));

        OPHLib.roar(user.getPlayer(), dmg, 10, effects);
    }
    
    //launch player in the looking direction
    public void frontCrunch(double dmg) {
        Player player = user.getPlayer();
        OPHLib.dash(player, 3);
        new OphRunnable(25) {
            @Override
            public void OphRun() {
                if(getCurrentRunIteration() >= 20) ophCancel();
                if(getCurrentRunIteration() % 4 == 0) OPHSoundLib.OphPlaySound(OPHSounds.CRUNCH, player.getLocation(), 1, 1);

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(dmg,(Entity) user.getPlayer());
                        OPHLib.bleeding(((LivingEntity) entity), 200, player);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
            }
        }.ophRunTaskTimer(0, 1);
    }

    public void multipleCrunch(double damage) {
        int nAttacks = 5, ticksPerAttack = 21;
        new OphRunnable(nAttacks*ticksPerAttack + 10){
            @Override
            public void OphRun() {
                if(getCurrentRunIteration() >= nAttacks*ticksPerAttack) ophCancel();
                if(getCurrentRunIteration()%ticksPerAttack == 0) frontCrunch(damage);
            }
        }.ophRunTaskTimer(5, 1);
    }

    public void rageMode(){
        Player player = user.getPlayer();
        raged = true;
        OPHSoundLib.OphPlaySound(OPHSounds.ROAR, player.getLocation(), 1, 1);

        skinsChanger.changeSkin(user.getPlayer(), rage);
        
        new OphRunnable() {
            @Override
            public void OphRun() {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 2, false, false));
            }
        }.ophRunTaskLater(20);

        //Turn off on 60 seconds

        new OphRunnable(rageTime*20+10) {
            @Override
            public void OphRun() {
                if(raged){
                    raged = false;
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);

                    transformed =false;
                    transformation();
                }
            }
        }.ophRunTaskLater(rageTime*20);
    }


    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Material material = event.getItem().getType();
        if (material == Material.BEEF || 
            material == Material.CHICKEN || 
            material == Material.PORKCHOP ||
            material == Material.RABBIT || 
            material == Material.MUTTON) 
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1, false, false));
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event){
        if(user.getPlayer().getHealth() < user.getPlayer().getMaxHealth()/2 && !raged)
            rageMode();
    }

    @Override
    protected void onAddFruit() {

    }
}
