package abilitieSystem;

import htt.ophabs.OPhabs;
import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.EulerAngle;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * @brief Bane bane no mi ability Class.
 * @author Vaelico786.
 */
public class bane_bane extends paramecia {
    private boolean resort;
    private ItemStack glove;

    private int UltimateAttackCD = 2;
    // ---------------------------------------------- CONSTRUCTORS ---------------------------------------------------------------------
    /**
     * @brief bane_bane constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public bane_bane(OPhabs plugin) {
        super(plugin, castIdentification.castMaterialBane, castIdentification.castItemNameBane, fruitIdentification.fruitCommandNameBane);
        abilitiesNames.add("Resort");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch");
        abilitiesCD.add(0);
        abilitiesNames.add("Resort Punch Storm");
        abilitiesCD.add(0);
        abilitiesNames.add("Ultimate Attack");
        abilitiesCD.add(0);

        // Crear el ItemStack con el objeto "raw iron"
        glove = new ItemStack(Material.RAW_IRON);
        ItemMeta itemMeta = glove.getItemMeta();
        itemMeta.setCustomModelData(1); // Establecer el Custom Model Data a 1

        //Sometimes on ability 2 player changes the caster with this object so set the same meta for fix it
        itemMeta.setDisplayName(castIdentification.castItemNameBane);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        glove.setItemMeta(itemMeta);
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------
    /**
     * @brief Ability 1: "RESORT".
     * @see gura_gura#resort()
     * @author Vaelico786.
     */
    public void ability1() {
        resort();
    }

    /**
     * @brief CORE ABILITY 1: "RESORT". Convert user on a resort man, making him invulnerable to fall damage and
     * some extra effects more.
     * @author Vaelico786.
     */
    public void resort() {
        if (!resort) {
            resort = true;
            user.getPlayer().sendMessage("Resort mode activated");
            new BukkitRunnable() {
                Player player = user.getPlayer();
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
                        if (jumpVelocity > 0)
                            velocity.setY(jumpVelocity);
                        
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
            }.runTaskTimer(plugin, 0, 2);
            
        } else {
            user.getPlayer().sendMessage("Resort mode deactivated");
            resort = false;
        }
    }

    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------
    /**
     * @brief Ability 2: "Resort punch".
     * @see bane_bane#resortPunch()
     * @author Vaelico786.
     */
    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            resortPunch(user.getPlayer().getLocation().add(user.getPlayer().getEyeLocation().clone().getDirection()).add(new Vector(0,0.7,0)));
            abilitiesCD.set(1, 5);
        }
    }

    /**
     * @brief CORE ABILITY 2: "Resort Punch". Unleash a punch in front of the player that deals extra damage.
     * @author Vaelico786.
     */
    public void resortPunch(Location loc) {

            ArmorStand armorStand = user.getPlayer().getWorld().spawn(user.getPlayer().getLocation().add(0,-0.5,0), ArmorStand.class);
            // Establecer el objeto con Custom Model Data en la mano del armor stand
            armorStand.getEquipment().setHelmet(glove);

            armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-90), 0, 0));
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setArms(true);
            armorStand.setCustomName("gloveStandBaneBaneNoMi");
        new BukkitRunnable(){

            // Configurar las propiedades del armor stand

            Entity entity = armorStand;
            Location origin=entity.getLocation();

            final Particle.DustOptions particle = new Particle.DustOptions(Color.fromRGB(128, 128, 128),0.5F);
            int ticks = 0;
            boolean first = true, back=false;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            double distancePerTick = 5.0 / 7.0, distanceFromPlayer = 0;
            
            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void run() {
                if(first){
                    entity.setGravity(false);
                    // entity.setVelocity(dir);
                    first=false;
                }
                distanceFromPlayer+=distancePerTick;
                Vector movement = dir.clone().multiply(distanceFromPlayer);
                entity.teleport(origin.clone().add(movement));

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent) {
                        ((LivingEntity) ent).damage(14);
                    }
                });

                aux = new Vector((entity.getLocation().getX()-origin.getX()), (entity.getLocation().getY()-origin.getY()), (entity.getLocation().getZ()-origin.getZ()));


                if(!back){
                    Location temp = entity.getLocation().add(0,0.5,0);
                    circleEyeVector(0.4,0.1,-2,particle, Particle.REDSTONE,user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-1.8,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-1.6,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-1.4,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-1.2,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-1,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-0.8,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-0.6,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-0.4,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,-0.2,particle, Particle.REDSTONE, user.getPlayer(), temp);
                    circleEyeVector(0.4,0.1,0,particle, Particle.REDSTONE, user.getPlayer(), temp);
               }

                if (entity.isDead()) {
                    entity.getLocation().getBlock().setType(Material.AIR);
                    // entity.getLocation().add(dir).getBlock().breakNaturally();
                    cancelTask();
                }

                if (aux.length() > 7 ){//|| aux.length() < 1)
                    distancePerTick*=-1;
                    
                    // entity.getLocation().add(dir).getBlock().breakNaturally();
                    back=true;
                }

                if (aux.length() < 0.5 && ticks>10 || ticks > 40) {
                    
                    entity.remove();
                    // entity.getLocation().getBlock().breakNaturally();

                    cancelTask();
                }
                
                ticks++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 2, 1);
    }

    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------
    /**
     * @brief Ability 3: "Resort punch storm".
     * @see bane_bane#resortPunchStorm()
     * @author Vaelico786.
     */
    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            new BukkitRunnable(){
                final int attacks=7;
                int count=0;
                double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;

                final Location loc = user.getPlayer().getLocation();
                final double yaw = -loc.getYaw(),
                        pitch = loc.getPitch();
                final World world = user.getPlayer().getWorld();
                final double separation=1;
                final double radius=0.4;
                double i=0;

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
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = loc.getX() + xY;
            yL = 1 + loc.getY() + yY;
            zL = loc.getZ() + zY;
            i+=separation;

            Location partLoc = new Location(world, xL, yL, zL);

                    resortPunch(partLoc);
                    count++;
                    if(count>=attacks)
                        cancelTask();
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 2, 3);

            abilitiesCD.set(2, 30);
        }
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------
    /**
     * @brief Ability 4: "Ultimate Attack".
     * @see bane_bane#UltimateAttack(Player)
     * @author MiixZ.
     */
    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            UltimateAttack(user.getPlayer());
            abilitiesCD.set(3, UltimateAttackCD);
        }
    }

    /**
     * @brief CORE ABILITY 4: "Ultimate Attack". Selecciona a todas las entidades en línea recta para hacerles un ataque frontal.
     * @param player Jugador que usa la habilidad.
     * @author MiixZ.
     */
    public void UltimateAttack(Player player) {
        Location loc = player.getEyeLocation().clone();
        ArrayList<LivingEntity> seleccionados = new ArrayList<>();

        loc.add(0, -0.5, 0);

        SeleccionarEntidad(Objects.requireNonNull(loc.getWorld()), loc, player, seleccionados);

        player.setVelocity(new Vector(0, 1, 0).multiply(2));

        ejecutarAtaque((ArrayList<LivingEntity>) seleccionados.clone(), player);

        efectuarAtaque(seleccionados, player);
    }

    /**
     * @brief Selecciona a las entidades de la línea recta (píxel actual).
     * @param seleccionados Lista de entidades seleccionadas.
     * @param player Jugador.
     * @param loc Localización del píxel actual (línea recta).
     * @param mundo Mundo en el que se encuentra el jugador.
     * @author MiixZ
     */
    public void SeleccionarEntidad(World mundo, Location loc, Player player, ArrayList<LivingEntity> seleccionados) {
        mundo.getNearbyEntities(loc, 20, 2, 20).forEach(entity -> {
            if (!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                seleccionados.add((LivingEntity) entity);
            }
        });
    }

    /**
     * @brief Para cada entidad seleccionada, cargo un ataque, rebota en direcciones random y después efectúa el verdadero ataque.
     * Se supone que es tan rápido que no se nota a simple vista.
     * @param seleccionados Lista de entidades seleccionadas.
     * @param player Jugador.
     * @author MiixZ
     */
    public void ejecutarAtaque(ArrayList<LivingEntity> seleccionados, Player player) {
        new BukkitRunnable() {
            int k = 0;
            final int randomSeleccionados = new Random().nextInt(seleccionados.size() % 20 + 5);
            final Vector direccion = new Vector(new Random().nextInt(10) - 5,
                    new Random().nextInt(10) - 5,
                    new Random().nextInt(10) - 5);

            public void run() {
                if (k == randomSeleccionados)
                    this.cancel();
                else {
                    if (k % 2 == 0)
                        player.setVelocity(direccion.multiply(-3));
                    else
                        player.setVelocity(direccion.multiply(3));

                    // Cada 3, cambiamos la dirección por otra random.
                    if (k % 3 == 0) {
                        direccion.setX(new Random().nextInt(10) - 5);
                        direccion.setY(new Random().nextInt(10) - 5);
                        direccion.setZ(new Random().nextInt(10) - 5);
                    }

                    k++;
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    /**
     * @brief Tepea al jugador hacia cada entidad de la lista seleccionada y genera una explosión..
     * @param seleccionados Lista de entidades seleccionadas.
     * @param player Jugador.
     * @author MiixZ
     */
    public void efectuarAtaque(ArrayList<LivingEntity> seleccionados, Player player) {
        new BukkitRunnable() {
            public void run() {
                if (seleccionados.size() == 0)
                    this.cancel();
                else {
                    final LivingEntity entity = seleccionados.get(0);
                    seleccionados.remove(0);

                    player.teleport(entity.getLocation());
                    entity.damage(20, player);
                    entity.setVelocity(new Vector(1, 1, 1).multiply(2));
                }
            }
        }.runTaskTimer(plugin, 40, 20);
    }

    /**
     * @brief Creates a circle of particles in the direction the player is looking at.
     * @param radius Radius of the circle.
     * @param separation Separation between the particles.
     * @param prof How far away from the player you want the circle to appear.
     * @param dustOption Dust Option of the particle. Set it to null if your particle don't use dust options.
     * @param particle ParticleType of the circle.
     * @param player player that generates the circle next to.
     * @param loc center of the circle
     *
     * @see OPHLib#circleEyeVector()
     * @author RedRiotTank / Vaelico786
     */
    public static void circleEyeVector(double radius, double separation, double prof, Particle.DustOptions dustOption,
                                       Particle particle, Player player, Location loc) {

        double x,y, xY, yY, zY, xX, yX, zX,xL,yL,zL;
        final double yaw = -player.getLocation().getYaw(),
                pitch = player.getLocation().getPitch();
        final World world = player.getWorld();

        for(double i=0; i<2*PI; i+= separation) {
            x = radius*cos(i);
            y = radius*sin(i);

            //Vertically (Arround X)
            xX = x;
            yX = cos(toRadians(pitch))* y - sin(toRadians(pitch))* prof;
            zX = sin(toRadians(pitch))* y + cos(toRadians(pitch))* prof;

            //horizontally (Arround Y)
            xY = cos(toRadians(yaw))*xX + sin(toRadians(yaw))*zX;
            yY =  yX;
            zY = -sin(toRadians(yaw))*xX + cos(toRadians(yaw))*zX;

            //Final (sum of player position)
            xL = loc.getX() + xY;
            yL = 0.3 + loc.getY() + yY;
            zL = loc.getZ() + zY;

            Location partLoc = new Location(world, xL, yL, zL);

            world.spawnParticle(particle,partLoc,0,0,0,0,dustOption);
        }
    }

    // ---------------------------------------------- PASSIVES ---------------------------------------------------------------------
    /**
     * @brief On fall listener.
     * @see bane_bane#onFall()
     * @author Vaelico786.
     */
    public void onFall(EntityDamageEvent event) {
        if (active) {
            if (resort) {
                event.setCancelled(true);
                Player player = user.getPlayer();
                if (!player.isSneaking()) {
                    Vector velocity = player.getVelocity();
                    double v = (-3.7+sqrt(9-8*(-(player.getFallDistance()))))/4;
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

    /**
     * @brief Cancels fallingblock --> block change to make the absorbing effect, and saves the blocks materials information.
     * @param event EntityChangeBlockEvent that Minecraft client sends to server to convert blocks.
     * @see bane_bane#resortPunch(boolean)
     * @note Called in his correspondent in the caster, as a listener.
     * @author RedRiotTank.
     */
    public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getBlock().getType().equals(Material.STONE)) {
            event.setCancelled(true);
        }
    }
}
