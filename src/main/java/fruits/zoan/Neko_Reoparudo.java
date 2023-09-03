package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHAnimationLib;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import runnables.OphRunnable;
import skin.skinsChanger;

public class Neko_Reoparudo extends Zoan{
    
    public static int getFruitID()
    {
        return 1008;
    }

    public Neko_Reoparudo(int id) {
        super(id, "Neko_Reoparudo", "Neko Neko Reoparudo", "Neko_Reoparudo", "reoparudo", "https://s.namemc.com/i/18525c829f6918fe.png");


        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Transform into hybrid mode
        basicSet.addAbility(transform);
        
        // Crunch
        basicSet.addAbility(new CooldownAbility("Frontal attack", 5, () -> {
            this.frontAttack(4);
        }));
        // Multiple Crunches
        basicSet.addAbility(new CooldownAbility("Tsume", 10, () -> {
            this.tsume(2);
        }));
        //Angry Roar
        basicSet.addAbility(new CooldownAbility("Neko Fearsome", 3, () -> {
            this.nekoFearsome();
        }));

        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);
    }


    @Override
    public void transformation() {
        super.transformation();

        if(transformed) {
            new OphRunnable() {
                @Override
                public void OphRun() {
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, false, false));

                }
            }.ophRunTaskLater(10);
        }
        else {
            user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
            user.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    //launch player in the looking direction
    public void frontAttack(double dmg) {
        Player player = user.getPlayer();
        OPHLib.dash(player, 3);
        new OphRunnable(10) {
            @Override
            public void OphRun() {
                OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);

                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,2,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(dmg,(Entity) user.getPlayer());
                        OPHLib.bleeding(((LivingEntity) entity), 200, player);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0.5, 1.5, 0.5, 0.1);
            }
        }.ophRunTaskTimer(0, 4);
    }

    public void tsumeAnimation() {
        Player player = user.getPlayer();
        double height = 4;
        OPHAnimationLib.horizontalCylinder(player.getLocation(), 0.5, 2, 0.2, Particle.CRIT, null);
        
        new OphRunnable(10) {
            @Override
            public void OphRun() {
                Location loc = player.getLocation().clone().setDirection(player.getLocation().getDirection().rotateAroundY(36));
                player.teleport(loc);
            }
        }.ophRunTaskTimer(0, 0);
    }

    public void tsume(double dmg) {
        Player player = user.getPlayer();
        Location loc = player.getLocation();

        OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, loc, 100, 10);
        tsumeAnimation();

        player.getWorld().getNearbyEntities(loc, 4, 2, 4).forEach(entity -> {
            if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), entity.getLocation().getY() - loc.getY(),
                        entity.getLocation().getZ() - loc.getZ()).normalize();

                ((LivingEntity) entity).damage(dmg, player);

                player.getWorld().playSound(loc, Sound.ENTITY_CAT_PURREOW, 100, 10);

                entity.setVelocity(dir.multiply(3));
                OPHLib.bleeding((LivingEntity) entity, 100, player);
            }
        });
    }

    public void nekoFearsome() {
        Player player = user.getPlayer();
        Location loc = player.getEyeLocation();
        double radio = 2;
        player.getWorld().getNearbyEntities(loc, radio, radio/2, radio).forEach(entity -> {
            if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
            }
        });
        OPHAnimationLib.circularWave(loc, 1,2,0.5,0.35,10, Particle.CRIT_MAGIC);
    }

    public void climbWall() {
        Player player = user.getPlayer();
        Location loc = player.getEyeLocation().add(player.getLocation().getDirection()); 
        if(transformed && loc.getBlock().getType()!= Material.AIR){
            player.setVelocity(player.getEyeLocation().getDirection());
        }
    }

    @Override
        public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if(event.isSneaking()){
            Player player = event.getPlayer();
            new OphRunnable(2000) {
                @Override
                public void OphRun() {
                if (player.isSneaking()) {
                    climbWall();
                }
                else
                    ophCancel();
                }
            }.ophRunTaskTimer(0, 4);
        } 
    }

    @Override
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
    public void onFall(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @Override
    protected void onAddFruit() {

    }
}
