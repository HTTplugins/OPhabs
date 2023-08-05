package newSystem;

import newSystem.cast.Caster;
import newSystem.fruits.DevilFruit;
import newSystem.haki.Haki;
import newSystem.rokushiki.Rokushiki;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class OPUser
{
    private final String playerName;

    private DevilFruit devilFruit;
    private Haki haki;
    private Rokushiki rokushiki;

    private HashMap<String, Caster> activeCasters;

    public OPUser(String playerName)
    {
        this.playerName = playerName;

        this.devilFruit = null;
        this.haki = null;
        this.rokushiki = null;

        this.activeCasters = new HashMap<>();
    }

    public void setDevilFruit(DevilFruit fruit)
    {
        this.devilFruit = fruit;
    }

    //
    // Procesamiento de eventos
    //

    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack eventItem = event.getItem();

        // Pasar el evento al caster activo
        {
            if (eventItem != null)
            {
                Caster caster = activeCasters.get(eventItem.getItemMeta().getDisplayName());

                if (Caster.IsCaster(caster, eventItem))
                    caster.onPlayerInteract(event);
            }
        }



        // Pasar el evento a todos los casters pasivos

        // Procesar el evento como usuario
    }

    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        // Pasar el evento al caster activo

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        // Pasar el evento al caster activo

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }
}
