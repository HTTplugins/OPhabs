package libs;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import runnables.OphRunnable;

import java.util.Random;

import static java.lang.Math.*;

public class OPHAnimationLib {

    private static final Random random = new Random();


    public static void spawnYawDependingParticle(Player player, double yaw, double height){
        Location pBehind = OPHLib.getBack(player,yaw,height);
        player.getWorld().spawnParticle(Particle.REDSTONE,pBehind,0,0,0,0);
    }
    public static void spawnYawDependingParticle(Player player, double yaw, double height, Particle.DustOptions dustOptions){
        Location pBehind = OPHLib.getBack(player,yaw,height);
        player.getWorld().spawnParticle(Particle.REDSTONE,pBehind,0,0,0,0,dustOptions);
    }
    public static void horizontalCircle(Location center, double radius,double particleDistance, Particle particle,Particle.DustOptions dustOptions){
        double x,z;
        World world = center.getWorld();
        for(double i=0; i<2*PI; i+=particleDistance){
            x = radius * cos(i);
            z = radius * sin(i);

            center.add(x,0,z);
            world.spawnParticle(particle,center,0,0,0,0,dustOptions);
            center.subtract(x,0,z);
        }
    }

    //if particle count==0, we are using speed particles.
    public static void groundRandomParticleLayer(Location center, Particle particle, int density, double xDistance, double yDistance, double zDistance, int iterations, int particleCount,double speedX, double speedY, double speedZ ) {
        new OphRunnable() {
            final World world = center.getWorld();

            final double
                    centerX = center.getX(),
                    centerY = center.getY(),
                    centerZ = center.getZ();
            final double
                    xMax = xDistance * 2,
                    xMin = xDistance;
            final double
                    zMax = zDistance * 2,
                    zMin = zDistance;
            double x, z;

            @Override
            public void OphRun() {
                for (int i = 0; i < density; i++) {
                    x = (random.nextDouble() * xMax) - xMin + centerX;
                    z = (random.nextDouble() * zMax) - zMin + centerZ;
                    double y = OPHLib.searchGround(x, z, centerY, world) + yDistance;

                    if (particleCount == 0) {
                        world.spawnParticle(particle, new Location(world, x, y, z), 0, speedX, speedY, speedZ);
                    } else {
                        world.spawnParticle(particle, new Location(world, x, y, z), particleCount);
                    }
                }

                if (getCurrentRunIteration() == iterations) ophCancel();
            }
        }.ophRunTaskTimer(0, 1);
    }

    public static void groundCircularWave(Location center, double initialRadius, double maxRadius, double radiusIncrement, double density,double densityIncrement , Particle particle, double overGroundParticleHeight){
        final double radius = initialRadius;

        new OphRunnable(){
            double incRadius = radius;
            double incDensity = density;

            @Override
            public void OphRun() {
                System.out.println(incRadius);
                if (incRadius > maxRadius) this.cancel();
                groundCircle(incRadius,center,incDensity, particle, overGroundParticleHeight);
                incRadius += radiusIncrement;
                incDensity =  density - incRadius/densityIncrement;


            }
        }.ophRunTaskTimer(0,1);
    }

    // less particle Distance -> more density
    public static void groundCircle(double radius, Location center, double particleDistance, Particle particle, double overGroundParticleHeight){
        World world = center.getWorld();

        for(double i=0; i<2*PI; i+=particleDistance){
            double xParticle = radius * Math.cos(i) + center.getX();
            double zParticle = radius * Math.sin(i) + center.getZ();
            double yParticle = OPHLib.searchGround(xParticle,zParticle, center.getY(), world) + overGroundParticleHeight;

            Location particleLocation = new Location(world,xParticle,yParticle + 1,zParticle);

            world.spawnParticle(particle,particleLocation,0,0,0,0);
        }

    }
}