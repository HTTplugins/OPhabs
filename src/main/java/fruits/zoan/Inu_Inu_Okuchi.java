package fruits.zoan;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Inu_Inu_Okuchi extends Zoan {
    private boolean himo = false;
    private double iceArmor = 0;
    private boolean creatingIceArmor = false;

    public static int getFruitID() {
        return 1012;
    }

    public Inu_Inu_Okuchi(int id) {
        super(id, "inu_inu_okuchi", "Inu Inu no Mi: Model Okuchi no Makami", "inu_inu_okuchi");

        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new CooldownAbility("Himorogiri", 10, this::himorogiri));
        basicSet.addAbility(new CooldownAbility("Namuji Hyoga", 15, this::namujiHyoga));
        basicSet.addAbility(new CooldownAbility("Angry Roar", 15, this::angryRoar));
        basicSet.addAbility(new CooldownAbility("Transformation", 5, this::transformation));
        this.abilitySets.add(basicSet);
    }

    public void himorogiri() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        himo = true;
        OPHSoundLib.OphPlaySound(OPHSounds.ROCKMOVE, player.getLocation(), 1, 1);
        // Initial upward boost
        player.setVelocity(player.getVelocity().setY(1.5));

        // Task 1: Hover phase - particles only, maintain upward momentum
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                OPUser u = Inu_Inu_Okuchi.this.getUser();
                if (u == null || !u.getPlayer().isOnline() || !himo) {
                    cancel();
                    return;
                }
                Player p = u.getPlayer();
                
                if (i > 5 * 4) {
                    // End of hover phase, push forward
                    p.setVelocity(p.getLocation().getDirection().multiply(2));
                    // Don't set himo=false here - let ground detection handle it
                    cancel();
                    return;
                }

                Vector sideWayOffset = p.getLocation().getDirection().crossProduct(new Vector(0, 1, 0)).normalize().multiply(0.4);
                Vector downOffset = p.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                p.getWorld().spawnParticle(Particle.BLOCK, p.getEyeLocation().add(p.getLocation().getDirection()).add(sideWayOffset).add(downOffset), 10, 0, 0.2, 0, 0, Material.PACKED_ICE.createBlockData());
                // Small upward boost to maintain height
                Vector vel = p.getVelocity();
                if (vel.getY() < 0) {
                    p.setVelocity(vel.setY(0.2));
                }
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 15, 2);

        // Task 2: Ground impact detection and forward push
        new BukkitRunnable() {
            boolean first = true;
            @Override
            public void run() {
                OPUser u = Inu_Inu_Okuchi.this.getUser();
                if (u == null || !u.getPlayer().isOnline()) {
                    himo = false;
                    cancel();
                    return;
                }
                Player p = u.getPlayer();
                
                // Check if on ground
                if (p.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    p.getWorld().getNearbyEntities(p.getLocation(), 5, 2, 5).forEach(entity -> {
                        if (entity instanceof LivingEntity && !entity.getName().equals(p.getName())) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.damage(15, p);
                        }
                    });
                    p.getWorld().spawnParticle(Particle.BLOCK, p.getLocation(), 10000, 5, 0, 5, 0.1, Material.PACKED_ICE.createBlockData());
                    himo = false;
                    cancel();
                    return;
                }
                
                if (first) {
                    p.setVelocity(p.getLocation().getDirection().multiply(2));
                    first = false;
                }

                Vector sideWayOffset = p.getLocation().getDirection().crossProduct(new Vector(0, 1, 0)).normalize().multiply(0.4);
                Vector downOffset = p.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                p.getWorld().spawnParticle(Particle.BLOCK, p.getEyeLocation().add(p.getLocation().getDirection()).add(sideWayOffset).add(downOffset),
                        10, 0, 0.2, 0, 0, Material.PACKED_ICE.createBlockData());
            }
        }.runTaskTimer(OPhabs.getInstance(), 60, 4);
    }

    public void namujiHyoga() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FOG, player.getLocation(), 1, 1);
        iceBreath(user);
    }

    private void iceBreath(OPUser user) {
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FOG, player.getLocation(), 1, 1);

        new BukkitRunnable() {
            int steps = 0;
            Location current = player.getEyeLocation().clone();
            
            @Override
            public void run() {
                if (steps > 20) { cancel(); return; }

                current.add(player.getLocation().getDirection().clone().multiply(1.2));
                
                // Cone of ice
                for (double r = 0.5; r < 8; r += 0.5) {
                    Location coneLoc = player.getEyeLocation().clone().add(user.getPlayer().getLocation().getDirection().clone().multiply(r));
                    double radius = r * 0.3;
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, coneLoc, 5, radius, radius, radius, 0);
                    player.getWorld().spawnParticle(Particle.CLOUD, coneLoc, 2, radius, radius, radius, 0);
                    
                    // Damage in cone
                    for (Entity ent : player.getWorld().getNearbyEntities(coneLoc, radius, radius, radius)) {
                        if (ent instanceof LivingEntity && !ent.equals(player)) {
                            LivingEntity target = (LivingEntity) ent;
                            target.setFreezeTicks(target.getFreezeTicks() + 60);
                            target.damage(3, player);
                        }
                    }
                }
                
                steps++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 0);
    }

    public void createIceArmor() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (!creatingIceArmor && iceArmor < 14 && transformed) {
            creatingIceArmor = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    iceArmor = 14;
                    creatingIceArmor = false;
                }
            }.runTaskLater(OPhabs.getInstance(), 100);
        }
    }

    public void angryRoar() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FOG, player.getLocation(), 1, 1);

        ArrayList<org.bukkit.potion.PotionEffect> effects = new ArrayList<>();
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 200, 2));
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS, 200, 3));
        effects.add(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NAUSEA, 200, 1));

        // Exclude self from roar effects
        roar(player, 20, effects);
    }

    @Override
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        super.onEntityDamage(event);
        OPUser user = this.getUser();
        if (user == null) return;
        if (event.getEntity() != user.getPlayer()) return;
        
        // Cancel fall damage during Himorogiri
        if (himo && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
        }
        
        if (isFruitActive() && event.getCause().equals(EntityDamageEvent.DamageCause.FREEZE)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        OPUser user = this.getUser();
        if (user == null) return;
        if (event.getEntity() == user.getPlayer()) {
            if (iceArmor > 0) {
                double damage = event.getDamage();
                iceArmor -= damage;
                if (iceArmor < 0) {
                    iceArmor = 0;
                    event.setDamage(damage - iceArmor);
                } else {
                    event.setDamage(0);
                }
                event.getEntity().getWorld().spawnParticle(Particle.BLOCK, event.getEntity().getLocation(),
                        10, 0.5, 0.5, 0.5, 0.1, Material.PACKED_ICE.createBlockData());
            }
        }
    }
}
