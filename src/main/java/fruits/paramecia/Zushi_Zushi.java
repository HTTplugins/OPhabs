package fruits.paramecia;

import cosmetics.cosmeticsArmor;
import htt.ophabs.OPhabs;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHLib;
import libs.OPHAnimationLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import runnables.OphRunnable;

import java.util.*;


public class Zushi_Zushi extends Paramecia
{

    private static final List<Material> meteorsMaterials = Arrays.asList(
            Material.COBBLESTONE,
            Material.MAGMA_BLOCK,
            Material.NETHERRACK
    );

    private final Particle gravityParticle = Particle.REDSTONE;
    private final Particle.DustOptions purple = new Particle.DustOptions(Color.PURPLE,1.0F);

    private boolean meteorExploded = false;
    private List<Entity> heavyEntity = new ArrayList<>();

    private final Set<Player> togglePlayer = new HashSet<>();

    public static int getFruitID()
    {
        return 1002;
    }

    public Zushi_Zushi(int id){
        super(id, "Zushi_Zushi", "Zushi zushi no Mi", "Zushi_Zushi");

        AbilitySet basicSet = new AbilitySet("Base Set");

        // Heavy Field
        basicSet.addAbility(new CooldownAbility("Heavy Field", 5, () -> {
            Player player = user.getPlayer();
            this.heavyGravityField(player);
        }));

        // Meteor
        basicSet.addAbility(new CooldownAbility("Meteor", 5, () -> {
            Player player = user.getPlayer();
            this.meteor(player);
        }));

        // Attraction
        basicSet.addAbility(new CooldownAbility("Atracction", 5, () -> {
            Player player = user.getPlayer();
            this.attraction(player);
        }));

        // FlyRock
        basicSet.addAbility(new CooldownAbility("FlyRock", 5, () -> {
            Player player = user.getPlayer();
            this.flyRock(player);
        }));

        this.abilitySets.add(basicSet);
    }

    public void heavyGravityField(Player player){
        int duration = 100;
        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        OPHSoundLib.OphPlaySound(OPHSounds.HEAVYGRAVITY,playerLoc,1,1);

        OPHAnimationLib.groundRandomParticleLayer(playerLoc,Particle.SPELL_WITCH,30,10,1,10,100,2,0,0,0);
        OPHAnimationLib.groundRandomParticleLayer(playerLoc,Particle.CRIT_MAGIC,60,10,1.5,10,100,0,0,-0.3,0);



        new OphRunnable(){
            @Override
            public void OphRun() {
                heavyEntity = player.getNearbyEntities(20,50,20);

                for(Entity ent : heavyEntity)
                    if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR))
                        ent.setVelocity(new Vector(0,-50,0));

                for(Entity ent : heavyEntity){
                    if(ent instanceof Player){
                        Location entiLoc = ent.getLocation();
                        if (!togglePlayer.contains(ent)) {
                            (ent).teleport(new Location(world,entiLoc.getX(),entiLoc.getY(),entiLoc.getZ(),entiLoc.getYaw(),-10));
                            //((Player)ent).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,10,-10));
                            ((Player)ent).setGliding(true);
                            togglePlayer.add((Player) ent);
                        } else
                            (ent).teleport(new Location(world,entiLoc.getX(),entiLoc.getY(),entiLoc.getZ(),entiLoc.getYaw(),-10));


                    } else if(ent instanceof LivingEntity)
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10,10));
                }

                if(getCurrentRunIteration() == duration){
                    togglePlayer.clear();
                    heavyEntity.clear();
                    this.ophCancel();
                }
            }
        }.ophRunTaskTimer(0,1);
    }

    public void meteor(Player player){
        Location targetPoint = OPHLib.getTargetBlock(user.getPlayer(), 40);

        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        new OphRunnable(){
            @Override
            public void OphRun() {
                world.playSound(playerLoc,"magneticfield",1,-1);


            }
        }.ophRunTaskLater(10);

        new OphRunnable(191){
            final double radius = 1.5;
            double height = 2;
            final Location playerLoc = player.getLocation();
            final int chargeCircleFinalTick = 100, launchMeteoreTick = 155;

            @Override
            public void OphRun() {
                if(getCurrentRunIteration() < chargeCircleFinalTick)
                    OPHAnimationLib.horizontalCircle(playerLoc.clone().add(0,height,0),radius,0.1,gravityParticle,purple);
                else if(getCurrentRunIteration() % 2 == 0){
                    OPHAnimationLib.horizontalCircle(playerLoc.clone().add(0,height+0.5,0),radius,0.1,gravityParticle,purple);
                    OPHAnimationLib.horizontalCircle(playerLoc.clone().add(0,height+1,0),radius,0.1,gravityParticle,purple);
                    height++;
                }

                if(getCurrentRunIteration() == launchMeteoreTick)
                    launchMeteor(playerLoc.clone().add(0,50,0),targetPoint,5,15);
            }

        }.ophRunTaskTimer(0,1);
    }

    public void launchMeteor(Location start, Location end, int radius, int explosionForce) {
        World world = start.getWorld();
        meteorExploded = false;
        OPHSoundLib.OphPlaySound(OPHSounds.FLYMETEOR,start,10,1);

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++)
                    if (x * x + y * y + z * z <= 25) {
                        Location blockLocation = start.clone().add(x, y, z);
                        Material material = meteorsMaterials.get(OPHLib.getRandom().nextInt(meteorsMaterials.size()));
                        FallingBlock fallingBlock = world.spawnFallingBlock(blockLocation,material.createBlockData());
                        Vector vector = end.toVector().subtract(blockLocation.toVector());
                        fallingBlock.setVelocity(vector.multiply(0.025));
                        fallingBlock.setDropItem(false);

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if(meteorExploded) this.cancel();

                                if (fallingBlock.isDead() && !meteorExploded) {
                                    world.createExplosion(fallingBlock.getLocation(), explosionForce);
                                    meteorExploded = true;
                                }
                            }
                        }.runTaskTimer(OPhabs.getInstance(),0,1);
                    }
    }

    public void attraction(Player player){
        final int attractionRadius = 30;
        Location playerLoc = player.getLocation();

        OPHSoundLib.OphPlaySound(OPHSounds.MAGNETICFIELD,playerLoc,1,1);

        for(Entity ent : player.getNearbyEntities(attractionRadius,attractionRadius,attractionRadius))
            if(ent instanceof LivingEntity && !player.equals(ent)) {
                OPHLib.attractEntitie(ent, player);
                OPHAnimationLib.groundCircularWave(ent.getLocation(),1,2,0.5,0.35,10,Particle.SPELL_WITCH, 0.4);
            }


    }

    public void flyRock(Player player){
        cosmeticsArmor.summonCosmeticArmor(1,"flying_rock" ,player,Material.PUMPKIN );
        player.setAllowFlight(true);
        OPHSoundLib.OphPlaySound(OPHSounds.ROCKMOVE,player.getLocation(),1,1);

        new OphRunnable(){
            @Override
            public void OphRun() {
                cosmeticsArmor.killCosmeticArmor(player,"flying_rock");
                player.setAllowFlight(false);
            }
        }.ophRunTaskLater(300);
    }

    @Override
    public void onEntityToggleGlide(EntityToggleGlideEvent event){
        if(this.user == null)
            return;

        if(togglePlayer.contains((Player)(event.getEntity())))
            event.setCancelled(true);

    }

    @Override
    protected void onAddFruit()
    {

    }

    @Override
    protected void onRemoveFruit()
    {

    }

}