package users;

import cast.Caster;
import events.IEventProcessor;
import fruits.DevilFruit;
import haki.Haki;
import rokushiki.Rokushiki;
import stats.UserStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OPUser implements IEventProcessor
{
    private final String playerName;
    private final UUID uuid;
    private final UserStats stats;

    private Boolean fastCasting=false;

    private DevilFruit devilFruit;
    private Haki haki;
    private Rokushiki rokushiki;

    private final HashMap<Integer, Caster> activeCasters;

    public OPUser(UUID uuid, String playerName)
    {
        this.uuid = uuid;
        this.playerName = playerName;
        this.stats = new UserStats(this);

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
    public UserStats getStats()
    {
        return this.stats;
    }

    public void setDevilFruit(DevilFruit fruit)
    {
        this.devilFruit = fruit;
    }

    public Map<Integer, Caster> getActiveCasters()
    {
        return this.activeCasters;
    }

    public Boolean isFastCasting(){
        return fastCasting;
    }


    public void toggleFastCasting(){
        fastCasting = !fastCasting;
    }

    //
    // Procesamiento de eventos
    //

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack eventItem = event.getItem();

        // xPasar el evento al caster activo
        {
            if (eventItem != null && !fastCasting)
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


        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }


    @Override
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
    {
        ItemStack mainHandItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        ItemStack offHandItem = event.getPlayer().getInventory().getItemInOffHand();

        // Pasar el evento al caster activo
        if (mainHandItem != null && mainHandItem.hasItemMeta() && mainHandItem.getItemMeta().hasCustomModelData())
        {
            Caster caster = activeCasters.get(mainHandItem.getItemMeta().getCustomModelData());

            if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(mainHandItem)) {
                caster.onPlayerItemHeldEvent(event);
            }

        }
        else
        if (offHandItem != null && offHandItem.hasItemMeta() && offHandItem.getItemMeta().hasCustomModelData())
        {
            Caster caster = activeCasters.get(offHandItem.getItemMeta().getCustomModelData());

            if (caster != null && caster.isOwnedBy(this) && caster.isThisItem(offHandItem)) {
                caster.onPlayerItemHeldEvent(event);
            }
        }

        // Pasar el evento  a todos los casters pasivos

        // Procesar el evento como usuario
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(event.getCause().equals(DamageCause.FALL)){
            if (this.devilFruit != null)this.devilFruit.onFall(event);
        }

        if (this.devilFruit != null)this.devilFruit.onEntityDamage(event);
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        if (this.devilFruit != null)
            this.devilFruit.onPlayerToggleSneakEvent(event);
    }
    
    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if (this.devilFruit != null)
            this.devilFruit.onPlayerItemConsume(event);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        this.stats.apply();
    }

    public void onPlayerLogin(PlayerLoginEvent event)
    {
        this.stats.apply();
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
    public boolean equals(Object other)
    {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        return this.uuid == ((OPUser) other).uuid;
    }
}
