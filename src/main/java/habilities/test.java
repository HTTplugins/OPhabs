package habilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class test implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entidad = event.getHitEntity();
        Location loc = entidad.getLocation();

        Location delante = loc;
        delante.setX(delante.getBlockX() + 2);
        delante.setY(delante.getBlockY() - 1);

        delante.getBlock().setType(Material.FIRE);
    }
}
