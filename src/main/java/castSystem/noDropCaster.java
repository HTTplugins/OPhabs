package castSystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;


import java.util.List;

public class noDropCaster implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        for(ItemStack drop : event.getDrops())
            if(castIdentification.itemIsCaster(drop))
                    drop.setAmount(0);

    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(castIdentification.itemIsCaster(event.getItemDrop().getItemStack()))
            event.setCancelled(true);
    }

}
