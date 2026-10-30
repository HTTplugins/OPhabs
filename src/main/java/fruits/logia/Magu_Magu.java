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

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Magu_Magu extends Logia {

    private static final int LAVA_METEORS_COOLDOWN = 180;
    private static final int LAVA_GROUND_COOLDOWN = 30;
    private static final int LAVA_RISE_COOLDOWN = 120;

    private final Random rand = new Random();
    private final Queue<Location> meteorTargets = new ConcurrentLinkedQueue<>();

    public static int getFruitID() {
        return 1009;
    }

    public Magu_Magu(int id) {
        super(id, "Magu_Magu", "Magu Magu no Mi", "Magu_Magu", Particle.FLAME);

        AbilitySet basicSet = new AbilitySet("Logia Body");

        basicSet.addAbility(new CooldownAbility("Lava Meteors", LAVA_METEORS_COOLDOWN, this::lavaMeteors));
        basicSet.addAbility(new CooldownAbility("Lava Ground", LAVA_GROUND_COOLDOWN, this::lavaGround));
        basicSet.addAbility(new CooldownAbility("Lava Rise", LAVA_RISE_COOLDOWN, this::lavaRise));

        this.abilitySets.add(basicSet);
    }

    @Override
    protected void logiaModeON() {
        OPUser user = this.getUser();
        if (user != null) {
            Player player = user.getPlayer();
            if (player != null) {
                player.setAllowFlight(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
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
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
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
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
        }
    }

    public void lavaMeteors() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);

        List<LivingEntity> targets = new ArrayList<>();
        for (Entity ent : player.getNearbyEntities(15, 10, 15)) {
            if (ent instanceof LivingEntity && !ent.equals(player)) {
                targets.add((LivingEntity) ent);
            }
        }

        int maxMeteors = 3;
        int count = 0;
        for (LivingEntity target : targets) {
            if (count >= 3) break;
            sendMeteorite(player, target);
            count++;
        }
    }

    private void sendMeteorite(Player player, LivingEntity target) {
        Location upHead = player.getLocation().clone().add(0, 7, 0);
        World world = player.getWorld();

        new BukkitRunnable() {
            int particles = 0;

            @Override
            public void run() {
                if (target.isDead() || !target.isValid()) { cancel(); return; }

                Vector vec = target.getLocation().toVector().subtract(upHead.toVector()).normalize();
                Location spawnPos = upHead.clone();

                for (double i = 0; i < 2 * Math.PI * 4; i += 0.1) {
                    double angle = i;
                    double x = Math.sin(angle);
                    double y = Math.sin(i * 0.5) * Math.cos(i);
                    double z = Math.cos(i * 0.5) * Math.cos(i);

                    Location particleLoc = spawnPos.clone().add(x, y, z);
                    particleLoc.add(new Vector(0, 0, 0));

                    int option = rand.nextInt(3);
                    Color color;
                    if (option == 0) color = Color.ORANGE;
                    else if (option == 1) color = Color.BLACK;
                    else color = Color.RED;

                    Particle.DustOptions dust = new Particle.DustOptions(color, 1.0f);
                    if (spawnPos.getBlock().getType() == Material.AIR) {
                        world.spawnParticle(Particle.SMOKE, particleLoc, 1, 0.0, 0.0, 0.0, 0.0, dust);
                    }
                }

                for (Entity ent : world.getNearbyEntities(spawnPos, 2, 2, 2)) {
                    if (ent instanceof LivingEntity && !ent.equals(player) && ent.equals(target)) {
                        target.damage(2, player);
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (target.isDead() || ticks > 100) { cancel(); return; }
                if (target.getLocation().distance(upHead) < 2) {
                    target.getWorld().createExplosion(target.getLocation(), 6);
                    target.setFireTicks(100);
                    target.damage(8, player);
                    cancel();
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 1);
    }

    public void lavaGround() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        if (player.isSneaking()) {
            lavaRiver(player);
        } else {
            lavaLake(player);
        }
    }

    private void lavaRiver(Player player) {
        OPHSoundLib.OphPlaySound(OPHSounds.FOG, player.getLocation(), 1, 1);
        Vector dir = player.getLocation().getDirection();

        new BukkitRunnable() {
            int index = 2;
            double randomQuote = 1;
            Location playerLoc = player.getLocation();

            @Override
            public void run() {
                int x = (int)(playerLoc.getDirection().getX() * index);
                int z = (int)(playerLoc.getDirection().getZ() * index);
                int y = 0;

                putLavaBlock(x, y, z, randomQuote, playerLoc);
                if (x == 0) {
                    putLavaBlock(x + 1, y, z, randomQuote, playerLoc);
                    putLavaBlock(x - 1, y, z, randomQuote, playerLoc);
                } else if (z == 0) {
                    putLavaBlock(x, y, z + 1, randomQuote, playerLoc);
                    putLavaBlock(x, y, z - 1, randomQuote, playerLoc);
                } else {
                    putLavaBlock(x + (x > 0 ? 1 : -1), y, z, randomQuote, playerLoc);
                    putLavaBlock(x, y, z + (z > 0 ? 1 : -1), randomQuote, playerLoc);
                }
                putLavaBlock(x, y, z, randomQuote, playerLoc);

                index++;
                randomQuote -= 0.02;
                if (index == 12) cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 3);
    }

    private void lavaLake(Player player) {
        new BukkitRunnable() {
            int index = 1;
            final Location playerLoc = player.getLocation();
            double randomQuote = 1;

            @Override
            public void run() {
                for (double i = -index; i <= index; i++) {
                    putLavaBlock((int)i, 0, -index, randomQuote, playerLoc);
                    putLavaBlock((int)i, 0, index, randomQuote, playerLoc);
                }
                for (double i = -index; i <= index; i++) {
                    putLavaBlock(-index, 0, (int)i, randomQuote, playerLoc);
                    putLavaBlock(index, 0, (int)i, randomQuote, playerLoc);
                }
                randomQuote -= 0.1;
                index++;
                if (index == 5) cancel();
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 5);
    }

    private void putLavaBlock(int x, int y, int z, double randomQuote, Location playerLoc) {
        double aleatory = rand.nextDouble();
        double randMagmaBlock = rand.nextDouble();

        Material blockMaterial = Material.LAVA;
        if (rand.nextDouble() < 0.1) blockMaterial = Material.MAGMA_BLOCK;

        if (isAirOrSimilar(playerLoc.getBlock().getRelative(x, y, z).getType()) && rand.nextDouble() < randomQuote) {
            if (!playerLoc.getBlock().getRelative(x, y - 1, z).getType().equals(Material.AIR)) {
                playerLoc.getBlock().getRelative(x, y - 1, z).setType(blockMaterial);
                playerLoc.getWorld().playSound(playerLoc.getBlock().getRelative(x, y - 1, z).getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
            }
        }
    }

    private boolean isAirOrSimilar(Material mat) {
        return mat.isAir() || mat == Material.GRASS_BLOCK || mat == Material.TALL_GRASS || mat == Material.DIRT || 
               mat == Material.SHORT_GRASS || mat == Material.FERN || mat == Material.DEAD_BUSH;
    }

    public void lavaRise() {
        OPUser user = this.getUser();
        if (user == null) return;
        Player player = user.getPlayer();
        if (player == null) return;

        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR, player.getLocation(), 1, 1);

        int nGeysers = 8;
        int area = 10;
        Queue<Location> locations = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 8; i++) {
            Location loc = player.getLocation().clone().add(
                rand.nextInt(20) - 10, 0, rand.nextInt(20) - 10);
            loc.setY(OPHLib.searchGround(loc.getX(), loc.getZ(), loc.getY(), player.getWorld()));
            locations.add(loc);
        }

        new BukkitRunnable() {
            int ticks = 0;
            List<Location> active = new ArrayList<>();

            @Override
            public void run() {
                if (ticks >= 250 || player.isDead()) { cancel(); return; }

                if (ticks % 50 == 0 && !active.isEmpty()) active.remove(0);
                if (ticks % 20 == 0 && !locations.isEmpty()) active.add(locations.poll());

                for (Location loc : active) {
                    player.getWorld().spawnParticle(Particle.LAVA, loc, 10, 1, 2, 1, 0);
                    player.getWorld().spawnParticle(Particle.LAVA, loc.clone().add(0, 2, 0), 5, 2, 1, 2, 3);
                    for (Entity ent : player.getWorld().getNearbyEntities(loc.clone().add(0, 1, 0), 2, 2, 2)) {
                        if (ent instanceof LivingEntity && !ent.equals(player)) {
                            ((LivingEntity) ent).damage(4, player);
                            ent.setFireTicks(100);
                        }
                    }
                }
                ticks += 5;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 5);
    }


    @Override
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        super.onPlayerMove(event);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (isFruitActive() && event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
        }
    }
}
