package fruits.paramecia;

import htt.ophabs.OPhabs;
import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHLib;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Bane_Bane extends Paramecia
{
    private boolean resort;
    private ItemStack glove;

    public static int getFruitID()
    {
        return 1001;
    }

    public Bane_Bane(int id)
    {
        super(id, "Bane_Bane", "Bane Bane no Mi", "Bane_Bane");

        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Resort
        basicSet.addAbility(new Ability("Resort", this::resort_ability));

        // Resort Punch
        basicSet.addAbility(new CooldownAbility("Resort Punch", 5, () -> {
            Player player = user.getPlayer();
            this.resortPunch(player.getLocation().add(player.getEyeLocation().clone().getDirection()).add(new Vector(0,0.7,0)));
        }));

        // Resort Punch Storm
        basicSet.addAbility(new CooldownAbility("Resort Punch Storm", 30, this::resortPunchStorm));

        //  Resort Persecution
        basicSet.addAbility(new CooldownAbility("Resort Persecution", 20, this::resortPersecution));

        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);
    }

    @Override
    protected void onAddFruit()
    {

    }

    @Override
    protected void onRemoveFruit()
    {
        this.resort = false;
    }

    private void resort_ability()
    {
        Player player = user.getPlayer();

        if (!resort)
        {
            resort = true;
            player.sendMessage("Resort mode activated");

            new BukkitRunnable()
            {
                float jumpVelocity = 0;
                @Override
                public void run() {
                    if(!resort){
                        this.cancel();
                    }

                    if(player.isDead()){
                        resort = false;
                        this.cancel();
                    }

                    if(player.isOnGround() && player.isSneaking()){
                        if(jumpVelocity < 10){
                            jumpVelocity += 0.5;
                        }
                    }

                    if(player.isOnGround() && !player.isSneaking()){
                        Vector velocity = player.getVelocity();
                        if (jumpVelocity > 0) {
                            player.getWorld().playSound(player.getLocation(), "resort", 1, 1);
                            velocity.setY(jumpVelocity);
                        }

                        if (player.isOnGround() && player.getFallDistance() > 0) {
                            double v = (-3+sqrt(9-8*(-(player.getFallDistance()))))/4;
                            velocity.setY(v);
                        }

                        if(player.isSprinting()){
                            velocity.setX(player.getEyeLocation().getDirection().getX() * 3);
                            velocity.setZ(player.getEyeLocation().getDirection().getZ() * 3);
                            if(velocity.getY() < 2)
                                velocity.setY(player.getEyeLocation().getDirection().getY() * 3);
                        }

                        player.setVelocity(velocity);
                        jumpVelocity = 0;
                    }
                }
            }.runTaskTimer(OPhabs.getInstance(), 0, 2);

        }
        else
        {
            player.sendMessage("Resort mode deactivated");
            resort = false;
        }
    }

    private void resortPunch(Location loc)
    {
        Player player = user.getPlayer();
        World playerWorld = player.getWorld();
        Location playerLocation = player.getLocation();
        ArmorStand armorStand = OPHLib.generateCustomFloatingItem(playerLocation.add(0,-0.5,0), glove, false);

        playerWorld.playSound(playerLocation, "resort", 1, 1);

        new BukkitRunnable()
        {
            // Configurar las propiedades del armor stand
            Entity entity = armorStand;
            Location origin = entity.getLocation();

            final Particle.DustOptions particle = new Particle.DustOptions(Color.fromRGB(128, 128, 128),0.5F);
            int ticks = 0;
            boolean first = true, back = false;
            Vector dir = player.getEyeLocation().getDirection(), aux;
            double distancePerTick = 5.0 / 7.0, distanceFromPlayer = 0;

            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void run() {
                distanceFromPlayer += distancePerTick;
                Vector movement = dir.clone().multiply(distanceFromPlayer);
                entity.teleport(origin.clone().add(movement));

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent && !(ent instanceof ArmorStand)) {
                        ((LivingEntity) ent).damage(14,(Entity) user.getPlayer());
                    }
                });

                aux = new Vector((entity.getLocation().getX()-origin.getX()), (entity.getLocation().getY()-origin.getY()), (entity.getLocation().getZ()-origin.getZ()));

                if(!back)
                {
                    Location temp = entity.getLocation().add(0,1,0);

                    for(double i = -2; i<0; i+=0.2)
                        OPHLib.circleEyeVector(0.4,0.4,i,particle, Particle.REDSTONE,user.getPlayer(), temp);
                }

                if (entity.isDead()){
                    entity.getLocation().getBlock().setType(Material.AIR);
                    cancelTask();
                }

                if (aux.length() > 7 ){//|| aux.length() < 1)
                    distancePerTick*=-1;
                    back=true;
                }

                if (aux.length() < 0.5 && ticks>10 || ticks > 40) {
                    entity.remove();
                    cancelTask();
                }

                ticks++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(OPhabs.getInstance(), 2, 1);
    }

    private void resortPunchStorm()
    {
        Player player = user.getPlayer();

        new BukkitRunnable(){
            final int attacks = 7;
            int count = 0;
            double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;

            final Location loc = player.getLocation();
            final double yaw = -loc.getYaw(),
                    pitch = loc.getPitch();
            final World world = player.getWorld();
            final double separation = 1;
            final double radius = 0.4;
            double i = 0;

            @Override
            public void run() {
                x = radius*cos(i);
                y = radius*sin(i);

                //Vertically (Arround X)
                xX = x;
                yX = cos(toRadians(pitch))* y - sin(toRadians(pitch));
                zX = sin(toRadians(pitch))* y + cos(toRadians(pitch));

                //horizontally (Arround Y)
                xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
                yY = yX;
                zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

                //Final (sum of player position)
                xL = loc.getX() + xY;
                yL = 1 + loc.getY() + yY;
                zL = loc.getZ() + zY;
                i += separation;

                Location partLoc = new Location(world, xL, yL, zL);

                resortPunch(partLoc);
                count++;

                if(count >= attacks)
                    cancelTask();
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(OPhabs.getInstance(), 2, 3);
    }

    public void resortPersecution()
    {
        Player player = user.getPlayer();
        World playerWorld = player.getWorld();
        Location loc = player.getEyeLocation().clone();

        ArrayList<LivingEntity> selected = new ArrayList<>();

        loc.add(0, -0.5, 0);

        playerWorld.getNearbyEntities(loc, 20, 2, 20).forEach(entity -> {
            if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
                selected.add((LivingEntity) entity);
            }
        });

        player.setVelocity(new Vector(0, 1, 0));

        if(selected.size() > 0)
            new BukkitRunnable() {
                int time = 0;
                int timeOut = 8;
                LivingEntity entity = selected.remove(0);

                public void run() {
                    if (selected.isEmpty() || entity == null)
                        this.cancel();
                    if(entity.isDead()) {
                        entity = selected.remove(0);
                        playerWorld.playSound(player.getLocation(), "resort", 1, 1);
                    }

                    Vector dir = entity.getLocation().toVector().subtract(player.getLocation().toVector());
                    dir.normalize();
                    player.setVelocity(dir.clone().multiply(2));
                    for(Entity ent : player.getNearbyEntities(2, 3, 2)){
                        if(ent instanceof LivingEntity && ent != player && !(entity instanceof ArmorStand)){
                            ((LivingEntity) ent).damage(20, player);

                            if(ent == entity){
                                if(selected.size() > 0) {
                                    playerWorld.playSound(player.getLocation(), "resort", 1, 1);
                                    entity = selected.remove(0);
                                }
                                else
                                    entity = null;
                            }
                            else{
                                if(selected.contains(ent)){
                                    if(selected.size() > 0) {
                                        playerWorld.playSound(player.getLocation(), "resort", 1, 1);
                                        selected.remove(ent);
                                    }
                                }
                            }
                        }
                        time = 0;
                    }
                    if(entity != null){
                        if(player.getLocation().add(dir).getBlock().getType() != Material.AIR || time == timeOut){
                            if(!selected.isEmpty()) {
                                playerWorld.playSound(player.getLocation(), "resort", 1, 1);
                                entity = selected.remove(0);
                            }
                            time = 0;
                        }
                    }
                    time++;
                }
            }.runTaskTimer(OPhabs.getInstance(), 20, 1);
    }

    @Override
    public void onFall(EntityDamageEvent event)
    {
        if (this.isFruitActive)
        {
            if (this.resort) {
                event.setCancelled(true);

                Player player = user.getPlayer();

                if (!player.isSneaking()) {
                    player.playSound(player.getLocation(), "resort", 1, 1);
                    Vector velocity = player.getVelocity();

                    double v = (-3.7 + sqrt(9 - 8*(-(player.getFallDistance())))) / 4;
                    velocity.setY(v);

                    if (player.isSprinting()) {
                        velocity.setX(player.getEyeLocation().getDirection().getX() * 4);
                        velocity.setZ(player.getEyeLocation().getDirection().getZ() * 4);
                    }

                    player.setVelocity(velocity);
                }
                user.getPlayer().setFallDistance(0);
            }
        }
    }
}
