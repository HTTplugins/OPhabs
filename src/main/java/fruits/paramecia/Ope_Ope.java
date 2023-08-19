package fruits.paramecia;


import abilities.AbilitySet;
import abilities.CooldownAbility;
import cast.Caster;
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
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import runnables.OphRunnable;
import textures.OPTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Ope_Ope extends Paramecia {

    private List<Block> roomBlocks;
    private Location roomcenter;
    private boolean activeRoom;
    private final int roomTime = 400;

    private LivingEntity currentHeart;
    private UUID currentHeartUUID;
    private int availableSqueezes;

    public static int getFruitID()
    {
        return 1003;
    }

    public Ope_Ope(int id) {
        super(id, "Ope_Ope", "Ope ope no Mi", "Ope_Ope");

        roomcenter = null;
        activeRoom = false;
        roomBlocks = new ArrayList<>();
        currentHeart = null;
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

        // StealHeart
        basicSet.addAbility(new CooldownAbility("StealHeart", 5, () -> {
            Player player = user.getPlayer();
            this.stealHeart(player);
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
    public void stealHeart(Player player){
        if(!player.isSneaking()){
            if(activeRoom){
                currentHeart = OPHLib.rayCastLivEnt(player,2);
                if (currentHeart != null) {
                    if(currentHeart instanceof Player)
                        currentHeartUUID = (currentHeart).getUniqueId();

                    availableSqueezes = 5;
                    OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
                    OPHLib.spawnBloodParticles(currentHeart);
                    msgSystem.OphPlayerMsg(player,"You have stolen " + currentHeart.getName() + "'s ❤.");

                    OPHLib.changeCasterTexture(OPTexture.OPE_OPE_STOLEN_HEART,player.getInventory().getItemInMainHand());
                }
            } else
                msgSystem.OphPlayerError(player,"You must have an active room to use this ability.");
        } else {
            if(currentHeart == null) {
                msgSystem.OphPlayerError(player, "You must have a heart to squeeze.");
                return;
            }

            if(currentHeart instanceof Player){
                if(!Bukkit.getServer().getOnlinePlayers().contains((Player) currentHeart)){
                    msgSystem.OphPlayerError(player, "The player is not online");
                    return;
                }
            }

            availableSqueezes--;

            currentHeart.damage(2, user.getPlayer());

            OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, player.getLocation(), 1, 1);
            OPHSoundLib.OphPlaySound(OPHSounds.SWORDCUT, currentHeart.getLocation(), 1, 1);

            currentHeart.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, currentHeart.getLocation().add(0, currentHeart.getHeight(),0),4);

            if(availableSqueezes == 0) resetCaster();
        }
    }

    private void resetCaster(){
        currentHeart = null;
        currentHeartUUID = null;
        availableSqueezes = 0;

        if(user == null) return;
        user.getPlayer().getInventory().forEach(item -> {
            if (item != null) {
                if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                    Caster caster = this.user.getActiveCasters().get(item.getItemMeta().getCustomModelData());
                    if (caster != null && caster.isOwnedBy(this.user) && caster.isThisItem(item))
                        OPHLib.changeCasterTexture(Ope_Ope.getFruitID(),item);
                }
            }
        });
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(activeRoom)
            if(roomBlocks.contains(event.getBlock()))
                event.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(currentHeart == null) resetCaster();
        else if(currentHeartUUID != null)
            currentHeart = Bukkit.getPlayer(currentHeartUUID);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity().equals(currentHeart)){
            resetCaster();
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        World world = event.getDamager().getWorld();
        Player player = null;
        boolean found = false;
        Location auxTeleport = null;

        if(activeRoom){
            if(event.getEntity() instanceof Player){
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

    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }
}