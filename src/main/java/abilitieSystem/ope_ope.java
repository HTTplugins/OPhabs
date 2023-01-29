package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class ope_ope extends paramecia {

    private List<Location> roomBlocks = new ArrayList<>();

    private Location roomcenter;
    private static boolean activeRoom;

    private final int radius = 15;

    private final int roomTime = 400;

    public ope_ope(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialOpe, castIdentification.castItemNameOpe, fruitIdentification.fruitCommandNameOpe);

        abilitiesNames.add("Room");
        abilitiesCD.add(0);
        abilitiesNames.add("Levitation");
        abilitiesCD.add(0);
        abilitiesNames.add("Dash");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);

        roomcenter = null;
        activeRoom = false;

    }

    public void ability1() {
        if (abilitiesCD.get(0) == 0) {
            room(user.getPlayer().getLocation(), radius, user.getPlayer());
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2() {
        if (abilitiesCD.get(1) == 0) {
            levitation();
            abilitiesCD.set(1, 0); // Pon el cooldown en segundos
        }
    }

    public void ability3() {
        if (abilitiesCD.get(2) == 0) {
            dash(user.getPlayer());
            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    public void room(Location location, int radius, Player player) {
        Random rand = new Random();
        World world = location.getWorld();
        world.playSound(location, "openroom", 1, 1);

        activeRoom = true;
        roomcenter = location;

        new BukkitRunnable() {
            Material material;

            @Override
            public void run() {
                for (double x = -radius; x <= radius; x++) {
                    for (double y = -radius; y <= radius; y++) {
                        for (double z = -radius; z <= radius; z++) {
                            double distance = Math.sqrt((x * x) + (y * y) + (z * z));
                            if (distance <= radius) {
                                Location blockLocation = location.clone().add(x, y, z);
                                if (blockLocation.getBlockY() >= location.getBlockY() && blockLocation.getBlock().getType() == Material.AIR) {
                                    if (distance >= radius - 1) {
                                        int randMaterial = rand.nextInt(3);
                                        if (randMaterial == 0 || randMaterial == 1)
                                            material = Material.BLUE_STAINED_GLASS;
                                        else
                                            material = Material.LIGHT_BLUE_STAINED_GLASS;

                                        blockLocation.getBlock().setType(material);
                                        roomBlocks.add(blockLocation);

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(plugin, 20);

        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {
                ticks++;

                if(ticks == roomTime) this.cancel();

                double yaw = player.getLocation().getYaw();
                double y = player.getLocation().getY();

                spawnBackParticle(player,yaw - 90,y+0.5);
                spawnBackParticle(player, yaw - 45,y+0.5);
                spawnBackParticle(player, yaw - 135,y+0.5);

                spawnBackParticle(player,yaw - 90,y+1);
                spawnBackParticle(player, yaw - 45,y+1);
                spawnBackParticle(player, yaw - 135,y+1);
            }


            public void spawnBackParticle(Player player, double yaw, double y){
                double x = cos(toRadians(yaw)) / 2;
                double z = sin(toRadians(yaw)) / 2;
                //Behind
                double xBehind = player.getLocation().getX() + x;
                double zBehind = player.getLocation().getZ() + z;
                Location pBehind = new Location(player.getWorld(),xBehind,y,zBehind);

                Random random = new Random();
                Particle.DustOptions dustOptions = new Particle.DustOptions(random.nextBoolean() ? Color.fromRGB(179,232,244) : Color.AQUA, 3.0f);

                player.getWorld().spawnParticle(Particle.REDSTONE,pBehind,0,0,0,0,dustOptions);
            }

            public void createParticlesBehindPlayer(Player player) {
                Location location = player.getLocation();
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                double x = -1 * Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(-pitch));
                double y = -1 * Math.sin(Math.toRadians(-pitch));
                double z = -1 * Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(-pitch));
                location.add(x, y, z);

                Random random = new Random();
                Particle.DustOptions dustOptions = new Particle.DustOptions(random.nextBoolean() ? Color.fromRGB(179,232,244) : Color.AQUA, 1.0f);


                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,0.2,0), 0,0,0,0, dustOptions);
                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,0.4,0), 0,0,0,0, dustOptions);
                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,0.6,0), 0,0,0,0, dustOptions);
                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,0.8,0), 0,0,0,0, dustOptions);
                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,1,0), 0,0,0,0, dustOptions);
                player.getWorld().spawnParticle(Particle.REDSTONE, location.add(0,1.5,0), 0,0,0,0, dustOptions);

                location.subtract(0,2,0);

            }



        }.runTaskTimer(plugin,0,1);

        new BukkitRunnable() {

            @Override
            public void run() {
                world.playSound(location, "closeroom", 1, 1);

                roomcenter = null;
                activeRoom = false;

                for (Location roomBlock : roomBlocks)
                    roomBlock.getBlock().setType(Material.AIR);

                roomBlocks.clear();
            }
        }.runTaskLater(plugin, roomTime);


    }

    public void levitation() {
        if (activeRoom) {
            for (Entity ent : user.getPlayer().getWorld().getNearbyEntities(roomcenter, radius, radius, radius)) {

                if (ent instanceof LivingEntity && ent != user.getPlayer())
                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));

            }

        }


    }

    public void dash(Player player){

        if(activeRoom){
            Location location = player.getLocation();
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            double x = 2 * Math.cos(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(-pitch));
            double y = 2 * Math.sin(Math.toRadians(-pitch));
            double z = 2 * Math.sin(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(-pitch));
            player.setVelocity(new Vector(x, y, z));


        }



    }

    public static void onBlockBreak(BlockBreakEvent event) {
        if(activeRoom)
            if(event.getBlock().getType().equals(Material.BLUE_STAINED_GLASS) || event.getBlock().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS))
                event.setCancelled(true);

    }

}
