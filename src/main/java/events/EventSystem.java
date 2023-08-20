package events;

import htt.ophabs.OPhabs;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import registry.fruits.IFruitRegistry;
import users.OPUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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

}
