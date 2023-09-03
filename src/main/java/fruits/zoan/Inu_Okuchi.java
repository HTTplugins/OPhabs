package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import runnables.OphRunnable;

public class Inu_Okuchi extends Zoan{
    private final BlockData iceBlock = Material.PACKED_ICE.createBlockData();
    public double iceArmor = 0.0, maxIceArmor = 14.0;
    public boolean himo = false;

    public static int getFruitID()
    {
        return 1006;
    }

    public Inu_Okuchi(int id) {
        super(id, "Inu_Okuchi", "Inu Inu Okuchi", "Inu_Okuchi", "white_wolf", "http://novask.in/4672583547.png");


        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Transform into hybrid mode
        basicSet.addAbility(transform);
        
        // Himorogiri
        basicSet.addAbility(new Ability("Himorogiri", () -> {
            this.himorogiri(15);
        }));
        //Namuji Hyoga
        basicSet.addAbility(new Ability("Namuji Hyoga", () -> {
            this.namujiHyoga(4);
        }));
        //Angry Roar
        basicSet.addAbility(new Ability("Angry Roar", () -> {
            this.angryRoar(10);
        }));


        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);

        // Bukkit.getScheduler().scheduleSyncRepeatingTask(OPhabs.getInstance(), () -> {
        //     if(isFruitActive() && iceArmor != maxIceArmor) iceArmor++;
        // }, 0, 20L);

    }

    public void himorogiri(double damage) {
        Player player = user.getPlayer();

        player.setVelocity(player.getVelocity().setY(1.5));
        new OphRunnable(25){
            @Override
            public void OphRun() {
                if(getCurrentRunIteration()>20) ophCancel();
                Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
                Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset), 10, 0, 0.2, 0, 0, iceBlock);
                player.setVelocity(new Vector(0,0.1,0));
                player.setFallDistance(0);
            }
        }.ophRunTaskTimer(15, 2);
        
        new OphRunnable(){
            boolean first = true;
            @Override
            public void OphRun() {
                if(player.getLocation().add(0,-1,0).getBlock().getType() != Material.AIR) {
                    player.getWorld().playSound(player.getLocation(), "icecrack", 1, 1);
                    player.getWorld().getNearbyEntities(player.getLocation(), 5, 2, 5).forEach(entity -> {
                        if(entity instanceof LivingEntity && entity.getName() != player.getName()) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.damage(damage, player);
                        }
                    });
                    player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 10000, 5, 0, 5,
                                                    0.1, iceBlock);
                    himo = false;
                    ophCancel();
                }
                if(first) {
                    player.setVelocity(player.getLocation().getDirection().multiply(2));
                    first = false;
                    himo = true;
                }
                

                Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
                Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset),
                                                10, 0, 0.2, 0, 0, iceBlock);
            }
        }.ophRunTaskTimer(55, 4);
    }

    public void namujiHyoga(double dmg) {
        Player player = user.getPlayer();
        World world = player.getWorld();

        OPHSoundLib.OphPlaySound(OPHSounds.ICEBREATHNAMUJI, player.getLocation(), 1, 1);

        OPHLib.breath(player, new Vector(0,0,0), new Vector(0,0,0),0,12, 90,0.5,40,1.5,4,"none", Particle.BLOCK_CRACK, iceBlock, (amplitudeX, amplitudeY, amplitudeZ, location) -> {

            world.getNearbyEntities(location, amplitudeX, amplitudeY, amplitudeZ).forEach(entity -> {
                if (entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                    ((LivingEntity) entity).damage(dmg, player);
                    if (((LivingEntity) entity).getFreezeTicks() <= 0) {
                        ((LivingEntity) entity).setFreezeTicks(((LivingEntity) entity).getFreezeTicks() + 300);
                    }
                }
            });
        });
    }

    public void angryRoar(double dmg){
        ArrayList<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.SLOW, 200, 2));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));

        OPHLib.roar(user.getPlayer(), dmg, 10, effects);
    }

    public void onFall(EntityDamageEvent event) {
        if(himo) event.setCancelled(true);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(isFruitActive() && iceArmor > 0 && event.getDamage() > 0) {
            double damage = event.getDamage();
            iceArmor -= damage;
            if(iceArmor < 0) {
                iceArmor = 0;
                event.setDamage(damage - iceArmor);
            } else {
                event.setDamage(0);
            }
        }
    }
    
    @Override
    public void onPlayerCasterHeldEvent(PlayerItemHeldEvent event) {
        event.getPlayer().sendMessage("Held");
        new OphRunnable(999999) {
            Player player = event.getPlayer();
            ItemStack caster = event.getPlayer().getInventory().getItem(event.getNewSlot());
            @Override
            public void OphRun() {
                if(player.getInventory().getItem(event.getNewSlot()) != caster) {ophCancel();}
                if(iceArmor<maxIceArmor) iceArmor+=0.1;
            }
        }.ophRunTaskTimer(0, 0);;

    }

    @Override
    protected void onAddFruit() {

    }
}
