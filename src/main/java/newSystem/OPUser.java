package newSystem;

import newSystem.cast.Caster;
import newSystem.events.IEventProcessor;
import newSystem.fruits.DevilFruit;
import newSystem.haki.Haki;
import newSystem.rokushiki.Rokushiki;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OPUser implements IEventProcessor
{
    private final String playerName;
    private final UUID uuid;

    private DevilFruit devilFruit;
    private Haki haki;
    private Rokushiki rokushiki;

    private HashMap<String, Caster> activeCasters;

    public OPUser(UUID uuid, String playerName)
    {
        this.uuid = uuid;
        this.playerName = playerName;

        this.devilFruit = null;
        this.haki = null;
        this.rokushiki = null;

        this.activeCasters = new HashMap<>();
    }

    public String getPlayerName()
    {
        return this.playerName;
    }
    public UUID getUUID()
    {
        return this.uuid;
    }

    public void setDevilFruit(DevilFruit fruit)
    {
        this.devilFruit = fruit;
    }

    public Map<String, Caster> getActiveCasters()
    {
        return this.activeCasters;
    }

    //
    // Procesamiento de eventos
    //

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack eventItem = event.getItem();

        // Pasar el evento al caster activo
        {
            if (eventItem != null)
            {
                /*Caster caster = activeCasters.get(eventItem.getItemMeta().getDisplayName());

                if (Caster.IsCaster(caster, eventItem))
                    caster.onPlayerInteract(event);*/
            }
        }



        // Pasar el evento a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        // Pasar el evento al caster activo

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        // Pasar el evento al caster activo

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    public boolean hasDevilFruit()
    {
        return this.devilFruit != null;
    }

    public DevilFruit getDevilFruit()
    {
        return this.devilFruit;
    }
}
