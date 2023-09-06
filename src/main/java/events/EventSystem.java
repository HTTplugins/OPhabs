package events;

import htt.ophabs.OPhabs;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import users.OPUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.UUID;

public class EventSystem implements Listener
{
    private final Map<UUID, OPUser> users;

    public EventSystem()
    {
        this.users = OPhabs.newUsers.getReadonlyContainer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!users.containsKey(uuid))
            return;

        users.get(uuid).onPlayerInteract(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!users.containsKey(uuid))
            return;

        users.get(uuid).onPlayerDropItem(event);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!users.containsKey(uuid))
            return;

        users.get(uuid).onPlayerSwapHandItems(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getName();

        OPhabs.newUsers.getOrSetUser(uuid, name);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        UUID uuid = event.getEntity().getUniqueId();

        if(users.containsKey(uuid))
        {
            OPUser user = users.get(uuid);
            user.onEntityDamage(event);

            if(event.getCause() == EntityDamageEvent.DamageCause.FALL)
                user.onFall(event);
        }

        if(event.getDamage() <= 0)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if(users.containsKey(uuid))
        {
            OPUser user = users.get(uuid);
            user.onPlayerItemHeldEvent(event);
        }

    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    }

}
