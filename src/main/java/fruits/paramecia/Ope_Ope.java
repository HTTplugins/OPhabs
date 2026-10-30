package fruits.paramecia;


import abilities.AbilitySet;
import abilities.CooldownAbility;
import cast.Caster;
import htt.ophabs.OPhabs;
import libs.OPHAnimationLib;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import logs.msgSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import runnables.OphRunnable;
import textures.OPTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Ope_Ope extends Paramecia {

    public enum ShamblesMode {
        ALL,
        DAMAGE_DEALT,
        DAMAGE_TAKEN,
        PROJECTILES
    }

    private Location roomCenter;
    private boolean activeRoom;
    private final int roomSize = 12;
    private final int roomHeight = 11;

    private LivingEntity currentHeart;
    private UUID currentHeartUUID;
    private int availableSqueezes;
    private ShamblesMode shamblesMode = ShamblesMode.ALL;

    public static int getFruitID()
    {
        return 1003;
    }

    public Ope_Ope(int id) {
        super(id, "Ope_Ope", "Ope ope no Mi", "Ope_Ope");

        roomCenter = null;
        activeRoom = false;
        currentHeart = null;
        availableSqueezes = 0;

        AbilitySet basicSet = new AbilitySet("Base Set");

        basicSet.addAbility(new CooldownAbility("Room", 70, () -> {
            Player player = user.getPlayer();
            this.room(player);
        }));

        basicSet.addAbility(new CooldownAbility("Levitation", 5, this::levitation));

        basicSet.addAbility(new CooldownAbility("Dash", 0, () -> {
            Player player = user.getPlayer();
            this.dash(player);
        }));
        
        basicSet.addAbility(new CooldownAbility("Steal Heart", 10, () -> {
            Player player = user.getPlayer();
            this.stealHeart(player);
        }));
        
        basicSet.addAbility(new CooldownAbility("Squeeze Heart", 3, () -> {
            Player player = user.getPlayer();
            this.squeezeHeart(player);
        }));
        
        basicSet.addAbility(new CooldownAbility("Cycle Shambles", 0, () -> {
            this.cycleShamblesMode();
        }));
        
        this.abilitySets.add(basicSet);
    }

    public void room(Player player) {
        OPHSoundLib.OphPlaySound(OPHSounds.OPENROOM, player.getLocation(), 1, 1);
        activeRoom = true;
        roomCenter = player.getLocation().clone();

        createCuboid(roomCenter);

        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!activeRoom) return;
                OPHSoundLib.OphPlaySound(OPHSounds.CLOSEROOM, roomCenter, 1, 1);
                removeCuboid(roomCenter);
                roomCenter = null;
                activeRoom = false;
            }
        }.runTaskLater(OPhabs.getInstance(), 1200);

        new org.bukkit.scheduler.BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!activeRoom || ticks >= 1200) { cancel(); return; }
                
                int cx = roomCenter.getBlockX();
                int cy = roomCenter.getBlockY();
                int cz = roomCenter.getBlockZ();
                Particle.DustOptions dust = new Particle.DustOptions(Color.AQUA, 1.0f);
                
                for (int dx = -12; dx <= 12; dx++) {
                    for (int dz = -12; dz <= 12; dz++) {
                        if (Math.abs(dx) == 12 || Math.abs(dz) == 12) {
                            roomCenter.getWorld().spawnParticle(Particle.DUST,
                                roomCenter.clone().add(dx, -1, dz), 1, 0, 0, 0, 0,
                                new Particle.DustOptions(Color.AQUA, 1.0f));
                            roomCenter.getWorld().spawnParticle(Particle.DUST,
                                roomCenter.clone().add(dx, 10, dz), 1, 0, 0, 0, 0,
                                new Particle.DustOptions(Color.AQUA, 1.0f));
                        }
                    }
                }
                for (int y = -1; y <= 10; y++) {
                    for (int dx : new int[]{-12, 12}) {
                        for (int dz : new int[]{-12, 12}) {
                            roomCenter.getWorld().spawnParticle(Particle.DUST,
                                roomCenter.clone().add(dx, y, dz), 1, 0, 0, 0, 0,
                                new Particle.DustOptions(Color.AQUA, 1.0f));
                        }
                    }
                }
                ticks++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 20);

        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!activeRoom) return;
                OPHSoundLib.OphPlaySound(OPHSounds.CLOSEROOM, roomCenter, 1, 1);
                removeCuboid(roomCenter);
                roomCenter = null;
                activeRoom = false;
            }
        }.runTaskLater(OPhabs.getInstance(), 1200);
    }

    private void createCuboid(Location center) {
        World world = center.getWorld();
        
        for (int dx = -12; dx <= 12; dx++) {
            for (int dz = -12; dz <= 12; dz++) {
                Block b = center.getWorld().getBlockAt(center.getBlockX() + dx, center.getBlockY() - 1, center.getBlockZ() + dz);
                if (b.getType() == Material.AIR) b.setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
        }
        
        for (int dx = -12; dx <= 12; dx++) {
            for (int dz = -12; dz <= 12; dz++) {
                Block b = center.getWorld().getBlockAt(center.getBlockX() + dx, center.getBlockY() + 10, center.getBlockZ() + dz);
                if (b.getType() == Material.AIR) b.setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
        }
        
        for (int y = 0; y <= 10; y++) {
            for (int dz = -12; dz <= 12; dz++) {
                Block b1 = center.getWorld().getBlockAt(center.getBlockX() - 12, center.getBlockY() + y, center.getBlockZ() + dz);
                Block b2 = center.getWorld().getBlockAt(center.getBlockX() + 12, center.getBlockY() + y, center.getBlockZ() + dz);
                if (b1.getType() == Material.AIR) b1.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                if (b2.getType() == Material.AIR) b2.setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
            for (int dx = -12; dx <= 12; dx++) {
                Block b1 = center.getWorld().getBlockAt(center.getBlockX() + dx, center.getBlockY() + y, center.getBlockZ() - 12);
                Block b2 = center.getWorld().getBlockAt(center.getBlockX() + dx, center.getBlockY() + y, center.getBlockZ() + 12);
                if (b1.getType() == Material.AIR) b1.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                if (b2.getType() == Material.AIR) b2.setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
        }
    }

    private void removeCuboid(Location center) {
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        
        for (int dx = -12; dx <= 12; dx++) {
            for (int dz = -12; dz <= 12; dz++) {
                Block b = world.getBlockAt(cx + dx, cy - 1, cz + dz);
                if (b.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b.setType(Material.AIR);
                b = world.getBlockAt(cx + dx, cy + 10, cz + dz);
                if (b.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b.setType(Material.AIR);
            }
        }
        
        for (int y = 0; y <= 10; y++) {
            for (int dz = -12; dz <= 12; dz++) {
                Block b = world.getBlockAt(cx - 12, cy + y, cz + dz);
                if (b.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b.setType(Material.AIR);
                Block b2 = world.getBlockAt(cx + 12, cy + y, cz + dz);
                if (b2.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b2.setType(Material.AIR);
            }
            for (int dx = -12; dx <= 12; dx++) {
                Block b = world.getBlockAt(cx + dx, cy + y, cz - 12);
                if (b.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b.setType(Material.AIR);
                Block b2 = world.getBlockAt(cx + dx, cy + y, cz + 12);
                if (b2.getType() == Material.LIGHT_BLUE_STAINED_GLASS) b2.setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
        }
    }

    public void levitation() {
        if (activeRoom) {
            int roomRadius = 30;
            for (Entity ent : user.getPlayer().getWorld().getNearbyEntities(roomCenter, roomRadius, roomRadius, roomRadius))
                if (ent instanceof LivingEntity && ent != user.getPlayer())
                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));
        } else
            msgSystem.OphPlayerError(user.getPlayer(), "You must have an active room to use this ability.");
    }

    public void dash(Player player) {
        if (activeRoom) {
            OPHLib.dash(player, 2);
        } else
            msgSystem.OphPlayerError(player, "You must have an active room to use this ability.");
    }

    public void stealHeart(Player player){
        if(!activeRoom){
            msgSystem.OphPlayerError(player, "You must have an active room to use this ability.");
            return;
        }
        
        currentHeart = OPHLib.rayCastLivEnt(player, 5);
        if (currentHeart == null) {
            msgSystem.OphPlayerError(player, "No entity in range.");
            return;
        }
        
        if(currentHeart instanceof Player)
            currentHeartUUID = currentHeart.getUniqueId();

        availableSqueezes = 5;
        OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
        OPHLib.spawnBloodParticles(currentHeart);
        msgSystem.OphPlayerMsg(player, "You have stolen " + currentHeart.getName() + "'s <3.");

        OPHLib.changeCasterTexture(OPTexture.OPE_OPE_STOLEN_HEART, player.getInventory().getItemInMainHand());
    }
    
    public void squeezeHeart(Player player){
        if(currentHeart == null) {
            msgSystem.OphPlayerError(player, "You must steal a heart first.");
            return;
        }
        
        if (currentHeart.isDead() || !currentHeart.isValid()) {
            msgSystem.OphPlayerError(player, "The heart is no longer valid.");
            resetCaster();
            return;
        }
        
        if(currentHeart instanceof Player){
            if(!Bukkit.getOnlinePlayers().contains(currentHeart)){
                msgSystem.OphPlayerError(player, "The player is not online.");
                resetCaster();
                return;
            }
        }

        availableSqueezes--;
        currentHeart.damage(2, user.getPlayer());

        OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
        OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, currentHeart.getLocation(), 1, 1);
        currentHeart.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, currentHeart.getLocation().add(0, currentHeart.getHeight(), 0), 4);

        if(availableSqueezes == 0) resetCaster();
    }

    public void cycleShamblesMode() {
        if (user == null || user.getPlayer() == null) {
            return;
        }
        
        ShamblesMode[] modes = ShamblesMode.values();
        int currentIndex = shamblesMode.ordinal();
        int nextIndex = (currentIndex + 1) % modes.length;
        shamblesMode = modes[nextIndex];
        
        String[] modeNames = {"ALL (all)", "DAMAGE_DEALT (outgoing)", "DAMAGE_TAKEN (incoming)", "PROJECTILES (thrown)"};
        msgSystem.OphPlayerMsg(user.getPlayer(), "Shambles Mode: " + modeNames[nextIndex]);
        OPHSoundLib.OphPlaySound(OPHSounds.RESORT, user.getPlayer().getLocation(), 1, 1);
    }

    // Shambles swap - cancels damage, swaps entities, redirects damage
    private void performShamblesSwap(Entity userEntity, Entity target, double damage) {
        if (!activeRoom) return;
        if (!isInRoom(target.getLocation())) return;
        
        OPHSoundLib.OphPlaySound(OPHSounds.SHAMBLESS, target.getLocation(), 1, 1);
        
        Location userLoc = userEntity.getLocation().clone();
        Location targetLoc = target.getLocation().clone();
        
        // Swap positions
        target.teleport(userEntity.getLocation());
        userEntity.teleport(target.getLocation());
        
        // Apply damage to the entity that took user's place (the target)
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).damage(5, (Entity) user.getPlayer());
        }
        
        // Apply original damage to the swapped entity (now at user's original position)
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).damage(damage, (Entity) user.getPlayer());
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (user == null || user.getPlayer() == null) return;
        
        // DAMAGE_DEALT mode: User deals damage -> swap with target, damage goes to target
        if ((shamblesMode == ShamblesMode.ALL || shamblesMode == ShamblesMode.DAMAGE_DEALT) 
            && event.getDamager() instanceof Player 
            && event.getDamager().equals(user.getPlayer()) 
            && event.getEntity() instanceof LivingEntity) {
            
            Entity target = event.getEntity();
            if (!target.equals(user.getPlayer()) && isInRoom(target.getLocation())) {
                double damage = event.getDamage();
                event.setCancelled(true); // Cancel original damage
                
                // Perform swap: user <-> target
                performShamblesSwap(user.getPlayer(), target, damage);
            }
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (user == null || user.getPlayer() == null) return;
        
        // DAMAGE_TAKEN mode: User takes damage -> swap with damager, damage goes to damager
        if ((shamblesMode == ShamblesMode.ALL || shamblesMode == ShamblesMode.DAMAGE_TAKEN) 
            && event.getEntity() instanceof Player 
            && event.getEntity().equals(user.getPlayer()) 
            && event instanceof EntityDamageByEntityEvent) {
            
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof LivingEntity && isInRoom(damager.getLocation())) {
                double damage = event.getDamage();
                event.setCancelled(true); // Cancel original damage
                
                // Perform swap: user <-> damager
                performShamblesSwap(user.getPlayer(), damager, damage);
            }
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (user == null || user.getPlayer() == null) return;
        
        if ((shamblesMode == ShamblesMode.ALL || shamblesMode == ShamblesMode.PROJECTILES)) {
            Entity projectile = event.getEntity();
            Player shooter = null;
            
            if (projectile instanceof org.bukkit.entity.Projectile) {
                shooter = (Player) ((org.bukkit.entity.Projectile) projectile).getShooter();
            }
            
            if (shooter != null && shooter.equals(user.getPlayer())) {
                Location hitLoc = event.getHitBlock() != null ? event.getHitBlock().getLocation() : projectile.getLocation();
                
                if (shamblesMode == ShamblesMode.PROJECTILES) {
                    // PROJECTILES mode: always swap with nearest entity at hit location, or teleport to hit location
                    boolean swapped = false;
                    for (Entity ent : shooter.getWorld().getNearbyEntities(hitLoc, 5, 5, 5)) {
                        if (ent instanceof LivingEntity && !ent.equals(shooter)) {
                            // Swap with hit entity
                            double damage = 5;
                            // We need to create a fake damage event for the swap
                            performShamblesSwap(shooter, ent, damage);
                            break;
                        }
                    }
                } else if (shamblesMode == ShamblesMode.ALL) {
                    // ALL mode - only if hit location is in room
                    if (isInRoom(hitLoc)) {
                        for (Entity ent : shooter.getWorld().getNearbyEntities(hitLoc, 5, 5, 5)) {
                            if (ent instanceof LivingEntity && !ent.equals(shooter) && isInRoom(ent.getLocation())) {
                                performShamblesSwap(shooter, ent, 5);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void resetCaster(){
        currentHeart = null;
        currentHeartUUID = null;
        availableSqueezes = 0;

        if(user == null) return;
        user.getPlayer().getInventory().forEach(item -> {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                Caster caster = this.user.getActiveCasters().get(item.getItemMeta().getCustomModelData());
                if (caster != null && caster.isOwnedBy(this.user) && caster.isThisItem(item))
                    OPHLib.changeCasterTexture(Ope_Ope.getFruitID(), item);
            }
        });
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(activeRoom && isInRoom(event.getBlock().getLocation()))
            event.setCancelled(true);
    }
    
    private boolean isInRoom(Location loc) {
        if (roomCenter == null) return false;
        double dx = loc.getX() - roomCenter.getX();
        double dy = loc.getY() - roomCenter.getY();
        double dz = loc.getZ() - roomCenter.getZ();
        return Math.abs(dx) <= 12 && Math.abs(dy + 1) <= 11 && Math.abs(dz) <= 12;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(currentHeart == null) resetCaster();
        else if(currentHeartUUID != null)
            currentHeart = Bukkit.getPlayer(currentHeartUUID);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity().equals(currentHeart)){
            resetCaster();
        }
    }

    @Override
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        if (activeRoom && event.getEntity().equals(user.getPlayer())) {
            OPHSoundLib.OphPlaySound(OPHSounds.CLOSEROOM, roomCenter, 1, 1);
            removeCuboid(roomCenter);
            roomCenter = null;
            activeRoom = false;
        }
    }

    @Override
    public void onAddFruit() {}

    @Override
    public void onRemoveFruit() {}

}
