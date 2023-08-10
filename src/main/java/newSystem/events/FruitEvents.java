package newSystem.events;

import abilitieSystem.abilityUser;
import castSystem.noDropCaster;
import htt.ophabs.OPhabs;
import htt.ophabs.fileSystem;
import newSystem.OPUser;
import newSystem.cast.Caster;
import newSystem.cast.FruitCaster;
import newSystem.consumables.ConsumableDevilFruit;
import newSystem.fruits.DevilFruit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
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
            int fruitID = item.getItemMeta().getCustomModelData();

            // Ya tiene una fruta
            if (user.hasDevilFruit() && user.getDevilFruit().getID() != fruitID)
            {
                player.getWorld().strikeLightningEffect(player.getLocation());
                player.setHealth(0);
                return;
            }

            if (OPhabs.registrySystem.fruitRegistry.linkFruitUser(fruitID, user))
            {
                // Darle el caster
                player.getInventory().addItem(FruitCaster.getAsItem(fruitID));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        OPUser user = OPhabs.newUsers.getOrSetUser(player.getUniqueId(), player.getName());

        if (user.hasDevilFruit())
        {
            Map<Integer, Caster> casters = user.getActiveCasters();

            // Eliminar el caster de los drops
            for (ItemStack drop : event.getDrops())
            {
                if (!drop.hasItemMeta() || !drop.getItemMeta().hasCustomModelData())
                    continue;

                Caster caster = casters.get(drop.getItemMeta().getCustomModelData());

                if (caster != null && caster.isThisItem(drop))
                    drop.setAmount(0);
            }

            user.getDevilFruit().onPlayerDeath(event);
            OPhabs.registrySystem.fruitRegistry.unlinkFruitUser(user);
        }
    }
}
