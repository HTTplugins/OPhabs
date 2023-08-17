package fruits.paramecia;


import abilities.AbilitySet;
import abilities.CooldownAbility;
import htt.layeredstructures.LayeredStructuresAPI;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import runnables.OphRunnable;

import java.util.ArrayList;
import java.util.List;


public class Ope_Ope extends Paramecia {

    private List<Block> roomBlocks = null;
    private Location roomcenter;
    private boolean activeRoom;
    private final int roomTime = 400;

    private LivingEntity currentHearth;
    private int availableSqueezes;

    public Ope_Ope(int id) {
        super(id, "Ope_Ope", "Ope ope no Mi", "Ope_Ope");

        roomcenter = null;
        activeRoom = false;
        roomBlocks = new ArrayList<>();
        currentHearth = null;
        availableSqueezes = 0;

        AbilitySet basicSet = new AbilitySet("Base Set");

        // Room
        basicSet.addAbility(new CooldownAbility("Room", 40, () -> {
            Player player = user.getPlayer();
            this.room(player);
        }));

        // Levitation
        basicSet.addAbility(new CooldownAbility("Levitation", 5, this::levitation));

        // Dash
        basicSet.addAbility(new CooldownAbility("Dash", 0, () -> {
            Player player = user.getPlayer();
            this.dash(player);
        }));
        this.abilitySets.add(basicSet);

        // StealHearth
        basicSet.addAbility(new CooldownAbility("StealHearth", 5, () -> {
            Player player = user.getPlayer();
            this.stealHearth(player);
        }));

    }

    public void room(Player player) {
        OPHSoundLib.OphPlaySound(OPHSounds.OPENROOM, player.getLocation(), 1, 1);
        activeRoom = true;
        roomcenter = player.getLocation();
        roomBlocks = LayeredStructuresAPI.generatePreChargedLayeredStructure("room", player.getLocation(), "north",0,1,false);

        new OphRunnable(roomTime+1){
            @Override
            public void OphRun() {
                if(getCurrentRunIteration() >= roomTime) this.cancel();

                double yaw = player.getLocation().getYaw();
                double y = player.getLocation().getY();

                Particle.DustOptions dustOptions = new Particle.DustOptions(OPHLib.getRandom().nextBoolean() ? Color.fromRGB(179,232,244) : Color.AQUA, 3.0f);

                OPHAnimationLib.spawnYawDependingParticle(player,yaw - 90, y+0.5,dustOptions);
                OPHAnimationLib.spawnYawDependingParticle(player, yaw - 45,y+0.5, dustOptions);
                OPHAnimationLib.spawnYawDependingParticle(player, yaw - 135,y+0.5, dustOptions);

                OPHAnimationLib.spawnYawDependingParticle(player,yaw - 90,y+1, dustOptions);
                OPHAnimationLib.spawnYawDependingParticle(player, yaw - 45,y+1, dustOptions);
                OPHAnimationLib.spawnYawDependingParticle(player, yaw - 135,y+1, dustOptions);
            }
        }.ophRunTaskTimer(0,1);

        new OphRunnable(roomTime+1) {
            @Override
            public void OphRun() {

                OPHSoundLib.OphPlaySound(OPHSounds.CLOSEROOM, player.getLocation(), 1, 1);

                roomBlocks.forEach(block -> block.setType(Material.AIR));

                roomBlocks.clear();
                roomcenter = null;
                activeRoom = false;
            }
        }.ophRunTaskLater(roomTime);
    }

    public void levitation() {
        if (activeRoom) {
            int roomRadius = 15;
            for (Entity ent : user.getPlayer().getWorld().getNearbyEntities(roomcenter, roomRadius, roomRadius, roomRadius))
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

    // Habilidad pendiente de rework de remapeo de texturas.
    public void stealHearth(Player player){
        if(!player.isSneaking()){
            if(activeRoom){
                currentHearth = OPHLib.rayCastLivEnt(player,2);

                if (currentHearth != null) {
                    availableSqueezes = 5;
                    OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
                    OPHLib.spawnBloodParticles(currentHearth);
                    msgSystem.OphPlayerMsg(player,"You have stolen " + currentHearth.getName() + "'s ❤.");

                    /*  Pendiente del rework de remapeo de texturas
                    ItemStack caster;
                    if (castIdentification.itemIsCaster(user.getPlayer().getInventory().getItemInMainHand(), user))
                        caster = user.getPlayer().getInventory().getItemInMainHand();
                    else
                        caster = user.getPlayer().getInventory().getItemInOffHand();

                    ItemMeta meta = caster.getItemMeta();
                    meta.setCustomModelData(fruit.getCustomModelDataId()+1000);
                    caster.setItemMeta(meta);
                     */
                }
            } else {
                msgSystem.OphPlayerError(player,"You must have an active room to use this ability.");
            }
        } else {
            if(currentHearth == null) {
                msgSystem.OphPlayerError(player,"You must have a hearth to squeeze.");
            } else if(!currentHearth.isDead()) {
                availableSqueezes--;

                currentHearth.damage(2, user.getPlayer());

                OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
                OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, currentHearth.getLocation(), 1, 1);

                currentHearth.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,currentHearth.getLocation().add(0,currentHearth.getHeight(),0),4);

                if(availableSqueezes == 0){
                    currentHearth = null;
                    /*
                    Pendiente rework remapeo de texturas.
                    ItemStack caster;
                    if (castIdentification.itemIsCaster(user.getPlayer().getInventory().getItemInMainHand(), user)) {
                        caster = user.getPlayer().getInventory().getItemInMainHand();
                    }
                    else {
                        caster = user.getPlayer().getInventory().getItemInOffHand();
                    }

                    //
                    //ItemMeta meta = caster.getItemMeta();
                    //meta.setCustomModelData(fruit.getCustomModelDataId());
                    //caster.setItemMeta(meta);

                     */
                }
            } else {
                currentHearth = null;
                availableSqueezes = 0;
            }

        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(activeRoom)
            if(roomBlocks.contains(event.getBlock()))
                event.setCancelled(true);
    }

    /* Pendiente de saber si caster en la mano.
    @Override
    public void onUserDamageByEntity(EntityDamageByEntityEvent event){
        World world = event.getDamager().getWorld();
        Player player = null;
        boolean found = false;
        Location auxTeleport = null;

        if(activeRoom){this.
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
    */
    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }
}