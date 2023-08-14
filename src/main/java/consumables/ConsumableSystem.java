package consumables;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

// TODO: Pensar si mover aquí todos los comprobadores de consumibles en vez de procesarlos por separado
// TODO: Alternativamente, podríamos hacer a través de una interfaz que se procesen en un array en este sistema
// TODO: y que el método llamado devuelva true si ha sido positivo el check. Esto es una buena optimización :).
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
