package fruits.logia;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Yami_Yami extends Logia {

    private static final int BLACK_VOID_COOLDOWN = 20;
    private static final int LIBERATE_ABSORPTIONS_COOLDOWN = 20;
    private static final int LIVING_VOID_COOLDOWN = 20;
    private static final int VOID_METEORE_COOLDOWN = 20;

    private static final Material VOID_MATERIAL = Material.BLACK_CONCRETE;
    private static final int RADIUS = 3;
    private static final int MAX_RADIUS_AMPLIFICATION = 9;
    private static final int DAMAGE_AMOUNT = 1;

    private final Stack<Material> absorbedMaterials = new Stack<>();
    private final List<Block> convertedToVoidBlocks = new ArrayList<>();
    private int repealAnimationCounter = 0;

    public static int getFruitID() {
        return 1010;
    }

    public Yami_Yami(int id) {
        super(id, "Yami_Yami", "Yami Yami no Mi", "Yami_Yami", Particle.SMOKE);

        AbilitySet basicSet = new AbilitySet("Logia Body");

        // Black Void
        basicSet.addAbility(new CooldownAbility("Black Void", BLACK_VOID_COOLDOWN, this::blackVoid));

        // Liberate Absorptions
        basicSet.addAbility(new CooldownAbility("Liberate Absorptions", LIBERATE_ABSORPTIONS_COOLDOWN, this::liberateAbsorptions));

        // Living Void
        basicSet.addAbility(new CooldownAbility("Living Void", LIVING_VOID_COOLDOWN, this::livingVoid));

        // Void Meteore
        basicSet.addAbility(new CooldownAbility("Void Meteore", VOID_METEORE_COOLDOWN, this::voidMeteore));

        this.abilitySets.add(basicSet);
    }

    @Override
    protected void logiaModeON() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
                OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);
            }
        }
    }

    @Override
    protected void logiaModeOFF() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
        super.logiaModeOFF();
    }

    @Override
    public void onAddFruit() {
        OPUser user = this.getUser();
        if (user != null) {
            user.getPlayer().setFireTicks(0);
        }
    }

    @Override
    public void onRemoveFruit() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }

    // Black Void - Absorbs blocks in radius
    public void blackVoid() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.HEAVYGRAVITY, player.getLocation(), 1, 2);
        Location playerLocation = player.getLocation();

        bresenham(RADIUS, playerLocation, true);

        int voidExpansionDelay = 6;
        int voidExpansionSpeed = 5;

        new BukkitRunnable() {
            int radiusCounter = RADIUS;

            @Override
            public void run() {
                radiusCounter++;
                bresenham(radiusCounter, playerLocation, true);
                if (radiusCounter == RADIUS + 9) this.cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 6, 5);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (LivingEntity livEnt : player.getWorld().getLivingEntities()) {
                    if (livEnt != player && livEnt.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.BLACK_CONCRETE)) {
                        livEnt.damage(1, user.getPlayer());
                    }
                }

                for (double x = player.getLocation().getX() - 9; x < player.getLocation().getX() + 9; x++) {
                    for (double z = player.getLocation().getZ() - 9; z < player.getLocation().getZ() + 9; z++) {
                        for (double y = player.getLocation().getBlockY() - 1; y > player.getLocation().getBlockY() - 3; y--) {
                            Block block = new Location(player.getWorld(), x, y, z).getBlock();
                            if (block.getType().equals(Material.BLACK_CONCRETE)) {
                                player.getWorld().spawnParticle(element, block.getRelative(0, 1, 0).getLocation(), 0, 0, 1, 0);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 15);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Block convertedTVblock : convertedToVoidBlocks) {
                    convertedTVblock.setType(Material.AIR);
                }
                convertedToVoidBlocks.clear();
                absorbedMaterials.clear();
                player.getWorld().playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 2);
            }
        }.runTaskLater(OPhabs.getInstance(), 90);
    }

    private void bresenham(int radius, Location playerLocation, boolean fill) {
        int x = 0, z = radius;
        int d = 3 - 2 * radius;

        setBlockAndFill(playerLocation, x, z, true);
        setBlockAndFill(playerLocation, z, x, true);

        while (x < z) {
            if (d < 0) d += 4 * x + 6;
            else {
                d += 4 * (x - z) + 10;
                z--;
            }
            x++;
            setBlockAndFill(playerLocation, x, z, true);
            setBlockAndFill(playerLocation, z, x, true);
        }
    }

    private void setBlockAndFill(Location playerLocation, int x, int z, boolean fill) {
        Location perimeterPixel = new Location(playerLocation.getWorld(), 0, 0, 0);
        perimeterPixel.setY(playerLocation.getBlockY() - 1);
        perimeterPixel.setX(playerLocation.getBlockX() + x);
        perimeterPixel.setZ(playerLocation.getBlockZ() + z);
        if (!fill) setBlockAndLineUP(perimeterPixel);
        else {
            for (Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() >= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ() - 1))
                setBlockAndLineUP(fillBlock);
        }

        perimeterPixel.setX(playerLocation.getBlockX() - x);
        perimeterPixel.setZ(playerLocation.getBlockZ() + z);
        if (!fill) setBlockAndLineUP(perimeterPixel);
        else {
            for (Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() >= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ() - 1))
                setBlockAndLineUP(fillBlock);
        }

        perimeterPixel.setX(playerLocation.getBlockX() + x);
        perimeterPixel.setZ(playerLocation.getBlockZ() - z);
        if (!fill) setBlockAndLineUP(perimeterPixel);
        else {
            for (Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() <= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ() + 1))
                setBlockAndLineUP(fillBlock);
        }

        perimeterPixel.setX(playerLocation.getBlockX() - x);
        perimeterPixel.setZ(playerLocation.getBlockZ() - z);
        if (!fill) setBlockAndLineUP(perimeterPixel);
        else {
            for (Location fillBlock = perimeterPixel.clone(); fillBlock.getBlockZ() <= playerLocation.getBlockZ(); fillBlock.setZ(fillBlock.getBlockZ() + 1))
                setBlockAndLineUP(fillBlock);
        }
    }

    private void setBlockAndLineUP(Location perimeterPixel) {
        boolean found = false;
        for (int i = 0; i < 3 && !found; i++) {
            if (!perimeterPixel.getBlock().getRelative(0, -i, 0).getType().equals(Material.AIR)) {
                found = true;
                Block voidBlock = perimeterPixel.getBlock().getRelative(0, -i, 0);
                voidBlock.setType(Material.BLACK_CONCRETE);
                convertedToVoidBlocks.add(voidBlock);
            }
        }

        Location upBlockLocation = perimeterPixel.clone();
        for (int i = 1; i < 10; i++) {
            upBlockLocation.setY(perimeterPixel.getBlockY() + i);
            if (!upBlockLocation.getBlock().getType().equals(Material.AIR)) {
                Material mat = upBlockLocation.getBlock().getType();
                upBlockLocation.getBlock().setType(Material.AIR);
                upBlockLocation.getWorld().spawnFallingBlock(upBlockLocation, mat.createBlockData());
            }
        }
    }

    public void dissappearVoidBlocks() {
        for (Block block : convertedToVoidBlocks) {
            block.setType(Material.AIR);
        }
        convertedToVoidBlocks.clear();
    }

    // Liberate Absorptions - Release absorbed blocks
    public void liberateAbsorptions() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);

        new BukkitRunnable() {
            int tickTime = 0;
            @Override
            public void run() {
                tickTime++;
                for (int i = 0; i < 100; i++) {
                    double x = new Random().nextInt(9) - 4;
                    double y = new Random().nextInt(9) - 4;
                    double z = new Random().nextInt(9) - 4;
                    player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation().clone().add(x, y + 2, z), 0, 0, 1, 0);
                }
                if (tickTime > 100) cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 4);

        new BukkitRunnable() {
            int i = 0;
            World world = player.getWorld();

            @Override
            public void run() {
                if (!absorbedMaterials.empty()) {
                    Material mat = absorbedMaterials.pop();
                    FallingBlock block = world.spawnFallingBlock(player.getLocation().clone().add(0, 2, 0), mat.createBlockData());
                    double x = new Random().nextInt(3) - 1;
                    double z = new Random().nextInt(3) - 1;
                    block.setVelocity(new Vector(x, 0.5, z));
                }
                if (absorbedMaterials.empty()) cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 4);
    }

    // Living Void - Absorb and launch entities
    public void livingVoid() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);
        absorbAnimation(player);

        for (Entity ent : player.getNearbyEntities(10, 10, 10)) {
            if (ent instanceof LivingEntity && !player.equals(ent)) {
                livingVoidForEntity(ent, player);
            }
        }
    }

    private void absorbAnimation(Player player) {
        new BukkitRunnable() {
            double start = 2 * Math.PI * 5;
            double finish = 2 * Math.PI * 5 - 2 * Math.PI * 5 / 10;

            @Override
            public void run() {
                for (double i = start; i > finish; i -= 0.05) {
                    double x = i * Math.sin(i) / 5;
                    double y = i * Math.cos(i) / 5;
                    double z = i;
                    double angle = -player.getLocation().getYaw();
                    double xr = player.getLocation().getX() + Math.cos(Math.toRadians(angle)) * x + Math.sin(Math.toRadians(angle)) * z;
                    double yr = 1 + player.getLocation().getY() + i * Math.cos(i) / 5;
                    double zr = player.getLocation().getZ() + -Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * z;
                    Location rotation = new Location(player.getWorld(), xr, yr, zr);
                    player.getWorld().spawnParticle(Particle.SMOKE, rotation, 0, 0, 0, 0);
                }
                start = finish;
                finish = finish - 2 * Math.PI * 5 / 10;
                if (finish < 0) cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    private void livingVoidForEntity(Entity ent, Player player) {
        new BukkitRunnable() {
            Vector firstVector = new Vector();
            boolean firstVectorSet = false;
            boolean entityInHand = false;

            @Override
            public void run() {
                if (ent.isDead() || player.isDead()) { cancel(); return; }

                Vector movement = player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize();

                if (!firstVectorSet) {
                    firstVectorSet = true;
                }

                if (player.getLocation().getY() >= ent.getLocation().getY() && !entityInHand) {
                    movement.setY(movement.getY() + (player.getLocation().getY() - ent.getLocation().getY()) + 3);
                }

                ent.setVelocity(movement);

                double vx = player.getLocation().getX() - ent.getLocation().getX();
                double vy = player.getLocation().getY() - ent.getLocation().getY();
                double vz = player.getLocation().getZ() - ent.getLocation().getZ();

                if (Math.sqrt(vx * vx + vy * vy + vz * vz) <= 1) {
                    if (player.isSneaking()) {
                        cancel();
                        repealEntity(ent, player);
                    }
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    private void repealEntity(Entity ent, Player player) {
        player.getWorld().playSound(ent.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 10, 2);
        Vector dir = player.getLocation().getDirection();
        dir.setY(1);
        ent.setVelocity(dir.multiply(3));
    }

    // Void Meteore - Black meteor attack
    public void voidMeteore() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);
        Location startPosition = player.getLocation().clone().add(0, 3, 0);
        Vector direction = new Vector(0, 0, 0);
        Vector facingDirection = player.getLocation().getDirection();

        new BukkitRunnable() {
            int ticks = 0;
            int numParticles = 0;

            @Override
            public void run() {
                ticks++;
                numParticles = 0;

                Location spawnBallPos = null;
                for (double i = 0; i < 2 * Math.PI * 4; i += 0.05) {
                    double x = Math.sin(i / 10 * Math.PI);
                    double y = Math.sin((i % 10) * Math.PI) * Math.cos(i / 10 * Math.PI);
                    double z = Math.cos((i % 10) * Math.PI) * Math.cos(i / 10 * Math.PI);

                    spawnBallPos = startPosition.clone().add(x, y, z);
                    spawnBallPos.add(direction);

                    if (spawnBallPos.getBlock().getType() == Material.AIR) {
                        player.getWorld().spawnParticle(Particle.SMOKE, spawnBallPos, 0, 0, 0, 0);
                        numParticles++;
                    }
                }

                for (Entity ent : player.getWorld().getNearbyEntities(spawnBallPos, 2, 2, 2)) {
                    if (ent instanceof LivingEntity && !player.equals(ent)) {
                        ((LivingEntity) ent).damage(10, player);
                    }
                }

                direction.add(new Vector(facingDirection.getX(), facingDirection.getY(), facingDirection.getZ()));

                if (numParticles < 25 || ticks > 50) {
                    cancel();
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }


    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        super.onPlayerMove(event);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (isFruitActive() && event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            event.setCancelled(true);
        }
    }

}
