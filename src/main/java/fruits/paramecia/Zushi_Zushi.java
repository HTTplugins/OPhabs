package fruits.paramecia;

import htt.ophabs.OPhabs;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHLib;
import libs.OPHAnimationLib;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import runnables.OphRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    private static List<Entity> heavyEntity = null;
    private static List<Player> togglePlayer = new ArrayList<>();



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

        world.playSound(playerLoc,"heavygravity",1,1);

        OPHAnimationLib.groundRandomParticleLayer(playerLoc,Particle.SPELL_WITCH,30,10,1,10,100,2,0,0,0);
        OPHAnimationLib.groundRandomParticleLayer(playerLoc,Particle.CRIT_MAGIC,60,10,1.5,10,100,0,0,-0.3,0);

        heavyEntity = player.getNearbyEntities(20,50,20);

        for(Entity ent : heavyEntity)
            if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.AIR))
                ent.setVelocity(new Vector(0,-50,0));

        new OphRunnable(){
            @Override
            public void OphRun() {
                for(Entity ent : heavyEntity){
                    if(!togglePlayer.contains(ent) && ent instanceof Player){
                        Location entiLoc = ent.getLocation();
                        (ent).teleport(new Location(world,entiLoc.getX(),entiLoc.getY(),entiLoc.getZ(),entiLoc.getYaw(),-10));
                        ((Player)ent).setGliding(true);
                        togglePlayer.add((Player) ent);
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
        world.playSound(start,"flymeteor",10,1);

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
        World world = player.getWorld();

        world.playSound(playerLoc,"magneticfield",1,1);

        for(Entity ent : player.getNearbyEntities(attractionRadius,attractionRadius,attractionRadius))
            if(ent instanceof LivingEntity && !player.equals(ent)) {
                OPHLib.attractEntitie(ent, player);
                OPHAnimationLib.groundCircularWave(ent.getLocation(),1,2,0.5,0.35,10,Particle.SPELL_WITCH, 0.4);
            }


    }


    //TODO: Pendiente de modificación (modelo de alfonso) mejora eficiencia y código.
    public void flyRock(Player player){
        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        double x = playerLoc.getX();
        double z = playerLoc.getZ();
        double y = playerLoc.getY();

        world.playSound(playerLoc, "rockmove",1,1);

        player.setAllowFlight(true);

        double yCenter = oldSystem.abilitieSystem.OPHLib.searchGround(x, z, y, world) - 1;
        Location center = new Location(world,x,yCenter, z);
        Material centerMat = center.getBlock().getType();

        double yDownCenter = oldSystem.abilitieSystem.OPHLib.searchGround(x, z, y-1, world) - 1;
        Location downcenter = new Location(world,x,yDownCenter-1, z);
        Material downcenterMat = downcenter.getBlock().getType();

        double yUP = oldSystem.abilitieSystem.OPHLib.searchGround(x+1, z, y, world) - 1;
        Location up = new Location(world,x+1,yUP, z);
        Material upMat = up.getBlock().getType();

        double yDown = oldSystem.abilitieSystem.OPHLib.searchGround(x-1, z, y, world) - 1;
        Location down = new Location(world,x-1,yDown, z);
        Material downMat = down.getBlock().getType();

        double yLeft = oldSystem.abilitieSystem.OPHLib.searchGround(x, z+1, y, world) - 1;
        Location left = new Location(world,x,yLeft, z+1);
        Material leftMat = left.getBlock().getType();

        double yRight = oldSystem.abilitieSystem.OPHLib.searchGround(x, z-1, y, world) - 1;
        Location right = new Location(world,x,yRight, z-1);
        Material rightMat = right.getBlock().getType();

        new OphRunnable(){
            Block previusCenterBlock = null, previusup = null, previusdown= null, previusleft = null, previusRight = null, previusDownCenterBlock = null;
            boolean firstiteration = true;

            int ticks = 0;
            @Override
            public void OphRun() {
                ticks++;

                Block centerBlock = player.getLocation().getBlock().getRelative(0,-1,0);
                Block downcenterBlock = player.getLocation().getBlock().getRelative(0,-2,0);
                Block upBlock = player.getLocation().getBlock().getRelative(1,-1,0);
                Block downBlock = player.getLocation().getBlock().getRelative(-1,-1,0);
                Block leftBlock = player.getLocation().getBlock().getRelative(0,-1,1);
                Block rightBlock = player.getLocation().getBlock().getRelative(0,-1,-1);

                if(!firstiteration){

                    quitBlock(previusCenterBlock,centerBlock);
                    quitBlock(previusup,upBlock);
                    quitBlock(previusdown,downBlock);
                    quitBlock(previusleft,leftBlock);
                    quitBlock(previusRight,rightBlock);
                    quitBlock(previusDownCenterBlock,downcenterBlock);


                    previusCenterBlock.getRelative(0,-1,0).setType(Material.AIR);
                    previusup.getRelative(0,-1,0).setType(Material.AIR);
                    previusdown.getRelative(0,-1,0).setType(Material.AIR);
                    previusleft.getRelative(0,-1,0).setType(Material.AIR);
                    previusRight.getRelative(0,-1,0).setType(Material.AIR);
                    previusDownCenterBlock.getRelative(0,-1,0).setType(Material.AIR);



                } else
                    firstiteration = false;

                if(!player.isSneaking()) {
                    setBlock(centerBlock, centerMat);
                    setBlock(downcenterBlock, downcenterMat);
                    setBlock(upBlock, upMat);
                    setBlock(downBlock, downMat);
                    setBlock(leftBlock, leftMat);
                    setBlock(rightBlock, rightMat);
                } else {

                    centerBlock.setType(Material.AIR);
                    downcenterBlock.setType(Material.AIR);
                    upBlock.setType(Material.AIR);
                    downBlock.setType(Material.AIR);
                    leftBlock.setType(Material.AIR);
                    rightBlock.setType(Material.AIR);

                    centerBlock.getRelative(0,-1,0).setType(centerMat);
                    downcenterBlock.getRelative(0,-1,0).setType(downcenterMat);
                    upBlock.getRelative(0,-1,0).setType(upMat);
                    downBlock.getRelative(0,-1,0).setType(downMat);
                    leftBlock.getRelative(0,-1,0).setType(leftMat);
                    rightBlock.getRelative(0,-1,0).setType(rightMat);


                }

                previusCenterBlock = centerBlock;
                previusDownCenterBlock = downcenterBlock;
                previusup = upBlock;
                previusdown = downBlock;
                previusleft = leftBlock;
                previusRight = rightBlock;


                if(ticks > 500){
                    player.setAllowFlight(false);
                    centerBlock.setType(Material.AIR);
                    downcenterBlock.setType(Material.AIR);
                    upBlock.setType(Material.AIR);
                    downBlock.setType(Material.AIR);
                    leftBlock.setType(Material.AIR);
                    rightBlock.setType(Material.AIR);

                    world.spawnFallingBlock(downcenterBlock.getLocation(),downcenterMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(centerBlock.getLocation(),centerMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(upBlock.getLocation(),upMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(downBlock.getLocation(),downMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(leftBlock.getLocation(),leftMat.createBlockData()).setVelocity(new Vector(0,-2,0));
                    world.spawnFallingBlock(rightBlock.getLocation(),rightMat.createBlockData()).setVelocity(new Vector(0,-2,0));

                    this.cancel();
                }


            }

            public void setBlock(Block block, Material material){
                if(!block.getType().isSolid())
                    block.setType(material);
            }

            public void quitBlock(Block previusBlock, Block block){
                if(previusBlock != block)
                    previusBlock.setType(Material.AIR);
            }
        }.ophRunTaskTimer(0,1);
    }

    @Override
    public void onEntityToggleGlide(EntityToggleGlideEvent event){
        if(togglePlayer.contains((Player)(event.getEntity()))){
            event.setCancelled(true);
        }
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