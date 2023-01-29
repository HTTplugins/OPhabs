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

    public ope_ope(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialOpe, castIdentification.castItemNameOpe, fruitIdentification.fruitCommandNameOpe);

        abilitiesNames.add("Room");
        abilitiesCD.add(0);
        abilitiesNames.add("Levitation");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);

        roomcenter = null;
        activeRoom = false;

    }

    public void ability1() {
        if (abilitiesCD.get(0) == 0) {
            room(user.getPlayer().getLocation(), radius);
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

            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    public void room(Location location, int radius) {
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
        }.runTaskLater(plugin, 400);


    }

    public void levitation() {
        if (activeRoom) {
            for (Entity ent : user.getPlayer().getWorld().getNearbyEntities(roomcenter, radius, radius, radius)) {

                if (ent instanceof LivingEntity && ent != user.getPlayer())
                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));

            }

        }


    }

    public static void onBlockBreak(BlockBreakEvent event) {
        if(activeRoom)
            if(event.getBlock().getType().equals(Material.BLUE_STAINED_GLASS) || event.getBlock().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS))
                event.setCancelled(true);

    }

}
