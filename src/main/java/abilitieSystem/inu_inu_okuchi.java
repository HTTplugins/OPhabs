package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.LivingEntity;
;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.bukkit.util.Vector;

import java.util.ArrayList;

import static java.lang.Math.*;

public class inu_inu_okuchi extends zoan {
    private final ItemStack iceBlock = new ItemStack(Material.PACKED_ICE);
    public double iceArmor = 0.0;
    public boolean creatingIceArmor = false;
    public boolean himo = false;
    public boolean atronar = false;

    private ArrayList<LivingEntity> atronados = new ArrayList<>();

    public inu_inu_okuchi(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialInuOkuchi, castIdentification.castItemNameInuOkuchi,
                fruitIdentification.fruitCommandNameInuOkuchi,"http://novask.in/4672583547.png","white_wolf");

        abilitiesNames.add("Himorogiri");
        abilitiesCD.add(0);
        abilitiesNames.add("Namuji hyoga");
        abilitiesCD.add(0);
        abilitiesNames.add("Atrocity");
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
            abilitiesCD.set(3, 15);

            if(!atronar)
                Atrocity(user.getPlayer());
            else {
                Colapse(user.getPlayer());
                abilitiesNames.remove(3);
                abilitiesNames.add("Atrocity");
            }
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
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 5, false, false));
                }
                else {
                    user.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    user.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    user.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
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
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i>10*2 || user == null) {
                    cancelTask();
                }
                Vector direction = player.getEyeLocation().getDirection();
                Location location = player.getEyeLocation();
                for(int j = 0; j<=i; j++) {
                    location.add(direction);
                    player.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 100, 0.5, 0.5, 0.5,
                                                0.1*j, Material.PACKED_ICE.createBlockData());

                    location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).forEach(entity -> {
                        if(entity instanceof LivingEntity && !entity.getName().equals(player.getName())) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.damage(10, player);
                        }
                    });
                }
                i++;
            }

            public void cancelTask(){
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 4);
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

    public void Atrocity(Player player) {
        Location loc = player.getEyeLocation().clone();
        Vector direccion = loc.getDirection();
        World mundo = player.getWorld();

        loc.add(0, -0.5, 0);

        new BukkitRunnable() {
            int k = 0;
            public void run() {
                if(k > 30)
                    this.cancel();

                loc.add(direccion);
                animacionAtrocity(mundo, loc);
                efectoAtrocity(mundo, loc, player);

                k++;
            }
        }.runTaskTimer(plugin, 0, 0);
    }

    public void efectoAtrocity(World mundo, Location loc, Player player) {
        mundo.getNearbyEntities(loc, 1, 1, 1).forEach(entity -> {
            if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                if(!atronar) {
                    atronar = true;
                    abilitiesNames.remove(3);
                    abilitiesNames.add("Colapse");
                }

                ((LivingEntity) entity).damage(2, player);
                atronados.add((LivingEntity) entity);
                Atormentar((LivingEntity) entity);
            }
        });
    }

    public void animacionAtrocity(World mundo, Location loc) {

        Particle particula = Particle.ELECTRIC_SPARK;

        for(double i = -2*PI; i < 2*PI; i+=0.2) {
            double  x = sin(i)/5,
                    y = cos(i)/5,
                    z = 0;

            mundo.spawnParticle(particula, loc.clone().add(x, y, z), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(z, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(z, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(-x, -y, z), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(x, y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(-x, -y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(-x/2, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(x, y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(-x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(-x, -y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(particula, loc.clone().add(x/2, -y, -x), 0, 0, 0, 0);
        }
    }

    public void Atormentar(LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getEyeLocation(), 3, 1, 1, 1);

                if(!atronar)
                    this.cancel();
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public void Colapse(Player player) {
        if(!atronados.isEmpty()) {
            for(LivingEntity entity : atronados) {
                Subyugar(entity);
            }
        }

        atronar = false;
        atronados.clear();
    }

    public void Subyugar(LivingEntity entity) {
        new BukkitRunnable() {
            int j = 0;
            @Override
            public void run() {
                if(j > 5)
                    this.cancel();

                entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 5, 1, 1, 1);
                entity.setVelocity(new Vector(0, 1, 0));
                entity.damage(5, user.getPlayer());
                j++;
            }
        }.runTaskTimer(plugin, 0, 20);

        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 10, 5, 5, 5);
        entity.damage(7.5, user.getPlayer());
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
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)) {
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else {
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)) {
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameInuOkuchi)) {
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

