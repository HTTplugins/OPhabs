package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import cast.Caster;
import htt.layeredstructures.LayeredStructuresAPI;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import runnables.OphRunnable;
import textures.OPTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Hie_Hie extends Logia{

    public List<Trident> tridents = new ArrayList<>();
    boolean usingDrake = false;
    boolean morphedCaster;

    public static int getFruitID()
    {
        return 1011;
    }

    public Hie_Hie(int id) {
        super(id, "Hie_Hie", "Hie Hie no mi", "Hie_Hie", null);

        // ----- BasicSet ----- //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Ice Age
        basicSet.addAbility(new Ability("Ice Age", this::iceAge));

        //Partisan
        basicSet.addAbility(new Ability("Partisan", this::partisan));

        //Ice drake
        basicSet.addAbility(new Ability("Ice Drake", this::iceDrake));

        //Ice Sword
        basicSet.addAbility(new Ability("Ice Sword", this::morphCaster));

        this.abilitySets.add(basicSet);
    }

    // ----- Abilities ----- //

    public void iceAge(){

        Player player = user.getPlayer();
        Location playerCenter = player.getLocation();

        OPHSoundLib.OphPlaySound(OPHSounds.ICECRACK,player.getLocation(),1,1);

        OPHLib.graduallyChangeLayerMaterials(Material.BLUE_ICE,playerCenter,1.0,0.02,50);
        OPHLib.cageEntities(player,Material.BLUE_ICE,50,50);
    }

    public void partisan() {
        Player player = user.getPlayer();

        OPHSoundLib.OphPlaySound(OPHSounds.ICEPARTISAN, player.getLocation(), 1, 1);

        Location headPos = player.getEyeLocation();
        Vector dir = headPos.getDirection();

        for (int i = -1; i <= 1; i++) {
            Location spawnLocation = headPos.clone().add(dir.clone().rotateAroundY(Math.PI / 4 * i)).add(0, 2, 0);
            Trident trident = (Trident) player.getWorld().spawnEntity(spawnLocation, EntityType.TRIDENT);
            trident.setShooter(player);
            tridents.add(trident);
            trident.setVelocity(dir.clone().multiply(1.5));
        }

        // Particles animation:
        new OphRunnable() {
            @Override
            public void OphRun() {
                for (Trident trident : tridents) {
                    trident.getWorld().spawnParticle(Particle.BLOCK_DUST, trident.getLocation(), 10, Material.BLUE_ICE.createBlockData());
                }

                if (tridents.isEmpty() || getCurrentRunIteration() > 200) {
                    this.cancel();
                }
            }
        }.ophRunTaskTimer(0, 1);
    }


    public void iceDrake(){
        Player player = user.getPlayer();
        usingDrake = true;
        player.getWorld().playSound(player.getLocation(),"icecrack",1,1);
        LayeredStructuresAPI.generatePreChargedLayeredStructure("iceDrake", user.getPlayer().getLocation(), player.getFacing().toString(), 0, 2, false);

        new OphRunnable() {
            @Override
            public void OphRun() {
                usingDrake = false;
            }
        }.ophRunTaskLater(180); //180 = 40 + 140 (breath duration + blocks construction)

        //OPHLib.iceBreath(user,new Vector(0,14,0), new Vector(0,-0.9,0),40,18,140,2,0.7,30,2,2,"icebreathdrake" );

    }

    private void resetCaster(){
        if(user == null) return;
        user.getPlayer().getInventory().forEach(item -> {
            if (item != null) {
                if (item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasCustomModelData()) {
                    Caster caster = this.user.getActiveCasters().get(item.getItemMeta().getCustomModelData());
                    if (caster != null && caster.isOwnedBy(this.user) && caster.isThisItem(item))
                        OPHLib.changeCasterTexture(Hie_Hie.getFruitID(),item);
                }
            }
        });
    }

    public void morphCaster(){
        Player player = user.getPlayer();
        morphedCaster = true;

        ItemStack caster = player.getInventory().getItemInMainHand();

        OPHLib.changeCasterTexture(OPTexture.HIE_HIE_ICE_SWORD,caster);

        ItemMeta meta = caster.getItemMeta();

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);

        caster.setItemMeta(meta);

        // turn to custom model data(1) after 20 seconds
        new OphRunnable(){
            @Override
            public void OphRun() {
                resetCaster();
                morphedCaster = false;
            }
        }.ophRunTaskLater( 400);
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile tridente = event.getEntity();

        if (tridente instanceof Trident) {
            if(tridents.contains(tridente)){
                tridente.remove();
                tridents.remove(tridente);
                Entity hitEnt = event.getHitEntity();

                if(hitEnt != null) {
                    if (hitEnt instanceof LivingEntity) {
                        ((LivingEntity) hitEnt).damage(5, user.getPlayer());
                        OPHLib.cageEntity(hitEnt, Material.BLUE_ICE);
                        hitEnt.setFreezeTicks(hitEnt.getFreezeTicks() + 250);
                    }
                } else Objects.requireNonNull(event.getHitBlock()).setType(Material.BLUE_ICE);
            }
        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(usingDrake){
            World world = event.getPlayer().getWorld();
            double x = event.getFrom().getX();
            double y = event.getFrom().getY();
            double z = event.getFrom().getZ();
            float pitch = Objects.requireNonNull(event.getTo()).getPitch();
            float yaw = event.getTo().getYaw();
            event.setTo(new Location(world,x,y,z,yaw,pitch));
        }
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(morphedCaster){
            Entity hitEnt = event.getEntity();

            ItemStack item = ((Player) event.getDamager()).getInventory().getItemInMainHand();

            if (item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasCustomModelData()) {

                Caster caster = this.user.getActiveCasters().get(item.getItemMeta().getCustomModelData());
                if (caster != null && caster.isOwnedBy(this.user) && caster.isThisItem(item)) {
                    event.getEntity().setFreezeTicks(hitEnt.getFreezeTicks() + 80);
                }
            }
        }

    }

    @Override
    public void onEntityDamage(EntityDamageEvent event){

        if(event.getCause().equals(EntityDamageEvent.DamageCause.FREEZE))
            event.setCancelled(true);

    }

    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }

    @Override
    protected void logiaModeON() {

    }
}
