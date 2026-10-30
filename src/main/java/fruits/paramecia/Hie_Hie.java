package fruits.paramecia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.ophabs.OPhabs;
import htt.layeredstructures.LayeredStructuresAPI;
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

public class Hie_Hie extends Paramecia {

    private static final int ICE_AGE_COOLDOWN = 30;
    private static final int PARTISAN_COOLDOWN = 3;
    private static final int MORPH_CASTER_COOLDOWN = 20;
    private static final int DRAKE_COOLDOWN = 60;

    private final Random rand = new Random();
    private boolean usingDrake = false;
    private static final List<Trident> tridents = new ArrayList<>();
    private static final List<Entity> cagedEnts = new ArrayList<>();
    private static boolean iceAge = false;

    public static int getFruitID() {
        return 1007;
    }

    public Hie_Hie(int id) {
        super(id, "Hie_Hie", "Hie Hie no Mi", "Hie_Hie");

        AbilitySet basicSet = new AbilitySet("Base Set");

        basicSet.addAbility(new CooldownAbility("Ice Age", ICE_AGE_COOLDOWN, this::iceAge));
        basicSet.addAbility(new CooldownAbility("Partisan", PARTISAN_COOLDOWN, this::partisan));
        basicSet.addAbility(new CooldownAbility("Morph Caster", MORPH_CASTER_COOLDOWN, this::morphCaster));
        basicSet.addAbility(new CooldownAbility("Drake", DRAKE_COOLDOWN, this::drake));

        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {
        iceAge = false;
        cagedEnts.clear();
        tridents.clear();
        usingDrake = false;
    }

    @Override
    public void onRemoveFruit() {
        iceAge = false;
        cagedEnts.clear();
        tridents.clear();
        usingDrake = false;
    }

    public void iceAge() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        iceAge = true;
        OPHSoundLib.OphPlaySound(OPHSounds.HEAVYGRAVITY, player.getLocation(), 1, 1);

        new BukkitRunnable() {
            final World world = player.getWorld();
            double extension = 1;
            double convRate = 0.8;
            double xPlayer = player.getLocation().getX();
            double zPlayer = player.getLocation().getZ();
            double yPlayer = player.getLocation().getY();

            @Override
            public void run() {
                for(double i = xPlayer - extension; i < xPlayer + extension; i++) {
                    for (double j = zPlayer - extension; j < zPlayer + extension; j++) {
                        double convertProb = rand.nextDouble();
                        if (convertProb < convRate) {
                            Location blockLoc = new Location(world, i, OPHLib.searchGround(i, j, yPlayer, world), j);
                            blockLoc.getBlock().setType(Material.BLUE_ICE);
                        }
                    }
                }
                extension++;
                convRate -= 0.02;
                if (extension >= 50) {
                    iceAge = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 2);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity ent : player.getNearbyEntities(50, 30, 50)) {
                    if (ent instanceof LivingEntity && !cagedEnts.contains(ent)) {
                        if (ent.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.BLUE_ICE)) {
                            ((LivingEntity) ent).damage(5);
                            cagedEnts.add(ent);
                            createIceBox(ent);
                            ent.setFreezeTicks(ent.getFreezeTicks() + 600);
                        }
                    }
                }
                if (!iceAge) {
                    cagedEnts.clear();
                    this.cancel();
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    private void createIceBox(Entity entity) {
        Block entBlock = entity.getLocation().getBlock();
        entBlock.getRelative(1,0,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(-1,0,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,0,1).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,0,-1).setType(Material.BLUE_ICE);
        entBlock.getRelative(1,1,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(-1,1,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,1,1).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,1,-1).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,2,0).setType(Material.BLUE_ICE);
    }

    public void partisan() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.ROCKMOVE, player.getLocation(), 1, 1);

        Location headPos = player.getEyeLocation();
        Vector dir = headPos.getDirection();

        Location center = headPos.clone().add(dir).add(0, 2, 0);
        Location left = headPos.clone().add(dir.clone().rotateAroundY(Math.PI / 4)).add(0, 2, 0);
        Location right = headPos.clone().add(dir.clone().rotateAroundY(-Math.PI / 4)).add(0, 2, 0);

        Trident tridentCenter = (Trident) player.getWorld().spawnEntity(center, EntityType.TRIDENT);
        Trident tridentLeft = (Trident) player.getWorld().spawnEntity(left, EntityType.TRIDENT);
        Trident tridentRight = (Trident) player.getWorld().spawnEntity(right, EntityType.TRIDENT);

        tridentCenter.setShooter(player);
        tridentLeft.setShooter(player);
        tridentRight.setShooter(player);

        tridents.add(tridentCenter);
        tridents.add(tridentRight);
        tridents.add(tridentLeft);

        dir.multiply(1.5);
        tridentCenter.setVelocity(dir);
        tridentLeft.setVelocity(dir);
        tridentRight.setVelocity(dir);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                if (!tridents.isEmpty()) {
                    tridentCenter.getWorld().spawnParticle(Particle.SNOWFLAKE, tridentCenter.getLocation(), 10);
                    tridentLeft.getWorld().spawnParticle(Particle.SNOWFLAKE, tridentLeft.getLocation(), 10);
                    tridentRight.getWorld().spawnParticle(Particle.SNOWFLAKE, tridentRight.getLocation(), 10);
                }
                if (tridents.isEmpty() || ticks > 200) this.cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    public void morphCaster() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.getWorld().playSound(player.getLocation(), "icecrack", 1, 1);

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.CARVED_PUMPKIN) {
            itemInHand = player.getInventory().getItemInOffHand();
        }
        if (itemInHand == null || itemInHand.getType() != Material.CARVED_PUMPKIN) return;

        final ItemStack finalItem = itemInHand;
        final ItemMeta old = itemInHand.getItemMeta().clone();
        ItemMeta meta = itemInHand.getItemMeta();
        meta.setCustomModelData(this.getID() + 1000);

        org.bukkit.attribute.AttributeModifier dmgMod = new org.bukkit.attribute.AttributeModifier(UUID.randomUUID(), "attack_damage", 10, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(org.bukkit.attribute.Attribute.ATTACK_DAMAGE, dmgMod);
        org.bukkit.attribute.AttributeModifier speedMod = new org.bukkit.attribute.AttributeModifier(UUID.randomUUID(), "attack_speed", 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(org.bukkit.attribute.Attribute.ATTACK_SPEED, speedMod);

        itemInHand.setItemMeta(meta);

        new BukkitRunnable() {
            @Override
            public void run() {
                finalItem.setItemMeta(old);
            }
        }.runTaskLater(OPhabs.getInstance(), 20 * 20);
    }

    public void drake() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        usingDrake = true;
        player.getWorld().playSound(player.getLocation(), "icecrack", 1, 1);
        LayeredStructuresAPI.generatePreChargedLayeredStructure("iceDrake", player.getLocation(), player.getFacing().toString(), 0, 2, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                usingDrake = false;
            }
        }.runTaskLater(OPhabs.getInstance(), 180);

        iceBreath(player);
    }

    private void iceBreath(Player player) {
        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection();
        World world = player.getWorld();

        OPHSoundLib.OphPlaySound(OPHSounds.FOG, loc, 1, 1);

        new BukkitRunnable() {
            int steps = 0;
            Location current = loc.clone();
            
            @Override
            public void run() {
                if (steps > 20) { cancel(); return; }

                current.add(direction.clone().multiply(1.2));
                
                for (double r = 0.5; r < 8; r += 0.5) {
                    Location coneLoc = player.getEyeLocation().clone().add(direction.clone().multiply(r));
                    double radius = r * 0.3;
                    world.spawnParticle(Particle.SNOWFLAKE, coneLoc, 5, radius, radius, radius, 0);
                    world.spawnParticle(Particle.CLOUD, coneLoc, 2, radius, radius, radius, 0);
                    
                    for (Entity ent : world.getNearbyEntities(coneLoc, radius, radius, radius)) {
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

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        super.onPlayerMove(event);
        if (usingDrake) {
            event.setTo(new Location(event.getTo().getWorld(), event.getFrom().getX(), event.getFrom().getY(), event.getFrom().getZ(), event.getTo().getYaw(), event.getTo().getPitch()));
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        super.onEntityDamage(event);
        if (isFruitActive() && event.getCause().equals(EntityDamageEvent.DamageCause.FREEZE)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile trident = event.getEntity();
        if (trident instanceof Trident && tridents.contains(trident)) {
            tridents.remove(trident);
            Entity hitEnt = event.getHitEntity();

            if (hitEnt != null) {
                if (hitEnt instanceof LivingEntity) {
                    ((LivingEntity) hitEnt).damage(5);
                    createIceBox(hitEnt);
                    hitEnt.setFreezeTicks(hitEnt.getFreezeTicks() + 250);
                }
            } else {
                event.getHitBlock().setType(Material.BLUE_ICE);
            }
            Bukkit.getScheduler().runTaskLater(OPhabs.getInstance(), () -> trident.remove(), 60L);
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        super.onEntityDamageByEntity(event);

        Entity hitEnt = event.getEntity();
        ItemStack sword = event.getDamager() instanceof Player ? ((Player) event.getDamager()).getInventory().getItemInMainHand() : null;

        int fruitID = this.getID();
        if (sword != null && sword.hasItemMeta() && sword.getItemMeta().hasCustomModelData()) {
            int itemCustomModelData = sword.getItemMeta().getCustomModelData();
            if (itemCustomModelData == fruitID || itemCustomModelData == fruitID + 1000) {
                event.getEntity().setFreezeTicks(event.getEntity().getFreezeTicks() + 80);
            }
        }
    }

}
