package newSystem.events;

import abilitieSystem.abilityUser;
import castSystem.noDropCaster;
import htt.ophabs.OPhabs;
import htt.ophabs.fileSystem;
import newSystem.OPUser;
import newSystem.consumables.ConsumableDevilFruit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FruitEvents implements Listener
{
    public FruitEvents()
    {

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        ItemStack item = event.getItem();

        if (ConsumableDevilFruit.isConsumableDevilFruit(item))
        {
            Player player = event.getPlayer();
            OPUser user = OPhabs.newUsers.getOrSetUser(player.getUniqueId(), player.getName());

            OPhabs.registrySystem.fruitRegistry.linkFruitUser(item.getItemMeta().getCustomModelData(), user);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        OPUser user = OPhabs.newUsers.getOrSetUser(player.getUniqueId(), player.getName());

        if (user.hasDevilFruit())
        {
            // Eliminar el caster de los drops
            for (ItemStack drop : event.getDrops())
            {
                if (true) // TODO: Check si es un caster
                    drop.setAmount(0);
            }

            user.onPlayerDeath(event);
            OPhabs.registrySystem.fruitRegistry.unlinkFruitUser(user);
        }
    }
}
