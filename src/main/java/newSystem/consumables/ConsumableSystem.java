package newSystem.consumables;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ConsumableSystem implements Listener
{
    @EventHandler
    public void onPlayerItemCosume(PlayerItemConsumeEvent event)
    {
        ItemStack consumedItem = event.getItem();

        // Comprobar si es una fruta


        // Comprobar si es otro tipo ...
    }
}
