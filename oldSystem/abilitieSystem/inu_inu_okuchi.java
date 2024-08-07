package oldSystem.abilitieSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class inu_inu_okuchi extends zoan {
    private final ItemStack iceBlock = new ItemStack(Material.PACKED_ICE);
    public double iceArmor = 0.0;
    public boolean creatingIceArmor = false;
    public boolean himo = false;


    public inu_inu_okuchi(OPhabs plugin) {
        super(plugin, 5, 3, 10, "inu_inu_okuchi", "Inu Inu no Mi Moderu Okuchi no Makami", "Inu Inu Okuchi caster", 7, 2,"http://novask.in/4672583547.png","white_wolf");

        abilitiesNames.add("Himorogiri");
        abilitiesCD.add(0);
        abilitiesNames.add("Namuji hyoga");
        abilitiesCD.add(0);
        abilitiesNames.add("Angry Roar");
        abilitiesCD.add(0);

        runIceArmor();
    }

    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            himorogiri();
            abilitiesCD.set(1, 10);
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            namujiHyoga();
            abilitiesCD.set(2, 15);
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            angryRoar();
            abilitiesCD.set(3, 15);
        }
    }

    public void transformation() {
        super.transformation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(transformed) {
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 5, false, false));
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3, false, false));
                }
                else {
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                }
            }
        }.runTaskLater(plugin, 20);
        
    }

    public void himorogiri() {
        Player player = user.getPlayer();

        player.setVelocity(player.getVelocity().setY(1.5));
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>5*4 || user == null || !user.getPlayer().isOnline()) {
                    player.setVelocity(player.getLocation().getDirection().multiply(2));
                    cancelTask();
                }
                
                Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
                Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset), 10, 0, 0.2, 0, 0, Material.PACKED_ICE.createBlockData());
                player.setVelocity(new Vector(0,0.1,0));
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 15, 2);
        
    new BukkitRunnable() {
        boolean first = true;
        @Override
        public void run() { 
            if(user == null || !user.getPlayer().isOnline()) {
                cancelTask();
            }
            if(player.getLocation().add(0,-1,0).getBlock().getType() != Material.AIR) {
                player.getWorld().playSound(player.getLocation(), "icecrack", 1, 1);
                player.getWorld().getNearbyEntities(player.getLocation(), 5, 2, 5).forEach(entity -> {
                    if(entity instanceof LivingEntity && entity.getName() != player.getName()) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.damage(15, player);
                    }
                });
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 10000, 5, 0, 5,
                                            0.1, Material.PACKED_ICE.createBlockData());
                himo = false;
                cancelTask();
            }
            if(first) {
                player.setVelocity(player.getLocation().getDirection().multiply(2));
                first = false;
                himo = true;
            }
            

            Vector sideWayOffset = player.getLocation().getDirection().crossProduct(new Vector(0,1,0)).normalize().multiply(0.4);
            Vector downOffset = player.getLocation().getDirection().crossProduct(sideWayOffset).normalize().multiply(0.2);
            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getEyeLocation().add(player.getLocation().getDirection()).add(sideWayOffset).add(downOffset),
                                        10, 0, 0.2, 0, 0, Material.PACKED_ICE.createBlockData());
        }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 60, 4);
    }
   
    public void namujiHyoga() {
        Player player = user.getPlayer();

        player.getWorld().playSound(player.getLocation(), "icebreathnamuji", 1, 1);
        OPHLib.iceBreath(user, new Vector(0,0,0), new Vector(0,0,0),0,12, 90,4,0.5,40,1.5,4,"none");
    }

    public void createIceArmor() {
        if(!active) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    createIceArmor();
                }
            }.runTaskLater(plugin, 100);
        } else {
            iceArmor = 14;
            creatingIceArmor = false;
        }
    }

    public void angryRoar(){
        ArrayList<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.SLOW, 200, 2));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));

        roar(user.getPlayer(), 20, effects);
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() == user.getPlayer()) {
            if(event.getCause() == DamageCause.FALL && himo) {
                event.setCancelled(true);
            }
            if(iceArmor > 0) {
                double damage = event.getDamage();
                iceArmor -= damage;
                if(iceArmor < 0) {
                    iceArmor = 0;
                    event.setDamage(damage - iceArmor);
                } else {
                    event.setDamage(0);
                }

                event.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, event.getEntity().getLocation(),
                        10, 0.5, 0.5, 0.5, 0.1, Material.PACKED_ICE.createBlockData());
            }
        }
    }

    public void runIceArmor() {
        new BukkitRunnable(){

            @Override
            public void run() {
                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null){
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null) {
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)) {
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else {
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)) {
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(fruit.getCasterName())) {
                            if(!creatingIceArmor && iceArmor < 14 && transformed){
                                creatingIceArmor = true;
                                createIceArmor();
                            }
                        }
            }
            }


        }.runTaskTimer(plugin, 0, 1);

    }
}

