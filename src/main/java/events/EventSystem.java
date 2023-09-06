package events;

import htt.ophabs.OPhabs;
import skin.skinsChanger;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import users.OPUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

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
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!users.containsKey(uuid))
            return;

        skinsChanger.resetSkin(users.get(uuid).getPlayer());
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

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if(users.containsKey(uuid))
        {
            OPUser user = users.get(uuid);
            user.onPlayerToggleSneakEvent(event);
        }

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        if(users.containsKey(uuid))
        {
            OPUser user = users.get(uuid);
            user.onPlayerItemConsume(event);
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    }

}
