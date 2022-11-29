package habilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class test implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Location loc = (event.getHitEntity()).getLocation();


        Location delante = event.getHitEntity().getLocation();
        delante.setX(delante.getBlockX() + 1);


        delante.getBlock().setType(Material.FIRE);

    }

}
