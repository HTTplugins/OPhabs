package newSystem;

import newSystem.cast.Caster;
import newSystem.events.IEventProcessor;
import newSystem.fruits.DevilFruit;
import newSystem.haki.Haki;
import newSystem.rokushiki.Rokushiki;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


// TODO: Añadir estadísticas de daño, etc

public class OPUser implements IEventProcessor
{
    private final String playerName;
    private final UUID uuid;

    private DevilFruit devilFruit;
    private Haki haki;
    private Rokushiki rokushiki;

    private final HashMap<Integer, Caster> activeCasters;

    public OPUser(UUID uuid, String playerName)
    {
        this.uuid = uuid;
        this.playerName = playerName;

        this.devilFruit = null;
        this.haki = null;
        this.rokushiki = null;

        this.activeCasters = new HashMap<>();
    }

    // TODO: Si en el futuro decidimos optimizar y almacenar players directamente, hacerlo en este método
    public Player getPlayer()
    {
        return Bukkit.getPlayer(this.uuid);
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

    public Map<Integer, Caster> getActiveCasters()
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
                if (eventItem.hasItemMeta() && eventItem.getItemMeta().hasCustomModelData())
                {
                    Caster caster = activeCasters.get(eventItem.getItemMeta().getCustomModelData());

                    if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(eventItem))
                    {
                        caster.onPlayerInteract(event);
                    }
                }
            }
        }

        // Pasar el evento a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        ItemStack eventItem = event.getItemDrop().getItemStack();

        // Pasar el evento al caster activo
        {
            if (eventItem.hasItemMeta() && eventItem.getItemMeta().hasCustomModelData())
            {
                Caster caster = activeCasters.get(eventItem.getItemMeta().getCustomModelData());

                if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(eventItem))
                {
                    caster.onPlayerDropItem(event);
                }
            }
        }

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        ItemStack mainHandItem = event.getMainHandItem();
        ItemStack offHandItem = event.getOffHandItem();

        // Pasar el evento al caster activo
        {
            if (mainHandItem != null && mainHandItem.hasItemMeta() && mainHandItem.getItemMeta().hasCustomModelData())
            {
                Caster caster = activeCasters.get(mainHandItem.getItemMeta().getCustomModelData());

                if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(mainHandItem))
                    caster.onPlayerSwapHandItems(event);
            }

            if (offHandItem != null && offHandItem.hasItemMeta() && offHandItem.getItemMeta().hasCustomModelData())
            {
                Caster caster = activeCasters.get(offHandItem.getItemMeta().getCustomModelData());

                if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(offHandItem))
                    caster.onPlayerSwapHandItems(event);
            }
        }

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onFall(EntityDamageEvent event)
    {
        if (this.devilFruit != null)
            this.devilFruit.onFall(event);
    }

    public boolean hasDevilFruit()
    {
        return this.devilFruit != null;
    }

    public DevilFruit getDevilFruit()
    {
        return this.devilFruit;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OPUser otherUser = (OPUser) o;
        return this.uuid == otherUser.uuid;
    }
}
