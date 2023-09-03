package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import cosmetics.cosmeticsArmor;
import libs.OPHAnimationLib;
import libs.OPHLib;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import runnables.OphRunnable;
import skin.skinsChanger;

public class Ryu_Allosaurs extends Zoan{
    private ArrayList<LivingEntity> golpeadosHabilidades = new ArrayList();
    private ArrayList<LivingEntity> StompedList;

    public static int getFruitID()
    {
        return 1009;
    }

    public Ryu_Allosaurs(int id) {
        super(id, "Ryu_Allosaurs", "Ryu Ryu Allosaurs", "Ryu_Allosaurs", "allosaurs", "http://novask.in/5915273049.png");


        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Transform into hybrid mode
        basicSet.addAbility(transform);
        
        // Crunch
        basicSet.addAbility(new Ability("Crunch", () -> {
            this.frontCrunch(3);
        }));
        // Multiple Crunches
        basicSet.addAbility(new Ability("Tail Spin", () -> {
            this.tailSpin(5);
        }));
        //Angry Roar
        basicSet.addAbility(new Ability("Stomp", () -> {
            this.stomp(5);
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
            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1, false, false));

            user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1, false, false));
        }
        else {
            user.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            user.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    }

    
    //launch player in the looking direction
    public void frontCrunch(double dmg) {
        Player player = user.getPlayer();
        OPHLib.dash(player, 3);
        new OphRunnable() {
            @Override
            public void OphRun() {
                if(getCurrentRunIteration() > 20) ophCancel();
                if(getCurrentRunIteration() % 4 == 0) player.getWorld().playSound(player.getLocation(), "crunch", 1, 1);
                player.getWorld().getNearbyEntities(player.getEyeLocation(), 1,1,1).forEach(entity -> {
                    if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !(entity instanceof Marker)) {
                        ((LivingEntity) entity).damage(dmg,(Entity) user.getPlayer());
                        OPHLib.bleeding(((LivingEntity) entity), 200, player);
                    }
                });

                player.getWorld().spawnParticle(Particle.CRIT, player.getEyeLocation(), 10, 0, 0, 0, 0);
            }
        }.ophRunTaskTimer(0, 1);
    }


    public void tailSpin(double dmg) {
        Player player = user.getPlayer();
        double radius = 3;
        cosmeticsArmor.killCosmeticArmor(player, "allosaurs_tail");
        cosmeticsArmor.summonCosmeticArmor(4, "allosaurs_tail", player, Material.PUMPKIN);
        player.getWorld().getNearbyEntities(player.getLocation(), radius,1,radius).forEach(entity -> {
            if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity)
                ((LivingEntity) entity).damage(dmg, (Entity) user.getPlayer());
        });

        new OphRunnable() {
            @Override
            public void OphRun() {
                if(getCurrentRunIteration()>10) ophCancel();
                Location loc = player.getLocation().clone().setDirection(player.getLocation().getDirection().rotateAroundY(36));
                player.teleport(loc);
            }
            @Override
            public void ophCancel(){
                super.ophCancel();

                cosmeticsArmor.killCosmeticArmor(player, "allosaurs_tail");
                cosmeticsArmor.summonCosmeticArmor(3, "allosaurs_tail", player, Material.PUMPKIN);
            }
        }.ophRunTaskTimer(0, 0);
    }

    public void stomp(double dmg) {
        final Player player = user.getPlayer();
        final World world = player.getWorld();
        this.StompedList = new ArrayList();
        player.setVelocity(new Vector(0, -50, 0));
        new OphRunnable(20000) {
            @Override
            public void OphRun() {
                Location loc = player.getLocation();
                if (!player.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    animationStomp(world, player.getLocation());
                    world.playSound(player.getLocation(), "stomp", 1, 1);
                    StompedList.forEach((livingEntity) -> {
                        livingEntity.damage(dmg, player);
                    });
                    ophCancel();
                }

                player.setFallDistance(0);

                world.getNearbyEntities(loc, 5.0, 1.0, 5.0).forEach((entity) -> {
                    if (entity instanceof LivingEntity && !StompedList.contains(entity) && !entity.equals(player)) {
                        StompedList.add((LivingEntity)entity);
                        if (entity instanceof Player) {
                            ((Player)entity).setFlying(false);
                        }

                        entity.setVelocity(new Vector(0, -50, 0));
                    }
                });
            }
            
            @Override
            public void ophCancel() {
                super.ophCancel();
                StompedList.clear();
            };

        }.ophRunTaskTimer(0, 1);
    }
        
    //TODO
    public void animationStomp(World world, Location loc) {
        for(int j = 0; j < 5; ++j) {
            for(int i = 0; i < 10; ++i) {
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)i) * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)i) * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)(-i)) * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)(-i)) * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)i) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)i) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)i) * (double)j, 1.0, Math.cos((double)(-i)) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
                world.spawnParticle(Particle.CRIT, loc.clone().add(Math.sin((double)(-i)) * (double)j, 1.0, Math.cos((double)(-i)) / 2.0 * (double)j), 0, 0.0, 0.0, 0.0);
            }
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
    protected void onAddFruit() {
        //
        //GIVE TAIL
        new OphRunnable(){
            @Override
            public void OphRun() {
                cosmeticsArmor.summonCosmeticArmor(3, "allosaurs_tail", user.getPlayer(), Material.PUMPKIN);
            }
        }.ophRunTaskLater(10);

    }

    @Override
    protected void onRemoveFruit() {
        super.onRemoveFruit();
        cosmeticsArmor.killCosmeticArmor(user.getPlayer(), "allosaurs_tail");
    }
}
