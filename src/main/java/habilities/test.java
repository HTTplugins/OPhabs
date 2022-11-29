package habilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.checkerframework.checker.units.qual.Time;

public class test implements Listener {

    final float ExplosionRadius = 4;
    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entidad;
        Block bloque;
        Location loc;

        if((entidad = event.getHitEntity()) != null){
            loc = entidad.getLocation();
            Location delante = loc;

            delante.setX(delante.getBlockX() + 1);

            if(entidad.getWorld().isClearWeather())
                delante.getBlock().setType(Material.FIRE);

            entidad.getWorld().createExplosion(delante, ExplosionRadius);
        }
        else if ((bloque = event.getHitBlock()) != null){
            Location delante1 = bloque.getLocation(), delante2 = bloque.getLocation(),
                     delante3 = bloque.getLocation(), delante4 = bloque.getLocation();

            delante1.setX(delante1.getBlockX() + 1);
            delante2.setX(delante2.getBlockX() - 1);
            delante3.setZ(delante3.getBlockZ() + 1);
            delante4.setZ(delante4.getBlockZ() - 1);

            delante1.getBlock().setType(Material.NETHERRACK);
            delante2.getBlock().setType(Material.NETHERRACK);
            delante3.getBlock().setType(Material.NETHERRACK);
            delante4.getBlock().setType(Material.NETHERRACK);

            bloque.setType(Material.NETHERRACK);

            if(bloque.getWorld().isClearWeather())
                bloque.setType(Material.FIRE);
        }
    }
}
