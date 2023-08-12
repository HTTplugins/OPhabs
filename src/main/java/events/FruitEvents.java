package events;

import htt.ophabs.OPhabs;
import users.OPUser;
import cast.Caster;
import cast.FruitCaster;
import consumables.ConsumableDevilFruit;
import registry.fruits.IFruitRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FruitEvents implements Listener
{
    private IFruitRegistry fruitRegistry;

    public FruitEvents()
    {
        this.fruitRegistry = OPhabs.registrySystem.getRegistry(IFruitRegistry.class);
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

            if (this.fruitRegistry.linkFruitUser(fruitID, user))
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
            this.fruitRegistry.unlinkFruitUser(user);
        }
    }
}
