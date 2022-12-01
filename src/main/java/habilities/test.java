package habilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.checkerframework.checker.units.qual.Time;

public class test implements Listener {

    final float ExplosionRadius = 4;
    final Material NETHERRACK = Material.NETHERRACK;
    final Material FIRE = Material.FIRE;
    final Material DIRT = Material.DIRT;
    final Material GREEN_DIRT = Material.GRASS_BLOCK;

    public boolean isDirt(Block bloque){
        boolean esTierra = false;

        if((bloque.getType() == DIRT) || (bloque.getType() == GREEN_DIRT))
            esTierra = true;

        return esTierra;
    }
    public void createNetherrackEffect(Block bloque){
        Location delante1 = bloque.getLocation(), delante2 = bloque.getLocation(),
                 delante3 = bloque.getLocation(), delante4 = bloque.getLocation(),
                 delante5 = bloque.getLocation(), delante6 = bloque.getLocation(),
                 delante7 = bloque.getLocation(), delante8 = bloque.getLocation();

        delante1.setX(delante1.getBlockX() + 1);
        delante2.setX(delante2.getBlockX() - 1);
        delante3.setZ(delante3.getBlockZ() + 1);
        delante4.setZ(delante4.getBlockZ() - 1);

        delante5.setX(delante5.getBlockX() + 1);    delante5.setZ(delante5.getBlockZ() + 1);
        delante6.setX(delante6.getBlockX() + 1);    delante6.setZ(delante6.getBlockZ() - 1);
        delante7.setX(delante7.getBlockX() - 1);    delante7.setZ(delante7.getBlockZ() + 1);
        delante8.setX(delante8.getBlockX() - 1);    delante8.setZ(delante8.getBlockZ() - 1);

        if(isDirt(delante1.getBlock()))
            delante1.getBlock().setType(NETHERRACK);

        if(isDirt(delante2.getBlock()))
            delante2.getBlock().setType(NETHERRACK);

        if(isDirt(delante3.getBlock()))
            delante3.getBlock().setType(NETHERRACK);

        if(isDirt(delante4.getBlock()))
            delante4.getBlock().setType(NETHERRACK);

        if(isDirt(delante5.getBlock()))
            delante5.getBlock().setType(NETHERRACK);

        if(isDirt(delante6.getBlock()))
            delante6.getBlock().setType(NETHERRACK);

        if(isDirt(delante7.getBlock()))
            delante7.getBlock().setType(NETHERRACK);

        if(isDirt(delante8.getBlock()))
            delante8.getBlock().setType(NETHERRACK);
    }
    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event){
        Entity entidad;
        Block bloque;
        Location loc;

        if((entidad = event.getHitEntity()) != null){
            loc = entidad.getLocation();
            Location delante = loc;

            delante.setX(delante.getBlockX() + 1);

            if(entidad.getWorld().isClearWeather())
                delante.getBlock().setType(FIRE);
        }
        else if ((bloque = event.getHitBlock()) != null){
            if(bloque.getType() != NETHERRACK){
                createNetherrackEffect(bloque);
                if (bloque.getWorld().isClearWeather())
                    bloque.setType(FIRE);
            }
            else
                bloque.getWorld().createExplosion(bloque.getLocation(), ExplosionRadius);
        }

        event.getEntity().remove();
    }
}