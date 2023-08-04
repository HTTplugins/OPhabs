package newSystem.cast;

import htt.ophabs.OPhabs;
import newSystem.OPUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Map;

public class CastSystem implements Listener
{
    private final Map<String, OPUser> users;

    public CastSystem()
    {
        this.users = OPhabs.newUsers;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        String playerName = event.getPlayer().getName();

        if (!users.containsKey(playerName))
            return;

        users.get(playerName).onPlayerInteract(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        String playerName = event.getPlayer().getName();

        if (!users.containsKey(playerName))
            return;

        users.get(playerName).onPlayerDropItem(event);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        String playerName = event.getPlayer().getName();

        if (!users.containsKey(playerName))
            return;

        users.get(playerName).onPlayerSwapHandItems(event);
    }
}
