package fruits.paramecia;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.ArrayList;
import java.util.Random;

public class Gura_Gura extends Paramecia {

    private static final int EARTHQUAKE_COOLDOWN = 30;
    private static final int CREATE_WAVE_COOLDOWN = 10;
    private static final int HAND_VIBRATION_COOLDOWN = 10;
    private static final int EXPANSION_WAVE_COOLDOWN = 30;

    private final int waveDistance = 8;
    private final int radiusFloor = 4;
    private final int earthquakeDamage = 5;
    private final int waveDamage = 14;
    private final int createWaveDamage = 9;
    private final int earthquakeDuration = 5;
    private int numberOfWaves = 0;
    private final int numberOfWavesMax = 5;
    private final Random rand = new Random();

    public static int getFruitID() {
        return 1008;
    }

    public Gura_Gura(int id) {
        super(id, "Gura_Gura", "Gura Gura no Mi", "Gura_Gura");

        AbilitySet basicSet = new AbilitySet("Base Set");

        basicSet.addAbility(new CooldownAbility("Earthquake", EARTHQUAKE_COOLDOWN, this::earthquake));
        basicSet.addAbility(new CooldownAbility("Create Wave", CREATE_WAVE_COOLDOWN, this::createWave));
        basicSet.addAbility(new CooldownAbility("Hand Vibration", HAND_VIBRATION_COOLDOWN, this::handVibration));
        basicSet.addAbility(new CooldownAbility("Expansion Wave", EXPANSION_WAVE_COOLDOWN, this::expansionWaveBlocks));

        this.abilitySets.add(basicSet);
    }

    @Override
    public void onAddFruit() {
        numberOfWaves = 0;
    }

    @Override
    public void onRemoveFruit() {
        numberOfWaves = 0;
    }

    public void earthquake() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.HEAVYGRAVITY, player.getLocation(), 1, 1);

        Location currentPL = player.getLocation();
        ArrayList<Location> blocks = new ArrayList<>();

        int x1 = currentPL.getBlockX() - radiusFloor;
        int y1 = currentPL.getBlockY() - radiusFloor;
        int z1 = currentPL.getBlockZ() - radiusFloor;
        int x2 = currentPL.getBlockX() + radiusFloor;
        int y2 = currentPL.getBlockY() + radiusFloor;
        int z2 = currentPL.getBlockZ() + radiusFloor;

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Location block = new Location(player.getWorld(), x, y, z);
                    blocks.add(block);
                    if (block.getBlock().getType() != Material.WATER) {
                        setFallingBlock(block);
                    }
                }
            }
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 90, 50, false, false));

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= earthquakeDuration) { cancel(); return; }

                for (Location block : blocks) {
                    Block b = block.getBlock();
                    if (b.getType() != Material.AIR && b.getType() != Material.WATER) {
                        if (b.getType().getHardness() < Material.DIRT.getHardness() && b.getType() != Material.BEDROCK) {
                            b.breakNaturally();
                        } else if (b.getType().getHardness() <= Material.STONE.getHardness() && b.getType() != Material.BEDROCK) {
                            if (rand.nextInt(25) == 1) b.breakNaturally();
                        }
                        b.getWorld().spawnParticle(Particle.BLOCK, block.clone().add(0, 1, 0), 10, b.getBlockData());
                    }

                    for (Entity entity : block.getWorld().getNearbyEntities(block, 1, 1, 1)) {
                        if (entity instanceof LivingEntity && !entity.equals(getUser().getPlayer())) {
                            ((LivingEntity) entity).damage(earthquakeDamage, getUser().getPlayer());
                        }
                    }
                }
                i++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 5, 15);
    }

    public void createWave() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        Location currentPL = player.getLocation().clone().add(0, 1, 0);
        Vector direction = player.getEyeLocation().getDirection();
        // Normalize horizontal direction for any angle
        Vector horizDir = direction.clone();
        horizDir.setY(0);
        horizDir.normalize();
        
        ArrayList<Location> blocks = new ArrayList<>();

        for (int i = 0; i < waveDistance; i++) {
            currentPL.add(horizDir);
            blocks.add(currentPL.clone());
            blocks.addAll(getPositions(currentPL.clone(), horizDir));
        }

        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), waveDistance, waveDistance, waveDistance)) {
            if (entity instanceof LivingEntity && !entity.equals(getUser().getPlayer())) {
                ((LivingEntity) entity).damage(createWaveDamage, getUser().getPlayer());
            } else if (entity instanceof Projectile) {
                entity.setVelocity(new Vector(0, 0, 0));
            }
        }

        for (Location b : blocks) {
            Block block = b.getBlock();
            if (block.getType() != Material.AIR && block.getType() != Material.WATER) {
                if (block.getType().getHardness() < Material.DIRT.getHardness() && block.getType() != Material.BEDROCK) {
                    block.breakNaturally();
                } else if (block.getType().getHardness() <= Material.GRASS_BLOCK.getHardness() && block.getType() != Material.BEDROCK) {
                    if (block.getType() == Material.GRASS_BLOCK) block.setType(Material.DIRT);
                    else if (rand.nextInt(15) == 1) block.breakNaturally();
                }
            }
            if (block.getType() == Material.AIR) {
                if (rand.nextInt(15) == 1) block.getWorld().spawnParticle(Particle.SONIC_BOOM, b, 1);
            } else {
                block.getWorld().spawnParticle(Particle.BLOCK, b.clone().add(0, 1, 0), 4, block.getBlockData());
            }
            setFallingBlock(b);
        }
    }

    public void handVibration() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 10, false, false));

        Location loc = player.getEyeLocation().clone().add(player.getLocation().getDirection());

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i > 3) { cancel(); return; }

                Vector direction = player.getLocation().getDirection();
                Vector sideWayOffset = direction.crossProduct(new Vector(0, 1, 0)).normalize().multiply(0.4);
                Vector downOffset = direction.crossProduct(sideWayOffset).normalize().multiply(0.2);

                Location particleLoc = player.getEyeLocation().add(direction).add(sideWayOffset).add(downOffset);
                particleLoc.getWorld().spawnParticle(Particle.SONIC_BOOM, particleLoc, 1, 0, 0, 0, 0);
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 20);
    }

    public void expansionWaveBlocks() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (numberOfWaves == 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    numberOfWaves = 0;
                }
            }.runTaskLater(OPhabs.getInstance(), 60);
        }

        int deep = 3;
        Location loc = player.getLocation().add(0, -1, 0);
        if (loc.getBlock().getType() != Material.AIR && numberOfWaves < numberOfWavesMax) {
            numberOfWaves++;
            new BukkitRunnable() {
                int i = 1;
                @Override
                public void run() {
                    if (i > waveDistance) { cancel(); return; }

                    // All rings: falling blocks with upward velocity (wave effect) + crack particles
                    for (int j = 0; j < (i * 2) + 1; j++) {
                        expansionWaveFallingBlock(loc.clone().add(j - i, 0, -i), deep);
                        expansionWaveFallingBlock(loc.clone().add(-i, 0, j - i), deep);
                        expansionWaveFallingBlock(loc.clone().add(j - i, 0, i), deep);
                        expansionWaveFallingBlock(loc.clone().add(i, 0, j - i), deep);
                    }

                    // Final ring: damage + falling blocks
                    if (i >= waveDistance) {
                        for (int j = 0; j < ((i + 1) * 2) + 1; j++) {
                            expansionWaveDamage(loc.clone().add(j - i, 0, -i), deep, i, j);
                            expansionWaveDamage(loc.clone().add(-i, 0, j - i), deep, i, j);
                            expansionWaveDamage(loc.clone().add(j - i, 0, i), deep, i, j);
                            expansionWaveDamage(loc.clone().add(i, 0, j - i), deep, i, j);
                        }
                        numberOfWaves--;
                        cancel();
                    }
                    i++;
                }
            }.runTaskTimer(OPhabs.getInstance(), 5, 7);
        }
    }

    // Damage stage - only on final ring
    private void expansionWaveDamage(Location l, int deep, int i, int j) {
        OPHLib.getSolidRelativeY(l, deep);

        Block b = l.getBlock();
        if (isSolidBlock(b)) {
            // Falling block with upward velocity + CRACK PARTICLES
            Entity falling = setFallingBlock(l);
            if (falling != null) {
                falling.setVelocity(new Vector(0, 0.15, 0));
            }
            // Crack particles
            l.getWorld().spawnParticle(Particle.BLOCK, l, 15, 0.5, 0.5, 0.5, 0.2, b.getBlockData());

            for (Entity entity : l.getWorld().getNearbyEntities(l, 1, 4, 1)) {
                entity.setVelocity(new Vector(0, 2, 0));
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).damage(waveDamage, getUser().getPlayer());
                }
            }
        }
    }

    // Visual wave ring - falling blocks with upward velocity + CRACK PARTICLES
    private void expansionWaveFallingBlock(Location l, int deep) {
        for (Entity entity : l.getWorld().getNearbyEntities(l, 1, 4, 1)) {
            if (entity instanceof LivingEntity && !entity.getName().equals(getUser().getPlayer().getName())) {
                ((LivingEntity) entity).damage(waveDamage / 2, getUser().getPlayer());
            }
        }

        OPHLib.getSolidRelativeY(l, deep);
        Block b = l.getBlock();
        if (isSolidBlock(b)) {
            // Falling block with upward velocity for wave effect
            Entity falling = setFallingBlock(l);
            if (falling != null) {
                falling.setVelocity(new Vector(0, 0.15, 0));
            }
            // Crack particles showing the wave ring
            l.getWorld().spawnParticle(Particle.BLOCK, l, 10, 0.3, 0.3, 0.3, 0.1, b.getBlockData());
        }
    }

    private Vector getDirectionVector(Vector direction) {
        // Use normalized horizontal direction for any angle
        Vector dir = direction.clone();
        dir.setY(0);
        dir.normalize();
        return dir;
    }

    private ArrayList<Location> getPositions(Location loc, Vector direction) {
        // Generate perpendicular offsets for wave width (3 blocks wide)
        ArrayList<Location> blocks = new ArrayList<>();
        
        // Get perpendicular vector (90 degrees rotated)
        Vector perp = new Vector(-direction.getZ(), 0, direction.getX());
        
        for (int offset = -1; offset <= 1; offset++) {
            blocks.add(loc.clone().add(perp.clone().multiply(offset)));
            blocks.add(loc.clone().add(direction.clone()).add(perp.clone().multiply(offset)));
            blocks.add(loc.clone().add(direction.clone().multiply(-1)).add(perp.clone().multiply(offset)));
        }
        return blocks;
    }

    private Entity setFallingBlock(Location loc) {
        Block b = loc.getBlock();
        if (b.getType() != Material.AIR && b.getType() != Material.WATER && b.getType() != Material.BEDROCK) {
            if (b.getState() instanceof org.bukkit.block.Container) {
                b.breakNaturally();
                return null;
            }
            Material mat = b.getType();
            b.setType(Material.AIR);
            Entity falling = loc.getWorld().spawnFallingBlock(loc, mat.createBlockData());
            // Prevent item drops from falling block
            if (falling instanceof FallingBlock) {
                ((FallingBlock) falling).setDropItem(false);
            }
            return falling;
        }
        return null;
    }

    private boolean isSolidBlock(Block b) {
        return !(b.getType().getHardness() <= Material.TORCH.getHardness() ||
                b.getType() == Material.AIR ||
                b.getType() == Material.WATER ||
                b.getType() == Material.LAVA ||
                b.getType() == Material.BEDROCK);
    }

    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        super.onPlayerMove(event);
    }
}
