package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

/**
 * @brief Ope ope no mi ability Class.
 * @author RedRiotTank
 */
public class ope_ope extends paramecia {

    private List<Location> roomBlocks = new ArrayList<>();
    private static Location roomcenter;
    private static boolean activeRoom;
    private final int radius = 15, roomTime = 400;
    private LivingEntity currentHearth = null;
    private int availableSqueezes;

    /**
     * @brief ope_ope constructor. Also initializes roomcenter, activeRoom and availableSqueezes.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank.
     */
    public ope_ope(OPhabs plugin) {
        super(plugin, 0, 0, 12, "ope_ope", "Ope Ope no Mi", "Ope Ope caster", 5, 1.6);

        abilitiesNames.add("Room");
        abilitiesCD.add(0);
        abilitiesNames.add("Levitation");
        abilitiesCD.add(0);
        abilitiesNames.add("Dash");
        abilitiesCD.add(0);
        abilitiesNames.add("stealHearth");
        abilitiesCD.add(0);

        roomcenter = null;
        activeRoom = false;
        availableSqueezes = 5;
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------

    /**
     * @brief Ability 1 caller: "Room".
     * @see ope_ope#room(int, Player)
     * @author RedRiotTank.
     */
    public void ability1() {
        if (abilitiesCD.get(0) == 0) {
            room(radius, user.getPlayer());
            abilitiesCD.set(0, 60); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Creates the room space and update states (activeRoom and roomCenter).
     * @param radius Room radius.
     * @param player Player who activates the room.
     * @note It also controls back particles animation and closing room timing (updates status when it closes).
     * @author RedRiotTank.
     */
    public void room(int radius, Player player) {
        Random rand = new Random();
        World world = player.getLocation().getWorld();
        world.playSound(player.getLocation(), "openroom", 1, 1);

        activeRoom = true;
        roomcenter = player.getLocation();

        new BukkitRunnable() {
            Material material;

            @Override
            public void run() {
                World world = roomcenter.getWorld();
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Location blockLoc = roomcenter.clone().add(x, y, z);
                            double distance = blockLoc.distance(roomcenter);
                            if (distance < radius + 0.5 && distance > radius - 0.5) {
                                int randMaterial = rand.nextInt(3);
                                if (randMaterial == 0 || randMaterial == 1)
                                    material = Material.BLUE_STAINED_GLASS;
                                else
                                    material = Material.LIGHT_BLUE_STAINED_GLASS;

                                blockLoc.getBlock().setType(material);
                                roomBlocks.add(blockLoc);
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

        }.runTaskTimer(plugin,0,1);

        new BukkitRunnable() {

            @Override
            public void run() {
                world.playSound(player.getLocation(), "closeroom", 1, 1);

                roomcenter = null;
                activeRoom = false;

                for (Location roomBlock : roomBlocks)
                    roomBlock.getBlock().setType(Material.AIR);

                roomBlocks.clear();
            }
        }.runTaskLater(plugin, roomTime);


    }

    /**
     * @brief Cancels onBlockBreak event if it's a room material and room is active to avoid breaking the room.
     * @param event BlockBreakEvent that Minecraft client sends to server to break the block.
     * @see ope_ope#room(int, Player)
     * @note Called in his correspondent in the caster, as a listener.
     * @note Abilities can destroy room blocks, is the way to scape from it.
     * @author RedRiotTank.
     */
    public static void roomBlockBreak(BlockBreakEvent event) {
        if(activeRoom)
            if(event.getBlock().getType().equals(Material.BLUE_STAINED_GLASS) || event.getBlock().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS))
                event.setCancelled(true);
    }

    /**
     * @brief Passive ability of the room, if the user is hit, it "shambless" the user and some other living entity around the room positions.
     * The damage will be dealt to this Living entity.
     * @param event userDamageByEntityEvent that Minecraft client sends to server to say there was a hit.
     * @see ope_ope#room(int, Player)
     * @note Called in his correspondent in the caster, as a listener.
     * @todo projectiles don't works correctly with this.
     * @author RedRiotTank.
     */
    public void onUserDamageByEntity(EntityDamageByEntityEvent event){
        World world = event.getDamager().getWorld();
        Player player = null;
        boolean found = false;
        Location auxTeleport = null;

        if(activeRoom){
            if(event.getEntity() instanceof Player){
                if(((Player)event.getEntity()).getInventory().getItemInMainHand().getType() != Material.AIR){
                    if(((Player)event.getEntity()).getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(fruit.getCasterName())) {
                        player = (Player) event.getEntity();
                        for (Entity ent : player.getPlayer().getWorld().getNearbyEntities(roomcenter, 13, 13, 13)) {
                            if(ent instanceof LivingEntity && !found){
                                if(!ent.equals(player) && !ent.equals(event.getDamager())){
                                    world.playSound(event.getEntity().getLocation(),"shambless",1,1);
                                    auxTeleport = ent.getLocation();
                                    ent.teleport(event.getEntity());
                                    event.getEntity().teleport(auxTeleport);

                                    ((LivingEntity) ent).damage(event.getDamage(), (Entity) user.getPlayer());
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    /**
     * @brief Ability 2 caller: "Levitation".
     * @see ope_ope#levitation()
     * @note Only can be used if there is an active room.
     * @author RedRiotTank.
     */
    public void ability2() {
        if (abilitiesCD.get(1) == 0) {
            levitation();
            abilitiesCD.set(1, 8); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: Makes the Living entities inside the room levitate.
     * @author RedRiotTank.
     */
    public void levitation() {
        if (activeRoom)
            for (Entity ent : user.getPlayer().getWorld().getNearbyEntities(roomcenter, radius, radius, radius))

                if (ent instanceof LivingEntity && ent != user.getPlayer())
                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));

    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    /**
     * @brief Ability 3 caller: "Dash".
     * @see ope_ope#dash(Player)
     * @note It has no cooldown.
     * @author RedRiotTank.
     */
    public void ability3() {
        if (abilitiesCD.get(2) == 0) {
            dash(user.getPlayer());
            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: The player will dash in a *2 block distance to the direction he is looking at.
     * @param player who is going to dash.
     * @note Only can be used if a room is active.
     * @author RedRiotTank.
     */
    public void dash(Player player){
        if(activeRoom){
            Location location = player.getLocation();
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            double x = 2 * Math.cos(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(-pitch));
            double y = 2 * Math.sin(Math.toRadians(-pitch));
            double z = 2 * Math.sin(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(-pitch));
            player.setVelocity(new Vector(x, y, z));
            player.getWorld().playSound(player.getLocation(),"fastsoru",1,1);
        }



    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    /**
     * @brief Ability 4 caller: "Steal Hearth".
     * @see ope_ope#stealHearth(Player)
     * @note It has no cooldown.
     * @author RedRiotTank.
     */
    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            stealHearth(user.getPlayer());
            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }

    /**
     * @brief CORE ABILITY: The player steals hearth to the entity he is looking at. Shift + click will make damage to the hearth.
     * @param player who is activating the ability.
     * @note Only can steal if room is active, but can hurt the hearth whenever he wants.
     * @author RedRiotTank.
     */
    public void stealHearth(Player player){
        if(!player.isSneaking()){
            if(activeRoom){
                currentHearth = OPHLib.rayCastLivEnt(player,2);

                if (currentHearth != null) {
                    player.getWorld().playSound(currentHearth.getLocation(),"swordcut",1,1);
                    OPHLib.spawnBloodParticles(currentHearth);
                    player.sendMessage("You have stolen " + currentHearth.getName() + "'s ❤.");
                }
            }
        } else {
            if(currentHearth == null) {
                player.sendMessage("You don't have anybody's ❤ right now.");
            } else {
                availableSqueezes--;

                currentHearth.damage(2, (Entity) user.getPlayer());
                currentHearth.getWorld().playSound(currentHearth.getLocation(),"swordcut",1,1);
                player.getWorld().playSound(player.getLocation(),"swordcut",1,1);
                currentHearth.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,currentHearth.getLocation().add(0,currentHearth.getHeight(),0),4);

                if(availableSqueezes == 0){
                    availableSqueezes = 5;
                    currentHearth = null;
                }
            }

        }
    }
}
