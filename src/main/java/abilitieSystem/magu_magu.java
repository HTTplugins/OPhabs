package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class magu_magu extends logia {

    public static Particle.DustOptions maguOrange = new Particle.DustOptions(Color.ORANGE,1.0F);
    public static Particle.DustOptions maguBlack = new Particle.DustOptions(Color.BLACK,1.0F);
    public static Particle.DustOptions maguRed = new Particle.DustOptions(Color.RED,1.0F);

    public Random rand = new Random();

    public magu_magu(OPhabs plugin){
        super(plugin, Particle.FLAME, castIdentification.castMaterialMagu, castIdentification.castItemNameMagu, fruitIdentification.fruitCommandNameMagu); 
        abilitiesNames.add("Lava Meteorites");
        abilitiesCD.add(0);
        abilitiesNames.add("Lava Ground");
        abilitiesCD.add(0);
        this.runParticles();

    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            lavaMeteorites(user.getPlayer());
            abilitiesCD.set(0, 60); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            lavaGround(user.getPlayer());
            abilitiesCD.set(1, 0); // Pon el cooldown en segundos
        }
    }

    public void lavaMeteorites(Player player){
        final int Maximum = 3;
        int index = 0;

        List<Entity> entityList = player.getNearbyEntities(15,10,15);

        List<LivingEntity> Players = new ArrayList<>();
        List<LivingEntity> livingEntities = new ArrayList<>();

        for(Entity ent : entityList){
            if(ent instanceof Player)
                Players.add((LivingEntity) ent);
            else if(ent instanceof LivingEntity)
                livingEntities.add((LivingEntity) ent);
        }

        for(int i=0; i<Players.size() && index < Maximum; i++){
            sendMeteorite(player,Players.get(i));
            index++;
        }

        for(int i=0; i<livingEntities.size() && index < Maximum; i++){
            sendMeteorite(player,livingEntities.get(i));
            index++;
        }

    }

    public void sendMeteorite(Player player, LivingEntity ent){
        Location playerLoc = player.getLocation();
        Location upHead = playerLoc.add(0,7,0);
        World world = player.getWorld();

        new BukkitRunnable(){
            double x,y,z, xr,yr,zr;
            final double tenPI = 10*PI;
            double angle, modAngle;
            Location spawnBallPos;
            final Vector direction = new Vector(0,0,0);
            int numplarticles = 0;

            boolean reachTarget = false;

            @Override
            public void run() {
                numplarticles = 0;
                Vector vec = new Vector(ent.getLocation().getX() - upHead.getX(), ent.getLocation().getY() - upHead.getY(), ent.getLocation().getZ() - upHead.getZ());
                vec.normalize();

                double sumX = vec.getX();
                double sumY = vec.getY();
                double sumZ = vec.getZ();

                for (double i = 0; i < 2*PI*4; i+=0.05){
                    angle = i/10*PI;
                    modAngle = (i%10)*PI;
                    x = sin(angle);
                    y = sin(modAngle) * cos(angle);
                    z = cos(modAngle) * cos(angle);

                    xr = upHead.getX() + x ;
                    yr = upHead.getY() + y ;
                    zr = upHead.getZ() + z ;

                    spawnBallPos = new Location(world, xr,yr,zr);

                    spawnBallPos.add(direction);

                    int option = rand.nextInt(3);

                    Particle.DustOptions maguDO = new Particle.DustOptions(Color.PURPLE,1.0F);;

                    switch (option){
                        case 0: maguDO = maguOrange; break;
                        case 1: maguDO = maguBlack; break;
                        case 2: maguDO = maguRed; break;
                    }

                    if(spawnBallPos.getBlock().getType().equals(Material.AIR)) {
                        world.spawnParticle(Particle.REDSTONE, spawnBallPos, 0, 0, 0, 0,maguDO);
                        numplarticles++;
                    }

                }

                direction.add(new Vector(sumX,sumY,sumZ));

                for(Entity ent : world.getNearbyEntities(spawnBallPos,2,2,2))
                    if(ent instanceof LivingEntity && !player.equals(ent)){
                        ((LivingEntity)ent).damage(2);
                        reachTarget = true;
                    }

                if(numplarticles < 25 || reachTarget){
                    world.createExplosion(ent.getLocation(),6);
                    ent.getLocation().getBlock().setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(1,0,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(1,0,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(0,1,-1).setType(Material.LAVA);
                    ent.getLocation().getBlock().getRelative(-2,-1,0).setType(Material.LAVA);
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin,0,1);

    }

    public void lavaGround(Player player){
        if(player.isSneaking()){
            lavaRiver(player);
        } else {
            lavaLake(player);
        }
    }

    public void lavaRiver(Player player){
        new BukkitRunnable(){
            final Vector facingDirection = player.getLocation().getDirection();
            int index = 2;
            double randomQuote = 1;

            Location playerLoc = player.getLocation();
            @Override
            public void run() {

                int x = (int)(facingDirection.getX()*index);
                int y = 0;
                int z = (int)(facingDirection.getZ()*index);

                System.out.println(x + " " + y + " "+ z);

                if(x == 0){
                    System.out.println("A");
                    putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                    putLavaBlock(x-1,y,z,randomQuote,playerLoc);
                } else if( z == 0){
                    System.out.println("B");
                    putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                    putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                } else {
                    if(x > 0 && z > 0){
                        System.out.println("C");
                        putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                    } else if(x > 0 && z < 0){
                        System.out.println("D");
                        putLavaBlock(x+1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                    } else if(x<0 && z > 0){
                        System.out.println("E");
                        putLavaBlock(x,y,z+1,randomQuote,playerLoc);
                        putLavaBlock(x-1,y,z,randomQuote,playerLoc);

                    } else if( x < 0 && z < 0) {
                        System.out.println("F");
                        putLavaBlock(x-1,y,z,randomQuote,playerLoc);
                        putLavaBlock(x,y,z-1,randomQuote,playerLoc);
                    }

                }




                putLavaBlock(x,y,z,randomQuote,playerLoc);

                index++;
                randomQuote-=0.02;

                if(index==12) this.cancel();

            }
        }.runTaskTimer(plugin,0,3);
    }

    public void lavaLake(Player player ){

        new BukkitRunnable(){
                int index = 1;

                final Location playerLoc = player.getLocation();

                double randomQuote = 1;

                @Override
                public void run() {

                    for(double i=-index; i <= index; i++ ){
                        putLavaBlock((int)i,0,-index,randomQuote,playerLoc);
                        putLavaBlock((int)i,0,index,randomQuote,playerLoc);
                    }

                    for(double i=-index; i <= index; i++ ){
                        putLavaBlock(-index,0,(int)i,randomQuote,playerLoc);
                        putLavaBlock(index,0,(int)i,randomQuote,playerLoc);
                    }
                    randomQuote-=0.1;
                    index++;

                    if(index == 5) this.cancel();
                }


            }.runTaskTimer(plugin,0,5);


    }

    public void putLavaBlock(int x,int y, int z, double randomQuote, Location playerLoc){
        Random Rand = new Random();
        double aleatory = rand.nextDouble();
        double randMagmaBlock = rand.nextDouble();

        Material putBlock = Material.LAVA;

        if(randMagmaBlock < 0.1) putBlock = Material.MAGMA_BLOCK;

        if(airOrSimilar(playerLoc.getBlock().getRelative(x,y,z).getType()) && aleatory < randomQuote){
            if(!playerLoc.getBlock().getRelative(x,y-1,z).getType().equals(Material.AIR)) playerLoc.getBlock().getRelative(x,y-1,z).setType(putBlock);
        } else if(airOrSimilar(playerLoc.getBlock().getRelative(x,y+1,z).getType()) && aleatory < randomQuote){
            if(!playerLoc.getBlock().getRelative(x,y,z).getType().equals(Material.AIR)) playerLoc.getBlock().getRelative(x,y,z).setType(putBlock);
        }
    }

    public boolean airOrSimilar(Material mat){
        if(mat.equals(Material.AIR)
                || mat.equals(Material.DEAD_BUSH)
                || mat.equals(Material.POTTED_AZALEA_BUSH)
                || mat.equals(Material.ROSE_BUSH)
                || mat.equals(Material.SWEET_BERRY_BUSH)
                || mat.equals(Material.POTTED_DEAD_BUSH)
                || mat.equals(Material.POTTED_FLOWERING_AZALEA_BUSH)
                || mat.equals(Material.BROWN_MUSHROOM)
                || mat.equals(Material.RED_MUSHROOM)
                || mat.equals(Material.DANDELION)
                || mat.equals(Material.POPPY)
                || mat.equals(Material.BLUE_ORCHID)
                || mat.equals(Material.ALLIUM)
                || mat.equals(Material.AZURE_BLUET)
                || mat.equals(Material.RED_TULIP)
                || mat.equals(Material.ORANGE_TULIP)
                || mat.equals(Material.WHITE_TULIP)
                || mat.equals(Material.PINK_TULIP)
                || mat.equals(Material.OXEYE_DAISY)
                || mat.equals(Material.SUNFLOWER)
                || mat.equals(Material.LILAC)
                || mat.equals(Material.PEONY)
                || mat.equals(Material.TALL_GRASS)
                || mat.equals(Material.GRASS)
                || mat.equals(Material.LARGE_FERN)
                || mat.equals(Material.CORNFLOWER)
                || mat.equals(Material.LILY_OF_THE_VALLEY)
                || mat.equals(Material.WITHER_ROSE)
                || mat.equals(Material.CRIMSON_FUNGUS)
                || mat.equals(Material.WARPED_FUNGUS)
                || mat.equals(Material.CRIMSON_ROOTS)
                || mat.equals(Material.WARPED_ROOTS)
                || mat.equals(Material.NETHER_SPROUTS)
                || mat.equals(Material.AZALEA)
                || mat.equals(Material.FLOWERING_AZALEA)
                || mat.equals(Material.SPORE_BLOSSOM)
        )return true;
        else return false;
    }


    @Override
    public void runParticles() {

        new BukkitRunnable(){

            int ticks = 0;

            Random random = new Random();
            boolean lavaAmbiance = false;
            @Override
            public void run() {

                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null){
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null){
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameMagu)){
                            double xdecimals = random.nextDouble();
                            double zdecimals = random.nextDouble();

                            double x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                            double z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(element,player.getLocation().add(x,1,z),0,0,0,0);

                             xdecimals = random.nextDouble();
                             zdecimals = random.nextDouble();

                             x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                             z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(Particle.LAVA,player.getLocation().add(x,1,z),0,0,0,0);



                            if (!lavaAmbiance){
                                lavaAmbiance = true;
                                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_AMBIENT,100,1);

                                double r = random.nextDouble();

                                if(r < 0.5)
                                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP,100,1);
                            }

                            player.setAllowFlight(true);


                        }else {
                            lavaAmbiance = false;

                            player.setAllowFlight(false);
                            player.setFlying(false);
                        }
                    }

                if(ticks == 80){
                    lavaAmbiance = false;
                    ticks = 0;
                }

                ticks++;

            }

        }.runTaskTimer(plugin,0,2);

    }
}

